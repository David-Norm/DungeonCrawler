package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * Represents the exit of the dungeon.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public class Exit extends GameObject {

    /**
     * Constructs an Exit object with specified position and dimensions.
     *
     * @param theX      The x-coordinate of the exit.
     * @param theY      The y-coordinate of the exit.
     * @param theWidth  The width of the exit.
     * @param theHeight The height of the exit.
     */
    public Exit(final int theX, final int theY, final int theWidth, final int theHeight) {
        super(theX, theY, theWidth, theHeight);
    }

    /**
     * Updates the state of the exit. This method is empty as the exit is a static object.
     */
    @Override
    public void update() {
    }
}