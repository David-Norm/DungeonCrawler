package drippyspelunk.view;

import drippyspelunk.controller.input.IController;
import drippyspelunk.controller.states.IState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * A separated application window. For MVC.
 * This class manages the main JFrame of the application, handling its content
 * and window properties. It is designed to work with the Model-View-Controller (MVC)
 * pattern by accepting scene content (Views) and controllers.
 *
 * @author Devin Arroyo
 * @version 1.3
 */
public class ApplicationWindow {

    /**
     * The main window frame of the application.
     */
    private final JFrame myFrame;

    /**
     * Constructs the main application window.
     *
     * @param theIsUndecorated true if the window should be undecorated, false otherwise.
     */
    public ApplicationWindow(final boolean theIsUndecorated) {
        myFrame = new JFrame("Drippy Spelunk");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setUndecorated(theIsUndecorated);
        myFrame.setVisible(true);
        myFrame.setResizable(false);
    }

    /**
     * Updates the content of the window to display a new scene.
     * This method handles replacing the current UI, updating key listeners,
     * and ensuring the window is correctly sized and focused.
     * The update is performed on the Event Dispatch Thread (EDT).
     *
     * @param theNewScene      The new scene (IState) to display.
     * @param theNewController The new controller to handle keyboard input for the scene.
     * @param theIsUndecorated true to set the window as undecorated, false otherwise.
     */
    public void updateContent(final IState theNewScene, final IController theNewController, final boolean theIsUndecorated) {
        SwingUtilities.invokeLater(() -> {
            setUndecorated(theIsUndecorated);

            // Remove existing key listeners to avoid conflicts
            for (final KeyListener listener : myFrame.getKeyListeners()) {
                myFrame.removeKeyListener(listener);
            }

            // Now, update the content
            myFrame.getContentPane().removeAll();

            final JComponent sceneUI = theNewScene.getSceneUI();
            if (sceneUI != null) {
                myFrame.add(sceneUI, BorderLayout.CENTER);
            }

            if (theNewController != null) {
                myFrame.addKeyListener(theNewController);
                myFrame.setFocusable(true);
                myFrame.requestFocusInWindow();
            }

            myFrame.revalidate();
            myFrame.pack();
            myFrame.setLocationRelativeTo(null);
            myFrame.repaint();
        });
    }

    /**
     * Removes a specific key listener from the window.
     * This is useful for cleaning up when a scene is exited.
     *
     * @param theController The controller (KeyListener) to remove.
     */
    public void removeCurrentKeyListener(final IController theController) {
        if (theController != null) {
            myFrame.removeKeyListener(theController);
        }
    }

    /**
     * Toggles the application between undecorated and decorated mode.
     * A decorated window has a native title bar and borders, while an undecorated one does not.
     *
     * @param theIsUndecorated true to enable undecorated, false for decorated.
     */
    public void setUndecorated(final boolean theIsUndecorated) {
        if (myFrame.isUndecorated() == theIsUndecorated) {
            return;
        }

        myFrame.dispose();
        myFrame.setUndecorated(theIsUndecorated);
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
        myFrame.requestFocusInWindow();
    }
}