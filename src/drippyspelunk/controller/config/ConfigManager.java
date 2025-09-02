package drippyspelunk.controller.config;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

/**
 * A singleton ConfigManager that handles INI file operations.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
public final class ConfigManager {

    /**
     * The path to the configuration file, located in the user's working directory.
     */
    private static final File MY_CONFIG_FILE = new File(
            System.getProperty("user.dir") + "/res/userdata/config/settings.ini");

    /**
     * The single instance of the ConfigManager, enforcing the singleton pattern.
     */
    private static ConfigManager myInstance;

    /**
     * The Ini object that holds the parsed configuration data.
     */
    private final Ini myIni;

    /**
     * Private constructor to enforce the singleton pattern.
     *
     * @param theIni The Ini object to be managed.
     */
    private ConfigManager(final Ini theIni) {
        myIni = theIni;
    }

    /**
     * Initializes the singleton instance of ConfigManager.
     * It checks for an existing configuration file or creates a default one if it doesn't exist.
     */
    public static synchronized void initialize() {
        if (myInstance != null) {
            return;
        }

        try {
            if (!MY_CONFIG_FILE.exists()) {
                if (!createDefaultConfig()) {
                    return;
                }
            }
            Ini loadedIni = new Ini(MY_CONFIG_FILE);
            myInstance = new ConfigManager(loadedIni);
        } catch (IOException ignored) {
        }
    }

    /**
     * Creates a new configuration file with default settings.
     *
     * @return True if the file was created successfully, false otherwise.
     */
    private static boolean createDefaultConfig() {
        boolean mySuccess = false;
        final Ini defaultIni = new Ini();
        defaultIni.put("Scene", "gui_scale", "1");
        defaultIni.put("Scene", "undecorated", "false");
        defaultIni.put("Audio", "music", "0.8f");
        defaultIni.put("Audio", "sfx", "0.8f");
        defaultIni.put("Audio", "ui", "0.8f");
        defaultIni.put("Scene", "difficulty", "1");


        try {
            final File parentDirectory = MY_CONFIG_FILE.getParentFile();
            if (parentDirectory != null && !parentDirectory.exists()) {
                if (parentDirectory.mkdirs()) {
                    defaultIni.store(MY_CONFIG_FILE);
                    mySuccess = true;
                }
            } else {
                defaultIni.store(MY_CONFIG_FILE);
                mySuccess = true;
            }
        } catch (final IOException ignored) {

        }
        return mySuccess;
    }

    /**
     * Gets the singleton instance of the ConfigManager.
     *
     * @return The singleton ConfigManager instance.
     * @throws IllegalStateException if the manager has not been initialized.
     */
    public static ConfigManager getInstance() {
        if (myInstance == null) {
            throw new IllegalStateException("ConfigManager has not been initialized.");
        }
        return myInstance;
    }

    /**
     * Retrieves an integer value from the configuration.
     *
     * @param theSection      The section of the INI file.
     * @param theKey          The key for the value.
     * @param theDefaultValue The default value to return if the key is not found or is invalid.
     * @return The integer value from the INI file, or the default value.
     */
    public int getInt(final String theSection, final String theKey, final int theDefaultValue) {
        int myValue;
        try {
            myValue = myIni.get(theSection, theKey, int.class);
        } catch (final IllegalArgumentException exception) {
            myValue = theDefaultValue;
        }
        return myValue;
    }

    /**
     * Retrieves a boolean value from the configuration.
     *
     * @param theSection      The section of the INI file.
     * @param theKey          The key for the value.
     * @param theDefaultValue The default value to return if the key is not found or is invalid.
     * @return The boolean value from the INI file, or the default value.
     */
    public boolean getBoolean(final String theSection, final String theKey, final boolean theDefaultValue) {
        boolean myValue;
        try {
            myValue = myIni.get(theSection, theKey, boolean.class);
        } catch (final IllegalArgumentException exception) {
            myValue = theDefaultValue;
        }
        return myValue;
    }

    /**
     * Retrieves a float value from the configuration.
     *
     * @param theSection      The section of the INI file.
     * @param theKey          The key for the value.
     * @param theDefaultValue The default value to return if the key is not found or is invalid.
     * @return The float value from the INI file, or the default value.
     */
    public float getFloat(final String theSection, final String theKey, final float theDefaultValue) {
        float myValue;
        try {
            myValue = myIni.get(theSection, theKey, float.class);
        } catch (final IllegalArgumentException exception) {
            myValue = theDefaultValue;
        }
        return myValue;
    }

    /**
     * Sets a value in the configuration and saves the changes.
     *
     * @param theSection The section of the INI file.
     * @param theKey     The key for the value.
     * @param theValue   The new value to set.
     * @throws IOException if an I/O error occurs while saving the file.
     */
    public void setValue(final String theSection, final String theKey, final String theValue) throws IOException {
        myIni.put(theSection, theKey, theValue);
        saveChanges();
    }

    /**
     * Saves all changes to the INI file.
     *
     * @throws IOException if an I/O error occurs during the save operation.
     */
    private void saveChanges() throws IOException {
        myIni.store();
    }
}