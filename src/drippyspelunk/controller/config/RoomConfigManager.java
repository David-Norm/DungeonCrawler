package drippyspelunk.controller.config;

import drippyspelunk.model.dungeon.RoomTemplate;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A singleton ConfigManager for loading room templates from an INI file.
 *
 * @author Devin Arroyo
 * @version 1.4
 */
public final class RoomConfigManager {

    /**
     * The path to the configuration file for room layouts.
     */
    private static final File ROOMS_CONFIG_FILE = new File(
            System.getProperty("user.dir") + "/res/layout/rooms.ini");
    /**
     * The height of the room grid in tiles.
     */
    private static final int ROOM_GRID_HEIGHT = 13;
    /**
     * The width of the room grid in tiles.
     */
    private static final int ROOM_GRID_WIDTH = 15;
    /**
     * The single instance of the RoomConfigManager.
     */
    private static RoomConfigManager myInstance;
    /**
     * The current difficulty level of the game.
     */
    private static int myDifficulty;
    /**
     * A map to store and access room templates by their name.
     */
    private final Map<String, RoomTemplate> myRoomTemplates;

    /**
     * Private constructor to enforce the singleton pattern.
     *
     * @param theDifficulty The current game difficulty level.
     */
    private RoomConfigManager(int theDifficulty) {
        myDifficulty = theDifficulty;
        myRoomTemplates = new HashMap<>();
        try {
            Ini ini = new Ini(ROOMS_CONFIG_FILE);
            parseRoomTemplates(ini);
        } catch (IOException ignored) {
        }
    }

    /**
     * Initializes the singleton instance of the RoomConfigManager.
     */
    public static synchronized void initialize() {
        if (myInstance == null) {
            myInstance = new RoomConfigManager(myDifficulty);
        }
    }

    /**
     * Gets the singleton instance of the RoomConfigManager.
     *
     * @param theDifficulty The current game difficulty level.
     * @return The singleton RoomConfigManager instance.
     * @throws IllegalStateException if the manager has not been initialized.
     */
    public static RoomConfigManager getInstance(int theDifficulty) {
        myDifficulty = theDifficulty;
        if (myInstance == null) {
            throw new IllegalStateException("RoomConfigManager has not been initialized.");
        }
        return myInstance;
    }

    /**
     * Parses all room templates from the INI file and populates the map.
     *
     * @param theIni The Ini object containing room template data.
     */
    private void parseRoomTemplates(final Ini theIni) {
        for (String sectionName : theIni.keySet()) {
            Section section = theIni.get(sectionName);
            int[][] layout = parseLayoutGrid(section);
            int[][] tileset = parseTilesetGrid(section);
            int[][] spawnsGrid = parseSpawnsGrid(section);

            int enemyChance = parseSpawnChance(section, "enemyChance", 50) + (20 * myDifficulty);
            int potionChance = parseSpawnChance(section, "potionChance", 75) - (8 * myDifficulty);
            int chestChance = parseSpawnChance(section, "chestChance", 25) - (8 * myDifficulty);
            int bombChance = parseSpawnChance(section, "bombChance", 25) - (8 * myDifficulty);
            int trapChance = parseSpawnChance(section, "trapChance", 25) + (5 * myDifficulty);
            int breakableWallChance = parseSpawnChance(section, "breakableWallChance", 50);

            myRoomTemplates.put(sectionName, new RoomTemplate(layout, tileset, spawnsGrid, enemyChance, potionChance, chestChance, bombChance, trapChance, breakableWallChance));
        }
    }

    /**
     * Parses a spawn chance value from a section of the INI file.
     *
     * @param theSection The INI section to parse.
     * @param theKey     The key for the spawn chance value.
     * @param theDefault The default value if the key is not found or is invalid.
     * @return The parsed spawn chance value.
     */
    private int parseSpawnChance(final Section theSection, final String theKey, final int theDefault) {
        if (theSection != null) {
            try {
                return Integer.parseInt(theSection.get(theKey));
            } catch (NumberFormatException ignored) {
            }
        }
        return theDefault;
    }

