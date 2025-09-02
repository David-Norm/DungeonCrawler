package drippyspelunk.controller.core;

import drippyspelunk.controller.audio.AudioManager;
import drippyspelunk.controller.config.ConfigManager;
import drippyspelunk.controller.config.RoomConfigManager;
import drippyspelunk.controller.input.IController;
import drippyspelunk.controller.sprite.SpriteManager;
import drippyspelunk.controller.states.*;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic.GameEndingType;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Character;

import javax.swing.*;

/**
 * Manages the state of the application and allows for scene changes.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.15
 */
public class StateManager {

    /**
     * The singleton instance of the StateManager.
     */
    private static StateManager myInstance;
    /**
     * The current scene or game state being displayed.
     */
    private IState myCurrentScene;
    /**
     * The main program driver responsible for the game loop and window management.
     */
    private ProgramDriver myDriver;

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private StateManager() {
        ConfigManager.initialize();
        AudioManager.initialize();
        SpriteManager.initialize();
        RoomConfigManager.initialize();
    }

    /**
     * Gets the singleton instance of the StateManager.
     *
     * @return The singleton StateManager instance.
     */
    public static synchronized StateManager getInstance() {
        if (myInstance == null) {
            myInstance = new StateManager();
        }
        return myInstance;
    }

    /**
     * Sets the game engine (ProgramDriver) for this StateManager.
     *
     * @param theDriver The ProgramDriver instance.
     */
    public void setGameEngine(final ProgramDriver theDriver) {
        myDriver = theDriver;
    }

    /**
     * Transitions the application to a new game state.
     *
     * @param theNewState The new state to transition to.
     */
    private void transitionToState(final IState theNewState) {

        if (myCurrentScene != null) {
            IController oldController = myCurrentScene.getController();

            myCurrentScene.dispose();

            if (oldController != null) {
                oldController.cleanup();
            }
        }

        AudioManager.stopAllLoopingSFX();

        GameState targetMusicState = null;
        if (theNewState instanceof TitleScreenState) {
            targetMusicState = GameState.TITLE_SCREEN;
        } else if (theNewState instanceof DungeonCrawlerState) {
            targetMusicState = GameState.GAMEPLAY;
        } else if (theNewState instanceof GameOverState) {
            targetMusicState = GameState.GAME_OVER;
        }

        if (targetMusicState != null) {
            AudioManager.playMusicForState(targetMusicState);
        } else {
            AudioManager.stopMusic();
        }

        // Starts the new state.
        theNewState.init();
        theNewState.activate();

        // Let the Driver handle the stuff
        if (myDriver != null) {
            SwingUtilities.invokeLater(() -> {
                myDriver.updateWindowContent(theNewState,
                        theNewState.getController(),
                        ConfigManager.getInstance().getBoolean("Scene", "undecorated", false));
            });
        }

        // Update StateManager's internal reference *after* driver has accepted the new scene
        myCurrentScene = theNewState;
    }

    /**
     * Reinitializes the title screen, resetting configurations and audio.
     */
    public void reinitializeTitleScreen() {
        ConfigManager.initialize();

        AudioManager.stopMusic();
        AudioManager.playMusicForState(GameState.TITLE_SCREEN);

        goToTitleScreen();
    }


    /**
     * Transitions the application to the title screen state.
     */
    public void goToTitleScreen() {
        transitionToState(new TitleScreenState(this, myDriver.getGuiScale()));
    }

    /**
     * Transitions the application to the dungeon crawler game state.
     *
     * @param theSelectedCharacter The character selected by the player.
     * @param thePlayerName        The name of the player.
     */
    public void goToDungeonCrawler(final Character theSelectedCharacter, final String thePlayerName) {
        if (myCurrentScene instanceof DungeonCrawlerState) {
            return;
        }

        DungeonCrawlerLogic gameLogic = new DungeonCrawlerLogic(theSelectedCharacter, thePlayerName, myDriver.getDifficulty());

        transitionToState(new DungeonCrawlerState(gameLogic, myDriver.getGuiScale()));
    }

    /**
     * Transitions the application to the game over state.
     *
     * @param theEndingType The type of game ending (win or loss).
     */
    public void goToGameOver(final GameEndingType theEndingType) {
        transitionToState(new GameOverState(this, myDriver.getGuiScale(), theEndingType));
    }

    /**
     * Pauses the game and sets the music to the pause menu music.
     */
    public void pauseGame() {
        if (myDriver != null && !myDriver.isPaused()) {
            myDriver.togglePause();
            AudioManager.playMusicForState(GameState.PAUSE_MENU);
        }
    }

    /**
     * Unpauses the game and resumes the appropriate music.
     */
    public void unpauseGame() {
        if (myDriver != null && myDriver.isPaused()) {
            myDriver.togglePause();

            GameState stateAfterUnpause = null;
            if (myCurrentScene instanceof DungeonCrawlerState) {
                stateAfterUnpause = GameState.GAMEPLAY;
            } else if (myCurrentScene instanceof TitleScreenState) {
                stateAfterUnpause = GameState.TITLE_SCREEN;
            }

            if (stateAfterUnpause != null) {
                AudioManager.playMusicForState(stateAfterUnpause);
            } else {
                AudioManager.stopMusic();
            }
        }
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return True if the game is paused, false otherwise.
     */
    public boolean isGamePaused() {
        return myDriver != null && myDriver.isPaused();
    }

}