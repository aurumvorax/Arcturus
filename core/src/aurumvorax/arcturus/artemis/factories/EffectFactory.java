package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.AnimatedSprite;
import aurumvorax.arcturus.artemis.components.Ephemeral;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
public class EffectFactory{

    private static final EffectFactory INSTANCE = new EffectFactory();
    private static World world;
    private static Archetype protoExplosion;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Ephemeral> mEphemeral;
    private static ComponentMapper<AnimatedSprite> mSprite;


    public static void init(World world){
        EffectFactory.world = world;
        world.inject(INSTANCE);

        protoExplosion = new ArchetypeBuilder()
                .add(Physics2D.class)
                .add(AnimatedSprite.class)
                .add(Ephemeral.class)
                .build(world);
    }

    public static int createExplosion(String name, Physics2D physics){
        int effect = world.create(protoExplosion);

        Physics2D p = mPhysics.get(effect);
        p.p.set(physics.p);
        p.v.set(physics.v);

        AnimatedSprite s = mSprite.get(effect);
        s.name = "Boom";
        s.offsetX = 127;
        s.offsetY = 127;

        mEphemeral.get(effect).lifespan = Services.getAnimation("Boom").getAnimationDuration();

        return effect;
    }
}
