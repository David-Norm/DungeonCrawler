package drippyspelunk.controller.audio;

import drippyspelunk.controller.config.ConfigManager;
import drippyspelunk.controller.states.GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all audio for the application, acting as its own event dispatcher.
 *
 * @author David Norman
 * @author Devin Arroyo
 * @version 2.3
 */
public class AudioManager implements AudioEventListener {

    /**
     * The file path for sound effects.
     */
    private static final String SFX_PATH = "audio/sfx/";

    /**
     * The file path for music tracks.
     */
    private static final String MUSIC_PATH = "audio/music/";

    /**
     * The file path for user interface sounds.
     */
    private static final String UI_PATH = "audio/ui/";

    /**
     * The unique identifier for the footstep sound effect.
     */
    private static final String SFX_FOOTSTEP_ID = "footstep";

    /**
     * The full file path to the footstep sound effect.
     */
    private static final String SFX_FOOTSTEP_PATH = SFX_PATH + "footstep.wav";

    /**
     * The full file path to the door opening sound effect.
     */
    private static final String DOOR_SFX = SFX_PATH + "door_open.wav";

    /**
     * The full file path to the menu select sound effect.
     */
    private static final String MENU_SELECT_SFX = UI_PATH + "menu_select.wav";

    /**
     * The default volume for all audio, ranging from 0.0 to 1.0.
     */
    private static final float DEFAULT_VOLUME = 0.8f;

    /**
     * The singleton instance of the AudioManager.
     */
    private static final AudioManager INSTANCE = new AudioManager();

    /**
     * A map of currently active looping sound effects, with their unique ID as the key.
     */
    private static final Map<String, AudioPlayer> myActiveLoopingSFX = new HashMap<>();

    /**
     * A list of registered listeners for audio events.
     */
    private static final List<AudioEventListener> myListeners = new ArrayList<>();

    /**
     * The current volume for music.
     */
    private static float myCurrentMusicVolume;

    /**
     * The current volume for sound effects.
     */
    private static float myCurrentSfxVolume;

    /**
     * The current volume for user interface sounds.
     */
    private static float myCurrentUiVolume;

    /**
     * The currently playing music track.
     */
    private static AudioPlayer myCurrentMusic;

    /**
     * The current game state, used to determine which music to play.
     */
    private static GameState myCurrentState;

    /**
     * The game state before entering the pause menu, used to resume the correct music.
     */
    private static GameState myPreviousMusicState = null;

    /**
     * Private constructor to prevent instantiation.
     */
    private AudioManager() {
        // Private constructor
    }

    /**
     * Initializes the AudioManager, preloading all necessary audio files and
     * registering itself as an audio event listener.
     */
    public static synchronized void initialize() {
        myCurrentMusicVolume = ConfigManager.getInstance().getFloat("Audio", "music", DEFAULT_VOLUME);
        myCurrentSfxVolume = ConfigManager.getInstance().getFloat("Audio", "sfx", DEFAULT_VOLUME);
        myCurrentUiVolume = ConfigManager.getInstance().getFloat("Audio", "ui", DEFAULT_VOLUME);

        AudioPlayer.preloadMusic("title", MUSIC_PATH + "title_theme.wav");
        AudioPlayer.preloadMusic("dungeon", MUSIC_PATH + "dungeon_ambient.wav");
        AudioPlayer.preloadMusic("victory", MUSIC_PATH + "victory.wav");
        AudioPlayer.preloadMusic("gameOver", MUSIC_PATH + "gameover.wav");

        addListener(INSTANCE);
    }

    /**
     * Adds a listener to receive audio events.
     *
     * @param theListener The listener to add.
     */
    public static void addListener(final AudioEventListener theListener) {
        myListeners.add(theListener);
    }

    /**
     * Dispatches an audio event to all registered listeners.
     * This method is the public interface for other classes to trigger audio events.
     *
     * @param theEvent The event to dispatch.
     */
    public static void dispatch(final AudioEvent theEvent) {
        for (AudioEventListener listener : myListeners) {
            listener.onAudioEvent(theEvent);
        }
    }

    /**
     * Gets the default volume.
     *
     * @return The default volume.
     */
    public static float getDefaultVolume() {
        return DEFAULT_VOLUME;
    }

    /**
     * Sets the music volume.
     *
     * @param theVolume The volume to set.
     */
    public static void setMusicVolume(final float theVolume) {
        myCurrentMusicVolume = theVolume;
        if (myCurrentMusic != null) {
            myCurrentMusic.setVolume(theVolume);
        }
    }

    /**
     * Sets the SFX volume.
     *
     * @param theVolume The volume to set.
     */
    public static void setSfxVolume(final float theVolume) {
        myCurrentSfxVolume = theVolume;
    }

    /**
     * Sets the UI volume.
     *
     * @param theVolume The volume to set.
     */
    public static void setUiVolume(final float theVolume) {
        myCurrentUiVolume = theVolume;
    }

