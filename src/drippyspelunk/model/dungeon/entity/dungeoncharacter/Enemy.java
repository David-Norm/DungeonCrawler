package drippyspelunk.model.dungeon.entity.dungeoncharacter;

import drippyspelunk.controller.database.DatabaseManager;
import drippyspelunk.model.dungeon.entity.GameObject;

/**
 * Dungeon Enemies object with database integration.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @author Mark Malyshev
 * @version 1.6
 */
public class Enemy extends GameObject {

    /**
     * The maximum number of turns for which poison can affect the enemy.
     */
    private static final int MAX_TURNS_TO_TAKE_POISON = 5;
    /**
     * The name of the enemy.
     */
    private final String myName;
    /**
     * The movement speed of the enemy.
     */
    private final int mySpeed;
    /**
     * The database character template.
     */
    private final Character myCharacterData; // Database character template

    // Combat-related fields (now based on database stats)
    /**
     * The maximum health of the enemy.
     */
    private final int myMaxHealth;
    /**
     * The minimum damage the enemy can deal.
     */
    private final int myMinDamage;
    /**
     * The maximum damage the enemy can deal.
     */
    private final int myMaxDamage;
    /**
     * The chance for the enemy to hit a target.
     */
    private final double myHitChance;
    /**
     * The current health of the enemy.
     */
    private int myHealth;
    /**
     * A flag indicating if the enemy is poisoned.
     */
    private boolean myIsPoisoned;
    /**
     * A counter for the number of turns the enemy has been poisoned.
     */
    private int myPoisonTurnCounter;
    /**
     * The current state of the enemy.
     */
    private EnemyState myCurrentState;

    /**
     * Constructor using character name from a database.
     *
     * @param theX      The x-coordinate of the enemy.
     * @param theY      The y-coordinate of the enemy.
     * @param theWidth  The width of the enemy.
     * @param theHeight The height of the enemy.
     * @param theName   The name of the enemy.
     */
    public Enemy(final int theX, final int theY, final int theWidth, final int theHeight, final String theName) {
        super(theX, theY, theWidth, theHeight);

        myName = theName;
        myIsPoisoned = false;
        myPoisonTurnCounter = 0;

        // Load character data from database
        final DatabaseManager dbManager = DatabaseManager.getMyInstance();
        myCharacterData = dbManager.getCharacterByName(theName);

        if (myCharacterData != null) {
            // Initialize stats from a database
            myMaxHealth = myCharacterData.getBaseHP();
            myHealth = myMaxHealth;
            mySpeed = myCharacterData.getMoveSpeed();
            myMinDamage = myCharacterData.getBaseMinDamage();
            myMaxDamage = myCharacterData.getBaseMaxDamage();
            myHitChance = myCharacterData.getBaseChanceToHit();
        } else {
            // Fallback to default values if character not found in a database
            myMaxHealth = 100;
            myHealth = myMaxHealth;
            mySpeed = 2;
            myMinDamage = 10;
            myMaxDamage = 20;
            myHitChance = 0.7;
        }
        myCurrentState = EnemyState.STANDING_SOUTH;
    }

    /**
     * Constructor for creating an enemy from a database Character object.
     *
     * @param theX             The x-coordinate of the enemy.
     * @param theY             The y-coordinate of the enemy.
     * @param theWidth         The width of the enemy.
     * @param theHeight        The height of the enemy.
     * @param theCharacterData The Character object containing enemy data.
     */
    public Enemy(final int theX, final int theY, final int theWidth, final int theHeight, final Character theCharacterData) {
        super(theX, theY, theWidth, theHeight);

        myCharacterData = theCharacterData;
        myName = theCharacterData.getMyName();
        myMaxHealth = theCharacterData.getBaseHP();
        myHealth = myMaxHealth;
        mySpeed = theCharacterData.getMoveSpeed();
        myMinDamage = theCharacterData.getBaseMinDamage();
        myMaxDamage = theCharacterData.getBaseMaxDamage();
        myHitChance = theCharacterData.getBaseChanceToHit();

        myCurrentState = EnemyState.STANDING_SOUTH;
    }

    /**
     * Gets the name of the enemy.
     *
     * @return The enemy's name.
     */
    public String getName() {
        return myName;
    }