    /**
     * Parses the room layout grid from an INI section.
     *
     * @param theSection The INI section containing the layout data.
     * @return A 2D integer array representing the room's layout.
     */
    private int[][] parseLayoutGrid(final Section theSection) {
        int[][] grid = new int[ROOM_GRID_HEIGHT][ROOM_GRID_WIDTH];
        for (int i = 0; i < ROOM_GRID_HEIGHT; i++) {
            String key = "layout" + "-" + (char) ('A' + i);
            String line = theSection.get(key);
            if (line != null && line.length() == ROOM_GRID_WIDTH) {
                for (int j = 0; j < line.length(); j++) {
                    char charValue = line.charAt(j);
                    grid[i][j] = getWallValueFromChar(charValue);
                }
            }
        }
        return grid;
    }

    /**
     * Converts a character from the INI file to an integer wall value.
     *
     * @param theChar The character to convert.
     * @return The integer representation of the wall type.
     */
    private int getWallValueFromChar(final char theChar) {
        int result = 0;
        if (theChar >= 'A' && theChar <= 'U') {
            result = theChar - 'A' + 11;
        }
        return result;
    }

    /**
     * Parses the room tileset grid from an INI section.
     *
     * @param theSection The INI section containing the tileset data.
     * @return A 2D integer array representing the room's tileset.
     */
    private int[][] parseTilesetGrid(final Section theSection) {
        int[][] grid = new int[ROOM_GRID_HEIGHT][ROOM_GRID_WIDTH];
        for (int i = 0; i < ROOM_GRID_HEIGHT; i++) {
            String key = "tileset" + "-" + (char) ('A' + i);
            String line = theSection.get(key);
            if (line != null && line.length() == ROOM_GRID_WIDTH) {
                for (int j = 0; j < line.length(); j++) {
                    char charValue = line.charAt(j);
                    int value = switch (charValue) {
                        case 'a' -> 1; // Floor type 1
                        case 'b' -> 2; // Floor type 2
                        case 'c' -> 3; // Floor type 3
                        case 'd' -> 4; // Floor type 4
                        case 'e' -> 5; // Floor type 5
                        case 'f' -> 6; // Floor type 6
                        case 'g' -> 7; // Floor type 7
                        case 'h' -> 8; // Floor type 8
                        case 'i' -> 9; // Floor type 9
                        default -> 0; // Default to empty
                    };
                    grid[i][j] = value;
                }
            }
        }
        return grid;
    }

    /**
     * Parses the spawn grid from an INI section.
     *
     * @param theSection The INI section containing the spawn data.
     * @return A 2D integer array representing the room's spawns.
     */
    private int[][] parseSpawnsGrid(final Section theSection) {
        int[][] grid = new int[ROOM_GRID_HEIGHT][ROOM_GRID_WIDTH];
        for (int i = 0; i < ROOM_GRID_HEIGHT; i++) {
            String key = "spawns" + "-" + (char) ('A' + i);
            String line = theSection.get(key);
            if (line != null && line.length() == ROOM_GRID_WIDTH) {
                for (int j = 0; j < line.length(); j++) {
                    char charValue = line.charAt(j);
                    int value = switch (charValue) {
                        case 'P' -> 1; // Guaranteed Potion
                        case 'K' -> 2; // Guaranteed Enemy
                        case 'L' -> 3; // Guaranteed Chest
                        case 'p' -> 4; // Random Potion
                        case 'k' -> 5; // Random Enemy
                        case 'l' -> 6; // Random Chest
                        case 'B' -> 7; // Guaranteed Bomb
                        case 'b' -> 8; // Random Bomb
                        case 'T' -> 9; // Guaranteed Trap (random type)
                        case 't' -> 10; // Random Trap (random type)
                        case 'W' -> 11; // Guaranteed Breakable Wall
                        case 'w' -> 12; // Random Breakable Wall
                        case 'I' -> 13; // Guaranteed Pillar
                        case 'X' -> 14; // Guaranteed Exit
                        default -> 0; // Empty space
                    };
                    grid[i][j] = value;
                }
            }
        }
        return grid;
    }

    /**
     * Gets a room template by its name.
     *
     * @param theRoomName The name of the room template to retrieve.
     * @return The RoomTemplate object, or null if not found.
     */
    public RoomTemplate getRoomTemplate(final String theRoomName) {
        return myRoomTemplates.get(theRoomName);
    }
}