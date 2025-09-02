package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * A Chest object within the dungeon.
 * Can hold items
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public class Chest extends GameObject {
    /**
     * A flag indicating whether the chest is open or closed.
     */
    private boolean myIsOpen;

    /**
     * Constructs a Chest object with specified position and dimensions.
     * The chest starts initially closed.
     *
     * @param theX      The x-coordinate of the chest.
     * @param theY      The y-coordinate of the chest.
     * @param theWidth  The width of the chest.
     * @param theHeight The height of the chest.
     */
    public Chest(final int theX, final int theY, final int theWidth, final int theHeight) {
        super(theX, theY, theWidth, theHeight);
        myIsOpen = false;
    }

    /**
     * Checks if the chest is currently open.
     *
     * @return true if the chest is open, false otherwise.
     */
    public boolean isOpen() {
        return myIsOpen;
    }

    /**
     * Opens the chest if it's not already open.
     */
    public void open() {
        if (!myIsOpen) {
            myIsOpen = true;
        }
    }

    /**
     * Updates the state of the chest. This method is currently empty as chests are static objects.
     */
    @Override
    public void update() {
    }
}