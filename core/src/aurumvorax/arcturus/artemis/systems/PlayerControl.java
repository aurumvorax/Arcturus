package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Turret;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.systems.collision.Collision;
import aurumvorax.arcturus.artemis.systems.render.HUDRenderer;
import aurumvorax.arcturus.artemis.systems.render.WorldCam;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;


public class PlayerControl extends BaseSystem{

    private transient int player;
    private transient int thrust;
    private transient int helm;
    private transient int strafe;
    private transient boolean brake;
    private transient boolean fire;
    private transient Vector2 mouse = new Vector2();
    private transient static Vector2 select = new Vector2();

    private static ComponentMapper<Player> mPlayer;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Turret> mTurret;


    public void controlThrust(int thrust){ this.thrust = thrust; }
    public void controlHelm(int helm){ this.helm = helm; }
    public void controlStrafe(int strafe){ this.strafe = strafe; }
    public void controlBrake(boolean brake){ this.brake = brake; }
    public void controlFire(boolean fire){ this.fire = fire; }
    public void setMouse(int x, int y){ this.mouse.set(x, y); }

    public static void selectTarget(int x, int y){
        select.set(x, y);
        int targetID = Collision.pointCheck(WorldCam.unproject(select));
        PlayerShip.setTargetID(targetID);
    }

    @Override
    protected void processSystem(){
        player = PlayerShip.getID();
        if(!mPlayer.has(player))
            return;

        Physics2D physics2D = mPhysics.get(player);
        PoweredMotion pm = mPowered.get(player);
        if(!brake){
            pm.alpha = helm * pm.rotation;
            pm.accel.set(thrust, strafe);

            if(!pm.accel.isZero())
                pm.accel.rotate(physics2D.theta).setLength(pm.thrust);
        }else{
            pm.alpha = -Utils.sign(physics2D.omega) * pm.rotation;
            pm.accel.set(physics2D.v).scl(-1).setLength(pm.thrust);
        }
        updateWeapons(WorldCam.unproject(mouse));
    }

    private void updateWeapons(Vector2 target){
        Weapons w = mWeapons.get(player);
        for(int i = 0; i < w.manual.size(); i++){
            Turret t = mTurret.get(w.manual.get(i));
            t.target = (target);
            t.fire = fire;
        }
    }

    public void reset(){
        thrust = 0;
        helm = 0;
        strafe = 0;
        brake = false;
        fire = false;
    }
}
