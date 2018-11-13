package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.*;

class InventoryEntry extends Button implements Draggable.Source, Comparable<InventoryEntry>{

    private Item.Stack stack;
    private Inventory inventory;
    private Skin skin;
    private DragSource source;


    InventoryEntry(Item.Stack stack, Inventory inventory, Skin skin){
        super(skin);
        this.inventory = inventory;
        this.skin = skin;
        update(stack);
        setSkin(skin);
        source = new DragSource(this);
    }

    public DragSource getSource(){ return source; }
    @Override public Item.Stack getStack(){ return new Item.Stack(stack); }
    @Override public int add(Item.Stack stack){ return inventory.add(stack); }
    @Override public boolean take(Item.Stack stack){ return inventory.take(stack); }

    void update(Item.Stack stack){
        this.stack = stack;
        clearChildren();
        setDebug(true);
        add(new Image(stack.item.getTexture()));
        add(new Label(stack.item.name, skin));
        add(new Label(String.valueOf(stack.quantity), skin));
    }

    @Override
    public int compareTo(InventoryEntry o){
        return this.stack.item.compareTo(o.stack.item);
    }
}
