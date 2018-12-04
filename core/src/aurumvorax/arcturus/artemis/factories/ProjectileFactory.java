package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import aurumvorax.arcturus.services.EntityData;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;


public class ProjectileFactory{

    private static final ProjectileFactory INSTANCE = new ProjectileFactory();
    private static World world;
    private static Archetype protoBullet;
    private static Archetype protoMissile;

    private static ComponentMapper<Projectile> mProjectile;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Ephemeral> mEphemeral;
    private static ComponentMapper<CollisionRadius> mRadius;
    private static ComponentMapper<Missile> mMissile;
    private static ComponentMapper<Weapons> mWeapons;


    public static void init(World world){
        ProjectileFactory.world = world;
        world.inject(INSTANCE);

        protoBullet = new ArchetypeBuilder()
                .add(Projectile.class)
                .add(Physics2D.class)
                .add(SimpleSprite.class)
                .add(Ephemeral.class)
                .add(CollisionRadius.class)
                .build(world);
        protoMissile = new ArchetypeBuilder(protoBullet)
                .add(PoweredMotion.class)
                .add(Missile.class)
                .build(world);
    }

    public static int create(String name, float x, float y, float t, int firedFrom){
        ProjectileData data = EntityData.getProjectileData(name);
        switch(data.type){
            case BULLET:
                int bullet = world.create(protoBullet);
                buildProjectile(bullet, data, x, y, t, firedFrom);
                return bullet;

            case MISSILE:
                int missile = world.create(protoMissile);
                buildProjectile(missile, data, x, y, t, firedFrom);
                return missile;

            default:
                throw new IllegalArgumentException(data.type + " is not a known type");
        }
    }

    static void setWeaponData(Cannon c, String type){
        ProjectileData data = EntityData.getProjectileData(type);
        c.speed = data.speed;
        c.duration = data.duration;
    }

    private static void buildProjectile(int projectile, ProjectileData data, float x, float y, float t, int firedFrom){
        Physics2D p = mPhysics.get(projectile);
        p.p.set(x, y);
        p.theta = t;
        p.v.set(data.speed, 0).rotate(t).add(mPhysics.get(firedFrom).v);

        SimpleSprite s = mSprite.get(projectile);
        s.name = data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.ACTOR;

        Projectile proj = mProjectile.get(projectile);
        proj.firedFrom = firedFrom;
        proj.damage = data.damage;

        mEphemeral.get(projectile).lifespan = data.duration;
        mRadius.get(projectile).radius = data.collisionRadius;

        if(mMissile.has(projectile)){
            Missile m = mMissile.get(projectile);
            m.engineDuration = data.engineDuration;
                // TODO - set target for realsies here
            m.target = PlayerShip.getTargetID();
        }
    }
}
