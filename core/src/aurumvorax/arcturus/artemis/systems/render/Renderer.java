package aurumvorax.arcturus.artemis.systems.render;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;

public abstract class Renderer extends BaseEntitySystem implements RenderMarker{

    RenderBatcher principal;


    public Renderer(RenderBatcher principal, Aspect.Builder aspect){
        super(aspect);
        this.principal = principal;
    }

    protected abstract void draw(int entityID, float alpha);

    @Override
    protected abstract void inserted(int entityID);

    @Override
    protected abstract void removed(int entityID);

    // This system is called as a delegate from RenderBatcher via draw(float alpha), so
    // processSystem is never used.
    @Override protected void processSystem(){}

    public enum Layer{
        DEFAULT,
        STARFIELD,
        PLANETARY,
        ACTOR,
        DETAIL,
        EFFECT
    }
}
