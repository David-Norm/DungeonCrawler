package drippyspelunk.model.dungeon.entity.dungeoncharacter;

import drippyspelunk.controller.database.DatabaseManager;
import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * Dungeon Player Object with database integration for character classes.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.11
 */
public class Player extends GameObject {

    /**
     * Direction constant for north.
     */
    public static final int NORTH = 0;
    /**
     * Direction constant for south.
     */
    public static final int SOUTH = 1;
    /**
     * Direction constant for east.
     */
    public static final int EAST = 2;
    /**
     * Direction constant for west.
     */
    public static final int WEST = 3;
    /**
     * The width of the player.
     */
    private static final int PLAYER_WIDTH = 32;
    /**
     * The height of the player.
     */
    private static final int PLAYER_HEIGHT = 32;
    /**
     * Flag for moving left.
     */
    private boolean myMovingLeft;
    /**
     * Flag for moving right.
     */
    private boolean myMovingRight;
    /**
     * Flag for moving up.
     */
    private boolean myMovingUp;
    /**
     * Flag for moving down.
     */
    private boolean myMovingDown;
    /**
     * The movement speed of the player.
     */
    private int mySpeed;
    /**
     * The vision range of the player.
     */
    private int myVision;
    /**
     * The current state of the player.
     */
    private PlayerState myCurrentState;
    /**
     * The last direction the player was facing.
     */
    private int myLastDirection;
    /**
     * The current health of the player.
     */
    private int myHealth;

    // Combat-related fields (now can be based on database stats)
    /**
     * The maximum health of the player.
     */
    private int myMaxHealth;
    /**
     * The database character template.
     */
    private Character myCharacterData; // Database character template (not final)
    /**
     * The current level of the player.
     */
    private int myLevel;
    /**
     * The current experience points of the player.
     */
    private int myExperience;
    /**
     * The character class of the player.
     */
    private String myCharacterClass;
    /**
     * The base movement speed of the player.
     */
    private int myBaseMoveSpeed;
    /**
     * Flag indicating if a speed boost is active.
     */
    private boolean mySpeedBoostActive;
    /**
     * The duration of the speed boost.
     */
    private int mySpeedBoostTickCounter;
    /**
     * Flag indicating if the player is slowed.
     */
    private boolean mySlowed;
    /**
     * The duration of the slow effect.
     */
    private int mySlowTickCounter;
    /**
     * Flag indicating if the player is poisoned.
     */
    private boolean myPoisoned;
    /**
     * The duration of the poison effect.
     */
    private int myPoisonedTickCounter;
    /**
     * The base vision range of the player.
     */
    private int myBaseVision;
    /**
     * Flag indicating if a vision boost is active.
     */
    private boolean myVisionBoostActive;
    /**
     * The duration of the vision boost.
     */
    private int myVisionBoostTickCounter;
    /**
     * Flag indicating if the player is blinded.
     */
    private boolean myBlinded;
    /**
     * The duration of the blind effect.
     */
    private int myBlindTickCounter;

    /**
     * Default constructor - creates a basic player.
     *
     * @param theX The starting x-coordinate.
     * @param theY The starting y-coordinate.
     */
    public Player(final int theX, final int theY) {
        super(theX, theY, 32, 32);
        initializeMovement();
        initializeDefaultStats();
        myCurrentState = PlayerState.STANDING_SOUTH; // Default state
    }

    /**
     * Constructor with character class selection from database.
     *
     * @param theX                  The starting x-coordinate.
     * @param theY                  The starting y-coordinate.
     * @param theCharacterClassName The name of the character class to load.
     */
    public Player(final int theX, final int theY, final String theCharacterClassName) {
        super(theX, theY, 32, 32);
        initializeMovement();
        myCurrentState = PlayerState.STANDING_SOUTH;

        // Load character data from database if specified
        if (theCharacterClassName != null) {
            final DatabaseManager dbManager = DatabaseManager.getMyInstance();
            myCharacterData = dbManager.getCharacterByName(theCharacterClassName);

            if (myCharacterData != null) {
                initializeFromDatabase();
            } else {
                initializeDefaultStats();
            }
        } else {
            initializeDefaultStats();
        }
    }

    /**
     * Factory method to create a player with a specified character class.
     *
     * @param theX         The starting x-coordinate.
     * @param theY         The starting y-coordinate.
     * @param theClassName The name of the character class.
     * @return A new Player instance.
     */
    public static Player createWithClass(final int theX, final int theY, final String theClassName) {
        return new Player(theX, theY, theClassName);
    }

