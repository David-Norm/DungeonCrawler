package drippyspelunk.view.dungeon;

import drippyspelunk.controller.input.DungeonCrawlerController;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A panel that composes the various view components for the Dungeon Screen,
 * including the main game view, minimap, player information, and controls.
 * This class acts as a container, organizing and managing the layout of its subpanels.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @author Mark Malyshev
 * @version 1.7
 */
public class DungeonScreenPanel extends JPanel implements PropertyChangeListener {

    /**
     * The main game view, where the dungeon and its contents are rendered.
     */
    private final DungeonScreen myDungeonScreen;
    /**
     * The panel that displays the minimap.
     */
    private final MiniMapPanel myMiniMapPanel;
    /**
     * The panel that shows player controls and objectives.
     */
    private final PlayerControlsPanel myPlayerControlsPanel;

    /**
     * Constructs the DungeonScreenPanel.
     *
     * @param theLogic                        The main game logic.
     * @param theController                   The game controller for handling input.
     * @param theGuiScale                     The GUI scaling factor.
     * @param theGameViewLogicalWidth         The logical width of the main game view.
     * @param theGameViewLogicalHeight        The logical height of the main game view.
     * @param theMiniMapPanelWidth            The logical width of the minimap panel.
     * @param theMiniMapPanelHeight           The logical height of the minimap panel.
     * @param thePlayerInformationPanelWidth  The logical width of the player information panel.
     * @param thePlayerInformationPanelHeight The logical height of the player information panel.
     * @param theControlsPanelWidth           The logical width of the player controls panel.
     * @param theControlsPanelHeight          The logical height of the player controls panel.
     */
    public DungeonScreenPanel(final DungeonCrawlerLogic theLogic,
                              final DungeonCrawlerController theController,
                              final int theGuiScale,
                              final int theGameViewLogicalWidth,
                              final int theGameViewLogicalHeight,
                              final int theMiniMapPanelWidth,
                              final int theMiniMapPanelHeight,
                              final int thePlayerInformationPanelWidth,
                              final int thePlayerInformationPanelHeight,
                              final int theControlsPanelWidth,
                              final int theControlsPanelHeight) {

        super(new BorderLayout());

        // Register as a listener for state changes from the game logic
        theLogic.addPropertyChangeListener(this);

        // Main game view in the center
        myDungeonScreen = new DungeonScreen(theLogic, theGameViewLogicalWidth, theGameViewLogicalHeight, theGuiScale);
        add(myDungeonScreen, BorderLayout.CENTER);

        // Left panel for the minimap and player information
        final JPanel leftPanel = new JPanel(new BorderLayout());
        final int miniMapScaledWidth = theMiniMapPanelWidth * theGuiScale;
        final int miniMapScaledHeight = theMiniMapPanelHeight * theGuiScale;
        myMiniMapPanel = new MiniMapPanel(miniMapScaledWidth, miniMapScaledHeight, theLogic);
        leftPanel.add(myMiniMapPanel, BorderLayout.NORTH);

        final int playerInfoScaledWidth = thePlayerInformationPanelWidth * theGuiScale;
        final int playerInfoScaledHeight = thePlayerInformationPanelHeight * theGuiScale;
        leftPanel.add(new PlayerInformationPanel(playerInfoScaledWidth, playerInfoScaledHeight, theLogic, theController, theGuiScale), BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // Right panel for player controls
        final int controlsScaledWidth = theControlsPanelWidth * theGuiScale;
        final int controlsScaledHeight = theControlsPanelHeight * theGuiScale;
        myPlayerControlsPanel = new PlayerControlsPanel(controlsScaledWidth, controlsScaledHeight, theLogic, theGuiScale);
        add(myPlayerControlsPanel, BorderLayout.EAST);
    }

    /**
     * Gets the main dungeon screen view.
     *
     * @return The DungeonScreen object.
     */
    public DungeonScreen getDungeonScreen() {
        return myDungeonScreen;
    }

    /**
     * Gets the player controls panel.
     *
     * @return The PlayerControlsPanel object.
     */
    public PlayerControlsPanel getPlayerControlsPanel() {
        return myPlayerControlsPanel;
    }

    /**
     * Gets the minimap panel for external access to its methods, such as setting the dungeon grid.
     *
     * @return The MiniMapPanel object.
     */
    public MiniMapPanel getMiniMapPanel() {
        return myMiniMapPanel;
    }

    /**
     * Handles property changes from the game logic model.
     *
     * @param theEvent The property change event.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propertyName = theEvent.getPropertyName();

        if (DungeonCrawlerLogic.COMBAT_STATE_CHANGED_PROPERTY.equals(propertyName)) {
            final boolean inCombat = (Boolean) theEvent.getNewValue();
            myDungeonScreen.getCombatOverlay().setVisible(inCombat);
        }
    }

    /**
     * Renders the entire dungeon screen panel.
     * This method is a public-facing call to trigger a repaint.
     *
     * @param theCurrentTime The current system time, which can be used for animations.
     */
    public void render(final long theCurrentTime) {
        repaint();
    }
}