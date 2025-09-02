package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * Dungeon wall tiles.
 *
 * @author Devin Arroyo
 * @version 1.2
 */
public class Wall extends GameObject {
    /**
     * The type of wall, used to determine its specific appearance.
     */
    private final int myWallType;
    /**
     * The identifier for the wall's sprite, derived from its biome and type.
     */
    private final String mySpriteIdentifier;

    /**
     * Constructs a Wall object with specified position, dimensions, type, and biome.
     *
     * @param theX        The x-coordinate of the wall.
     * @param theY        The y-coordinate of the wall.
     * @param theWidth    The width of the wall.
     * @param theHeight   The height of the wall.
     * @param theWallType An integer representing the specific wall type.
     * @param theBiome    An integer representing the biome the wall belongs to.
     */
    public Wall(final int theX, final int theY, final int theWidth, final int theHeight, final int theWallType, final int theBiome) {
        super(theX, theY, theWidth, theHeight);
        myWallType = theWallType;
        mySpriteIdentifier = getSpriteIdentifierFromWallType(theBiome, theWallType);
    }

    /**
     * Updates the state of the wall. This method is empty as walls are static objects.
     */
    @Override
    public void update() {
    }

    /**
     * Gets the wall's specific type.
     *
     * @return The integer wall type.
     */
    public int getWallType() {
        return myWallType;
    }

    /**
     * Gets the identifier for the wall's sprite.
     *
     * @return A string representing the sprite identifier.
     */
    public String getSpriteIdentifier() {
        return mySpriteIdentifier;
    }

    /**
     * Determines the sprite identifier string based on the biome and wall type.
     * The wall type integers are mapped to specific characters ('A'-'Z', 'a'-'f').
     *
     * @param theBiome    The biome the wall is in.
     * @param theWallType The integer type of the wall.
     * @return A string formatted as "wall_sprite_biome[biome]_[character]", or null if the wall type is invalid.
     */
    private String getSpriteIdentifierFromWallType(final int theBiome, final int theWallType) {
        char wallCharacter;

        if (theWallType >= 11 && theWallType <= 36) {
            wallCharacter = (char) ('A' + (theWallType - 11));
        } else if (theWallType >= 37 && theWallType <= 42) {
            wallCharacter = (char) ('a' + (theWallType - 37));
        } else {
            return null;
        }

        return "wall_sprite_biome" + theBiome + "_" + wallCharacter;
    }
}