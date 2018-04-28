package aurumvorax.arcturus.inventory;

public class Item{


    String name;
    ItemType type;

    public enum ItemType{
        Simple,
        Weapon
    }

    public static class WeaponItem extends Item{

    }
}
