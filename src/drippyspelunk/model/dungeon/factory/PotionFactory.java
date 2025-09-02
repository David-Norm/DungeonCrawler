package drippyspelunk.model.dungeon.factory;

import drippyspelunk.model.dungeon.entity.asset.*;

import java.util.Random;

/**
 * A factory class for creating various Potion instances.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.3
 */
public class PotionFactory {

    /**
     * The string constant for a healing potion type.
     */
    public static final String TYPE_HEALING = "healing";
    /**
     * The string constant for a poison potion type.
     */
    public static final String TYPE_POISON = "poison";
    /**
     * The string constant for a speed potion type.
     */
    public static final String TYPE_SPEED = "speed";
    /**
     * The string constant for a vision potion type.
     */
    public static final String TYPE_VISION = "vision";
    /**
     * The string constant for a mystery potion type.
     */
    public static final String TYPE_MYSTERY = "mystery";

    /**
     * A random number generator.
     */
    private static final Random myRandom = new Random();

    /**
     * Creates a Potion instance of a specified type with given parameters.
     *
     * @param theType             The type of potion to create.
     * @param theX                The x-coordinate of the potion.
     * @param theY                The y-coordinate of the potion.
     * @param theWidth            The width of the potion.
     * @param theHeight           The height of the potion.
     * @param theAdditionalParams Variable number of parameters specific to the potion type.
     * @return The created Potion object.
     * @throws IllegalArgumentException if the potion type is unknown or parameters are invalid.
     */
    public static Potion createPotion(
            final String theType,
            final int theX,
            final int theY,
            final int theWidth,
            final int theHeight,
            final Object... theAdditionalParams) {

        return switch (theType.toLowerCase()) {
            case TYPE_HEALING -> {
                if (theAdditionalParams.length == 1 && theAdditionalParams[0] instanceof Integer) {
                    yield new HealthPotion(theX, theY, theWidth, theHeight, (int) theAdditionalParams[0]);
                } else {
                    throw new IllegalArgumentException("Health Potion Spawn Failed, invalid parameters.");
                }
            }
            case TYPE_POISON -> {
                if (theAdditionalParams.length == 1 &&
                        theAdditionalParams[0] instanceof Integer) {
                    yield new PoisonPotion(theX, theY, theWidth, theHeight, (int) theAdditionalParams[0]);
                } else {
                    throw new IllegalArgumentException("Poison Potion Spawn Failed, invalid parameters.");
                }
            }
            case TYPE_SPEED -> {
                if (theAdditionalParams.length == 2 &&
                        theAdditionalParams[0] instanceof Integer &&
                        theAdditionalParams[1] instanceof Double) {
                    yield new SpeedPotion(theX, theY, theWidth, theHeight, (int) theAdditionalParams[0], (double) theAdditionalParams[1]);
                } else {
                    throw new IllegalArgumentException("Speed Potion Spawn Failed, invalid parameters.");
                }
            }
            case TYPE_VISION -> {
                if (theAdditionalParams.length == 2 &&
                        theAdditionalParams[0] instanceof Integer &&
                        theAdditionalParams[1] instanceof Double) {
                    yield new VisionPotion(theX, theY, theWidth, theHeight, (int) theAdditionalParams[0], (double) theAdditionalParams[1]);
                } else {
                    throw new IllegalArgumentException("Vision Potion Spawn Failed, invalid parameters.");
                }
            }
            case TYPE_MYSTERY -> new MysteryPotion(theX, theY, theWidth, theHeight);

            default -> throw new IllegalArgumentException("Unknown Potion type: " + theType);
        };
    }

    /**
     * Creates a random Potion instance.
     *
     * @param theX      The x-coordinate of the potion.
     * @param theY      The y-coordinate of the potion.
     * @param theWidth  The width of the potion.
     * @param theHeight The height of the potion.
     * @return A randomly created Potion object.
     */
    public static Potion createRandomPotion(final int theX, final int theY, final int theWidth, final int theHeight) {
        final int potionType = myRandom.nextInt(5);

        return switch (potionType) {
            case 0 -> createPotion(TYPE_HEALING, theX, theY, theWidth, theHeight, 50);
            case 1 -> createPotion(TYPE_POISON, theX, theY, theWidth, theHeight, myRandom.nextInt(110, 600) + 1);
            case 2 -> createPotion(TYPE_SPEED, theX, theY, theWidth, theHeight, 600, 1.5);
            case 3 -> createPotion(TYPE_VISION, theX, theY, theWidth, theHeight, 600, 1.5);
            case 4 -> createPotion(TYPE_MYSTERY, theX, theY, theWidth, theHeight);
            default -> null;
        };
    }
}