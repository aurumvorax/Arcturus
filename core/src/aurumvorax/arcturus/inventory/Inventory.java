package aurumvorax.arcturus.inventory;

import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;


public class Inventory extends Table implements Draggable.Target{

    private VerticalGroup entryGroup = new VerticalGroup();
    private Array<Item.Stack> items = new Array<>();
    private Array<InventoryEntry> listEntries = new Array<>();
    private DragAndDrop draganddrop;
    private Skin skin;
    private Sort sort = new Sort();


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
        // Confirm removal of empty entries
        if(stack.quantity == 0){
            for(InventoryEntry entry : listEntries){
                if((entry.getStack().equals(stack) && entry.getStack().quantity == 0)){
                    items.removeValue(entry.getStack(), false);
                    draganddrop.removeSource(entry.getSource());
                    listEntries.removeValue(entry, false);
                }
            }
            sortEntries();
            return 0;
        }

        int idx = items.indexOf(stack, false);  // Relies on the fact that Item.Stack.equals() ignores quantity

        // Create new entry
        if(idx == -1){
            items.add(new Item.Stack(stack));
            InventoryEntry newEntry = new InventoryEntry(stack, this, skin);
            listEntries.add(newEntry);
            draganddrop.addSource(newEntry.getSource());
            sortEntries();
            return 0;
        }

        // Update existing entry
        items.get(idx).quantity += stack.quantity;
        for(InventoryEntry entry : listEntries){
            if(entry.getStack().equals(stack)){
                entry.update(items.get(idx));
                break;
            }
        }
        return 0;
    }

    boolean take(Item.Stack stack){
        int idx = items.indexOf(stack, false);

        if(idx == -1)
            return false;

        if(items.get(idx).quantity < stack.quantity)
            return false;

        items.get(idx).quantity -= stack.quantity;
        for(InventoryEntry entry : listEntries){
            if(entry.getStack().equals(stack)){
                entry.update(stack);
                break;
            }
        }
        return true;
    }

    public Actor update(){
        clearChildren();
        loadLists();
        sortEntries();
        add(entryGroup).pad(10);
        Drawable menuBG = new NinePatchDrawable(Services.MENUSKIN.getPatch("list"));
        setBackground(menuBG);

        draganddrop.addTarget(new DragTarget(this));

        return this;
    }

    private void loadLists(){

        for(InventoryEntry entry : listEntries)
            draganddrop.removeSource(entry.getSource());
        listEntries.clear();

        for(int idx = 0; idx < items.size; idx++){
            if(true){               // TODO add filters here
                InventoryEntry entry = new InventoryEntry(items.get(idx), this, skin);
                listEntries.add(entry);
                draganddrop.addSource(entry.getSource());
            }
        }
    }

    private void sortEntries(){
        entryGroup.clear();
        sort.sort(listEntries);
        for(InventoryEntry entry : listEntries)
            entryGroup.addActor(entry);
    }
}
