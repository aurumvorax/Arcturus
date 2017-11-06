package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.shipComponents.PlayerShip;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.systems.collision.Collision;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.abs;

public class PlayerControl extends BaseEntitySystem{

    private int playerShip;
    private transient int thrust;
    private transient int helm;
    private transient int strafe;
    private transient boolean brake;
    private transient boolean fire;
    private transient Vector2 accel = new Vector2();
    private transient Vector2 mouse = new Vector2();
    private transient Vector2 target = new Vector2();
    private transient Vector2 select = new Vector2();

    private ComponentMapper<PlayerShip> mPlayer;
    private ComponentMapper<Physics2D> mPosition;
    private ComponentMapper<Weapons> mWeapons;
    private WorldCam worldCam;


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
        target = worldCam.unproject(select);
        int entityID = Collision.pointCheck(target);
        System.out.println(target);

    }

    @Override
    protected void processSystem(){
        if(!mPlayer.has(playerShip))
            return;
        Physics2D physics2D = mPosition.get(playerShip);
        float delta = world.delta;
        if(!brake){
            physics2D.omega = MathUtils.clamp(-300, physics2D.omega + helm * 100 * delta, 300);
            accel.set(thrust, strafe);
            if(!accel.isZero())
                accel.rotate(physics2D.theta).setLength(300);
        }else{
            if(abs(physics2D.omega) < 100 * delta)
                physics2D.omega = 0;
            else
                physics2D.omega -= Utils.sign(physics2D.omega) * 100 * delta;
            if((physics2D.v.len2()) > 300 * delta)
                accel.set(physics2D.v).scl(-1).setLength(300);
            else
                accel.set(physics2D.v).scl(-1);
        }
        physics2D.v.mulAdd(accel, delta);
        physics2D.omega += helm * 100 * delta;
        Weapons w = mWeapons.get(playerShip);
        w.target.set(worldCam.unproject(mouse));
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
