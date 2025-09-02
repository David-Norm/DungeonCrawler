package drippyspelunk.controller.states;

import drippyspelunk.controller.input.IController;

import javax.swing.*;

/**
 * Interface for states in a game. This interface defines the
 * lifecycle methods for a game state, enabling a state-driven
 * architecture. Each state is responsible for its own initialization,
 * activation, update, rendering, and cleanup.
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public interface IState {

    /**
     * Initializes the state, loading all necessary resources. This method is
     * called once when the state is first created.
     */
    void init();

    /**
     * Activates the state, making it the current active state. This can be used
     * for logic that should run every time the state becomes active, but not on
     * initial creation.
     */
    void activate();

    /**
     * Disposes of the state's resources. This is called when the state is no
     * longer needed and is about to be removed from memory.
     */
    void dispose();

    /**
     * Updates the state's logic. This method is called repeatedly in the
     * game loop to handle game logic, physics, and other updates.
     *
     * @param currentTime The current system time, typically in nanoseconds,
     *                    for time-based calculations.
     */
    void update(long currentTime);

    /**
     * Renders the state's visual components. This method is called repeatedly
     * in the game loop to draw the scene to the screen.
     *
     * @param currentTime The current system time, typically in nanoseconds,
     *                    for time-based animations and rendering.
     */
    void render(long currentTime);

    /**
     * Retrieves the input controller associated with this state. The controller
     * handles user input for the specific state.
     *
     * @return The {@link IController} for this state.
     */
    IController getController();

    /**
     * Retrieves the main graphical user interface part for this state.
     *
     * @return The {@link JComponent} representing the state's scene.
     */
    JComponent getSceneUI();
}