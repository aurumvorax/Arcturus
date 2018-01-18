package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public class ProjectileFactory{

    private static final ProjectileFactory INSTANCE = new ProjectileFactory();
    private static World world;
    private static HashMap<String, ProjectileData> projectiles;
    private static Archetype protoBullet;

    private static ComponentMapper<Projectile> mProjectile;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Ephemeral> mEphemeral;
    private static ComponentMapper<CollisionRadius> mRadius;

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

        projectiles = new HashMap<>();
        for(FileHandle entry : Services.PROJECTILE_PATH.list()){
            Wrapper wrapper = Services.json.fromJson(Wrapper.class, entry);
            projectiles.put(wrapper.name, wrapper.data);
            Gdx.app.debug("INIT", "Registered Projectile - " + wrapper.name);
        }
    }

    public static int create(String name, float x, float y, float t, int firedFrom){

        if(!projectiles.containsKey(name))
            throw new IllegalArgumentException("Invalid projectile type - " + name);

        ProjectileData data = projectiles.get(name);
        switch(data.type){
            case BULLET:
                int bullet = world.create(protoBullet);
                buildProjectile(bullet, data, x, y, t, firedFrom);
                return bullet;
            case MISSILE:

            default:
                throw new IllegalArgumentException(data.type + " is not a known type");
        }
    }

    public static void setWeaponData(Cannon c, String type){
        if(!projectiles.containsKey(type))
            throw new IllegalArgumentException("Invalid projectile type - " + type);
        ProjectileData data = projectiles.get(type);
        c.speed = data.speed;
        c.maxRange = data.speed * data.duration;
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

    private static class Wrapper{
        String name;
        ProjectileData data;
    }

    private static class ProjectileData{

        //Common to all projectile types
        ProjectileType type;
        String imgName;
        Vector2 imgCenter;
        int collisionRadius;
        float duration;
        float speed;
        float damage;

        //Only for missiles

        float engineDuration;
        float turnRate;
    }

    private enum ProjectileType{
        BULLET, MISSILE
    }
}
