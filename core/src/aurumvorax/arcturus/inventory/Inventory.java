package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.utils.Array;

public class Inventory{

    private Array<Slot> slots;


    public Inventory(){
        slots = new Array<>(12);
        for(int i = 0; i < 12; i++)
            slots.add(new Slot(null, 0));

        slots.get(3).add(new Item(Item.ItemType.Weapon, "MultiBarrel"), 1);
        slots.get(4).add(new Item(Item.ItemType.Weapon, "MultiBarrel"), 1);
        slots.get(6).add(new Item(Item.ItemType.Weapon, "TestCannon"), 1);
        slots.get(7).add(new Item(Item.ItemType.Weapon, "TestCannon"), 1);
        slots.get(10).add(new Item(Item.ItemType.Weapon, "TestBeam"), 1);
        slots.get(11).add(new Item(Item.ItemType.Weapon, "TestBeam"), 1);

    }

    Array<Slot> getSlots() {
        return slots;
    }
}
