package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

/**
 * An example potion that uses the potion factory.
 * This potion restores a specified amount of health to the player.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @author Mark Malyshev
 * @version 1.3
 */
public class HealthPotion extends Potion {
    /**
     * The amount of health this potion restores.
     */
    private final int myHealAmount;

    /**
     * Constructs a HealthPotion object.
     *
     * @param theX          The x-coordinate of the potion.
     * @param theY          The y-coordinate of the potion.
     * @param theWidth      The width of the potion.
     * @param theHeight     The height of the potion.
     * @param theHealAmount The amount of health to be restored.
     */
    public HealthPotion(final int theX, final int theY, final int theWidth, final int theHeight, final int theHealAmount) {
        super(theX, theY, theWidth, theHeight, "Restores " + theHealAmount + " health.");
        myHealAmount = theHealAmount;
    }

    /**
     * Applies the healing effect to the player.
     *
     * @param theTarget The player who consumes the potion.
     */
    @Override
    public void applyEffect(final Player theTarget) {
        theTarget.heal(myHealAmount);
    }

    /**
     * Gets the name of the potion.
     *
     * @return The name "health_potion".
     */
    @Override
    public String getName() {
        return "health_potion";
    }
}