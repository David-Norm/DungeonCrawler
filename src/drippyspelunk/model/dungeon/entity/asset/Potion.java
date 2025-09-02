package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

/**
 * Base class for all potions.
 *
 * @author Devin Arroyo
 * @author Mark Malyshev
 * @author David Norman
 * @version 1.3
 */
public abstract class Potion extends Consumable {
    /**
     * A description of the potion's effect.
     */
    protected String myEffectDescription;

    /**
     * Constructs a Potion object with a specified position, dimensions, and effect description.
     *
     * @param theX                 The x-coordinate of the potion.
     * @param theY                 The y-coordinate of the potion.
     * @param theWidth             The width of the potion.
     * @param theHeight            The height of the potion.
     * @param theEffectDescription A string describing what the potion does.
     */
    public Potion(final int theX, final int theY, final int theWidth, final int theHeight, final String theEffectDescription) {
        super(theX, theY, theWidth, theHeight);
        myEffectDescription = theEffectDescription;
    }

    /**
     * Gets the description of the potion's effect.
     *
     * @return The effect description string.
     */
    public String getEffectDescription() {
        return myEffectDescription;
    }

    /**
     * Abstract method to apply the potion's effect to a player.
     *
     * @param theTarget The player who will receive the effect.
     */
    public abstract void applyEffect(final Player theTarget);

    /**
     * Updates the state of the potion. This method is currently empty as potions are static assets until picked up.
     */
    @Override
    public void update() {
        // No action required for a static object
    }

    /**
     * Gets the name of the potion.
     *
     * @return The name "potion".
     */
    public String getName() {
        return "potion";
    }

}