package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.artemis.components.Trail;
import aurumvorax.arcturus.services.Services;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.IntMap;

public class TrailRenderer extends Renderer{

    private static IntMap<TextureAtlas.AtlasRegion> texturesByID = new IntMap<>();
    private static float[] vertices = new float[20];

    private static ComponentMapper<Trail> mTrail;

    public TrailRenderer(RenderBatcher principal){
        super(principal, Aspect.all(Trail.class));
    }

    @Override
    protected void inserted(int entityID){
        texturesByID.put(entityID, Services.getTexture(mTrail.get(entityID).imgName));
        principal.register(entityID, this, Layer.TRAIL);
    }

    @Override
    protected void removed(int entityID){
        texturesByID.remove(entityID);
        principal.unregister(entityID, this, Layer.TRAIL);
    }

    @Override
    protected void draw(int entityID, float alpha){
        Trail t = mTrail.get(entityID);
        for(int i = 0; i < t.size - 1; i++){
            int idx1 = t.index - i;
            if(idx1 < 0) idx1 += t.length;
            int idx2 = (idx1 - 1);
            if(idx2 < 0) idx2 += t.length;

            int age = (!t.active) ? t.length - t.size + i : i;
            drawQuad(texturesByID.get(entityID), t.segments, idx1, idx2, age, t.texDiv);
        }
    }

    private void drawQuad(TextureAtlas.AtlasRegion tex, Trail.Segment[] segments, int idx1, int idx2, int age, float texDiv){
        vertices[0] = segments[idx1].left.x;
        vertices[1] = segments[idx1].left.y;
        vertices[2] = Color.WHITE.toFloatBits();
        vertices[3] = tex.getU2() - (age * texDiv);
        vertices[4] = tex.getV();

        vertices[5] = segments[idx1].right.x;
        vertices[6] = segments[idx1].right.y;
        vertices[7] = Color.WHITE.toFloatBits();
        vertices[8] = tex.getU2() - (age * texDiv);
        vertices[9] = tex.getV2();

        vertices[10] = segments[idx2].right.x;
        vertices[11] = segments[idx2].right.y;
        vertices[12] = Color.WHITE.toFloatBits();
        vertices[13] = tex.getU2() - ((age + 1) * texDiv);
        vertices[14] = tex.getV2();

        vertices[15] = segments[idx2].left.x;
        vertices[16] = segments[idx2].left.y;
        vertices[17] = Color.WHITE.toFloatBits();
        vertices[18] = tex.getU2() - ((age + 1) * texDiv);
        vertices[19] = tex.getV();

        Services.batch.draw(tex.getTexture(), vertices, 0, 20);
    }
}
