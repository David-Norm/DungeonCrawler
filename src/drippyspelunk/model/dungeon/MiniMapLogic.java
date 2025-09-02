package drippyspelunk.model.dungeon;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Handles the logic for determining visible and visited rooms on the minimap.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.0
 */
public class MiniMapLogic {

    /**
     * The main dungeon logic instance.
     */
    private final DungeonCrawlerLogic myLogic;
    /**
     * A set of all visited room IDs.
     */
    private final Set<Integer> myVisitedRooms;
    /**
     * A set of all currently visible room IDs.
     */
    private final Set<Integer> myVisibleRooms;
    /**
     * The 2D array representing the dungeon layout.
     */
    private int[][] myDungeonGrid;
    /**
     * The width of the dungeon grid.
     */
    private int myGridWidth;
    /**
     * The height of the dungeon grid.
     */
    private int myGridHeight;
    /**
     * The grid position of the current room.
     */
    private Point myCurrentRoomGridPosition;

    /**
     * Constructs a new MiniMapLogic instance.
     *
     * @param theLogic The dungeon crawler logic instance.
     */
    public MiniMapLogic(final DungeonCrawlerLogic theLogic) {
        myLogic = theLogic;
        myVisitedRooms = new HashSet<>();
        myVisibleRooms = new HashSet<>();
        myCurrentRoomGridPosition = new Point(-1, -1);
    }

    /**
     * Updates the set of visible rooms based on the player's vision range.
     */
    public void updateVisibility() {
        myVisibleRooms.clear();

        if (myLogic.getPlayer() == null || myLogic.getCurrentRoom() == null || myDungeonGrid == null) {
            return;
        }

        final int visionRange = myLogic.getPlayer().getVisionRange();
        final int currentRoomId = myLogic.getCurrentRoom().getRoomID();

        updateCurrentRoomPosition(currentRoomId);

        myVisitedRooms.add(currentRoomId);
        myVisibleRooms.add(currentRoomId);

        if (myCurrentRoomGridPosition.x != -1) {
            findConnectedRoomsWithinVision(myCurrentRoomGridPosition, visionRange);
        }
    }

    /**
     * Updates the grid position of the current room.
     *
     * @param theRoomId The ID of the current room.
     */
    private void updateCurrentRoomPosition(final int theRoomId) {
        final Point position = findRoomPositionById(theRoomId);
        if (position != null) {
            myCurrentRoomGridPosition = position;
        }
    }

    /**
     * Finds and adds rooms within the player's vision range to the visible rooms set.
     *
     * @param theStartPos    The starting grid position for the search.
     * @param theVisionRange The player's vision range.
     */
    private void findConnectedRoomsWithinVision(final Point theStartPos, final int theVisionRange) {
        final Queue<Point> queue = new LinkedList<>();
        final Set<Point> visited = new HashSet<>();
        final Queue<Integer> distanceQueue = new LinkedList<>();

        queue.add(theStartPos);
        distanceQueue.add(0);
        visited.add(theStartPos);

        while (!queue.isEmpty()) {
            final Point current = queue.poll();
            final int currentDistance = distanceQueue.poll();

            if (isRoom(current.x, current.y) && currentDistance <= theVisionRange) {
                if (currentDistance > 0) {
                    final int roomId = current.y * myGridWidth + current.x;
                    myVisibleRooms.add(roomId);
                }
            }

            if (currentDistance >= theVisionRange) {
                continue;
            }

            final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

            for (final int[] dir : directions) {
                final int newX = current.x + dir[0];
                final int newY = current.y + dir[1];
                final Point newPos = new Point(newX, newY);

                if (newX < 0 || newX >= myGridWidth || newY < 0 || newY >= myGridHeight || visited.contains(newPos)) {
                    continue;
                }

                if (myDungeonGrid[newY][newX] >= 1) { // Hallways and rooms
                    visited.add(newPos);
                    queue.add(newPos);

                    final int newDistance;
                    if (isRoom(newX, newY) && !isRoom(current.x, current.y)) {
                        newDistance = currentDistance + 1;
                    } else {
                        newDistance = currentDistance;
                    }

                    distanceQueue.add(newDistance);
                }
            }
        }
    }

    /**
     * Checks if a hallway at a given position is connected to a visible or visited room.
     *
     * @param theX The x-coordinate of the hallway.
     * @param theY The y-coordinate of the hallway.
     * @return True if the hallway is connected to a visible or visited room, false otherwise.
     */
    public boolean isHallwayLeadingToVisibleRoom(final int theX, final int theY) {
        final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (final int[] dir : directions) {
            final int checkX = theX + dir[0];
            final int checkY = theY + dir[1];

            if (checkX >= 0 && checkX < myGridWidth && checkY >= 0 && checkY < myGridHeight) {
                if (isRoom(checkX, checkY)) {
                    final int roomId = checkY * myGridWidth + checkX;
                    if (myVisibleRooms.contains(roomId) || myVisitedRooms.contains(roomId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds the grid position of a room by its ID.
     *
     * @param theRoomId The ID of the room to find.
     * @return The grid position of the room, or null if not found.
     */
    public Point findRoomPositionById(final int theRoomId) {
        if (myDungeonGrid == null) {
            return null;
        }
        for (int y = 0; y < myGridHeight; y++) {
            for (int x = 0; x < myGridWidth; x++) {
                if (isRoom(x, y)) {
                    if (y * myGridWidth + x == theRoomId) {
                        return new Point(x, y);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Checks if a grid position is a room.
     *
     * @param theX The x-coordinate on the grid.
     * @param theY The y-coordinate on the grid.
     * @return True if the position is a room, false otherwise.
     */
    public boolean isRoom(final int theX, final int theY) {
        return theX >= 0 && theX < myGridWidth && theY >= 0 && theY < myGridHeight && myDungeonGrid[theY][theX] >= 2;
    }

    /**
     * Gets a copy of the set of visible room IDs.
     *
     * @return A set of visible room IDs.
     */
    public Set<Integer> getVisibleRooms() {
        return new HashSet<>(myVisibleRooms);
    }

    /**
     * Gets a copy of the set of visited room IDs.
     *
     * @return A set of visited room IDs.
     */
    public Set<Integer> getVisitedRooms() {
        return new HashSet<>(myVisitedRooms);
    }

    /**
     * Gets the dungeon grid.
     *
     * @return The 2D array representing the dungeon grid.
     */
    public int[][] getDungeonGrid() {
        return myDungeonGrid;
    }

    /**
     * Sets the dungeon grid and updates the grid dimensions.
     *
     * @param theGrid The 2D array representing the dungeon grid.
     */
    public void setDungeonGrid(final int[][] theGrid) {
        if (theGrid != null && theGrid.length > 0) {
            myDungeonGrid = theGrid;
            myGridHeight = theGrid.length;
            myGridWidth = theGrid[0].length;
        }
    }

    /**
     * Gets the width of the dungeon grid.
     *
     * @return The grid width.
     */
    public int getGridWidth() {
        return myGridWidth;
    }

    /**
     * Gets the height of the dungeon grid.
     *
     * @return The grid height.
     */
    public int getGridHeight() {
        return myGridHeight;
    }

    /**
     * Gets the grid position of the current room.
     *
     * @return The current room's grid position.
     */
    public Point getCurrentRoomGridPosition() {
        return myCurrentRoomGridPosition;
    }
}