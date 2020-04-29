package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.services.Services;
import com.artemis.Aspect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DebugRenderer extends Renderer{

    private static Array<Vector2> points = new Array<>();
    private TextureRegion region = Services.getTexture("MapIcon");

    public DebugRenderer(RenderBatcher batcher){
        super(batcher, Aspect.all());
    }

    public void initialize(){
        principal.register(0, this, Layer.EFFECT);
    }

    public static void RenderPoint(Vector2 point){
        points.add(point);
    }

    @Override
    protected void draw(int entityID, float alpha){
        for(Vector2 point : points){
            Services.batch.draw(region, point.x - (region.getRegionWidth() / 2), point.y - (region.getRegionHeight() / 2));
        }

        points.clear();
    }

    @Override
    protected void inserted(int entityID){

    }

    @Override
    protected void removed(int entityID){

    }
}
