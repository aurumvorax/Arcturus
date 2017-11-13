package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.savegame.SaveManager;
import aurumvorax.arcturus.savegame.SaveObserver;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class WorldCam extends BaseSystem implements SaveObserver{

    private static OrthographicCamera cam;
    private static Vector2 position;
    private static Vector2 velocity;
    private static Vector2 temp;
    private static float halfWidth;
    private static float halfHeight;
    private int target = -1;
    private Vector2 targetP;
    private Vector2 targetV;
    private Vector2 mouseS;
    private Vector3 lerpMatrix;

    private static final float ZMIN = 0.2f;
    private static final float ZMAX = 2.0f;
    private static final float MINSPEED2 = 2000f;
    private static final float FRAMESIZE = 0.8f;
    private static final float PANSPEED = 3;

    private static ComponentMapper<Physics2D> mPhysics;

    public WorldCam(){
        cam = new OrthographicCamera();
        position = new Vector2();
        velocity = new Vector2();
        mouseS = new Vector2();
        temp = new Vector2();
        lerpMatrix = new Vector3();
    }

    public void Zoom(float zoom){ cam.zoom = MathUtils.clamp(cam.zoom * (1 + zoom), ZMIN, ZMAX); }
    public void setMouse(float x, float y){ mouseS.set(x, y); }

    public void setTarget(int entityID){
        if(mPhysics.has(entityID)){
            target = entityID;
            WorldCam.position.set(mPhysics.get(target).p);
            targetP = mPhysics.get(target).p;
            targetV = mPhysics.get(target).v;
        }else
            target = -1;
    }

    public void resize(int width, int height){
        cam.setToOrtho(false, width, height);
        cam.update();
        WorldCam.halfWidth = width * 0.5f;
        WorldCam.halfHeight = height * 0.5f;
    }

    Matrix4 getMatrix(float alpha){
        cam.position.set(position, 0);
        lerpMatrix.set(velocity, 0);
        cam.position.mulAdd(lerpMatrix, alpha);
        cam.update();
        return cam.combined;
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
    }

    public static Vector2 unproject(Vector2 screen){
        temp.set(position.x + ((screen.x - halfWidth) * cam.zoom), position.y + ((-screen.y + halfHeight) * cam.zoom));
        return temp;
    }

    public static Vector2 projectToScreen(Vector2 world){
        temp.set(((world.x - position.x) / cam.zoom) + halfWidth, halfHeight - ((world.y - position.y) / cam.zoom));
        return temp;
    }

    public static Vector2 projectToStage(Vector2 world){
        temp.set(((world.x - position.x) / cam.zoom) + halfWidth, ((world.y - position.y) / cam.zoom) + halfHeight);
        return temp;
    }

    @Override
    public void onNotify(SaveManager saveManager, SaveEvent saveEvent){
        switch(saveEvent){
            case SAVING:
                saveManager.saveElement("CamTarget", target);
                break;

            case LOADING:
                setTarget(saveManager.loadElement("CamTarget", int.class));
        }
    }
}
