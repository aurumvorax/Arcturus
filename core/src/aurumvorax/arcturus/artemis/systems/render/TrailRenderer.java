package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Trail;
import aurumvorax.arcturus.artemis.systems.Trails;
import aurumvorax.arcturus.services.Services;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TrailRenderer extends Renderer{

    private static float[] vertices = new float[20];
    private static ComponentMapper<Trail> mTrail;

    public TrailRenderer(RenderBatcher principal){
        super(principal, Aspect.all(Physics2D.class, Trail.class));
        principal.register(-1, this, Layer.TRAIL);

        // Because trails need to persist beyond the entity that created them, we cheat a bit here, and register
        // the entire TrailRenderer as a single job up front, as well as using the regular method.  This extra draw
        // call, with an entity ID of -1, then draws all the fading trails that no longer have an entity.
    }

    @Override
    protected void draw(int entityID, float alpha){
        if(entityID == -1){
            Bag<Trails.TrailData> fadingTrails = Trails.getFadingTrails();

            for(Trails.TrailData t: fadingTrails){
                drawTrail(t, true);
            }

        }else{
            drawTrail(mTrail.get(entityID).trailData, false);
        }
    }

    private void drawTrail(Trails.TrailData t, boolean fading){
        for(int i = 0; i < t.size - 1; i++){
            int idx1 = t.index - i;
            if(idx1 < 0) idx1 += t.length;
            int idx2 = (idx1 - 1);
            if(idx2 < 0) idx2 += t.length;

            int age = (fading) ? t.length - t.size + i : i;
            drawQuad(t.texture, t.segments, idx1, idx2, age, t.texDiv);
        }
    }

    private void drawQuad(TextureAtlas.AtlasRegion tex, Trails.TrailData.Segment[] segments, int idx1, int idx2, int age, float texDiv){
        vertices[0] = segments[idx1].left.x;
        vertices[1] = segments[idx1].left.y;
        vertices[2] = Color.WHITE_FLOAT_BITS;
        vertices[3] = tex.getU2() - (age * texDiv);
        vertices[4] = tex.getV();

        vertices[5] = segments[idx1].right.x;
        vertices[6] = segments[idx1].right.y;
        vertices[7] = Color.WHITE_FLOAT_BITS;
        vertices[8] = tex.getU2() - (age * texDiv);
        vertices[9] = tex.getV2();

        vertices[10] = segments[idx2].right.x;
        vertices[11] = segments[idx2].right.y;
        vertices[12] = Color.WHITE_FLOAT_BITS;
        vertices[13] = tex.getU2() - ((age + 1) * texDiv);
        vertices[14] = tex.getV2();

        vertices[15] = segments[idx2].left.x;
        vertices[16] = segments[idx2].left.y;
        vertices[17] = Color.WHITE_FLOAT_BITS;
        vertices[18] = tex.getU2() - ((age + 1) * texDiv);
        vertices[19] = tex.getV();

        Services.batch.draw(tex.getTexture(), vertices, 0, 20);
    }

    @Override
    protected void inserted(int entityID){
        principal.register(entityID, this, Layer.TRAIL);
    }

    @Override
    protected void removed(int entityID){
        principal.unregister(entityID, this, Layer.TRAIL);
    }
}
