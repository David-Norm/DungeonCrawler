package drippyspelunk.view.dungeon;

import drippyspelunk.model.dungeon.DungeonCrawlerLogic;
import drippyspelunk.model.dungeon.MiniMapLogic;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * Minimap panel that shows only connected rooms within vision range.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 2.1
 */
public class MiniMapPanel extends JPanel implements PropertyChangeListener {

    /**
     * The margin around the minimap grid.
     */
    private static final int MARGIN = 2;
    /**
     * The background color of the panel.
     */
    private static final Color BACKGROUND_COLOR = new Color(20, 20, 30);

    // Visual constants
    /**
     * The color for unknown or unexplored areas.
     */
    private static final Color UNKNOWN_COLOR = new Color(40, 40, 50);

    // Colors
    /**
     * The color for a standard room.
     */
    private static final Color ROOM_COLOR = new Color(120, 120, 140);
    /**
     * The color for the player's current room.
     */
    private static final Color CURRENT_ROOM_COLOR = new Color(255, 255, 100);
    /**
     * The color for hallways.
     */
    private static final Color HALLWAY_COLOR = new Color(100, 100, 120);
    /**
     * The color for the player's position marker.
     */
    private static final Color PLAYER_COLOR = new Color(255, 50, 50);
    /**
     * The color for the starting room.
     */
    private static final Color STARTING_ROOM_COLOR = new Color(50, 255, 50);
    /**
     * The color for the ending room (exit).
     */
    private static final Color ENDING_ROOM_COLOR = new Color(255, 50, 255);
    /**
     * The color for a room containing a pillar.
     */
    private static final Color PILLAR_ROOM_COLOR = new Color(150, 100, 200);
    /**
     * The main game logic model.
     */
    private final DungeonCrawlerLogic myLogic;
    /**
     * The logic component for the minimap.
     */
    private final MiniMapLogic myMinimapLogic;

    /**
     * Constructs the MiniMapPanel.
     *
     * @param theWidth  The width of the panel.
     * @param theHeight The height of the panel.
     * @param theLogic  The main dungeon crawler logic object.
     */
    public MiniMapPanel(final int theWidth, final int theHeight, final DungeonCrawlerLogic theLogic) {
        myLogic = theLogic;
        myMinimapLogic = new MiniMapLogic(theLogic);

        setPreferredSize(new Dimension(theWidth, theHeight));
        setBackground(BACKGROUND_COLOR);

        // Register as a listener for relevant property changes
        myLogic.addPropertyChangeListener(DungeonCrawlerLogic.CURRENT_ROOM_PROPERTY, this);
        myLogic.addPropertyChangeListener(DungeonCrawlerLogic.PLAYER_POSITION_PROPERTY, this);
    }

    /**
     * Sets the dungeon grid data from the generator.
     *
     * @param theGrid The 2D array representing the dungeon layout.
     */
    public void setDungeonGrid(final int[][] theGrid) {
        myMinimapLogic.setDungeonGrid(theGrid);
        updateRoomVisibility();
    }

    /**
     * Updates which rooms are visible based on player's vision stat and actual connections.
     */
    private void updateRoomVisibility() {
        myMinimapLogic.updateVisibility();
    }

