package drippyspelunk.controller.core;

import drippyspelunk.controller.config.ConfigManager;
import drippyspelunk.controller.input.IController;
import drippyspelunk.controller.states.IState;
import drippyspelunk.view.ApplicationWindow;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The driver of the program. Acts as the manager of the application. This class is responsible for
 * initializing and managing the application window, controlling the game loop, and handling state transitions.
 * It implements the {@link Runnable} interface to run the game logic in a separate thread.
 *
 * @author Devin Arroyo
 * @version 1.9
 */
public class ProgramDriver implements Runnable {

    /**
     * An integer representing 1,000,000. This constant is used for converting nanoseconds to milliseconds
     * when calculating sleep times for the game loop.
     */
    private static final int ONE_MILLION = 1000000;

    /**
     * An integer representing 60 FPS.
     */
    private static final long SIXTY_PER_SECOND = 1000000000 / 60;

    /**
     * The application window of the program. Holds all visual elements.
     */
    private final ApplicationWindow myApplicationWindow;
    /**
     * A variable that represents if the game is paused.
     */
    private final AtomicBoolean myIsPaused = new AtomicBoolean(false);
    /**
     * The current controller of the program.
     */
    private IController myCurrentController;
    /**
     * The thread that runs the game logic.
     */
    private Thread myLogicThread;
    /**
     * A variable that represents if the game thread is running.
     */
    private volatile boolean myIsRunning = false;
    /**
     * The current state of the program.
     */
    private volatile IState myCurrentState;


    /**
     * The constructor for the program driver. Creates a new application window.
     */
    public ProgramDriver() {
        myApplicationWindow = new ApplicationWindow(false);
    }

    /**
     * Switches the current state to a new state and updates the window content accordingly.
     * This method is now responsible for setting the GUI scale and undecorated state.
     *
     * @param theNewState      The new state to transition to.
     * @param theNewController The controller for the new state.
     * @param theIsUndecorated The undecorated state to apply.
     */
    public void updateWindowContent(final IState theNewState, final IController theNewController, final boolean theIsUndecorated) {
        synchronized (this) { // Synchronize to prevent race conditions
            stop();

            if (myCurrentController != null) {
                myCurrentController.cleanup();
                myApplicationWindow.removeCurrentKeyListener(myCurrentController);
            }

            if (myCurrentState != null) {
                myCurrentState.dispose();
            }

            myCurrentController = theNewController;
            myApplicationWindow.updateContent(theNewState, myCurrentController, theIsUndecorated);
            myCurrentState = theNewState;

            start();

        }
    }

    /**
     * Starts the game loop thread.
     */
    public synchronized void start() {
        if (myIsRunning) return;

        myIsRunning = true;

        myLogicThread = new Thread(this, "GameLoopThread");
        myLogicThread.start();
        System.out.println("ProgramDriver started.");
    }

    /**
     * Stops the game loop thread.
     */
    public synchronized void stop() {
        if (!myIsRunning) return;

        myIsRunning = false;

        try {
            if (myLogicThread != null) {
                myLogicThread.join();
            }
        } catch (InterruptedException theException) {
            Thread.currentThread().interrupt();
            System.out.println("Game thread interrupted during stop: " + theException.getMessage());
        } finally {
            myLogicThread = null;
        }

        System.out.println("ProgramDriver stopped.");
    }

    /**
     * The main game loop logic, responsible for updating and rendering the game state at a fixed rate.
     */
    @Override
    public void run() {

        long lastLoopTime = System.nanoTime();

        while (myIsRunning) {

            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;

            lastLoopTime = now;

            IState sceneToUpdate;
            synchronized (this) {
                sceneToUpdate = myCurrentState;
            }

            if (sceneToUpdate != null) {
                if (!myIsPaused.get()) {
                    sceneToUpdate.update(updateLength);
                }
                sceneToUpdate.render(now);
            }

            try {
                long timeSinceLastFrameStart = System.nanoTime() - now;
                long gameLoopSleepTime = (SIXTY_PER_SECOND - timeSinceLastFrameStart) / ONE_MILLION;

                if (gameLoopSleepTime > 0) {
                    Thread.sleep(gameLoopSleepTime);
                }

            } catch (InterruptedException theException) {
                Thread.currentThread().interrupt();
                System.out.println("Game loop interrupted " + theException.getMessage());

                myIsRunning = false;
            }
        }
    }

    /**
     * Gets the GUI scale from the configuration.
     *
     * @return The GUI scale as an integer.
     */
    public int getGuiScale() {
        return ConfigManager.getInstance().getInt("Scene", "gui_scale", 1);
    }

    /**
     * Toggles the pause state of the game.
     */
    public void togglePause() {
        myIsPaused.set(!myIsPaused.get());
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return True if the game is paused, false otherwise.
     */
    public boolean isPaused() { // New method to query pause state
        return myIsPaused.get();
    }

    /**
     * Gets the current difficulty from the configuration.
     *
     * @return The difficulty level as an integer.
     */
    public int getDifficulty() {
        return ConfigManager.getInstance().getInt("Scene", "difficulty", 1);
    }
}