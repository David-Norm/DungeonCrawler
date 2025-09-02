package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

/**
 * A Bomb object within the dungeon.
 * Represents a consumable item that can be used by the player.
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public class Bomb extends Consumable {

    /**
     * Constructs a Bomb object with specified position and dimensions.
     *
     * @param theX      The x-coordinate of the bomb.
     * @param theY      The y-coordinate of the bomb.
     * @param theWidth  The width of the bomb.
     * @param theHeight The height of the bomb.
     */
    public Bomb(final int theX, final int theY, final int theWidth, final int theHeight) {
        super(theX, theY, theWidth, theHeight);
    }

    /**
     * Applies the effect of the bomb.
     * This method is currently empty as the game logic handles the bomb's effect (e.g., breaking walls).
     *
     * @param thePlayer The player who is using the bomb.
     */
    @Override
    public void applyEffect(final Player thePlayer) {
        // Game logic, not here handles the effect of the bomb externally.
    }

    /**
     * Gets the name of the bomb consumable.
     *
     * @return The name "bomb_consumable".
     */
    @Override
    public String getName() {
        return "bomb_consumable";
    }

}