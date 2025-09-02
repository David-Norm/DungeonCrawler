package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

/**
 * Base class for all Consumables.
 * Consumable items are objects that can be picked up and used by the player,
 * applying some effect.
 *
 * @author Devin Arroyo
 * @author Mark Malyshev
 * @version 1.2
 */
public abstract class Consumable extends GameObject {

    /**
     * Constructs a Consumable object with specified position and dimensions.
     *
     * @param theX      The x-coordinate of the consumable.
     * @param theY      The y-coordinate of the consumable.
     * @param theWidth  The width of the consumable.
     * @param theHeight The height of the consumable.
     */
    public Consumable(final int theX, final int theY, final int theWidth, final int theHeight) {
        super(theX, theY, theWidth, theHeight);
    }

    /**
     * Updates the state of the consumable. This method is empty as consumables are static assets until picked up.
     */
    @Override
    public void update() {
        // No action required for a static object
    }

    /**
     * Gets the name of the consumable item.
     *
     * @return The name of the item.
     */
    public abstract String getName();

    /**
     * Applies the effect of the consumable to the player.
     *
     * @param thePlayer The player who is consuming the item.
     */
    public abstract void applyEffect(final Player thePlayer);
}