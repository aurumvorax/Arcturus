package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.AnimatedSprite;
import aurumvorax.arcturus.artemis.components.Ephemeral;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Trail;
import aurumvorax.arcturus.services.EntityData;
import aurumvorax.arcturus.services.Services;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public enum EffectFactory{

    INSTANCE;

    private static World world;
    private static Archetype protoExplosion;
    private static Archetype protoTrail;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Ephemeral> mEphemeral;
    private static ComponentMapper<AnimatedSprite> mSprite;
    private static ComponentMapper<Trail> mTrail;


    public static void init(World world){
        EffectFactory.world = world;
        world.inject(INSTANCE);

        protoExplosion = new ArchetypeBuilder()
                .add(Physics2D.class)
                .add(AnimatedSprite.class)
                .add(Ephemeral.class)
                .build(world);

        protoTrail = new ArchetypeBuilder()
                .add(Trail.class)
                .build(world);
    }

    public static int createExplosion(String name, Physics2D physics){
        int effect = world.create(protoExplosion);
        EffectData data = EntityData.getEffectData(name);

        Physics2D p = mPhysics.get(effect);
        p.p.set(physics.p);
        p.v.set(physics.v);

        AnimatedSprite s = mSprite.get(effect);
        s.name = data.animName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        Services.getAnimation(data.animName);
        mEphemeral.get(effect).lifespan = Services.getAnimation(data.animName).getAnimationDuration();

        return effect;
    }

    public static int createTrail(String name, int parentID, Vector2 offset){
        int trail = world.create(protoTrail);
        EffectData data = EntityData.getEffectData(name);

        Trail t = mTrail.get(trail);
        t.parent = parentID;
        t.offset.set(offset);
        t.width = data.width;
        t.widen = data.widen;
        t.length = data.segments;
        t.segments = new Trail.Segment[data.segments];

        t.imgName = data.textureName;
        TextureAtlas.AtlasRegion texture = Services.getTexture(data.textureName);
        t.texDiv = (texture.getU2() - texture.getU()) / t.length;

        return trail;
    }
}
