package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

public class Inventory extends VerticalGroup implements Draggable.Target{

    private Array<Item.Stack> items = new Array<>();
    private IntMap<InventoryEntry> listEntries = new IntMap<>();
    private IntMap<DragSource> listSources = new IntMap<>();
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
        int idx = items.indexOf(stack, false);  // Relies on the fact that Item.Stack.equals() ignores quantity

        // don't already have it
            //add to items
            // new listentry

        // do already have it
            //update items
            //updatelistentry

        // confirming empty
            //remove from items
            //remove listentry

        if((stack.quantity == 0) && (listEntries.get(idx).getStack().quantity == 0)){
            // Confirm removal or empty entry
            items.removeIndex(idx);
            this.removeActor(listEntries.get(idx));
            listEntries.remove(idx);
            draganddrop.removeSource(listSources.get(idx));
            listSources.remove(idx);
            return 0;
        }

        if(idx == -1){
            // Create new entry
            idx = items.size;
            items.add(new Item.Stack(stack));
            InventoryEntry newEntry = new InventoryEntry(stack, this, skin);
            listEntries.put(idx, newEntry);
            this.addActor(newEntry);
            DragSource newSource = new DragSource(newEntry);
            listSources.put(idx, newSource);
            draganddrop.addSource(newSource);
            return 0;
        }

        // Update existing entry
        items.get(idx).quantity += stack.quantity;
        listEntries.get(idx).update(items.get(idx));
        return 0;
    }

    boolean take(Item.Stack stack){
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

        for(InventoryEntry entry : listEntries.values())
            addActor(entry);

        draganddrop.addTarget(new DragTarget(this));
        for(DragSource source : listSources.values())
            draganddrop.addSource(source);

        return this;
    }
}
