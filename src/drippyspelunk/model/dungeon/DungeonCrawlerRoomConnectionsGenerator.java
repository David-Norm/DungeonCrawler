package drippyspelunk.model.dungeon;

import drippyspelunk.controller.config.RoomConfigManager;
import drippyspelunk.controller.database.DatabaseManager;
import drippyspelunk.model.dungeon.entity.GameObject;
import drippyspelunk.model.dungeon.entity.asset.Door;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Character;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Enemy;
import drippyspelunk.model.dungeon.factory.GameObjectFactory;
import drippyspelunk.model.dungeon.factory.PotionFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Dungeon generator for creating rooms with connections and content.
 *
 * @author Devin Arroyo
 * @author Mark Malyshev
 * @author David Norman
 * @version 1.9
 */
public class DungeonCrawlerRoomConnectionsGenerator {

    /**
     * Represents a hallway in the dungeon grid.
     */
    private static final int HALLWAY = 1;
    /**
     * Represents a blank room type.
     */
    private static final int BLANK_ROOM = 2;
    /**
     * Represents a blank room leading to a pillar room.
     */
    private static final int BLANK_ROOM_TO_PILLAR = 3;
    /**
     * Represents a room with a breakable wall.
     */
    private static final int BREAKABLE_WALL_ROOM = 11;
    /**
     * Constant for guaranteed potion spawn.
     */
    private static final int GUARANTEED_POTION = 1;
    /**
     * Constant for guaranteed enemy spawn.
     */
    private static final int GUARANTEED_ENEMY = 2;
    /**
     * Constant for guaranteed chest spawn.
     */
    private static final int GUARANTEED_CHEST = 3;
    /**
     * Constant for random potion spawn.
     */
    private static final int RANDOM_POTION = 4;
    /**
     * Constant for random enemy spawn.
     */
    private static final int RANDOM_ENEMY = 5;
    /**
     * Constant for random chest spawn.
     */
    private static final int RANDOM_CHEST = 6;
    /**
     * Constant for guaranteed bomb spawn.
     */
    private static final int GUARANTEED_BOMB = 7;
    /**
     * Constant for random bomb spawn.
     */
    private static final int RANDOM_BOMB = 8;
    /**
     * Constant for guaranteed trap spawn.
     */
    private static final int GUARANTEED_TRAP = 9;
    /**
     * Constant for random trap spawn.
     */
    private static final int RANDOM_TRAP = 10;
    /**
     * Constant for guaranteed breakable wall spawn.
     */
    private static final int GUARANTEED_BREAKABLE_WALL = 11;
    /**
     * Constant for random breakable wall spawn.
     */
    private static final int RANDOM_BREAKABLE_WALL = 12;
    /**
     * Constant for guaranteed pillar spawn.
     */
    private static final int GUARANTEED_PILLAR = 13;
    /**
     * Constant for guaranteed exit spawn.
     */
    private static final int GUARANTEED_EXIT = 14;
    /**
     * Width of a horizontal door in pixels.
     */
    private static final int DOOR_WIDTH_HORIZONTAL = 3 * Room.TILE_SIZE;
    /**
     * Height of a horizontal door in pixels.
     */
    private static final int DOOR_HEIGHT_HORIZONTAL = Room.TILE_SIZE;
    /**
     * Width of a vertical door in pixels.
     */
    private static final int DOOR_WIDTH_VERTICAL = Room.TILE_SIZE;
    /**
     * Height of a vertical door in pixels.
     */
    private static final int DOOR_HEIGHT_VERTICAL = 3 * Room.TILE_SIZE;
    /**
     * Width of a spawned enemy in pixels.
     */
    private static final int ENEMY_WIDTH = 32;
    /**
     * Height of a spawned enemy in pixels.
     */
    private static final int ENEMY_HEIGHT = 32;
    /**
     * Height of a room grid in tiles.
     */
    private static final int ROOM_GRID_HEIGHT = 13;
    /**
     * Width of a room grid in tiles.
     */
    private static final int ROOM_GRID_WIDTH = 15;
    /**
     * A random number generator.
     */
    private final Random myRandom = new Random();
    /**
     * The database manager for accessing game data.
     */
    private final DatabaseManager myDBManager;
    /**
     * The room configuration manager.
     */
    private final RoomConfigManager myRoomConfigManager;