    /**
     * Factory method to create a player from a Character data object.
     *
     * @param theX             The starting x-coordinate.
     * @param theY             The starting y-coordinate.
     * @param theCharacterData The Character data object.
     * @return A new Player instance.
     */
    public static Player createFromCharacterData(final int theX, final int theY, final Character theCharacterData) {
        final Player player = new Player(theX, theY);
        if (theCharacterData != null) {
            player.myCharacterData = theCharacterData;
            player.initializeFromDatabase();
        }
        return player;
    }

    /**
     * Initializes movement flags and last direction.
     */
    private void initializeMovement() {
        myMovingLeft = false;
        myMovingRight = false;
        myMovingUp = false;
        myMovingDown = false;
        myLastDirection = SOUTH;
    }

    /**
     * Initializes player stats from database character data.
     */
    private void initializeFromDatabase() {
        myMaxHealth = myCharacterData.getBaseHP();
        myHealth = myMaxHealth;
        mySpeed = myCharacterData.getMoveSpeed();
        myBaseMoveSpeed = mySpeed;
        myVision = myCharacterData.getVisionStat();
        myBaseVision = myVision;
        myLevel = myCharacterData.getLevel();
        myCharacterClass = myCharacterData.getCharacterClass();
        myExperience = 0;
    }

    /**
     * Initializes player stats with default values.
     */
    private void initializeDefaultStats() {
        myCharacterData = null;
        myMaxHealth = 100;
        myHealth = myMaxHealth;
        mySpeed = 3;
        myBaseMoveSpeed = mySpeed;
        myVision = 6;
        myBaseVision = myVision;
        myLevel = 1;
        myCharacterClass = "Adventurer";
        myExperience = 0;
    }

    @Override
    public int getWidth() {
        return PLAYER_WIDTH;
    }

    @Override
    public int getHeight() {
        return PLAYER_HEIGHT;
    }

    @Override
    public void update() {
        // This method is now empty as movement logic is handled externally.
    }

    /**
     * Returns true if the player is moving left.
     *
     * @return A boolean indicating left movement.
     */
    public boolean isMovingLeft() {
        return myMovingLeft;
    }

    /**
     * Sets the moving left flag.
     *
     * @param theMovingLeft The new state of the flag.
     */
    public void setMovingLeft(final boolean theMovingLeft) {
        myMovingLeft = theMovingLeft;
    }

    /**
     * Returns true if the player is moving right.
     *
     * @return A boolean indicating right movement.
     */
    public boolean isMovingRight() {
        return myMovingRight;
    }

    /**
     * Sets the moving right flag.
     *
     * @param theMovingRight The new state of the flag.
     */
    public void setMovingRight(final boolean theMovingRight) {
        myMovingRight = theMovingRight;
    }

    /**
     * Returns true if the player is moving up.
     *
     * @return A boolean indicating up movement.
     */
    public boolean isMovingUp() {
        return myMovingUp;
    }

    /**
     * Sets the moving up flag.
     *
     * @param theMovingUp The new state of the flag.
     */
    public void setMovingUp(final boolean theMovingUp) {
        myMovingUp = theMovingUp;
    }

    /**
     * Returns true if the player is moving down.
     *
     * @return A boolean indicating down movement.
     */
    public boolean isMovingDown() {
        return myMovingDown;
    }

    /**
     * Sets the moving down flag.
     *
     * @param theMovingDown The new state of the flag.
     */
    public void setMovingDown(final boolean theMovingDown) {
        myMovingDown = theMovingDown;
    }

    /**
     * Returns true if the player is moving in any direction.
     *
     * @return A boolean indicating any movement.
     */
    public boolean isMoving() {
        return myMovingLeft || myMovingRight || myMovingUp || myMovingDown;
    }

    /**
     * Gets the current state of the player.
     *
     * @return The current player state.
     */
    public PlayerState getCurrentState() {
        return myCurrentState;
    }

    /**
     * Sets the current state of the player.
     *
     * @param theState The new player state.
     */
    public void setCurrentState(final PlayerState theState) {
        myCurrentState = theState;
    }

    /**
     * Gets the last direction the player was facing.
     *
     * @return The last direction constant.
     */
    public int getLastDirection() {
        return myLastDirection;
    }

    /**
     * Sets the last direction the player was facing.
     *
     * @param theDirection The last direction constant.
     */
    public void setLastDirection(final int theDirection) {
        myLastDirection = theDirection;
    }

    /**
     * Gets the current health of the player.
     *
     * @return The current health.
     */
    public int getHealth() {
        return myHealth;
    }

    /**
     * Sets the health of the player, clamped between 0 and max health.
     *
     * @param theHealth The new health value.
     */
    public void setHealth(final int theHealth) {
        myHealth = Math.max(0, Math.min(theHealth, myMaxHealth));
    }

    /**
     * Gets the maximum health of the player.
     *
     * @return The maximum health.
     */
    public int getMaxHealth() {
        return myMaxHealth;
    }

