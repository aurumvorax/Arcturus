package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.utils.Array;

public class Inventory{

    private Array<Slot> slots;

    public Inventory(){
        slots = new Array<>(12);
        for(int i = 0; i < 12; i++)
            slots.add(new Slot(null, 0));

        slots.get(3).add("MultiBarrel", 1);
        slots.get(7).add("TestBeam", 1);
    }

    public int checkInventory(String item) {
        int amount = 0;
        for (Slot slot : slots) {
            if (slot.getItem().equals(item)) {
                amount += slot.getAmount();
            }
        }
        return amount;
    }

    public boolean store(String item, int amount) {
        // first check for a slot with the same item type
        Slot itemSlot = firstSlotWithItem(item);
        if (itemSlot != null) {
            itemSlot.add(item, amount);
            return true;
        } else {
            // now check for an available empty slot
            Slot emptySlot = firstSlotWithItem(null);
            if (emptySlot != null) {
                emptySlot.add(item, amount);
                return true;
            }
        }

        // no slot to add
        return false;
    }

    public Array<Slot> getSlots() {
        return slots;
    }

    private Slot firstSlotWithItem(String item) {
        for (Slot slot : slots) {
            if (slot.getItem().equals(item)) {
                return slot;
            }
        }

        return null;
    }
}