    /**
     * Constructs a new DungeonCrawlerRoomConnectionsGenerator.
     *
     * @param theDifficulty The difficulty level to use for room configuration.
     */
    public DungeonCrawlerRoomConnectionsGenerator(final int theDifficulty) {
        myDBManager = DatabaseManager.getMyInstance();
        myRoomConfigManager = RoomConfigManager.getInstance(theDifficulty);
    }

    /**
     * Creates a map of rooms based on the provided dungeon grid.
     *
     * @param theGrid The 2D integer array representing the dungeon layout.
     * @return A map of room IDs to Room objects.
     */
    public Map<Integer, Room> createDungeonFromGrid(final int[][] theGrid) {
        final Map<Integer, Room> rooms = new HashMap<>();

        for (int rowIndex = 0; rowIndex < theGrid.length; rowIndex = rowIndex + 2) {
            for (int columnIndex = 0; columnIndex < theGrid[rowIndex].length; columnIndex = columnIndex + 2) {

                if (theGrid[rowIndex][columnIndex] >= 2) {
                    final int roomID = rowIndex * theGrid.length + columnIndex;
                    final String roomTemplate = String.valueOf(roomID);

                    final int roomType = theGrid[rowIndex][columnIndex];
                    final int biome = getBiomeForRoomType(roomType);

                    final List<Rectangle> doorBounds = getDoorBounds(theGrid, rowIndex, columnIndex);

                    final RoomTemplate template = selectRoomTemplate(roomType, doorBounds.size(), theGrid, rowIndex, columnIndex);

                    final Room temp_room = new Room(roomID, roomTemplate);

                    RoomGenerator.generateBackgroundTiles(temp_room, template.getTileset(), biome);

                    RoomGenerator.generateRoomLayout(temp_room, doorBounds, template.getLayout(), biome);

                    createAndAddDoors(temp_room, theGrid, rowIndex, columnIndex);

                    spawnObjectsFromGrid(temp_room, template, biome);

                    if (roomType == BREAKABLE_WALL_ROOM) {
                        temp_room.addObject(GameObjectFactory.createGameObject(GameObjectFactory.TYPE_BREAKABLE_WALL, Room.TILE_SIZE * 5, Room.TILE_SIZE * 6, 32, 32));
                    }

                    rooms.put(roomID, temp_room);
                }
            }
        }
        return rooms;
    }

    /**
     * Determines the biome integer based on the room type.
     *
     * @param theRoomType The integer representing the room type.
     * @return The biome integer.
     */
    private int getBiomeForRoomType(final int theRoomType) {
        return switch (theRoomType) {
            case BLANK_ROOM -> 1;
            case BLANK_ROOM_TO_PILLAR -> 2;
            case 4 -> 3;
            case 5 -> 4;
            case 6 -> 5;
            default -> 1;
        };
    }

