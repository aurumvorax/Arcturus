package aurumvorax.arcturus.artemis.systems.ai.gunnery;

import aurumvorax.arcturus.artemis.components.Turret;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.utils.BitVector;
import com.artemis.utils.IntBag;

public class GunneryAI{
    private static GunneryAI INSTANCE = new GunneryAI();
    private GunneryAI(){} // Single static class with DI/callback

    @SuppressWarnings("FieldCanBeLocal")
    private static BitVector fireControl = new BitVector();

    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Turret> mTurret;


    public static void initialize(World world){
        world.inject(GunneryAI.INSTANCE);
        Aiming.initialize(world);
    }

    public static void update(int ship, int target){
        fireControl = Aiming.aimPrimary(ship, target);
        IntBag weapons = mWeapons.get(ship).all;
        for(int i = 0; i < weapons.size(); i++){
            int weapon = weapons.get(i);
            mTurret.get(weapon).fire = fireControl.get(weapon);
        }
    }
}
