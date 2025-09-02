package drippyspelunk.controller.states;

import drippyspelunk.controller.core.StateManager;
import drippyspelunk.controller.input.IController;
import drippyspelunk.controller.input.TitleScreenController;
import drippyspelunk.model.menu.TitleScreenLogic;
import drippyspelunk.view.title.TitleScreenView;

import javax.swing.*;
import java.awt.*;

/**
 * State of title screen. This class represents the main menu
 * of the game, managing its own logic, view, and controller.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.5
 */
public class TitleScreenState implements IState {

    /**
     * The state manager for handling game state transitions.
     */
    private final StateManager myStateManager;

    /**
     * The graphical user interface scaling factor.
     */
    private final int myGuiScale;

    /**
     * The model component containing the logic for the title screen.
     */
    private TitleScreenLogic myLogic;

    /**
     * The view component responsible for rendering the title screen.
     */
    private TitleScreenView myView;

    /**
     * The controller component that handles user input for this state.
     */
    private TitleScreenController myController;

    /**
     * The main panel that holds all UI components for the title screen.
     */
    private JPanel myFullPanel;

    /**
     * Constructs a TitleScreenState.
     *
     * @param theStateManager The state manager for game state transitions.
     * @param theGuiScale     The graphical user interface scaling factor.
     */
    public TitleScreenState(final StateManager theStateManager, final int theGuiScale) {
        myStateManager = theStateManager;
        myGuiScale = theGuiScale;
    }

    /**
     * Initializes the title screen state by creating the model, view, and controller.
     * It also sets up the necessary listeners and the main panel for the view.
     */
    @Override
    public void init() {
        myLogic = new TitleScreenLogic();
        myView = new TitleScreenView(myLogic, 640, 360, myGuiScale);
        myController = new TitleScreenController(myStateManager, myLogic, myView);
        myView.setActionListener(myController);

        myLogic.addPropertyChangeListener(myController);

        myFullPanel = new JPanel(new BorderLayout());
        myFullPanel.add(myView, BorderLayout.CENTER);
        System.out.println("Title Screen Initialized");
    }

    /**
     * Placeholder method for state activation logic.
     */
    @Override
    public void activate() {
        System.out.println("Title Screen Activated");
    }

    /**
     * Disposes of the resources used by this state by setting references to null
     * and removing listeners to allow for garbage collection.
     */
    @Override
    public void dispose() {
        System.out.println("Disposing Title Screen");
        if (myLogic != null && myController != null) {
            myLogic.removePropertyChangeListener(myController);
        }
        myLogic = null;
        myView = null;
        myController = null;
        myFullPanel = null;
        System.out.println("Title Screen Disposed");
    }

    /**
     * Updates the state logic. This is not used for the title screen's current
     * implementation, but is required by the {@link IState} interface.
     *
     * @param theCurrentTime The current system time.
     */
    @Override
    public void update(final long theCurrentTime) {
        // No updates needed for the title screen.
    }

    /**
     * Renders the title screen view.
     *
     * @param theCurrentTime The current system time.
     */
    @Override
    public void render(final long theCurrentTime) {
        if (myView != null) {
            myView.render(theCurrentTime);
        }
    }

    /**
     * Gets the controller associated with this state.
     *
     * @return The {@link TitleScreenController} instance.
     */
    @Override
    public IController getController() {
        return myController;
    }

    /**
     * Gets the main UI part for the title screen.
     *
     * @return The main JPanel.
     */
    @Override
    public JComponent getSceneUI() {
        return myFullPanel;
    }
}