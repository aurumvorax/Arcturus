package aurumvorax.arcturus.inventory;

public class Item{

    String name;
    ItemType type;

    public Item(ItemType type, String name){
        this.name = name;
        this.type = type;
    }

    public enum ItemType{
        Simple,
        Weapon
    }

    @Override
    public boolean equals(Object that){
        return (that instanceof Item) &&
                (this.type == ((Item)that).type) &&
                (this.name == ((Item)that).name);
    }
}
