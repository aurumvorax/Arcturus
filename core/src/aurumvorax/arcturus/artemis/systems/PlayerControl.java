package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.Vector2;

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
        super(Aspect.all(Player.class, Velocity.class, Position.class));
    }


    public void controlThrust(int thrust){ this.thrust += thrust; }
    public void controlHelm(int helm){ this.helm += helm; }
    public void controlStrafe(int strafe){ this.strafe += strafe; }
    public void controlBrake(boolean brake){ this.brake = brake; }

    @Override
    protected void processSystem(){
        Velocity v = mVelocity.get(playerShip);
        Position p = mPosition.get(playerShip);
        float delta = world.delta;
        accel.set(thrust, strafe);
        if(!accel.isZero())
            accel.rotate(p.theta).scl(300);
        v.dx += accel.x * delta;
        v.dy += accel.y * delta;
        v.omega += helm * 100 * delta;
    }

    @Override
    public void inserted(int entityID){
        playerShip = entityID;
    }
}
