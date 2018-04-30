package aurumvorax.arcturus.inventory;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.factories.EntityData;
import aurumvorax.arcturus.artemis.factories.WeaponFactory;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Slot{

    private Item item;
    private int amount;

    private Array<SlotListener> slotListeners = new Array<>();

    Slot(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    Item getItem(){ return item; }
    int getAmount(){ return amount; }
    boolean isEmpty(){ return item == null || amount <= 0; }
    void addListener(SlotListener slotListener){ slotListeners.add(slotListener); }
    void removeListener(SlotListener slotListener){ slotListeners.removeValue(slotListener, true); }

    public boolean add(Item item, int amount) {
        if (this.item == item || this.item == null) {
            this.item = item;
            this.amount += amount;
            notifyListeners();
            return true;
        }
        return false;
    }

    boolean take(int amount) {
        if (this.amount >= amount) {
            this.amount -= amount;
            if (this.amount == 0) {
                item = null;
            }
            notifyListeners();
            return true;
        }
        return false;
    }

    TextureRegion getTexture(){
        if(isEmpty())
            return Services.getTexture("Empty");
        if(item.type == Item.ItemType.Weapon)
            return Services.getTexture(EntityData.getWeaponData(item.name).imgName);
        throw new IllegalArgumentException("You haven't implemented non weapon item types yet!");
    }

    private void notifyListeners() {
        for (SlotListener slotListener : slotListeners) {
            slotListener.hasChanged(this);
        }
    }

    static class WeaponSlot extends Slot{

        WeaponSlot(Item item){
            super(item, 1);
        }
    }
}
