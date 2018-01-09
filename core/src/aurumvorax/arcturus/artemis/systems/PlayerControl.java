package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.shipComponents.PlayerShip;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.systems.collision.Collision;
import aurumvorax.arcturus.artemis.systems.render.HUDRenderer;
import aurumvorax.arcturus.artemis.systems.render.WorldCam;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.abs;

public class PlayerControl extends BaseEntitySystem{

    private int playerShip;
    private transient int thrust;
    private transient int helm;
    private transient int strafe;
    private transient boolean brake;
    private transient boolean fire;
    private transient Vector2 mouse = new Vector2();
    private transient Vector2 target = new Vector2();
    private transient Vector2 select = new Vector2();

    private ComponentMapper<PlayerShip> mPlayer;
    private ComponentMapper<Physics2D> mPhysics;
    private ComponentMapper<PoweredMotion> mPowered;
    private ComponentMapper<Weapons> mWeapons;


    public PlayerControl(){
        super(Aspect.all(PlayerShip.class, Physics2D.class));
    }

    public void controlThrust(int thrust){ this.thrust = thrust; }
    public void controlHelm(int helm){ this.helm = helm; }
    public void controlStrafe(int strafe){ this.strafe = strafe; }
    public void controlBrake(boolean brake){ this.brake = brake; }
    public void controlFire(boolean fire){ this.fire = fire; }
    public void setMouse(int x, int y){ this.mouse.set(x, y); }

    public void selectTarget(int x, int y){
        this.select.set(x, y);
        target = WorldCam.unproject(select);
        HUDRenderer.setTarget(Collision.pointCheck(target));
    }

    @Override
    protected void processSystem(){
        if(!mPlayer.has(playerShip))
            return;
        Physics2D physics2D = mPhysics.get(playerShip);
        PoweredMotion pm = mPowered.get(playerShip);
        if(!brake){
            pm.alpha = helm * pm.rotation;
            pm.accel.set(thrust, strafe);
            if(!pm.accel.isZero())
                pm.accel.rotate(physics2D.theta).setLength(pm.thrust);
        }else{
            pm.alpha = -Utils.sign(physics2D.omega) * pm.rotation;
            pm.accel.set(physics2D.v).scl(-1).setLength(pm.thrust);
        }
        Weapons w = mWeapons.get(playerShip);
        w.target.set(WorldCam.unproject(mouse));
        w.fire = fire;
    }

    @Override
    public void inserted(int entityID){
        playerShip = entityID;
    }

    public void reset(){
        thrust = 0;
        helm = 0;
        strafe = 0;
        brake = false;
        fire = false;
    }


}