    /**
     * Paints the minimap component.
     *
     * @param theGraphics The Graphics object to draw on.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);

        final int[][] myDungeonGrid = myMinimapLogic.getDungeonGrid();
        final Point myCurrentRoomGridPosition = myMinimapLogic.getCurrentRoomGridPosition();

        if (myDungeonGrid == null || myLogic.getCurrentRoom() == null || myCurrentRoomGridPosition.x == -1) {
            return;
        }

        final Graphics2D g2d = (Graphics2D) theGraphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int myGridWidth = myMinimapLogic.getGridWidth();
        final int myGridHeight = myMinimapLogic.getGridHeight();
        final int visionRange = myLogic.getPlayer().getVisionRange();
        final int displayRange = Math.max(7, visionRange * 3 + 3);

        final int centerX = myCurrentRoomGridPosition.x;
        final int centerY = myCurrentRoomGridPosition.y;
        final int halfSize = displayRange / 2;

        int startGridX = Math.max(0, centerX - halfSize);
        int endGridX = Math.min(myGridWidth, centerX + halfSize + 1);
        int startGridY = Math.max(0, centerY - halfSize);
        int endGridY = Math.min(myGridHeight, centerY + halfSize + 1);

        final int visibleWidth = endGridX - startGridX;
        final int visibleHeight = endGridY - startGridY;

        final int availableWidth = getWidth() - 2 * MARGIN;
        final int availableHeight = getHeight() - 2 * MARGIN;
        final int cellSize = Math.max(3, Math.min(availableWidth / visibleWidth, availableHeight / visibleHeight));

        final int startX = MARGIN + (availableWidth - visibleWidth * cellSize) / 2;
        final int startY = MARGIN + (availableHeight - visibleHeight * cellSize) / 2;

        final int currentRoomId = myLogic.getCurrentRoom().getRoomID();
        final Set<Integer> myVisitedRooms = myMinimapLogic.getVisitedRooms();
        final Set<Integer> myVisibleRooms = myMinimapLogic.getVisibleRooms();

        // Draw the minimap grid
        for (int y = startGridY; y < endGridY; y++) {
            for (int x = startGridX; x < endGridX; x++) {
                final int cellX = startX + (x - startGridX) * cellSize;
                final int cellY = startY + (y - startGridY) * cellSize;

                final int cellValue = myDungeonGrid[y][x];
                final int roomId = y * myGridWidth + x;

                final boolean isCurrent = roomId == currentRoomId;
                final boolean isVisited = myVisitedRooms.contains(roomId);
                final boolean isVisible = myVisibleRooms.contains(roomId);

                Color cellColor = UNKNOWN_COLOR;

                if (isCurrent) {
                    cellColor = getRoomColor(cellValue, true, isVisited, isVisible);
                } else if (isVisible && cellValue >= 2) {
                    cellColor = getRoomColor(cellValue, false, isVisited, true);
                } else if (isVisited && cellValue >= 2) {
                    cellColor = getRoomColor(cellValue, false, true, false);
                } else if (cellValue == 1 && myMinimapLogic.isHallwayLeadingToVisibleRoom(x, y)) {
                    cellColor = HALLWAY_COLOR;
                }

                g2d.setColor(cellColor);
                g2d.fillRect(cellX, cellY, cellSize, cellSize);

                // Draw border for visible rooms
                if (cellValue >= 2 && (isVisited || isVisible || isCurrent)) {
                    g2d.setColor(isCurrent ? Color.YELLOW : Color.WHITE);
                    g2d.drawRect(cellX, cellY, cellSize, cellSize);
                }
            }
        }

        // Draw player position
        if (myCurrentRoomGridPosition.x >= startGridX && myCurrentRoomGridPosition.x < endGridX &&
                myCurrentRoomGridPosition.y >= startGridY && myCurrentRoomGridPosition.y < endGridY) {

            final int playerCellX = startX + (myCurrentRoomGridPosition.x - startGridX) * cellSize;
            final int playerCellY = startY + (myCurrentRoomGridPosition.y - startGridY) * cellSize;
            final int playerSize = Math.max(4, cellSize / 2);
            final int playerX = playerCellX + (cellSize - playerSize) / 2;
            final int playerY = playerCellY + (cellSize - playerSize) / 2;

            g2d.setColor(PLAYER_COLOR);
            g2d.fillOval(playerX, playerY, playerSize, playerSize);
            g2d.setColor(Color.WHITE);
            g2d.drawOval(playerX, playerY, playerSize, playerSize);

            // Crosshair
            g2d.setColor(new Color(255, 255, 255, 150));
            final int centerPlayerX = playerX + playerSize / 2;
            final int centerPlayerY = playerY + playerSize / 2;
            g2d.drawLine(centerPlayerX - 3, centerPlayerY, centerPlayerX + 3, centerPlayerY);
            g2d.drawLine(centerPlayerX, centerPlayerY - 3, centerPlayerX, centerPlayerY + 3);
        }

        g2d.dispose();
    }

    /**
     * Gets the appropriate color for a room based on its type and status.
     *
     * @param theCellValue The integer value representing the room type from the dungeon grid.
     * @param isCurrent    True if this is the player's current room.
     * @param isVisited    True if the room has been visited.
     * @param isVisible    True if the room is currently visible.
     * @return The color for the minimap cell.
     */
    private Color getRoomColor(final int theCellValue, final boolean isCurrent, final boolean isVisited, final boolean isVisible) {
        if (isCurrent) {
            return CURRENT_ROOM_COLOR;
        }

        final Color baseColor = switch (theCellValue) {
            case 0 -> BACKGROUND_COLOR; // Should not happen for rooms, but included for completeness
            case 1 -> HALLWAY_COLOR;
            case 2 -> ROOM_COLOR;
            case 9 -> STARTING_ROOM_COLOR;
            case 10 -> ENDING_ROOM_COLOR;
            case 13, 14, 15, 16 -> PILLAR_ROOM_COLOR;
            default -> theCellValue >= 2 ? ROOM_COLOR : UNKNOWN_COLOR;
        };

        // Dim color if only visible but not visited
        if (isVisible && !isVisited) {
            return new Color(
                    Math.max(0, baseColor.getRed() - 60),
                    Math.max(0, baseColor.getGreen() - 60),
                    Math.max(0, baseColor.getBlue() - 60)
            );
        }

        return baseColor;
    }

    /**
     * Handles property changes from the DungeonCrawlerLogic model.
     *
     * @param theEvent The property change event.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        String propertyName = theEvent.getPropertyName();

        if (DungeonCrawlerLogic.CURRENT_ROOM_PROPERTY.equals(propertyName)) {
            updateRoomVisibility();
        }
    }
}