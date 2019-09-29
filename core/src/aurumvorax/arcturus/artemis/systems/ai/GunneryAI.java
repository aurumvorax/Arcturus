package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Ship;
import aurumvorax.arcturus.artemis.components.Turret;
import aurumvorax.arcturus.artemis.components.Weapons;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public class GunneryAI{


    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Ship> mShip;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Turret> mTurret;

    public void primary(int ship){
        if(!mWeapons.has(ship))
            return;

        Weapons w = mWeapons.get(ship);
    }


    public void auto(int ship){

    }

    public void pd(int ship){

    }

    private void process(int ship, IntBag weaponGroup, IntBag contactGroup){
        Vector2 location = mPhysics.get(ship).p;

    }
}
