package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

import java.awt.*;

/**
 * Represents a door object within the dungeon.
 * This class handles doors that link to other rooms, storing information about the target room and direction.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public class Door extends GameObject {
    /**
     * Constant for a door facing north.
     */
    public static final int NORTH = 0;
    /**
     * Constant for a door facing south.
     */
    public static final int SOUTH = 1;
    /**
     * Constant for a door facing east.
     */
    public static final int EAST = 2;
    /**
     * Constant for a door facing west.
     */
    public static final int WEST = 3;

    /**
     * The ID of the room that this door leads to.
     */
    private final int myTargetRoomId;
    /**
     * The direction this door is facing.
     */
    private final int myDirection;

    /**
     * Constructs a Door object with specified position, dimensions, target room, and direction.
     *
     * @param theX            The x-coordinate of the door.
     * @param theY            The y-coordinate of the door.
     * @param theWidth        The width of the door.
     * @param theHeight       The height of the door.
     * @param theTargetRoomId The ID of the room the door leads to.
     * @param theDirection    The direction the door is facing (e.g., NORTH, SOUTH).
     */
    public Door(final int theX, final int theY, final int theWidth, final int theHeight, final int theTargetRoomId, final int theDirection) {
        super(theX, theY, theWidth, theHeight);
        myTargetRoomId = theTargetRoomId;
        myDirection = theDirection;
    }

    /**
     * Gets the bounding rectangle of the door.
     *
     * @return A Rectangle object representing the door's bounds.
     */
    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    /**
     * Gets the ID of the target room.
     *
     * @return The ID of the room this door leads to.
     */
    public int getTargetRoomId() {
        return myTargetRoomId;
    }

    /**
     * Gets the direction the door is facing.
     *
     * @return The direction constant (e.g., NORTH, SOUTH).
     */
    public int getDirection() {
        return myDirection;
    }

    /**
     * Updates the state of the door. This method is empty as the door is a static object.
     */
    @Override
    public void update() {
        // No action needed as doors are static objects
    }

}