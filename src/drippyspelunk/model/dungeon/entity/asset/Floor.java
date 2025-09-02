package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * Dungeon Floor Tiles
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public class Floor extends GameObject {
    /**
     * The type of floor, used to determine its specific appearance.
     */
    private final int myFloorType;
    /**
     * The identifier for the floor's sprite, derived from its biome and type.
     */
    private final String mySpriteIdentifier;

    /**
     * Constructs a Floor object with specified position, dimensions, type, and biome.
     *
     * @param theX         The x-coordinate of the floor tile.
     * @param theY         The y-coordinate of the floor tile.
     * @param theWidth     The width of the floor tile.
     * @param theHeight    The height of the floor tile.
     * @param theFloorType An integer representing the specific floor type.
     * @param theBiome     An integer representing the biome the floor belongs to.
     */
    public Floor(final int theX, final int theY, final int theWidth, final int theHeight, final int theFloorType, final int theBiome) {
        super(theX, theY, theWidth, theHeight);
        myFloorType = theFloorType;
        mySpriteIdentifier = getSpriteIdentifierFromFloorType(theBiome, theFloorType);
    }

    /**
     * Updates the state of the floor. This method is empty as floor tiles are static objects.
     */
    @Override
    public void update() {
    }

    /**
     * Gets the floor's specific type.
     *
     * @return The integer floor type.
     */
    public int getFloorType() {
        return myFloorType;
    }

    /**
     * Gets the identifier for the floor's sprite.
     *
     * @return A string representing the sprite identifier.
     */
    public String getSpriteIdentifier() {
        return mySpriteIdentifier;
    }

    /**
     * Determines the sprite identifier string based on the biome and floor type.
     * The floor type integers (1-10) are mapped to a specific character ('a'-'j').
     *
     * @param theBiome     The biome the floor is in.
     * @param theFloorType The integer type of the floor.
     * @return A string formatted as "floor_sprite_biome[biome]_[character]", or null if the floor type is invalid.
     */
    private String getSpriteIdentifierFromFloorType(final int theBiome, final int theFloorType) {
        if (theFloorType > 0 && theFloorType <= 10) {
            return "floor_sprite_biome" + theBiome + "_" + (char) ('a' + theFloorType - 1);
        }
        return null;
    }
}