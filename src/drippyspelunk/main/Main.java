package drippyspelunk.main;

import drippyspelunk.controller.core.ProgramDriver;
import drippyspelunk.controller.core.StateManager;

import javax.swing.*;

/**
 * Main for application.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.3
 */
public class Main {
    /**
     * Main method of the application, which initializes and runs the program.
     *
     * @param theArgs Command-line arguments for the application.
     */
    public static void main(String[] theArgs) {
        SwingUtilities.invokeLater(() -> {
            ProgramDriver application = new ProgramDriver();

            StateManager.getInstance().setGameEngine(application);
            StateManager.getInstance().goToTitleScreen();
        });
    }
}