package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.savegame.SaveManager;
import aurumvorax.arcturus.savegame.SaveObserver;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;


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
    private Rectangle screenBounds = new Rectangle();

    private static final float ZMIN = 0.02f;
    private static final float ZMAX = 20.0f;
    private static final float MINSPEED2 = 2000f;
    private static final float FRAMESIZE = 0.8f;
    private static final float PANSPEED = 3;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Player> mPlayer;

    public WorldCam(){
        cam = new OrthographicCamera();
        position = new Vector2();
        velocity = new Vector2();
        mouseS = new Vector2();
        temp = new Vector2();
        lerpMatrix = new Vector3();
        screenBounds.set(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void reset(){ cam.zoom = 1.0f; }
    public void zoom(float z){ cam.zoom = MathUtils.clamp(cam.zoom * (1 + z), ZMIN, ZMAX); }
    public void setMouse(float x, float y){ mouseS.set(x, y); }
    static float lerpX(float alpha){ return position.x + (velocity.x * alpha); }
    static float lerpY(float alpha){ return position.y + (velocity.y * alpha); }
    void setCullingFrame(){ ScissorStack.pushScissors(screenBounds); }
    void endCullingFrame(){ ScissorStack.popScissors(); }

    public void resize(int width, int height){
        cam.setToOrtho(false, width, height);
        cam.update();
        WorldCam.halfWidth = width * 0.5f;
        WorldCam.halfHeight = height * 0.5f;
        screenBounds.set(0,0, width, height);
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
        getTarget();
        if((target == -1) || (!mPhysics.has(target))){
            velocity.setZero();
            return;
        }

        targetP = mPhysics.get(target).p;
        targetV = mPhysics.get(target).v;

        velocity.set(targetP.x + ((mouseS.x - halfWidth) * cam.zoom * FRAMESIZE),
                targetP.y - ((mouseS.y - halfHeight) * cam.zoom * FRAMESIZE));
        velocity.sub(position).scl(PANSPEED).add(targetV);
        if(velocity.len2() < MINSPEED2 * cam.zoom)
            velocity.set(targetV);
        position.mulAdd(velocity, world.delta);
    }

    private void getTarget(){
        int targetID = PlayerShip.getTargetID();
        if(targetID == -1)
            target = PlayerShip.getID();
        else
            target = targetID;
    }

    public static Vector2 unproject(Vector2 screen){
        temp.set(position.x + ((screen.x - halfWidth) * cam.zoom), position.y + ((-screen.y + halfHeight) * cam.zoom));
        return temp;
    }

    public static Vector2 projectToScreen(Vector2 world){
        temp.set(((world.x - position.x) / cam.zoom) + halfWidth, halfHeight - ((world.y - position.y) / cam.zoom));
        return temp;
    }

    public static Vector2 projectToStage(Vector2 world, float alpha){
        temp.set(((world.x - lerpX(alpha)) / cam.zoom) + halfWidth, ((world.y - lerpY(alpha)) / cam.zoom) + halfHeight);
        return temp;
    }

    @Override
    public void onNotify(SaveManager saveManager, SaveEvent saveEvent){
        switch(saveEvent){
            case SAVING:
                saveManager.saveElement("CamPosition", position);
                saveManager.saveElement("Camera", cam);
                break;

            case LOADING:
                position.set(saveManager.loadElement("CamPosition", Vector2.class));
                cam = saveManager.loadElement("Camera", OrthographicCamera.class);
        }
    }
}
