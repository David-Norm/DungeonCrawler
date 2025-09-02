package drippyspelunk.controller.database;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Character;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages SQLite database connections and character data operations.
 *
 * @author David Norman
 * @version 1.2
 */
public class DatabaseManager {

    /**
     * The URL for the SQLite database file.
     */
    private static final String DB_URL = "jdbc:sqlite:res/userdata/database/gamedata.db";

    /**
     * The singleton instance of the DatabaseManager.
     */
    private static DatabaseManager myInstance;

    /**
     * The active connection to the SQLite database.
     */
    private Connection myConnection;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private DatabaseManager() {
        try {
            // Create database directory if it doesn't exist
            java.io.File dbDir = new java.io.File("res/userdata/database");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            myConnection = DriverManager.getConnection(DB_URL);
            initializeDatabase();
        } catch (SQLException ignored) {
        }
    }

    /**
     * Gets the singleton instance of the DatabaseManager.
     *
     * @return The singleton DatabaseManager instance.
     */
    public static synchronized DatabaseManager getMyInstance() {
        if (myInstance == null) {
            myInstance = new DatabaseManager();
        }
        return myInstance;
    }

    /**
     * Initializes the database by creating the 'Characters' table if it doesn't exist and populating it with default data.
     *
     * @throws SQLException If a database access error occurs.
     */
    private void initializeDatabase() throws SQLException {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Characters (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    characterType TEXT NOT NULL CHECK (characterType IN ('PLAYER', 'ENEMY')),
                    characterClass TEXT NOT NULL,
                    baseHP INTEGER NOT NULL,
                    baseAttackSpeed INTEGER NOT NULL,
                    baseChanceToHit REAL NOT NULL,
                    baseMinDamage INTEGER NOT NULL,
                    baseMaxDamage INTEGER NOT NULL,
                    moveSpeed INTEGER NOT NULL,
                    visionStat INTEGER NOT NULL,
                    level INTEGER NOT NULL DEFAULT 1,
                    preferredBiome INTEGER DEFAULT 0
                )
                """;

        try (Statement stmt = myConnection.createStatement()) {
            stmt.execute(createTableSQL);
            populateDefaultData();
        }
    }

    /**
     * Populates the 'Characters' table with default player and enemy data if it's currently empty.
     *
     * @throws SQLException If a database access error occurs.
     */
    private void populateDefaultData() throws SQLException {
        // Check if data already exists
        String checkSQL = "SELECT COUNT(*) FROM Characters";
        try (Statement stmt = myConnection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSQL)) {

            if (rs.getInt(1) > 0) {
                return;
            }
        }

        String insertSQL = """
                INSERT INTO Characters (name, characterType, characterClass, baseHP, baseAttackSpeed, 
                                      baseChanceToHit, baseMinDamage, baseMaxDamage, moveSpeed, visionStat, level, preferredBiome) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = myConnection.prepareStatement(insertSQL)) {
            // Insert Enemy data with biome preferences
            // Biome 1: Basic enemies (Skeletons)
            insertCharacter(pstmt, "Skeleton Warrior", "ENEMY", "Skeleton", 60, 4, 0.65, 8, 15, 2, 5, 1, 1);

            // Biome 2: Goblins
            insertCharacter(pstmt, "Goblin Scout", "ENEMY", "Goblin", 40, 5, 0.75, 5, 12, 3, 7, 1, 2);

            // Biome 3: Orcs
            insertCharacter(pstmt, "Orc Brute", "ENEMY", "Orc", 120, 2, 0.60, 15, 25, 1, 4, 1, 3);

            // Biome 4: Deeper cave enemies (More Orcs)
            insertCharacter(pstmt, "Orc Berserker", "ENEMY", "Orc", 100, 3, 0.70, 12, 22, 2, 4, 1, 4);

            // Biome 5: End-game/Dragon lairs (Dragons)
            insertCharacter(pstmt, "Ancient Dragon", "ENEMY", "Dragon", 200, 1, 0.80, 15, 40, 1, 10, 5, 5);

            // Insert Player class templates (no biome preference)
            insertCharacter(pstmt, "Shadow Thief", "PLAYER", "Thief", 80, 8, 0.90, 15, 30, 4, 2, 1, 0);
            insertCharacter(pstmt, "Noble Hero", "PLAYER", "Hero", 150, 3, 0.75, 25, 40, 3, 1, 1, 0);
            insertCharacter(pstmt, "Battle Warrior", "PLAYER", "Warrior", 120, 1, 0.70, 35, 50, 3, 1, 1, 0);
            insertCharacter(pstmt, "Holy Priestess", "PLAYER", "Priestess", 100, 3, 0.80, 20, 30, 3, 3, 1, 0);
        }
    }

    /**
     * Inserts a character's data into the database.
     *
     * @param thePSTMT          The prepared statement to use for the insertion.
     * @param theName           The character's name.
     * @param theType           The character's type (PLAYER or ENEMY).
     * @param theCharClass      The character's class.
     * @param theHP             The character's base hit points.
     * @param theAttackSpeed    The character's base attack speed.
     * @param theHitChance      The character's base chance to hit.
     * @param theMinDmg         The character's base minimum damage.
     * @param theMaxDmg         The character's base maximum damage.
     * @param theMoveSpeed      The character's move speed.
     * @param theVision         The character's vision stat.
     * @param theLevel          The character's level.
     * @param thePreferredBiome The character's preferred biome.
     * @throws SQLException If a database access error occurs.
     */
    private void insertCharacter(PreparedStatement thePSTMT, String theName, String theType, String theCharClass,
                                 int theHP, int theAttackSpeed, double theHitChance, int theMinDmg, int theMaxDmg,
                                 int theMoveSpeed, int theVision, int theLevel, int thePreferredBiome) throws SQLException {
        thePSTMT.setString(1, theName);
        thePSTMT.setString(2, theType);
        thePSTMT.setString(3, theCharClass);
        thePSTMT.setInt(4, theHP);
        thePSTMT.setInt(5, theAttackSpeed);
        thePSTMT.setDouble(6, theHitChance);
        thePSTMT.setInt(7, theMinDmg);
        thePSTMT.setInt(8, theMaxDmg);
        thePSTMT.setInt(9, theMoveSpeed);
        thePSTMT.setInt(10, theVision);
        thePSTMT.setInt(11, theLevel);
        thePSTMT.setInt(12, thePreferredBiome);
        thePSTMT.executeUpdate();
    }

    /**
     * Gets a character from the database by their name.
     *
     * @param theName The name of the character to retrieve.
     * @return A Character object if found, otherwise null.
     */
    public Character getCharacterByName(String theName) {
        String sql = "SELECT * FROM Characters WHERE name = ?";
        try (PreparedStatement pstmt = myConnection.prepareStatement(sql)) {
            pstmt.setString(1, theName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return createCharacterFromResultSet(rs);
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * Gets a list of characters from the database based on their type.
     *
     * @param theType The CharacterType (PLAYER or ENEMY).
     * @return A list of Character objects.
     */
    public List<Character> getCharactersByType(Character.CharacterType theType) {
        List<Character> characters = new ArrayList<>();
        String sql = "SELECT * FROM Characters WHERE characterType = ?";

        try (PreparedStatement pstmt = myConnection.prepareStatement(sql)) {
            pstmt.setString(1, theType.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                characters.add(createCharacterFromResultSet(rs));
            }
        } catch (SQLException ignored) {
        }
        return characters;
    }

    /**
     * Gets a list of characters from the database based on their class.
     *
     * @param theCharacterClass The character's class name.
     * @return A list of Character objects.
     */
    public List<Character> getCharactersByClass(String theCharacterClass) {
        List<Character> characters = new ArrayList<>();
        String sql = "SELECT * FROM Characters WHERE characterClass = ?";

        try (PreparedStatement pstmt = myConnection.prepareStatement(sql)) {
            pstmt.setString(1, theCharacterClass);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                characters.add(createCharacterFromResultSet(rs));
            }
        } catch (SQLException ignored) {
        }
        return characters;
    }

    /**
     * Gets a random enemy from the database.
     *
     * @return A random Character object of type ENEMY, or null if none are found.
     */
    public Character getRandomEnemy() {
        String sql = "SELECT * FROM Characters WHERE characterType = 'ENEMY' ORDER BY RANDOM() LIMIT 1";
        try (Statement stmt = myConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return createCharacterFromResultSet(rs);
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * Gets a random enemy for the specified biome.
     * Biome 1: All enemies except dragons
     * Biome 2: Goblins
     * Biome 3: Skeletons
     * Biome 4: Orcs
     * Biome 5: Dragons
     *
     * @param theBiome The biome number.
     * @return A random Character object of type ENEMY, or null if an error occurs.
     */
    public Character getRandomEnemyForBiome(int theBiome) {
        return switch (theBiome) {
            case 1 -> getRandomNonDragonEnemy();
            case 2, 3, 4 -> getRandomEnemyByPreference(theBiome);
            case 5 -> getRandomDragonEnemy();
            default -> null;
        };
    }

    /**
     * Gets a random non-dragon enemy (for starting area and cross-biome spawning).
     *
     * @return A random Character object that is not a Dragon.
     */
    private Character getRandomNonDragonEnemy() {
        String sql = "SELECT * FROM Characters WHERE characterType = 'ENEMY' AND characterClass != 'Dragon' ORDER BY RANDOM() LIMIT 1";
        try (Statement stmt = myConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return createCharacterFromResultSet(rs);
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * Gets a random dragon enemy (for dragon territory only).
     *
     * @return A random Character object of class Dragon.
     */
    private Character getRandomDragonEnemy() {
        String sql = "SELECT * FROM Characters WHERE characterType = 'ENEMY' AND characterClass = 'Dragon' ORDER BY RANDOM() LIMIT 1";
        try (Statement stmt = myConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return createCharacterFromResultSet(rs);
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * Gets a random enemy by biome preference (for specific biome spawning).
     *
     * @param theBiome The biome number.
     * @return A random Character object matching the biome preference, or a non-dragon enemy as a fallback.
     */
    private Character getRandomEnemyByPreference(int theBiome) {
        String enemyClass = switch (theBiome) {
            case 2 -> "Goblin";
            case 3 -> "Skeleton";
            case 4 -> "Orc";
            default -> null;
        };

        if (enemyClass != null) {
            String sql = "SELECT * FROM Characters WHERE characterType = 'ENEMY' AND characterClass = ? ORDER BY RANDOM() LIMIT 1";
            try (PreparedStatement pstmt = myConnection.prepareStatement(sql)) {
                pstmt.setString(1, enemyClass);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    return createCharacterFromResultSet(rs);
                }
            } catch (SQLException ignored) {
            }
        }

        // Fallback to any non-dragon enemy
        return getRandomNonDragonEnemy();
    }

    /**
     * Creates a Character object from a ResultSet.
     *
     * @param theResultSet The ResultSet containing character data.
     * @return A new Character object populated with data from the ResultSet.
     * @throws SQLException If a database access error occurs.
     */
    private Character createCharacterFromResultSet(ResultSet theResultSet) throws SQLException {
        Character character = new Character();
        character.setId(theResultSet.getInt("id"));
        character.setMyName(theResultSet.getString("name"));
        character.setCharacterType(Character.CharacterType.valueOf(theResultSet.getString("characterType")));
        character.setCharacterClass(theResultSet.getString("characterClass"));
        character.setBaseHP(theResultSet.getInt("baseHP"));
        character.setBaseAttackSpeed(theResultSet.getInt("baseAttackSpeed"));
        character.setBaseChanceToHit(theResultSet.getDouble("baseChanceToHit"));
        character.setBaseMinDamage(theResultSet.getInt("baseMinDamage"));
        character.setBaseMaxDamage(theResultSet.getInt("baseMaxDamage"));
        character.setMoveSpeed(theResultSet.getInt("moveSpeed"));
        character.setVisionStat(theResultSet.getInt("visionStat"));
        character.setLevel(theResultSet.getInt("level"));

        // Handle the new preferredBiome column (may not exist in older databases)
        try {
            character.setPreferredBiome(theResultSet.getInt("preferredBiome"));
        } catch (SQLException e) {
            // Column doesn't exist in an older database, default to 0
            character.setPreferredBiome(0);
        }

        return character;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (myConnection != null && !myConnection.isClosed()) {
                myConnection.close();
            }
        } catch (SQLException ignored) {
        }
    }
}