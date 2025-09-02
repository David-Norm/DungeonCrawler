package drippyspelunk.controller.input;

import java.awt.event.KeyListener;

/**
 * Interface for controls.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public interface IController extends KeyListener {
    /**
     * Performs any necessary cleanup for the controller.
     * This method is called when the controller is no longer in use,
     * such as when transitioning to a new game state.
     */
    void cleanup();
}