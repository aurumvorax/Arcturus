package aurumvorax.arcturus.inventory;


import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.factories.EntityData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Item{

    String name;
    ItemType type;


    Item(ItemType type, String name){
        this.name = name;
        this.type = type;
    }

    Item(Item item){ this(item.type, item.name); }

    public enum ItemType{
        Simple,
        Weapon
    }

    TextureRegion getTexture(){
        if(type == Item.ItemType.Weapon)
            return Services.getTexture(EntityData.getWeaponData(name).imgName);
        throw new IllegalArgumentException("You haven't implemented non weapon item types yet!");
    }

    @Override
    public boolean equals(Object that){
        return (that instanceof Item) &&
                (this.type == ((Item)that).type) &&
                (this.name.equals(((Item)that).name));
    }


    public static class Stack{

        Item item;
        int quantity;


        Stack(Item item, int quantity){
            this.item = item;
            this.quantity = quantity;
        }

        Stack(Item.Stack stack){ this(stack.item, stack.quantity); }

        @Override           // tests to see if the items are the same, ignores quantity of the stack
        public boolean equals(Object that){
            return (that instanceof Stack) &&
                    (this.item.type == ((Stack)that).item.type) &&
                    (this.item.name.equals(((Stack)that).item.name));
        }
    }
}
