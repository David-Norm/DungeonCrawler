package drippyspelunk.model.dungeon;

import drippyspelunk.model.dungeon.factory.GameObjectFactory;

import java.awt.*;
import java.util.List;

/**
 * Class that generates rooms based on tile presets.
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public class RoomGenerator {

    /**
     * Generates the solid wall and obstacle layout for a room.
     *
     * @param theRoom       The room to generate the layout in.
     * @param theDoorBounds The list of door boundaries to avoid placing walls.
     * @param theLayout     The 2D array representing the wall layout.
     * @param theBiome      The biome type for the wall tiles.
     */
    public static void generateRoomLayout(final Room theRoom, final List<Rectangle> theDoorBounds, final int[][] theLayout, final int theBiome) {
        for (int i = 0; i < theLayout.length; i++) {
            for (int j = 0; j < theLayout[i].length; j++) {
                if (theLayout[i][j] != 0) {
                    int x = j * Room.TILE_SIZE;
                    int y = i * Room.TILE_SIZE;
                    boolean isDoorLocation = false;
                    Rectangle wallRect = new Rectangle(x, y, Room.TILE_SIZE, Room.TILE_SIZE);

                    for (Rectangle doorRect : theDoorBounds) {
                        if (wallRect.intersects(doorRect)) {
                            isDoorLocation = true;
                            break;
                        }
                    }

                    if (!isDoorLocation) {
                        int wallType = theLayout[i][j];
                        theRoom.addObject(GameObjectFactory.createGameObject(GameObjectFactory.TYPE_WALL, x, y, Room.TILE_SIZE, Room.TILE_SIZE, wallType, theBiome));
                    }
                }
            }
        }
    }

    /**
     * Generates the background tiles for a room.
     *
     * @param theRoom    The room to generate the background tiles in.
     * @param theTileset The 2D array representing the background tileset.
     * @param theBiome   The biome type for the background tiles.
     */
    public static void generateBackgroundTiles(final Room theRoom, final int[][] theTileset, final int theBiome) {
        for (int i = 0; i < theTileset.length; i++) {
            for (int j = 0; j < theTileset[i].length; j++) {
                if (theTileset[i][j] != 0) {
                    int x = j * Room.TILE_SIZE;
                    int y = i * Room.TILE_SIZE;
                    int floorType = theTileset[i][j];
                    theRoom.addFloorObject(GameObjectFactory.createGameObject(GameObjectFactory.TYPE_FLOOR, x, y, Room.TILE_SIZE, Room.TILE_SIZE, floorType, theBiome));
                }
            }
        }
    }
}