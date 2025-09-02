package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

import java.util.Random;

/**
 * A potion that applies a random effect, which can be beneficial or harmful.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @author Mark Malyshev
 * @version 1.2
 */
public class MysteryPotion extends Potion {
    /**
     * A random number generator for selecting the effect.
     */
    private static final Random myRandom = new Random();

    /**
     * Constructs a MysteryPotion object.
     *
     * @param theX      The x-coordinate of the potion.
     * @param theY      The y-coordinate of the potion.
     * @param theWidth  The width of the potion.
     * @param theHeight The height of the potion.
     */
    public MysteryPotion(final int theX, final int theY, final int theWidth, final int theHeight) {
        super(theX, theY, theWidth, theHeight, "A potion with a random, unknown effect.");
    }

    /**
     * Gets the name of the potion.
     *
     * @return The name "mystery_potion".
     */
    @Override
    public String getName() {
        return "mystery_potion";
    }

    /**
     * Applies a random effect to the player upon consumption.
     * The effect is chosen from a predefined set of beneficial and harmful outcomes.
     *
     * @param theTarget The player who consumes the potion.
     */
    @Override
    public void applyEffect(final Player theTarget) {
        final int effectChoice = myRandom.nextInt(7);
        switch (effectChoice) {
            case 0 -> new HealthPotion(0, 0, 0, 0, 50).applyEffect(theTarget);
            case 1 -> new PoisonPotion(0, 0, 0, 0, 25).applyEffect(theTarget);
            case 2 -> new SpeedPotion(0, 0, 0, 0, 60, 1.5).applyEffect(theTarget);
            case 3 -> theTarget.setSlowed(true, 60);
            case 4 -> theTarget.takeDamage(theTarget.getHealth());
            case 5 -> new VisionPotion(0, 0, 0, 0, 60, 1.5).applyEffect(theTarget);
            case 6 -> theTarget.setBlinded(true, 60);
        }
    }
}