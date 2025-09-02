package drippyspelunk.model.dungeon.factory;

import drippyspelunk.model.dungeon.entity.GameObject;
import drippyspelunk.model.dungeon.entity.asset.*;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Character;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

/**
 * A factory class for creating GameObject instances with enhanced database integration
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.5
 */
public class GameObjectFactory {

    /**
     * The string constant for a door object type.
     */
    public static final String TYPE_DOOR = "door";
    /**
     * The string constant for a player object type.
     */
    public static final String TYPE_PLAYER = "player";
    /**
     * The string constant for a wall object type.
     */
    public static final String TYPE_WALL = "wall";
    /**
     * The string constant for a floor object type.
     */
    public static final String TYPE_FLOOR = "floor";
    /**
     * The string constant for a bomb object type.
     */
    public static final String TYPE_BOMB = "bomb";
    /**
     * The string constant for a trap object type.
     */
    public static final String TYPE_TRAP = "trap";
    /**
     * The string constant for a pillar object type.
     */
    public static final String TYPE_PILLAR = "pillar";
    /**
     * The string constant for a chest object type.
     */
    public static final String TYPE_CHEST = "chest";
    /**
     * The string constant for a breakable wall object type.
     */
    public static final String TYPE_BREAKABLE_WALL = "breakable_wall";
    /**
     * The string constant for an exit object type.
     */
    public static final String TYPE_EXIT = "exit";

    /**
     * Creates a GameObject instance of a specified type with given parameters.
     *
     * @param theType             The type of game object to create.
     * @param theX                The x-coordinate of the object.
     * @param theY                The y-coordinate of the object.
     * @param theWidth            The width of the object.
     * @param theHeight           The height of the object.
     * @param theAdditionalParams Variable number of parameters specific to the object type.
     * @return The created GameObject.
     * @throws IllegalArgumentException if the object type is unknown or parameters are invalid.
     */
    public static GameObject createGameObject(
            final String theType,
            final int theX,
            final int theY,
            final int theWidth,
            final int theHeight,
            final Object... theAdditionalParams) {

        return switch (theType.toLowerCase()) {

            case TYPE_PLAYER -> {
                if (theAdditionalParams.length == 1 && theAdditionalParams[0] instanceof Character) {
                    yield Player.createFromCharacterData(theX, theY, (Character) theAdditionalParams[0]);
                } else if (theAdditionalParams.length == 1 && theAdditionalParams[0] instanceof String) {
                    yield Player.createWithClass(theX, theY, (String) theAdditionalParams[0]);
                } else {
                    yield new Player(theX, theY);
                }
            }

            case TYPE_WALL -> {
                if (theAdditionalParams.length == 2 && theAdditionalParams[0] instanceof Integer && theAdditionalParams[1] instanceof Integer) {
                    final int wallType = (int) theAdditionalParams[0];
                    final int biome = (int) theAdditionalParams[1];
                    yield new Wall(theX, theY, theWidth, theHeight, wallType, biome);
                } else {
                    throw new IllegalArgumentException("Wall Spawn Failed");
                }
            }

            case TYPE_FLOOR -> {
                if (theAdditionalParams.length == 2 && theAdditionalParams[0] instanceof Integer && theAdditionalParams[1] instanceof Integer) {
                    final int floorType = (int) theAdditionalParams[0];
                    final int biome = (int) theAdditionalParams[1];
                    yield new Floor(theX, theY, theWidth, theHeight, floorType, biome);
                } else {
                    throw new IllegalArgumentException("Floor Spawn Failed");
                }
            }

            case TYPE_DOOR -> {
                if (theAdditionalParams.length == 2 && theAdditionalParams[0] instanceof Integer && theAdditionalParams[1] instanceof Integer) {
                    final int targetRoomId = (int) theAdditionalParams[0];
                    final int direction = (int) theAdditionalParams[1];
                    yield new Door(theX, theY, theWidth, theHeight, targetRoomId, direction);
                } else {
                    throw new IllegalArgumentException("Door Spawn Failed, invalid parameters.");
                }
            }

            case TYPE_PILLAR -> new Pillar(theX, theY, theWidth, theHeight);

            case TYPE_BOMB -> new Bomb(theX, theY, theWidth, theHeight);

            case TYPE_TRAP -> new Trap(theX, theY, theWidth, theHeight);

            case TYPE_CHEST -> new Chest(theX, theY, theWidth, theHeight);

            case TYPE_BREAKABLE_WALL -> new BreakableWall(theX, theY, theWidth, theHeight);

            case TYPE_EXIT -> new Exit(theX, theY, theWidth, theHeight);

            default -> throw new IllegalArgumentException("Unknown GameObject type: " + theType);
        };
    }
}