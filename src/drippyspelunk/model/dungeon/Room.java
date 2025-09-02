package drippyspelunk.model.dungeon;

import drippyspelunk.model.dungeon.entity.GameObject;
import drippyspelunk.model.dungeon.entity.asset.ActiveBomb;
import drippyspelunk.model.dungeon.entity.asset.Consumable;
import drippyspelunk.model.dungeon.entity.asset.Door;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A class representing a room.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 2.1
 */
public class Room {
    /**
     * The size of a single tile in pixels.
     */
    public static final int TILE_SIZE = 32;
    /**
     * The width of the room grid in tiles.
     */
    public static final int GRID_WIDTH = 15;
    /**
     * The height of the room grid in tiles.
     */
    public static final int GRID_HEIGHT = 13;

    /**
     * The unique identifier for the room.
     */
    private final int myRoomID;
    /**
     * The name of the room.
     */
    private final String myRoomName;
    /**
     * A list of all game objects in the room.
     */
    private final List<GameObject> myGameObjects;
    /**
     * A list of background floor objects.
     */
    private final List<GameObject> myFloorObjects;
    /**
     * A list of all doors in the room.
     */
    private final List<Door> myDoors;
    /**
     * A list of all active bombs in the room.
     */
    private final List<ActiveBomb> myActiveBombs;
    /**
     * A random number generator for the room.
     */
    private final Random myRandom = new Random();

    /**
     * Constructs a new room with a given ID and name.
     *
     * @param theRoomID   The unique ID of the room.
     * @param theRoomName The name of the room.
     */
    public Room(final int theRoomID, final String theRoomName) {
        myRoomID = theRoomID;
        myRoomName = theRoomName;
        myGameObjects = new ArrayList<>();
        myFloorObjects = new ArrayList<>();
        myDoors = new ArrayList<>();
        myActiveBombs = new ArrayList<>();
    }

    /**
     * Gets the unique ID of the room.
     *
     * @return The room's ID.
     */
    public int getRoomID() {
        return myRoomID;
    }

    /**
     * Gets the name of the room.
     *
     * @return The room's name.
     */
    public String getRoomName() {
        return myRoomName;
    }

    /**
     * Adds a game object to the room.
     *
     * @param theObject The game object to add.
     */
    public void addObject(final GameObject theObject) {
        myGameObjects.add(theObject);
        if (theObject instanceof ActiveBomb) {
            myActiveBombs.add((ActiveBomb) theObject);
        }
    }

    /**
     * Adds a floor object to the room.
     *
     * @param theFloorObject The floor object to add.
     */
    public void addFloorObject(final GameObject theFloorObject) {
        myFloorObjects.add(theFloorObject);
    }

    /**
     * Adds a door object to the room.
     *
     * @param theDoor The door to add.
     */
    public void addDoor(final Door theDoor) {
        myGameObjects.add(theDoor);
        myDoors.add(theDoor);
    }

    /**
     * Gets an unmodifiable list of all game objects in the room.
     *
     * @return A list of game objects.
     */
    public List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(myGameObjects);
    }

    /**
     * Gets an unmodifiable list of all floor objects in the room.
     *
     * @return A list of floor objects.
     */
    public List<GameObject> getFloorObjects() {
        return Collections.unmodifiableList(myFloorObjects);
    }

    /**
     * Gets an unmodifiable list of all doors in the room.
     *
     * @return A list of doors.
     */
    public List<Door> getDoors() {
        return Collections.unmodifiableList(myDoors);
    }

    /**
     * Gets an unmodifiable list of all enemies in the room.
     *
     * @return A list of enemies.
     */
    public List<Enemy> getEnemies() {
        final List<Enemy> enemies = new ArrayList<>();
        for (final GameObject obj : myGameObjects) {
            if (obj instanceof Enemy) {
                enemies.add((Enemy) obj);
            }
        }
        return Collections.unmodifiableList(enemies);
    }

    /**
     * Gets an unmodifiable list of all non-character, non-door objects in the room.
     *
     * @return A list of objects.
     */
    public List<GameObject> getObjectsInRoom() {
        final List<GameObject> objects = new ArrayList<>();
        for (final GameObject obj : myGameObjects) {
            if (!(obj instanceof Enemy) && !(obj instanceof Door)) {
                objects.add(obj);
            }
        }
        return Collections.unmodifiableList(objects);
    }

    /**
     * Removes a game object from the room.
     *
     * @param theObject The object to remove.
     */
    public void removeObject(final GameObject theObject) {
        myGameObjects.remove(theObject);
        if (theObject instanceof ActiveBomb) {
            myActiveBombs.remove(theObject);
        }
    }

    /**
     * Removes a consumable object from the room.
     *
     * @param theConsumable The consumable to remove.
     */
    public void removeObject(final Consumable theConsumable) {
        myGameObjects.remove(theConsumable);
    }

    /**
     * Gets a random valid spawn point within the room.
     *
     * @return A Point representing the spawn location.
     */
    public Point getSpawnPoint() {
        int x, y;
        int attempts = 0;
        do {
            x = Room.TILE_SIZE + myRandom.nextInt(Room.GRID_WIDTH - 2) * Room.TILE_SIZE;
            y = Room.TILE_SIZE + myRandom.nextInt(Room.GRID_HEIGHT - 2) * Room.TILE_SIZE;
            attempts++;
        } while (isPositionOccupied(x, y, TILE_SIZE, TILE_SIZE) && attempts < 100);

        return new Point(x, y);
    }

    /**
     * Checks if a given position is occupied by another game object.
     *
     * @param theX      The x-coordinate to check.
     * @param theY      The y-coordinate to check.
     * @param theWidth  The width of the area to check.
     * @param theHeight The height of the area to check.
     * @return True if the position is occupied, false otherwise.
     */
    private boolean isPositionOccupied(final int theX, final int theY, final int theWidth, final int theHeight) {
        final Rectangle newObjectBounds = new Rectangle(theX, theY, theWidth, theHeight);
        for (final GameObject object : getGameObjects()) {
            if (newObjectBounds.intersects(object.getBounds())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a list of objects from the room.
     *
     * @param theObjects The list of objects to remove.
     */
    public void removeObjects(final List<GameObject> theObjects) {
        if (theObjects != null) {
            myGameObjects.removeAll(theObjects);
            myActiveBombs.removeAll(theObjects);
        }
    }

    /**
     * Gets an unmodifiable list of all active bombs in the room.
     *
     * @return A list of active bombs.
     */
    public List<ActiveBomb> getActiveBombs() {
        return Collections.unmodifiableList(myActiveBombs);
    }
}