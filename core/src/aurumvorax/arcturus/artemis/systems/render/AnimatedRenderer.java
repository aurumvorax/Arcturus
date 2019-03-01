package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.services.Services;
import aurumvorax.arcturus.artemis.components.AnimatedSprite;
import aurumvorax.arcturus.artemis.components.Mounted;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;

public class AnimatedRenderer extends Renderer{

    private static ComponentMapper<AnimatedSprite> mSprite;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Mounted> mMounted;

    private IntMap<Animation> animationsByID = new IntMap<>();
    private Vector2 lerpPosition = new Vector2();

    @SuppressWarnings("unchecked")
    public AnimatedRenderer(RenderBatcher principal){
        super(principal, Aspect.all(AnimatedSprite.class).one(Physics2D.class, Mounted.class));
    }

    @Override
    protected void draw(int entityID, float alpha){
        Animation a = animationsByID.get(entityID);
        AnimatedSprite s = mSprite.get(entityID);
        TextureRegion region = (TextureRegion)a.getKeyFrame(s.time);
        float spriteAngle = 0;

        if(mPhysics.has(entityID)){         // Independent sprite
            Physics2D physics2D = mPhysics.get(entityID);
            lerpPosition.set(physics2D.p).mulAdd(physics2D.v, alpha);
            spriteAngle = physics2D.theta + (physics2D.omega * alpha);

        }else if(mMounted.has(entityID)){   // Mounted sprite
            Mounted m = mMounted.get(entityID);
            Physics2D parent = mPhysics.get(m.parent);
            if(parent == null)
                return;
            float lerpAngle = parent.theta + (parent.omega * alpha);
            lerpPosition.set(m.location).rotate(lerpAngle).add(parent.p).mulAdd(parent.v, alpha);
            spriteAngle = m.theta + (m.omega * alpha);
        }

        Services.batch.draw(region, lerpPosition.x - s.offsetX, lerpPosition.y - s.offsetY, s.offsetX, s.offsetY,
                region.getRegionWidth(), region.getRegionHeight(), 1, 1, spriteAngle);

        s.time += alpha;
    }

    @Override
    protected void inserted(int entityID){
        if(mSprite.has(entityID)){
            animationsByID.put(entityID, Services.getAnimation(mSprite.get(entityID).name));
            principal.register(entityID, this, mSprite.get(entityID).layer);
        }
    }

    @Override
    protected void removed(int entityID){
        if(mSprite.has(entityID)){
            animationsByID.remove(entityID);
            principal.unregister(entityID, this, mSprite.get(entityID).layer);
        }
    }
}