    /**
     * Calculates the bounding rectangles for doors in a room.
     *
     * @param theGrid        The dungeon grid.
     * @param theRowIndex    The row index of the room.
     * @param theColumnIndex The column index of the room.
     * @return A list of rectangles representing the door bounds.
     */
    private List<Rectangle> getDoorBounds(final int[][] theGrid, final int theRowIndex, final int theColumnIndex) {
        final List<Rectangle> doorBounds = new ArrayList<>();
        if (theRowIndex - 2 >= 0 && theGrid[theRowIndex - 1][theColumnIndex] == HALLWAY) {
            doorBounds.add(new Rectangle((Room.GRID_WIDTH / 2 - 1) * Room.TILE_SIZE, 0, DOOR_WIDTH_HORIZONTAL, DOOR_HEIGHT_HORIZONTAL));
        }
        if (theColumnIndex + 2 < theGrid[theRowIndex].length && theGrid[theRowIndex][theColumnIndex + 1] == HALLWAY) {
            doorBounds.add(new Rectangle((Room.GRID_WIDTH - 1) * Room.TILE_SIZE, (Room.GRID_HEIGHT / 2 - 1) * Room.TILE_SIZE, DOOR_WIDTH_VERTICAL, DOOR_HEIGHT_VERTICAL));
        }
        if (theRowIndex + 2 < theGrid.length && theGrid[theRowIndex + 1][theColumnIndex] == HALLWAY) {
            doorBounds.add(new Rectangle((Room.GRID_WIDTH / 2 - 1) * Room.TILE_SIZE, (Room.GRID_HEIGHT - 1) * Room.TILE_SIZE, DOOR_WIDTH_HORIZONTAL, DOOR_HEIGHT_HORIZONTAL));
        }
        if (theColumnIndex - 2 >= 0 && theGrid[theRowIndex][theColumnIndex - 1] == HALLWAY) {
            doorBounds.add(new Rectangle(0, (Room.GRID_HEIGHT / 2 - 1) * Room.TILE_SIZE, DOOR_WIDTH_VERTICAL, DOOR_HEIGHT_VERTICAL));
        }
        return doorBounds;
    }

    /**
     * Creates and adds door objects to a room based on the dungeon grid.
     *
     * @param room           The room to add doors to.
     * @param theGrid        The dungeon grid.
     * @param theRowIndex    The row index of the room.
     * @param theColumnIndex The column index of the room.
     */
    private void createAndAddDoors(final Room room, final int[][] theGrid, final int theRowIndex, final int theColumnIndex) {
        if (theRowIndex - 2 >= 0 && theGrid[theRowIndex - 1][theColumnIndex] == HALLWAY) {
            room.addDoor((Door) GameObjectFactory.createGameObject(GameObjectFactory.TYPE_DOOR, (Room.GRID_WIDTH / 2 - 1) * Room.TILE_SIZE, 0, DOOR_WIDTH_HORIZONTAL, DOOR_HEIGHT_HORIZONTAL, (theRowIndex - 2) * theGrid.length + theColumnIndex, Door.NORTH));
        }
        if (theColumnIndex + 2 < theGrid[theRowIndex].length && theGrid[theRowIndex][theColumnIndex + 1] == HALLWAY) {
            room.addDoor((Door) GameObjectFactory.createGameObject(GameObjectFactory.TYPE_DOOR, (Room.GRID_WIDTH - 1) * Room.TILE_SIZE, (Room.GRID_HEIGHT / 2 - 1) * Room.TILE_SIZE, DOOR_WIDTH_VERTICAL, DOOR_HEIGHT_VERTICAL, (theRowIndex) * theGrid.length + theColumnIndex + 2, Door.EAST));
        }
        if (theRowIndex + 2 < theGrid.length && theGrid[theRowIndex + 1][theColumnIndex] == HALLWAY) {
            room.addDoor((Door) GameObjectFactory.createGameObject(GameObjectFactory.TYPE_DOOR, (Room.GRID_WIDTH / 2 - 1) * Room.TILE_SIZE, (Room.GRID_HEIGHT - 1) * Room.TILE_SIZE, DOOR_WIDTH_HORIZONTAL, DOOR_HEIGHT_HORIZONTAL, (theRowIndex + 2) * theGrid.length + theColumnIndex, Door.SOUTH));
        }
        if (theColumnIndex - 2 >= 0 && theGrid[theRowIndex][theColumnIndex - 1] == HALLWAY) {
            room.addDoor((Door) GameObjectFactory.createGameObject(GameObjectFactory.TYPE_DOOR, 0, (Room.GRID_HEIGHT / 2 - 1) * Room.TILE_SIZE, DOOR_WIDTH_VERTICAL, DOOR_HEIGHT_VERTICAL, (theRowIndex) * theGrid.length + theColumnIndex - 2, Door.WEST));
        }
    }

