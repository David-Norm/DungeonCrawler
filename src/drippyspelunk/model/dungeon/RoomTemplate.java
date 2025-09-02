package drippyspelunk.model.dungeon;

/**
 * A data class for a room layout and tileset, parsed from an ini file.
 *
 * @author Devin Arroyo
 * @version 1.2
 */
public class RoomTemplate {

    /**
     * A 2D array representing the layout of the room.
     */
    private final int[][] myLayout;
    /**
     * A 2D array representing the tileset used for the room's appearance.
     */
    private final int[][] myTileset;
    /**
     * A 2D array representing the spawn points within the room.
     */
    private final int[][] mySpawns;
    /**
     * The chance of an enemy spawning in the room.
     */
    private final int myEnemyChance;
    /**
     * The chance of a potion spawning in the room.
     */
    private final int myPotionChance;
    /**
     * The chance of a chest spawning in the room.
     */
    private final int myChestChance;
    /**
     * The chance of a bomb spawning in the room.
     */
    private final int myBombChance;
    /**
     * The chance of a trap spawning in the room.
     */
    private final int myTrapChance;
    /**
     * The chance of a breakable wall appearing in the room.
     */
    private final int myBreakableWallChance;

    /**
     * Constructs a new RoomTemplate with the specified layout, tileset, and spawn information.
     *
     * @param theLayout              The 2D array for the room's layout.
     * @param theTileset             The 2D array for the room's tileset.
     * @param theSpawns              The 2D array for the spawn points.
     * @param theEnemyChance         The chance of an enemy spawning.
     * @param thePotionChance        The chance of a potion spawning.
     * @param theChestChance         The chance of a chest spawning.
     * @param theBombChance          The chance of a bomb spawning.
     * @param theTrapChance          The chance of a trap spawning.
     * @param theBreakableWallChance The chance of a breakable wall appearing.
     */
    public RoomTemplate(final int[][] theLayout, final int[][] theTileset, final int[][] theSpawns,
                        final int theEnemyChance, final int thePotionChance, final int theChestChance,
                        final int theBombChance, final int theTrapChance,
                        final int theBreakableWallChance) {
        myLayout = theLayout;
        myTileset = theTileset;
        mySpawns = theSpawns;
        myEnemyChance = theEnemyChance;
        myPotionChance = thePotionChance;
        myChestChance = theChestChance;
        myBombChance = theBombChance;
        myTrapChance = theTrapChance;
        myBreakableWallChance = theBreakableWallChance;
    }

    /**
     * Gets the 2D array representing the room's layout.
     *
     * @return The room's layout.
     */
    public int[][] getLayout() {
        return myLayout;
    }

    /**
     * Gets the 2D array representing the room's tileset.
     *
     * @return The room's tileset.
     */
    public int[][] getTileset() {
        return myTileset;
    }

    /**
     * Gets the 2D array representing the spawn points.
     *
     * @return The spawn points.
     */
    public int[][] getSpawns() {
        return mySpawns;
    }

    /**
     * Gets the chance of an enemy spawning.
     *
     * @return The enemy spawn chance.
     */
    public int getEnemyChance() {
        return myEnemyChance;
    }

    /**
     * Gets the chance of a potion spawning.
     *
     * @return The potion spawn chance.
     */
    public int getPotionChance() {
        return myPotionChance;
    }

    /**
     * Gets the chance of a chest spawning.
     *
     * @return The chest spawn chance.
     */
    public int getChestChance() {
        return myChestChance;
    }

    /**
     * Gets the chance of a bomb spawning.
     *
     * @return The bomb spawn chance.
     */
    public int getBombChance() {
        return myBombChance;
    }

    /**
     * Gets the chance of a trap spawning.
     *
     * @return The trap spawn chance.
     */
    public int getTrapChance() {
        return myTrapChance;
    }

    /**
     * Gets the chance of a breakable wall appearing.
     *
     * @return The breakable wall chance.
     */
    public int getBreakableWallChance() {
        return myBreakableWallChance;
    }
}