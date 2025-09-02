package drippyspelunk.controller.states;

import drippyspelunk.controller.core.StateManager;
import drippyspelunk.controller.input.DungeonCrawlerController;
import drippyspelunk.controller.input.IController;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic;
import drippyspelunk.view.dungeon.DungeonScreenPanel;

import javax.swing.*;

/**
 * The state of a Dungeon Crawler instance with integrated minimap grid data.
 * This class manages the game logic, view, and controller for the dungeon gameplay state.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.5
 */
public class DungeonCrawlerState implements IState {

    /**
     * The height of the mini-map panel in pixels.
     */
    private static final int MINI_MAP_HEIGHT = 80;

    /**
     * The width of the mini-map panel in pixels.
     */
    private static final int MINI_MAP_WIDTH = 80;

    /**
     * The height of the player information panel in pixels.
     */
    private static final int PLAYER_INFORMATION_HEIGHT = 280;

    /**
     * The width of the player information panel in pixels.
     */
    private static final int PLAYER_INFORMATION_WIDTH = 80;

    /**
     * The height of the control panel in pixels.
     */
    private static final int CONTROL_PANEL_HEIGHT = 360;

    /**
     * The width of the control panel in pixels.
     */
    private static final int CONTROL_PANEL_WIDTH = 80;
    /**
     * A reference to the singleton StateManager for state transitions.
     */
    private final StateManager myStateManager;
    /**
     * The scaling factor for the graphical user interface.
     */
    private final int myGuiScale;
    /**
     * The main game logic for the dungeon crawler.
     */
    private DungeonCrawlerLogic myLogic;
    /**
     * The controller that handles user input for this state.
     */
    private DungeonCrawlerController myController;
    /**
     * The main panel that holds all UI components for the dungeon screen.
     */
    private DungeonScreenPanel myFullPanel;

    /**
     * Constructs a DungeonCrawlerState.
     *
     * @param theLogic    The DungeonCrawlerLogic instance to manage.
     * @param theGuiScale The graphical user interface scaling factor.
     */
    public DungeonCrawlerState(DungeonCrawlerLogic theLogic, int theGuiScale) {
        myLogic = theLogic;
        myStateManager = StateManager.getInstance();
        myGuiScale = theGuiScale;
    }

    /**
     * Initializes the game state by creating the view and controller and setting up listeners.
     */
    @Override
    public void init() {
        System.out.println("Initializing Dungeon Crawler Logic");

        myController = new DungeonCrawlerController(myLogic, null, myStateManager);

        myFullPanel = new DungeonScreenPanel(
                myLogic,
                myController,
                myGuiScale,
                myLogic.getScreenWidth(),
                myLogic.getScreenHeight(),
                MINI_MAP_WIDTH,
                MINI_MAP_HEIGHT,
                PLAYER_INFORMATION_WIDTH,
                PLAYER_INFORMATION_HEIGHT,
                CONTROL_PANEL_WIDTH,
                CONTROL_PANEL_HEIGHT);

        // Now that the view exists, pass it to the controller.
        myController.setMyView(myFullPanel);

        // Add property change listeners to connect the model and the view/controller.
        myLogic.addPropertyChangeListener(myFullPanel);
        myLogic.addPropertyChangeListener(myController);

        // Initialize the game logic itself.
        myLogic.init();

        // Pass the dungeon grid data to the minimap panel.
        if (myLogic.getDungeonGrid() != null && myFullPanel.getMiniMapPanel() != null) {
            myFullPanel.getMiniMapPanel().setDungeonGrid(myLogic.getDungeonGrid());
            System.out.println("Grid data passed to minimap successfully");
        } else {
            System.out.println("Warning: Could not pass grid data to minimap");
        }

        myFullPanel.getDungeonScreen().addNotify();
        System.out.println("Dungeon Crawler Logic Initialized");
    }

    /**
     * Placeholder method for state activation logic.
     */
    @Override
    public void activate() {
        System.out.println("Dungeon Crawler Activated");
    }

    /**
     * Cleans up resources used by this state.
     */
    @Override
    public void dispose() {
        System.out.println("Disposing Dungeon Crawler ");
        // Remove listeners to prevent memory leaks and unexpected behavior.
        if (myLogic != null && myFullPanel != null) {
            myLogic.removePropertyChangeListener(myFullPanel);
        }
        if (myLogic != null && myController != null) {
            myLogic.removePropertyChangeListener(myController);
        }
        // Dereference objects to allow garbage collection.
        myLogic = null;
        myController = null;
        myFullPanel = null;
        System.out.println("Dungeon Crawler Disposed");
    }

    /**
     * Updates the game logic based on the current time.
     *
     * @param theCurrentTime The current system time in nanoseconds.
     */
    @Override
    public void update(long theCurrentTime) {
        if (myLogic != null) {
            myLogic.update(theCurrentTime);
        }
    }

    /**
     * Renders the game view.
     *
     * @param theCurrentTime The current system time in nanoseconds.
     */
    @Override
    public void render(long theCurrentTime) {
        if (myFullPanel != null) {
            myFullPanel.render(theCurrentTime);
        }
    }

    /**
     * Gets the controller associated with this state.
     *
     * @return The IController for this state.
     */
    @Override
    public IController getController() {
        return myController;
    }

    /**
     * Gets the main UI part for this state.
     *
     * @return The JComponent to be displayed.
     */
    @Override
    public JComponent getSceneUI() {
        return myFullPanel;
    }
}