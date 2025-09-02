package drippyspelunk.controller.audio;

/**
 * Interface for components that listen for audio events.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public interface AudioEventListener {

    /**
     * Called when an audio event is dispatched.
     *
     * @param theEvent The audio event that occurred.
     */
    void onAudioEvent(AudioEvent theEvent);

}