    /**
     * Gets the character class of the enemy.
     *
     * @return The character class name.
     */
    public String getCharacterClass() {
        return myCharacterData != null ? myCharacterData.getCharacterClass().toLowerCase() : "unknown";
    }

    /**
     * Gets the level of the enemy.
     *
     * @return The enemy's level.
     */
    public int getLevel() {
        return myCharacterData != null ? myCharacterData.getLevel() : 1;
    }

    /**
     * Gets the current health of the enemy.
     *
     * @return The current health.
     */
    public int getHealth() {
        return myHealth;
    }

    // Combat-related methods

    /**
     * Sets the enemy's health, clamped between 0 and max health.
     *
     * @param theHealth The new health value.
     */
    public void setHealth(final int theHealth) {
        myHealth = Math.max(0, Math.min(theHealth, myMaxHealth));
    }

    /**
     * Gets the maximum health of the enemy.
     *
     * @return The maximum health.
     */
    public int getMaxHealth() {
        return myMaxHealth;
    }

    /**
     * Decreases the enemy's health by a specified amount.
     *
     * @param theDamage The amount of damage to take.
     */
    public void takeDamage(final int theDamage) {
        setHealth(myHealth - theDamage);
    }

    /**
     * Increases the enemy's health by a specified amount.
     *
     * @param theHealAmount The amount of health to heal.
     */
    public void heal(final int theHealAmount) {
        setHealth(myHealth + theHealAmount);
    }

    /**
     * Checks if the enemy is alive.
     *
     * @return true if health is greater than 0, false otherwise.
     */
    public boolean isAlive() {
        return myHealth > 0;
    }

    /**
     * Gets the minimum damage the enemy can deal.
     *
     * @return The minimum damage.
     */
    public int getMinDamage() {
        return myMinDamage;
    }

    // New methods for database-driven combat

    /**
     * Gets the maximum damage the enemy can deal.
     *
     * @return The maximum damage.
     */
    public int getMaxDamage() {
        return myMaxDamage;
    }

    /**
     * Gets the enemy's hit chance.
     *
     * @return The hit chance.
     */
    public double getHitChance() {
        return myHitChance;
    }

    /**
     * Gets the enemy's attack speed.
     *
     * @return The attack speed.
     */
    public int getAttackSpeed() {
        return myCharacterData != null ? myCharacterData.getBaseAttackSpeed() : 3;
    }

    /**
     * Gets the enemy's movement speed.
     *
     * @return The movement speed.
     */
    public int getSpeed() {
        return mySpeed;
    }

    @Override
    public void update() {
        // This method is now empty as movement logic is handled externally.
    }

    /**
     * Gets the current state of the enemy.
     *
     * @return The current state.
     */
    public EnemyState getCurrentState() {
        return myCurrentState;
    }

    /**
     * Sets the current state of the enemy.
     *
     * @param theState The new state.
     */
    public void setCurrentState(final EnemyState theState) {
        myCurrentState = theState;
    }

    /**
     * Checks if the enemy is poisoned and updates the poison counter.
     *
     * @return true if poisoned, false otherwise.
     */
    public boolean isPoisoned() {
        myPoisonTurnCounter++;
        if (myPoisonTurnCounter > MAX_TURNS_TO_TAKE_POISON) {
            setPoisoned(false);
            myPoisonTurnCounter = 0;
        }
        return myIsPoisoned;
    }

    /**
     * Sets the poisoned status of the enemy.
     *
     * @param poison true to poison, false to remove poison.
     */
    public void setPoisoned(final boolean poison) {
        myIsPoisoned = poison;
    }

    @Override
    public String toString() {
        return String.format("Enemy{name='%s', class='%s', level=%d, hp=%d/%d}",
                myName, getCharacterClass(), getLevel(), myHealth, myMaxHealth);
    }

    /**
     * An enumeration for the enemy's states.
     */
    public enum EnemyState {
        STANDING_NORTH,
        STANDING_EAST,
        STANDING_SOUTH,
        STANDING_WEST,
        WALKING_NORTH,
        WALKING_EAST,
        WALKING_SOUTH,
        WALKING_WEST
    }
}