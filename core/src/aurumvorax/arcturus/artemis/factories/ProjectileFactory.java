package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import aurumvorax.arcturus.services.EntityData;
import com.artemis.*;


public enum ProjectileFactory{

    INSTANCE;

    private static World world;
    private static Archetype protoBullet;
    private static Archetype protoMissile;

    private static ComponentMapper<Projectile> mProjectile;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Ephemeral> mEphemeral;
    private static ComponentMapper<CollisionRadius> mRadius;
    private static ComponentMapper<MissileTargeting> mMissile;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<PoweredMotion> mPowered;
    private static ComponentMapper<Trail> mTrail;


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
                .add(MissileTargeting.class)
                .build(world);
    }

    public static int createBullet(String name, float x, float y, float t, int firedFrom){
        ProjectileData data = EntityData.getProjectileData(name);

        int bullet = world.create(protoBullet);
        buildProjectile(bullet, data, x, y, t, firedFrom);

        return bullet;
    }

    public static int createMissile(String name, float x, float y, float t, int firedFrom, int target){
        ProjectileData data = EntityData.getProjectileData(name);

        int missile = world.create(protoMissile);
        buildProjectile(missile, data, x, y, t, firedFrom);

        MissileTargeting m = mMissile.get(missile);
        m.engineDuration = data.engineDuration;
        m.target = target;

        PoweredMotion pm = mPowered.get(missile);
        pm.thrust = data.thrust;
        pm.maxV = data.maxV;

        EffectFactory.createTrail(data.trailName, missile, data.trailOffset);
        return missile;
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
    }
}
