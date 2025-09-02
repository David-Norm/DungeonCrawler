package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * A Trap object within the dungeon.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.1
 */
public class Trap extends GameObject {

    /**
     * Constructs a Trap object with specified position and dimensions.
     *
     * @param theX      The x-coordinate of the trap.
     * @param theY      The y-coordinate of the trap.
     * @param theWidth  The width of the trap.
     * @param theHeight The height of the trap.
     */
    public Trap(final int theX, final int theY, final int theWidth, final int theHeight) {
        super(theX, theY, theWidth, theHeight);
    }

    /**
     * Updates the state of the trap. This method is currently empty as the trap is a static object.
     */
    @Override
    public void update() {
    }
}