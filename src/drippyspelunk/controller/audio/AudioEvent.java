package drippyspelunk.controller.audio;

/**
 * Enum of audio events to be played.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public enum AudioEvent {
    /**
     * Represents the event of a menu item being selected.
     */
    MENU_SELECT,
    /**
     * Represents the event of a player's footstep sound beginning.
     */
    PLAYER_FOOTSTEP_START,
    /**
     * Represents the event of a player's footstep sound stopping.
     */
    PLAYER_FOOTSTEP_STOP,
    /**
     * Represents the event of a door being opened by the player.
     */
    PLAYER_DOOR_OPEN
}