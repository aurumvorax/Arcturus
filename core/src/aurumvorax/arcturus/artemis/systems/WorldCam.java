package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class WorldCam extends BaseSystem{

    @EntityId private int target = -1;
    private static OrthographicCamera cam;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 targetP;
    private Vector2 targetV;
    private Vector2 mouseS;
    private Vector2 unprojected;
    private float halfWidth;
    private float halfHeight;

    private static final float ZMIN = 0.2f;
    private static final float ZMAX = 2.0f;
    private static final float MINSPEED2 = 2000f;
    private static final float FRAMESIZE = 0.8f;
    private static final float PANSPEED = 3;

    ComponentMapper<Position> mPosition;
    ComponentMapper<Velocity> mVelocity;

    public WorldCam(){
        cam = new OrthographicCamera();
        position = new Vector2();
        velocity = new Vector2();
        mouseS = new Vector2();
        unprojected = new Vector2();
    }

    public static Matrix4 getMatrix(){ return cam.combined; }
    public void Zoom(float zoom){ cam.zoom = MathUtils.clamp(cam.zoom * (1 + zoom), ZMIN, ZMAX); }
    public void setMouse(float x, float y){ mouseS.set(x, y); }

    public void setTarget(int entityID){
        if(mPosition.has(entityID) && mVelocity.has(entityID)){
            target = entityID;
            this.position.set(mPosition.get(target).p);
            targetP = mPosition.get(target).p;
            targetV = mVelocity.get(target).v;
        }else
            target = -1;
    }

    public void resize(int width, int height){
        cam.setToOrtho(false, width, height);
        cam.update();
        this.halfWidth = width * 0.5f;
        this.halfHeight = height * 0.5f;
    }


    @Override
    protected void processSystem(){
        if(target ==-1){
            velocity.setZero();
            return;
        }
        velocity.set(targetP.x + ((mouseS.x - halfWidth) * cam.zoom * FRAMESIZE),
                targetP.y - ((mouseS.y - halfHeight) * cam.zoom * FRAMESIZE));
        velocity.sub(position).scl(PANSPEED).add(targetV);
        if(velocity.len2() < MINSPEED2 * cam.zoom)
            velocity.set(targetV);
        position.mulAdd(velocity, world.delta);
        cam.position.set(position, 0);
        cam.update();

    }


    public Vector2 unproject(Vector2 screen){
        unprojected.set(position.x + ((screen.x - halfWidth) * cam.zoom), position.y + ((-screen.y + halfHeight) * cam.zoom));
        return unprojected;
    }
}
