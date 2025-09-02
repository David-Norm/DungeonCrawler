package drippyspelunk.model.menu;

import drippyspelunk.controller.config.ConfigManager;
import drippyspelunk.controller.database.DatabaseManager;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Character;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

/**
 * The logic for the title screen.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.4
 */
public class TitleScreenLogic {

    /**
     * Property constant for game ticks.
     */
    public static final String GAME_TICK_PROPERTY = "gameTick";
    /**
     * Property constant for when settings are applied.
     */
    public static final String SETTINGS_APPLIED_PROPERTY = "settingsApplied";

    /**
     * Supports firing property change events.
     */
    private final PropertyChangeSupport myPCS = new PropertyChangeSupport(this);
    /**
     * The current settings of the application.
     */
    private SettingsData myCurrentSettings;
    /**
     * The character selected by the player.
     */
    private Character mySelectedCharacter;

    /**
     * Constructs a new TitleScreenLogic object and loads the current settings.
     */
    public TitleScreenLogic() {
        loadCurrentSettings();
    }

    /**
     * Applies and saves the new settings to the configuration file.
     *
     * @param theNewSettings The new settings data to apply.
     */
    public void applyAndSaveSettings(final SettingsData theNewSettings) {
        // Handle null settings
        if (theNewSettings == null) {
            return;
        }

        try {
            ConfigManager.getInstance().setValue("Scene", "gui_scale", String.valueOf(theNewSettings.getGuiScale()));
            ConfigManager.getInstance().setValue("Scene", "undecorated", String.valueOf(theNewSettings.isUndecorated()));
            ConfigManager.getInstance().setValue("Audio", "music", String.valueOf(theNewSettings.getMusicVolume()));
            ConfigManager.getInstance().setValue("Audio", "sfx", String.valueOf(theNewSettings.getSfxVolume()));
            ConfigManager.getInstance().setValue("Audio", "ui", String.valueOf(theNewSettings.getUiVolume()));
            ConfigManager.getInstance().setValue("Scene", "difficulty", String.valueOf(theNewSettings.getDifficulty()));

            final SettingsData oldSettings = myCurrentSettings;
            myCurrentSettings = theNewSettings;
            myPCS.firePropertyChange(SETTINGS_APPLIED_PROPERTY, oldSettings, myCurrentSettings);

        } catch (final IOException ignored) {
        }
    }

    /**
     * Gets the current settings.
     *
     * @return The current settings data.
     */
    public SettingsData getCurrentSettings() {
        return myCurrentSettings;
    }

    /**
     * Loads the current settings from the configuration file.
     */
    public void loadCurrentSettings() {
        final ConfigManager config = ConfigManager.getInstance();
        myCurrentSettings = new SettingsData(
                config.getInt("Scene", "gui_scale", 1),
                config.getBoolean("Scene", "undecorated", false),
                config.getFloat("Audio", "music", 0.8f),
                config.getFloat("Audio", "sfx", 0.8f),
                config.getFloat("Audio", "ui", 0.8f),
                config.getInt("Scene", "difficulty", 1)
        );
    }

    /**
     * Gets a list of available characters from the database.
     *
     * @return A list of available player characters.
     */
    public List<Character> getAvailableCharacters() {
        DatabaseManager dbManager = DatabaseManager.getMyInstance();
        return dbManager.getCharactersByType(Character.CharacterType.PLAYER);
    }

    /**
     * Gets the currently selected character.
     *
     * @return The selected character.
     */
    public Character getSelectedCharacter() {
        return mySelectedCharacter;
    }

    /**
     * Sets the currently selected character.
     *
     * @param theCharacter The character to be set as selected.
     */
    public void setSelectedCharacter(Character theCharacter) {
        mySelectedCharacter = theCharacter;
    }

    /**
     * Adds a property change listener.
     *
     * @param theListener The listener to add.
     */
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPCS.addPropertyChangeListener(theListener);
    }

    /**
     * Removes a property change listener.
     *
     * @param theListener The listener to remove.
     */
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPCS.removePropertyChangeListener(theListener);
    }
}