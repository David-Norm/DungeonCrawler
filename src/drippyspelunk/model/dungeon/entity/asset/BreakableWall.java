package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * A wall object that can be broken, specifically by a bomb.
 * Currently just exists as a placeholder and can be set to broken externally.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public class BreakableWall extends GameObject {
    /**
     * A flag indicating whether the wall is broken.
     */
    private boolean myIsBroken;

    /**
     * Constructs a BreakableWall object with specified position and dimensions.
     * The wall is initially intact.
     *
     * @param theX      The x-coordinate of the wall.
     * @param theY      The y-coordinate of the wall.
     * @param theWidth  The width of the wall.
     * @param theHeight The height of the wall.
     */
    public BreakableWall(final int theX, final int theY, final int theWidth, final int theHeight) {
        super(theX, theY, theWidth, theHeight);
        myIsBroken = false; // Starts intact
    }

    /**
     * Checks if the wall is broken.
     *
     * @return true if the wall is broken, false otherwise.
     */
    public boolean isBroken() {
        return myIsBroken;
    }

    /**
     * Sets the wall's status to broken.
     */
    public void breakWall() {
        myIsBroken = true;
    }

    /**
     * Updates the state of the wall. This method is empty as the wall is a static object.
     */
    @Override
    public void update() {
    }
}