    /**
     * Decreases the player's health by a specified amount.
     *
     * @param theDamage The amount of damage to take.
     */
    public void takeDamage(final int theDamage) {
        setHealth(myHealth - theDamage);
    }

    /**
     * Increases the player's health by a specified amount.
     *
     * @param theHealAmount The amount of health to heal.
     */
    public void heal(final int theHealAmount) {
        setHealth(myHealth + theHealAmount);
    }

    // New database-driven methods

    /**
     * Checks if the player is alive.
     *
     * @return true if health is greater than 0, false otherwise.
     */
    public boolean isAlive() {
        return myHealth > 0;
    }

    /**
     * Gets the player's character class.
     *
     * @return The character class name.
     */
    public String getCharacterClass() {
        return myCharacterClass;
    }

    /**
     * Gets the player's current level.
     *
     * @return The current level.
     */
    public int getLevel() {
        return myLevel;
    }

    /**
     * Gets the player's current experience points.
     *
     * @return The current experience.
     */
    public int getExperience() {
        return myExperience;
    }

    /**
     * Adds experience to the player and checks for a level up.
     *
     * @param theExperience The amount of experience to add.
     */
    public void addExperience(final int theExperience) {
        myExperience += theExperience;
        checkLevelUp();
    }

    /**
     * Checks if the player has enough experience to level up.
     */
    private void checkLevelUp() {
        final int experienceNeeded = myLevel * 100; // Simple formula: level * 100 XP needed
        if (myExperience >= experienceNeeded) {
            myLevel++;
            myExperience -= experienceNeeded;

            // Level up bonuses (simple progression)
            final int healthIncrease = 10 + (myLevel * 2);
            myMaxHealth += healthIncrease;
            myHealth = myMaxHealth; // Full heal on level up
        }
    }

    /**
     * Gets the base minimum damage for the player's class.
     *
     * @return The base minimum damage.
     */
    public int getBaseMinDamage() {
        return myCharacterData != null ? myCharacterData.getBaseMinDamage() : 5;
    }

    /**
     * Gets the base maximum damage for the player's class.
     *
     * @return The base maximum damage.
     */
    public int getBaseMaxDamage() {
        return myCharacterData != null ? myCharacterData.getBaseMaxDamage() : 10;
    }

    /**
     * Gets the base hit chance for the player's class.
     *
     * @return The base hit chance.
     */
    public double getBaseHitChance() {
        return myCharacterData != null ? myCharacterData.getBaseChanceToHit() : 0.75;
    }

    /**
     * Gets the base attack speed for the player's class.
     *
     * @return The base attack speed.
     */
    public int getAttackSpeed() {
        return myCharacterData != null ? myCharacterData.getBaseAttackSpeed() : 4;
    }

    /**
     * Gets the player's current movement speed.
     *
     * @return The current movement speed.
     */
    public int getMoveSpeed() {
        return mySpeed;
    }

    /**
     * Sets the movement speed of the player.
     *
     * @param theSpeed The new movement speed.
     */
    public void setMoveSpeed(final int theSpeed) {
        mySpeed = theSpeed;
    }

    /**
     * Applies a speed boost to the player.
     *
     * @param theActive     Flag to activate/deactivate the boost.
     * @param theMultiplier The speed multiplier.
     * @param theDuration   The duration of the boost in ticks.
     */
    public void setSpeedBoost(final boolean theActive, final double theMultiplier, final int theDuration) {
        if (theActive && !mySpeedBoostActive) {
            myBaseMoveSpeed = getMoveSpeed();
            setMoveSpeed((int) (myBaseMoveSpeed * theMultiplier));
        } else if (!theActive) {
            setMoveSpeed(myBaseMoveSpeed);
        }
        mySpeedBoostActive = theActive;
        mySpeedBoostTickCounter = theDuration;
    }

    /**
     * Decrements the speed boost tick counter and deactivates if it reaches zero.
     */
    public void tickSpeedBoost() {
        if (mySpeedBoostActive && mySpeedBoostTickCounter > 0) {
            mySpeedBoostTickCounter--;
            if (mySpeedBoostTickCounter == 0) {
                setSpeedBoost(false, 0, 0); // Deactivate boost and reset speed
            }
        }
    }

    /**
     * Checks if a speed boost is active.
     *
     * @return true if a speed boost is active, false otherwise.
     */
    public boolean isSpeedBoostActive() {
        return mySpeedBoostActive;
    }

    /**
     * Applies a slow effect to the player.
     *
     * @param theSlowed   Flag to activate/deactivate the slow effect.
     * @param theDuration The duration of the slow effect in ticks.
     */
    public void setSlowed(final boolean theSlowed, final int theDuration) {
        if (theSlowed && !mySlowed) {
            myBaseMoveSpeed = getMoveSpeed();
            setMoveSpeed((int) (myBaseMoveSpeed * 0.5));
        } else if (!theSlowed) {
            setMoveSpeed(myBaseMoveSpeed);
        }
        mySlowed = theSlowed;
        mySlowTickCounter = theDuration;
    }

