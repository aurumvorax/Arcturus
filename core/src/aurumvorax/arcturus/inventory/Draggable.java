package aurumvorax.arcturus.inventory;

public interface Draggable{

    interface Source{

        Item.Stack getStack();
        // Find out what there is to pick up for dragging.

        int add(Item.Stack stack);
        // Returns the number of items left over after adding.  If the target is not valid, this number will be equal
        // to the quantity of the attempted add.

        boolean take(Item.Stack stack);
        // If the requested stack is available(item and quantity) it is removed from the inventory, and returns true.
        // If the requested stack is not available, no removal happens, and returns false.

    }

    interface Target{

        boolean isValid(Item.Stack stack);
        // Is this a valid target for some/all of the payload?

        int add(Item.Stack stack);
        // Returns the number of items left over after adding.  If the target is not valid, this number will be equal
        // to the quantity of the attempted add.
    }
}
