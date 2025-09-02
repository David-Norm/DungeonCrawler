package drippyspelunk.controller.states;

import drippyspelunk.controller.core.StateManager;
import drippyspelunk.controller.input.GameOverController;
import drippyspelunk.controller.input.IController;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic.GameEndingType;
import drippyspelunk.model.gameover.GameOverLogic;
import drippyspelunk.view.gameover.GameOverView;

import javax.swing.*;
import java.awt.*;

/**
 * State for the game over screen.
 * This class handles the initialization, activation, and disposal of
 * the game over screen's model, view, and controller components.
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public class GameOverState implements IState {

    /**
     * The type of game ending that occurred, determining which screen is displayed.
     */
    private final GameEndingType myEndingType;
    /**
     * The singleton StateManager used for transitions to other states.
     */
    private final StateManager myStateManager;
    /**
     * The graphical user interface scaling factor.
     */
    private final int myGuiScale;
    /**
     * The model component for the game over screen, which contains its logic.
     */
    private GameOverLogic myLogic;
    /**
     * The view component for the game over screen, responsible for rendering.
     */
    private GameOverView myView;
    /**
     * The controller component that handles user input for this state.
     */
    private GameOverController myController;
    /**
     * The main panel that holds the GameOver view.
     */
    private JComponent myFullPanel;

    /**
     * Constructs a GameOverState.
     *
     * @param theStateManager The state manager to handle state transitions.
     * @param theGuiScale     The graphical user interface scaling factor.
     * @param theEndingType   The type of game ending that occurred.
     */
    public GameOverState(final StateManager theStateManager, final int theGuiScale, final GameEndingType theEndingType) {
        myStateManager = theStateManager;
        myGuiScale = theGuiScale;
        myEndingType = theEndingType;
    }

    /**
     * Initializes the game over state by creating the model, view, and controller.
     * It also sets up the main panel to hold the view.
     */
    @Override
    public void init() {
        myLogic = new GameOverLogic();
        myView = new GameOverView(640, 360, myGuiScale, myEndingType);
        myController = new GameOverController(myStateManager);

        myFullPanel = new JPanel(new BorderLayout());
        myFullPanel.add(myView, BorderLayout.CENTER);
    }

    /**
     * Placeholder for any activation logic for the game over screen.
     */
    @Override
    public void activate() {
    }

    /**
     * Disposes of the resources used by this state by setting references to null.
     */
    @Override
    public void dispose() {
        myLogic = null;
        myView = null;
        myController = null;
        myFullPanel = null;
    }

    /**
     * Updates the game over logic.
     *
     * @param theCurrentTime The current system time.
     */
    @Override
    public void update(final long theCurrentTime) {
        if (myLogic != null) {
            myLogic.update(theCurrentTime);
        }
    }

    /**
     * Renders the GameOver view.
     *
     * @param theCurrentTime The current system time.
     */
    @Override
    public void render(final long theCurrentTime) {
        if (myView != null) {
            myView.render();
        }
    }

    /**
     * Gets the main UI part for the game over screen.
     *
     * @return The main JPanel.
     */
    @Override
    public JComponent getSceneUI() {
        return myFullPanel;
    }

    /**
     * Gets the controller associated with this state.
     *
     * @return The GameOverController instance.
     */
    @Override
    public IController getController() {
        return myController;
    }
}