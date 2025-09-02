package drippyspelunk.model.dungeon.entity;

import drippyspelunk.model.dungeon.entity.asset.Bomb;
import drippyspelunk.model.dungeon.entity.asset.Consumable;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory system for storing consumable items like health potions and bombs.
 * Has a capacity of 9 items and is displayed as a 3x3 grid in the UI.
 * Players can click items on the grid to use them.
 *
 * @author David Norman
 * @author Devin Arroyo
 * @author Mark Malyshev
 * @version 1.4
 */
public class Inventory<T extends Consumable> {  // T must be Consumable or subclass
    /**
     * A list to store the inventory items.
     */
    private final List<T> myItems;
    /**
     * The maximum number of items the inventory can hold.
     */
    private final int myCapacity;

    /**
     * Constructs a new Inventory with a default capacity of 9.
     */
    public Inventory() {
        myItems = new ArrayList<>();
        myCapacity = 9;
    }

    /**
     * Checks if the inventory is at its maximum capacity.
     *
     * @return true if the inventory is full, false otherwise.
     */
    public boolean isFull() {
        return myItems.size() >= myCapacity;
    }

    /**
     * Adds an object to the inventory if there is space.
     *
     * @param theObj The object to add.
     * @return true if the object was added successfully, false otherwise.
     */
    public boolean add(final T theObj) {
        if (!isFull()) {
            myItems.add(theObj);
            return true;
        } else {
            // Notify function caller that the object could not be picked up
            return false;
        }
    }

    /**
     * Removes an object from the inventory.
     *
     * @param theObj The object to remove.
     * @return true if the object was removed successfully, false otherwise.
     */
    public boolean remove(final T theObj) {
        return myItems.remove(theObj);
    }

    /**
     * Gets a copy of all items in the inventory.
     *
     * @return A list of items in inventory.
     */
    public List<T> getItems() {
        return new ArrayList<>(myItems); // Return a copy to prevent external modification
    }

    /**
     * Gets the current number of items in the inventory.
     *
     * @return The number of items.
     */
    public int getSize() {
        return myItems.size();
    }

    /**
     * Gets the maximum capacity of the inventory.
     *
     * @return The maximum capacity.
     */
    public int getCapacity() {
        return myCapacity;
    }

    /**
     * Checks if the inventory is empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return myItems.isEmpty();
    }

    /**
     * Gets an item at a specific index.
     *
     * @param index The index of the item.
     * @return The item at the specified index, or null if the index is invalid.
     */
    public T getItem(final int index) {
        if (index >= 0 && index < myItems.size()) {
            return myItems.get(index);
        }
        return null;
    }

    /**
     * Gets the first Bomb found in the inventory.
     *
     * @return The Bomb object, or null if not found.
     */
    public Bomb getBomb() {
        for (final T item : myItems) {
            if (item instanceof Bomb) {
                return (Bomb) item;
            }
        }
        return null;
    }

    /**
     * Clears all items from the inventory.
     */
    public void clear() {
        myItems.clear();
    }
}