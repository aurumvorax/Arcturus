package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.PlayerShip;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings("WeakerAccess")
public class PlayerControl extends BaseEntitySystem{

    @EntityId private int playerShip;
    private int thrust;
    private int helm;
    private int strafe;
    private boolean brake;
    private Vector2 accel = new Vector2();

    ComponentMapper<Velocity> mVelocity;
    ComponentMapper<Position> mPosition;

    public PlayerControl(){
        super(Aspect.all(PlayerShip.class, Velocity.class, Position.class));
    }


    public void controlThrust(int thrust){ this.thrust += thrust; }
    public void controlHelm(int helm){ this.helm += helm; }
    public void controlStrafe(int strafe){ this.strafe += strafe; }
    public void controlBrake(boolean brake){ this.brake = brake; }

    @Override
    protected void processSystem(){
        Velocity velocity = mVelocity.get(playerShip);
        Position position = mPosition.get(playerShip);
        float delta = world.delta;
        if(!brake){
            accel.set(thrust, strafe);
            if(!accel.isZero())
                accel.rotate(position.theta).setLength(300);
        }else{
            if((velocity.v.len2()) > 300 * delta)
                accel.set(velocity.v).scl(-1).setLength(300);
            else
                accel.set(velocity.v).scl(-1);
        }
        velocity.v.mulAdd(accel, delta);
        velocity.omega += helm * 100 * delta;
    }

    @Override
    public void inserted(int entityID){
        playerShip = entityID;
    }
}
