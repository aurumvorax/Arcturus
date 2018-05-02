package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

public class InventoryActor extends Window{

    private Inventory inventory;
    private DragAndDrop dragAndDrop;
    private Skin skin;

    public InventoryActor(Inventory inventory, DragAndDrop dragAndDrop, Skin skin){
        super("Inventory...", skin);

        this.inventory = inventory;
        this.dragAndDrop = dragAndDrop;
        this.skin = skin;
        setPosition(400, 100);
        defaults().space(8);
        row().fill().expandX();
    }

    public void build(){
        clearChildren();
        int i = 0;
        for (Slot slot : inventory.getSlots()) {
            SlotActor slotActor = new SlotActor(skin, slot);
            dragAndDrop.addSource(new SlotSource(slotActor));
            dragAndDrop.addTarget(new SlotTarget(slotActor));
            add(slotActor);

            i++;
            if (i % 6 == 0) {
                row();
            }
        }
        pack();
    }
}