    /**
     * Spawns objects in the room based on the room template's spawn grid.
     *
     * @param theRoom         The room to spawn objects in.
     * @param theRoomTemplate The room template containing spawn information.
     * @param theBiome        The biome for the spawned objects.
     */
    private void spawnObjectsFromGrid(final Room theRoom, final RoomTemplate theRoomTemplate, final int theBiome) {
        final int[][] spawns = theRoomTemplate.getSpawns();
        if (spawns.length == 0 || spawns[0].length == 0) {
            return;
        }

        for (int theRowIndex = 0; theRowIndex < spawns.length; theRowIndex++) {
            for (int theColumnIndex = 0; theColumnIndex < spawns[theRowIndex].length; theColumnIndex++) {
                final int spawnType = spawns[theRowIndex][theColumnIndex];
                final int x = theColumnIndex * Room.TILE_SIZE;
                final int y = theRowIndex * Room.TILE_SIZE;

                final GameObject objectToAdd = switch (spawnType) {
                    case GUARANTEED_POTION -> PotionFactory.createRandomPotion(x, y, Room.TILE_SIZE, Room.TILE_SIZE);
                    case GUARANTEED_ENEMY -> {
                        final Character enemyTemplate = myDBManager.getRandomEnemyForBiome(theBiome);
                        yield (enemyTemplate != null) ? new Enemy(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, enemyTemplate) : null;
                    }
                    case GUARANTEED_CHEST ->
                            GameObjectFactory.createGameObject(GameObjectFactory.TYPE_CHEST, x, y, 32, 32);
                    case RANDOM_POTION -> {
                        if (myRandom.nextInt(100) < theRoomTemplate.getPotionChance()) {
                            yield PotionFactory.createRandomPotion(x, y, Room.TILE_SIZE, Room.TILE_SIZE);
                        }
                        yield null;
                    }
                    case RANDOM_ENEMY -> {
                        if (myRandom.nextInt(100) < theRoomTemplate.getEnemyChance()) {
                            final Character randomEnemyTemplate = myDBManager.getRandomEnemyForBiome(theBiome);
                            yield (randomEnemyTemplate != null) ? new Enemy(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, randomEnemyTemplate) : null;
                        }
                        yield null;
                    }
                    case RANDOM_CHEST -> {
                        if (myRandom.nextInt(100) < theRoomTemplate.getChestChance()) {
                            yield GameObjectFactory.createGameObject(GameObjectFactory.TYPE_CHEST, x, y, 32, 32);
                        }
                        yield null;
                    }
                    case GUARANTEED_BOMB ->
                            GameObjectFactory.createGameObject(GameObjectFactory.TYPE_BOMB, x, y, 32, 32);
                    case RANDOM_BOMB -> {
                        if (myRandom.nextInt(100) < theRoomTemplate.getBombChance()) {
                            yield GameObjectFactory.createGameObject(GameObjectFactory.TYPE_BOMB, x, y, 32, 32);
                        }
                        yield null;
                    }
                    case GUARANTEED_TRAP ->
                            GameObjectFactory.createGameObject(GameObjectFactory.TYPE_TRAP, x, y, 32, 32, myRandom.nextInt(7) + 1);
                    case RANDOM_TRAP -> {
                        if (myRandom.nextInt(100) < theRoomTemplate.getTrapChance()) {
                            yield GameObjectFactory.createGameObject(GameObjectFactory.TYPE_TRAP, x, y, 32, 32, myRandom.nextInt(7) + 1);
                        }
                        yield null;
                    }
                    case GUARANTEED_BREAKABLE_WALL ->
                            GameObjectFactory.createGameObject(GameObjectFactory.TYPE_BREAKABLE_WALL, x, y, 32, 32);
                    case RANDOM_BREAKABLE_WALL -> {
                        if (myRandom.nextInt(100) < theRoomTemplate.getBreakableWallChance()) {
                            yield GameObjectFactory.createGameObject(GameObjectFactory.TYPE_BREAKABLE_WALL, x, y, 32, 32);
                        }
                        yield null;
                    }
                    case GUARANTEED_PILLAR ->
                            GameObjectFactory.createGameObject(GameObjectFactory.TYPE_PILLAR, x, y, 32, 32);
                    case GUARANTEED_EXIT ->
                            GameObjectFactory.createGameObject(GameObjectFactory.TYPE_EXIT, x, y, 32, 32);
                    default -> null;
                };

                if (objectToAdd != null) {
                    theRoom.addObject(objectToAdd);
                }
            }
        }
    }

