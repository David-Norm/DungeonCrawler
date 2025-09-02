package drippyspelunk.model.menu;

/**
 * A data transferring object to hold and pass settings information.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public class SettingsData {

    /**
     * The scale of the graphical user interface.
     */
    private final int myGuiScale;
    /**
     * The difficulty level of the game.
     */
    private final int myDifficulty;
    /**
     * A flag indicating if the window is undecorated.
     */
    private final boolean myUndecorated;
    /**
     * The volume level for music.
     */
    private final float myMusicVolume;
    /**
     * The volume level for sound effects.
     */
    private final float mySfxVolume;
    /**
     * The volume level for the user interface.
     */
    private final float myUiVolume;

    /**
     * Constructs a new SettingsData object with the specified settings.
     *
     * @param theGuiScale    The GUI scale.
     * @param theUndecorated A boolean indicating if the window is undecorated.
     * @param theMusicVolume The music volume.
     * @param theSfxVolume   The sound effects volume.
     * @param theUiVolume    The user interface volume.
     * @param theDifficulty  The difficulty level.
     */
    public SettingsData(final int theGuiScale,
                        final boolean theUndecorated,
                        final float theMusicVolume,
                        final float theSfxVolume,
                        final float theUiVolume,
                        final int theDifficulty) {
        myGuiScale = theGuiScale;
        myUndecorated = theUndecorated;
        myMusicVolume = theMusicVolume;
        mySfxVolume = theSfxVolume;
        myUiVolume = theUiVolume;
        myDifficulty = theDifficulty;
    }

    /**
     * Gets the current GUI scale.
     *
     * @return The GUI scale.
     */
    public int getGuiScale() {
        return myGuiScale;
    }

    /**
     * Checks if the window is undecorated.
     *
     * @return A boolean indicating if the window is undecorated.
     */
    public boolean isUndecorated() {
        return myUndecorated;
    }

    /**
     * Gets the current music volume.
     *
     * @return The music volume.
     */
    public float getMusicVolume() {
        return myMusicVolume;
    }

    /**
     * Gets the current sound effects volume.
     *
     * @return The sound effects volume.
     */
    public float getSfxVolume() {
        return mySfxVolume;
    }

    /**
     * Gets the current user interface volume.
     *
     * @return The user interface volume.
     */
    public float getUiVolume() {
        return myUiVolume;
    }

    /**
     * Gets the current difficulty level.
     *
     * @return The difficulty level.
     */
    public int getDifficulty() {
        return myDifficulty;
    }

}