    /**
     * Plays the appropriate music for a given game state.
     *
     * @param theState The game state.
     */
    public static void playMusicForState(final GameState theState) {
        if (theState == GameState.PAUSE_MENU) {
            if (myCurrentState != GameState.PAUSE_MENU) {
                myPreviousMusicState = myCurrentState;
            }
            if (myCurrentMusic != null) {
                myCurrentMusic.setVolume(0.3f);
            }
            stopAllLoopingSFX();
            myCurrentState = theState;
            return;
        }

        if (myCurrentState == GameState.PAUSE_MENU && theState == myPreviousMusicState) {
            resumeMusic();
            myCurrentState = theState;
            return;
        }

        if (myCurrentState == theState && myCurrentMusic != null && myCurrentMusic.isPlaying()) {
            return;
        }

        if (myCurrentMusic != null) {
            myCurrentMusic.stop();
        }

        myCurrentState = theState;

        switch (theState) {
            case TITLE_SCREEN -> myCurrentMusic = AudioPlayer.getMusicPlayer("title");
            case GAMEPLAY -> myCurrentMusic = AudioPlayer.getMusicPlayer("dungeon");
            case VICTORY -> myCurrentMusic = AudioPlayer.getMusicPlayer("victory");
            case GAME_OVER -> myCurrentMusic = AudioPlayer.getMusicPlayer("gameOver");
            default -> {
                myCurrentMusic = null;
                return;
            }
        }

        if (myCurrentMusic != null) {
            myCurrentMusic.playLoop();
            myCurrentMusic.setVolume(myCurrentMusicVolume);
        }
    }

    /**
     * Resumes the current music.
     */
    public static void resumeMusic() {
        if (myCurrentMusic != null) {
            myCurrentMusic.setVolume(myCurrentMusicVolume);
            if (!myCurrentMusic.isPlaying()) {
                myCurrentMusic.playLoop();
            }
        }
    }

    /**
     * Stops the current music.
     */
    public static void stopMusic() {
        if (myCurrentMusic != null) {
            myCurrentMusic.stop();
            myCurrentMusic = null;
        }
        myCurrentState = null;
    }

    /**
     * Stops a specific looping sound effect.
     *
     * @param theSfxId The identifier for the looping sound effect to stop.
     */
    private static void stopLoopingSFX(final String theSfxId) {
        AudioPlayer player = myActiveLoopingSFX.get(theSfxId);
        if (player != null) {
            player.stop();
            player.dispose();
            myActiveLoopingSFX.remove(theSfxId);
        }
    }

    /**
     * Stops all currently looping sound effects.
     */
    public static void stopAllLoopingSFX() {
        for (String sfxId : new ArrayList<>(myActiveLoopingSFX.keySet())) {
            stopLoopingSFX(sfxId);
        }
    }

    /**
     * Handles incoming audio events and plays the corresponding sound effects.
     * This is the central point for all SFX playback logic.
     *
     * @param theEvent The audio event that occurred.
     */
    @Override
    public void onAudioEvent(final AudioEvent theEvent) {
        switch (theEvent) {
            case MENU_SELECT -> new AudioBuilder().setPath(MENU_SELECT_SFX).setVolume(myCurrentUiVolume).playOnce();
            case PLAYER_DOOR_OPEN -> new AudioBuilder().setPath(DOOR_SFX).setVolume(myCurrentSfxVolume).playOnce();
            case PLAYER_FOOTSTEP_START ->
                    new AudioBuilder().setPath(SFX_FOOTSTEP_PATH).setSfxId(SFX_FOOTSTEP_ID).setVolume(myCurrentSfxVolume).startLoop();
            case PLAYER_FOOTSTEP_STOP -> stopLoopingSFX(SFX_FOOTSTEP_ID);
            default -> {
                // Do nothing for unhandled events
            }
        }
    }

    /**
     * A builder class for playing sound effects.
     */
    public static class AudioBuilder {
        /**
         * The unique identifier for the sound effect.
         */
        private String mySfxId;

        /**
         * The path to the audio file.
         */
        private String myPath;

        /**
         * The volume for the audio, from 0.0 to 1.0.
         */
        private float myVolume = DEFAULT_VOLUME;

        /**
         * Sets the sound effect ID.
         *
         * @param theId The sound effect ID.
         * @return This AudioBuilder instance for method chaining.
         */
        public AudioBuilder setSfxId(final String theId) {
            mySfxId = theId;
            return this;
        }

        /**
         * Sets the audio file path.
         *
         * @param thePath The path to the audio file.
         * @return This AudioBuilder instance for method chaining.
         */
        public AudioBuilder setPath(final String thePath) {
            myPath = thePath;
            return this;
        }

        /**
         * Sets the volume for the audio.
         *
         * @param theVolume The volume, from 0.0 to 1.0.
         * @return This AudioBuilder instance for method chaining.
         */
        public AudioBuilder setVolume(final float theVolume) {
            myVolume = theVolume;
            return this;
        }

        /**
         * Plays a sound effect once.
         */
        public void playOnce() {
            if (myPath == null) {
                return;
            }
            AudioPlayer.playSoundEffect(myPath, myVolume);
        }

        /**
         * Starts a looping sound effect.
         */
        public void startLoop() {
            if (mySfxId == null || myPath == null) {
                return;
            }
            if (myActiveLoopingSFX.containsKey(mySfxId)) {
                AudioPlayer player = myActiveLoopingSFX.get(mySfxId);
                if (!player.isPlaying()) {
                    player.playLoop();
                    player.setVolume(myVolume);
                }
                return;
            }
            AudioPlayer player = new AudioPlayer(myPath);
            player.setVolume(myVolume);
            player.playLoop();
            myActiveLoopingSFX.put(mySfxId, player);
        }
    }
}