    /**
     * Decrements the slow tick counter and deactivates if it reaches zero.
     */
    public void tickSlow() {
        if (mySlowed && mySlowTickCounter > 0) {
            mySlowTickCounter--;
            if (mySlowTickCounter == 0) {
                setSlowed(false, 0);
            }
        }
    }

    /**
     * Checks if the player is slowed.
     *
     * @return true if slowed, false otherwise.
     */
    public boolean isSlowed() {
        return mySlowed;
    }

    /**
     * Sets the poisoned status and duration.
     *
     * @param thePoisoned Flag to activate/deactivate the poisoned status.
     * @param theDuration The duration of the poison effect.
     */
    public void setPoisoned(final boolean thePoisoned, final int theDuration) {
        myPoisoned = thePoisoned;
        myPoisonedTickCounter = theDuration;
    }

    /**
     * Decrements the poisoned tick counter and applies damage.
     */
    public void tickPoisoned() {
        if (myPoisoned && myPoisonedTickCounter > 0) {
            myPoisonedTickCounter--;
            if (myPoisonedTickCounter % 100 >= 90) {
                takeDamage(1);
            }
            if (myPoisonedTickCounter == 0) {
                myPoisoned = false;
            }
        }
    }

    /**
     * Checks if the player is poisoned.
     *
     * @return true if poisoned, false otherwise.
     */
    public boolean isPoisoned() {
        return myPoisoned;
    }

    /**
     * Gets the player's current vision range.
     *
     * @return The current vision range.
     */
    public int getVisionRange() {
        return myVision;
    }

    /**
     * Sets the player's vision range.
     *
     * @param theVision The new vision range.
     */
    public void setVisionRange(final int theVision) {
        myVision = theVision;
    }

    /**
     * Applies a vision boost to the player.
     *
     * @param theActive     Flag to activate/deactivate the boost.
     * @param theMultiplier The vision multiplier.
     * @param theDuration   The duration of the boost in ticks.
     */
    public void setVisionBoost(final boolean theActive, final double theMultiplier, final int theDuration) {
        if (theActive && !myVisionBoostActive) {
            myBaseVision = getVisionRange();
            setVisionRange((int) (myBaseVision * theMultiplier));
        } else if (!theActive) {
            setVisionRange(myBaseVision);
        }
        myVisionBoostActive = theActive;
        myVisionBoostTickCounter = theDuration;
    }

    /**
     * Decrements the vision boost tick counter and deactivates if it reaches zero.
     */
    public void tickVisionBoost() {
        if (myVisionBoostActive && myVisionBoostTickCounter > 0) {
            myVisionBoostTickCounter--;
            if (myVisionBoostTickCounter == 0) {
                setVisionBoost(false, 0, 0);
            }
        }
    }

    /**
     * Checks if a vision boost is active.
     *
     * @return true if a vision boost is active, false otherwise.
     */
    public boolean isVisionBoostActive() {
        return myVisionBoostActive;
    }

    /**
     * Applies a blind effect to the player.
     *
     * @param theBlinded  Flag to activate/deactivate the effect.
     * @param theDuration The duration of the effect in ticks.
     */
    public void setBlinded(final boolean theBlinded, final int theDuration) {
        if (theBlinded && !myBlinded) {
            myBaseVision = getVisionRange();
            setVisionRange((int) (myBaseVision * 0.5));
        } else if (!theBlinded) {
            setVisionRange(myBaseVision);
        }
        myBlinded = theBlinded;
        myBlindTickCounter = theDuration;
    }

    /**
     * Decrements the blind tick counter and deactivates if it reaches zero.
     */
    public void tickBlind() {
        if (myBlinded && myBlindTickCounter > 0) {
            myBlindTickCounter--;
            if (myBlindTickCounter == 0) {
                setBlinded(false, 0);
            }
        }
    }

    /**
     * Checks if the player is blinded.
     *
     * @return true if blinded, false otherwise.
     */
    public boolean isBlinded() {
        return myBlinded;
    }

    @Override
    public String toString() {
        return String.format("Player{class='%s', level=%d, hp=%d/%d, xp=%d}",
                myCharacterClass, myLevel, myHealth, myMaxHealth, myExperience);
    }

    /**
     * Enum for the player's states.
     */
    public enum PlayerState {
        STANDING_NORTH,
        STANDING_EAST,
        STANDING_SOUTH,
        STANDING_WEST,
        WALKING_NORTH,
        WALKING_EAST,
        WALKING_SOUTH,
        WALKING_WEST,
        IDLE_1,
        IDLE_2
    }
}