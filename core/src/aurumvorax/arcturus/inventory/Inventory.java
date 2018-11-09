package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

public class Inventory extends VerticalGroup implements Draggable.Target{

    private Array<Item.Stack> items = new Array<>();
    private Array<InventoryEntry> listEntries = new Array<>();
    private DragAndDrop draganddrop;
    private Skin skin;


    public Inventory(DragAndDrop draganddrop, Skin skin){
        this.draganddrop = draganddrop;
        this.skin = skin;

        add(new Item.Stack(new Item(Item.ItemType.Weapon, "MultiBarrel"), 1));
        add(new Item.Stack(new Item(Item.ItemType.Weapon, "TestCannon"), 2));
        add(new Item.Stack(new Item(Item.ItemType.Weapon, "TestBeam"), 4));
    }

    @Override
    public boolean isValid(Item.Stack stack){ return true; }

    @Override
    public int add(Item.Stack stack){
        int idx = items.indexOf(stack, false);

        if((stack.quantity == 0) && (listEntries.get(idx).getStack().quantity == 0)){
            listEntries.removeIndex(idx);
            update();
        }

        if(idx == -1){
            items.add(new Item.Stack(stack));
            listEntries.add(new InventoryEntry(new Item.Stack(stack), this, skin));
        }else{
            items.get(idx).quantity += stack.quantity;
            listEntries.get(idx).update(items.get(idx));
        }
        return 0;
    }

    public boolean take(Item.Stack stack){
        int idx = items.indexOf(stack, false);
        if(idx == -1)
            return false;
        if(items.get(idx).quantity < stack.quantity)
            return false;
        items.get(idx).quantity -= stack.quantity;
        listEntries.get(idx).update(items.get(idx));
        return true;
    }

    public Actor update(){
        clearChildren();
        draganddrop.addTarget(new DragTarget(this));
        for(InventoryEntry entry : listEntries){
            addActor(entry);
            draganddrop.addSource(new DragSource(entry));
        }
        return this;
    }
}
