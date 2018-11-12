package aurumvorax.arcturus.inventory;

import aurumvorax.arcturus.Services;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class Slot extends Button implements Draggable.Source, Draggable.Target{

    private Item.Stack itemStack;
    private Item.ItemType slotType;
    private int slotSize;


    Slot(Item.Stack itemStack, Item.ItemType slotType, Skin skin, int max){
        super(skin);
        this.itemStack = itemStack;
        this.slotType = slotType;
        this.slotSize = max;
    }

    void empty(){ itemStack = null; }

    @Override
    public Item.Stack getStack(){ return itemStack; }

    @Override
    public boolean isValid(Item.Stack stack){
        if(!stack.item.type.equals(slotType)) // Right type?
            return false;

        if(itemStack == null)   // Slot is empty
                return true;

        return (itemStack.equals(stack) && itemStack.quantity + stack.quantity <= slotSize); // Check if there is enough room
    }

    @Override
    public int add(Item.Stack stack){
        int amount = stack.quantity;

        if(!stack.item.type.equals(slotType))
            return amount;          // Slot filtering.  Can't put missiles in an engine slot.

        if(itemStack == null){      // Slot is empty
            int amountAdded = Math.min(stack.quantity, slotSize);
            itemStack = new Item.Stack(stack.item, amountAdded);
            update();
            return amount - amountAdded;
        }

        if(itemStack.item.equals(stack.item)){      // Slot contains same thing, add quantity as appropriate
            int amountAdded = Math.min(stack.quantity, slotSize - itemStack.quantity);
            itemStack.quantity += amountAdded;
            update();
            return amount - amountAdded;
        }
        return amount;
    }

    @Override
    public boolean take(Item.Stack stack){

        if(itemStack == null)
            return false;

        if(itemStack.equals(stack) && itemStack.quantity >= stack.quantity){
            itemStack.quantity -= stack.quantity;
            if(itemStack.quantity == 0)
                itemStack = null;
            update();
            return true;
        }
        return false;
    }

    private void update(){
        clearChildren();
        add(new Image(getTexture()));
    }

    TextureRegion getTexture(){
        if(itemStack == null)
            return Services.getTexture("Empty");
        else
            return itemStack.item.getTexture();
    }
}
