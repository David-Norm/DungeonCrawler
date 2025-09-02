package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

/**
 * A potion that gives the player a temporary speed boost.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @author Mark Malyshev
 * @version 1.2
 */
public class SpeedPotion extends Potion {
    /**
     * The duration of the speed boost in game ticks.
     */
    private final int myDurationInTicks;
    /**
     * The multiplier for the player's movement speed.
     */
    private final double mySpeedMultiplier;

    /**
     * Constructs a SpeedPotion object.
     *
     * @param theX               The x-coordinate of the potion.
     * @param theY               The y-coordinate of the potion.
     * @param theWidth           The width of the potion.
     * @param theHeight          The height of the potion.
     * @param theDurationInTicks The duration of the speed boost.
     * @param theSpeedMultiplier The multiplier for the speed.
     */
    public SpeedPotion(final int theX, final int theY, final int theWidth, final int theHeight, final int theDurationInTicks, final double theSpeedMultiplier) {
        super(theX, theY, theWidth, theHeight,
                "Boosts player speed by " + theSpeedMultiplier + " for " + theDurationInTicks + " ticks.");
        myDurationInTicks = theDurationInTicks;
        mySpeedMultiplier = theSpeedMultiplier;
    }

    /**
     * Applies the speed boost effect to the player.
     *
     * @param theTarget The player who consumes the potion.
     */
    @Override
    public void applyEffect(final Player theTarget) {
        theTarget.setSpeedBoost(true, mySpeedMultiplier, myDurationInTicks);
    }

    /**
     * Gets the name of the potion.
     *
     * @return The name "speed_potion".
     */
    public String getName() {
        return "speed_potion";
    }

}