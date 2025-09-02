package drippyspelunk.controller.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * AudioPlayer class with volume control and looping capabilities.
 *
 * @author David Norman
 * @author Devin Arroyo
 * @version 1.3
 */
public class AudioPlayer {

    /**
     * A map to cache preloaded music players by a string identifier.
     */
    private static final Map<String, AudioPlayer> myMusicPlayers = new HashMap<>();

    /**
     * The underlying Clip object used to play the audio.
     */
    private final Clip myClip;

    /**
     * The volume control for the audio clip. This can be null if not supported.
     */
    private final FloatControl myVolumeControl;

    /**
     * Constructs an AudioPlayer for a given file path.
     *
     * @param theFilepath The path to the audio file.
     */
    public AudioPlayer(final String theFilepath) {
        try {
            final InputStream audioStreamInput = getClass().getClassLoader().getResourceAsStream(theFilepath);

            if (audioStreamInput == null) {
                throw new IOException("Resource not found on classpath: " + theFilepath);
            }

            final InputStream bufferedAudioStream = new BufferedInputStream(audioStreamInput);

            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedAudioStream);
            myClip = AudioSystem.getClip();
            myClip.open(audioStream);

            if (myClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                myVolumeControl = (FloatControl) myClip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                myVolumeControl = null;
            }

        } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException exception) {
            throw new RuntimeException("Failed to initialize AudioPlayer for: " + theFilepath, exception);
        }
    }

    /**
     * Static method to preload and cache music players.
     *
     * @param theIdentifier The identifier for the music player.
     * @param theFilepath   The path to the audio file.
     */
    public static void preloadMusic(final String theIdentifier, final String theFilepath) {
        try {
            final AudioPlayer player = new AudioPlayer(theFilepath);
            player.setVolume(AudioManager.getDefaultVolume());
            myMusicPlayers.put(theIdentifier, player);
        } catch (final RuntimeException exception) {
            throw new RuntimeException("Failed to initialize AudioPlayer for: " + theFilepath, exception);
        }
    }

    /**
     * Get a preloaded music player.
     *
     * @param theIdentifier The identifier for the music player.
     * @return The preloaded AudioPlayer instance.
     */
    public static AudioPlayer getMusicPlayer(final String theIdentifier) {
        return myMusicPlayers.get(theIdentifier);
    }

    /**
     * Static helper method to play quick sound effects.
     *
     * @param theFilepath The path to the sound effect file.
     * @param theVolume   The volume to play the sound effect at.
     */
    public static void playSoundEffect(final String theFilepath, final float theVolume) {
        try {
            final InputStream streamInput = AudioPlayer.class.getClassLoader().getResourceAsStream(theFilepath);
            if (streamInput == null) {
                throw new IOException("Sound effect resource not found on classpath: " + theFilepath);
            }

            final InputStream bufferedStreamInput = new BufferedInputStream(streamInput);

            final AudioInputStream stream = AudioSystem.getAudioInputStream(bufferedStreamInput);
            final Clip effectClip = AudioSystem.getClip();
            effectClip.open(stream);

            if (effectClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                final FloatControl gainControl = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                final float min = gainControl.getMinimum();
                final float max = gainControl.getMaximum();
                final float gain = min + (max - min) * theVolume;
                gainControl.setValue(gain);
            }

            effectClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    event.getLine().close();
                }
            });

            effectClip.setFramePosition(0);
            effectClip.start();

        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Play the audio clip in a continuous loop.
     */
    public void playLoop() {
        if (myClip != null) {
            myClip.setFramePosition(0);
            myClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stop the audio clip.
     */
    public void stop() {
        if (myClip != null && myClip.isRunning()) {
            myClip.stop();
        }
    }

    /**
     * Set volume (0.0 to 1.0).
     *
     * @param theVolume The volume to set, a value between 0.0 and 1.0.
     */
    public void setVolume(final float theVolume) {
        if (myVolumeControl != null) {
            final float min = myVolumeControl.getMinimum();
            final float max = myVolumeControl.getMaximum();
            final float gain = min + (max - min) * theVolume;
            myVolumeControl.setValue(gain);
        }
    }

    /**
     * Check if the clip is currently playing.
     *
     * @return True if the clip is running, false otherwise.
     */
    public boolean isPlaying() {
        boolean playing = false;
        if (myClip != null) {
            playing = myClip.isRunning();
        }
        return playing;
    }

    /**
     * Cleanup method to dispose of resources.
     */
    public void dispose() {
        if (myClip != null) {
            myClip.close();
        }
    }
}