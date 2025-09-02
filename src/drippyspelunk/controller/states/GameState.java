package drippyspelunk.controller.states;

/**
 * Enum representing different GameStates for audio management.
 * This enumeration defines the distinct states of the game,
 * which can be used to manage different audio tracks or sound effects
 * appropriate for each game context.
 *
 * @author David Norman
 * @version 1.0
 */
public enum GameState {
    /**
     * Represents the initial title or main menu screen.
     */
    TITLE_SCREEN,

    /**
     * Represents the main gameplay state (e.g., dungeon crawling).
     */
    GAMEPLAY,

    /**
     * Represents a paused state, typically a menu overlay during gameplay.
     */
    PAUSE_MENU,

    /**
     * Represents the game over screen, typically displayed upon player death.
     */
    GAME_OVER,

    /**
     * Represents the victory screen, displayed upon successfully completing the game.
     */
    VICTORY
}