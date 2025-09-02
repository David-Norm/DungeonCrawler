package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

/**
 * A potion that deals instant damage to the player.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @author Mark Malyshev
 * @version 1.3
 */
public class PoisonPotion extends Potion {
    /**
     * The duration of the poison effect in game ticks.
     */
    private final int myDurationInTicks;

    /**
     * Constructs a PoisonPotion object.
     *
     * @param theX               The x-coordinate of the potion.
     * @param theY               The y-coordinate of the potion.
     * @param theWidth           The width of the potion.
     * @param theHeight          The height of the potion.
     * @param theDurationInTicks The duration of the poison effect in game ticks.
     */
    public PoisonPotion(final int theX, final int theY, final int theWidth, final int theHeight, final int theDurationInTicks) {
        super(theX, theY, theWidth, theHeight, "Player is poisoned for " + theDurationInTicks / 60 + " seconds.");
        myDurationInTicks = theDurationInTicks;
    }

    /**
     * Applies the poison effect to the player.
     *
     * @param theTarget The player who consumes the potion.
     */
    @Override
    public void applyEffect(final Player theTarget) {
        theTarget.setPoisoned(true, myDurationInTicks);
    }

    /**
     * Gets the name of the potion.
     *
     * @return The name "poison_potion".
     */
    @Override
    public String getName() {
        return "poison_potion";
    }
}