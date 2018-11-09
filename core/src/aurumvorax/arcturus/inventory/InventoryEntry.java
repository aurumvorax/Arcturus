package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.*;

class InventoryEntry extends Button implements Draggable.Source, Draggable.Target{

    private Item.Stack stack;
    private Inventory inventory;
    private Skin skin;


    InventoryEntry(Item.Stack stack, Inventory inventory, Skin skin){
        super(skin);
        this.stack = stack;
        this.inventory = inventory;
        this.skin = skin;
        update(stack);
        setSkin(skin);
    }

    @Override public boolean isValid(Item.Stack stack){ return true; }
    @Override public Item.Stack getStack(){ return new Item.Stack(stack); }
    @Override public int add(Item.Stack stack){ return inventory.add(stack); }
    @Override public boolean take(Item.Stack stack){ return inventory.take(stack); }

    void update(Item.Stack stack){
       clearChildren();
       setDebug(true);
       add(new Image(stack.item.getTexture()));
       add(new Label(stack.item.name, skin));
       add(new Label(String.valueOf(stack.quantity), skin));
    }
}
