package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * A Pillar object within the dungeon.
 * This class extends GameObject but has no unique functionality, serving primarily as a static,
 * impassable obstacle in the game world.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public class Pillar extends GameObject {

    /**
     * Constructs a Pillar object with specified position and dimensions.
     *
     * @param theX      The x-coordinate of the pillar.
     * @param theY      The y-coordinate of the pillar.
     * @param theWidth  The width of the pillar.
     * @param theHeight The height of the pillar.
     */
    public Pillar(final int theX, final int theY, final int theWidth, final int theHeight) {
        super(theX, theY, theWidth, theHeight);
    }

    /**
     * Updates the state of the pillar. This method is empty as the pillar is a static object.
     */
    @Override
    public void update() {
    }
}