    /**
     * Selects an appropriate room template based on its type and connections.
     *
     * @param theRoomType    The type of the room.
     * @param theConnections The number of connections (doors) the room has.
     * @param theGrid        The dungeon grid.
     * @param theRowIndex    The row index of the room.
     * @param theColumnIndex The column index of the room.
     * @return The selected RoomTemplate.
     */
    private RoomTemplate selectRoomTemplate(final int theRoomType, final int theConnections, final int[][] theGrid, final int theRowIndex, final int theColumnIndex) {
        final Random random = new Random();
        String roomTemplate;

        if (theRoomType == 9) {
            roomTemplate = "START_ROOM";
        } else if (theRoomType == 10) {
            roomTemplate = "END_ROOM";
        } else if (theRoomType >= 11 && theRoomType <= 16) {
            roomTemplate = "PILLAR_ROOM";
        } else if (theRoomType >= 2 && theRoomType <= 6) {
            final boolean hasNorth = theRowIndex - 2 >= 0 && theGrid[theRowIndex - 1][theColumnIndex] == HALLWAY;
            final boolean hasSouth = theRowIndex + 2 < theGrid.length && theGrid[theRowIndex + 1][theColumnIndex] == HALLWAY;
            final boolean hasEast = theColumnIndex + 2 < theGrid[theRowIndex].length && theGrid[theRowIndex][theColumnIndex + 1] == HALLWAY;
            final boolean hasWest = theColumnIndex - 2 >= 0 && theGrid[theRowIndex][theColumnIndex - 1] == HALLWAY;

            roomTemplate = switch (theConnections) {
                case 4 -> "CROSS_HALLWAY";
                case 3 -> {
                    if (hasNorth && hasEast && hasWest) yield "T_JUNCTION_NORTH_EAST_WEST";
                    if (hasSouth && hasEast && hasWest) yield "T_JUNCTION_SOUTH_EAST_WEST";
                    if (hasNorth && hasSouth && hasEast) yield "T_JUNCTION_NORTH_SOUTH_EAST";
                    else yield "T_JUNCTION_NORTH_SOUTH_WEST";
                }
                case 2 -> {
                    if (hasNorth && hasSouth) yield "HALLWAY_VERTICAL";
                    if (hasEast && hasWest) yield "HALLWAY_HORIZONTAL";
                    if (hasNorth && hasEast) yield "HALLWAY_CORNER_NE";
                    if (hasNorth && hasWest) yield "HALLWAY_CORNER_NW";
                    if (hasSouth && hasEast) yield "HALLWAY_CORNER_SE";
                    else yield "HALLWAY_CORNER_SW";
                }
                case 1 -> {
                    if (hasNorth) yield "DEAD_END_NORTH";
                    if (hasSouth) yield "DEAD_END_SOUTH";
                    if (hasEast) yield "DEAD_END_EAST";
                    else yield "DEAD_END_WEST";
                }
                default -> {
                    final String[] randomRoomTemplates = new String[]{"BASIC_ROOM", "FOUR_PILLAR_ROOM"};
                    yield randomRoomTemplates[random.nextInt(randomRoomTemplates.length)];
                }
            };
        } else {
            roomTemplate = "BASIC_ROOM";
        }

        return myRoomConfigManager.getRoomTemplate(roomTemplate);
    }
}