package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

/**
 * A potion that gives the player a temporary vision boost.
 *
 * @author Devin Arroyo
 * @author Mark Malyshev
 * @version 1.1
 */
public class VisionPotion extends Potion {
    /**
     * The duration of the vision boost in game ticks.
     */
    private final int myDurationInTicks;
    /**
     * The multiplier for the player's vision range.
     */
    private final double myVisionMultiplier;

    /**
     * Constructs a VisionPotion object.
     *
     * @param theX                The x-coordinate of the potion.
     * @param theY                The y-coordinate of the potion.
     * @param theWidth            The width of the potion.
     * @param theHeight           The height of the potion.
     * @param theDurationInTicks  The duration of the vision boost.
     * @param theVisionMultiplier The multiplier for the vision range.
     */
    public VisionPotion(final int theX, final int theY, final int theWidth, final int theHeight, final int theDurationInTicks, final double theVisionMultiplier) {
        super(theX, theY, theWidth, theHeight,
                "Boosts player vision by " + theVisionMultiplier + " for " + theDurationInTicks + " ticks.");
        myDurationInTicks = theDurationInTicks;
        myVisionMultiplier = theVisionMultiplier;
    }

    /**
     * Applies the vision boost effect to the player.
     *
     * @param theTarget The player who consumes the potion.
     */
    @Override
    public void applyEffect(final Player theTarget) {
        theTarget.setVisionBoost(true, myVisionMultiplier, myDurationInTicks);
    }

    /**
     * Gets the name of the potion.
     *
     * @return The name "vision_potion".
     */
    public String getName() {
        return "vision_potion";
    }

}