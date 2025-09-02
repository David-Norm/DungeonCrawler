package drippyspelunk.model.dungeon.entity.dungeoncharacter;

/**
 * Base character data model representing database character stats.
 *
 * @author David Norman
 * @version 1.3
 */
public class Character {
    /**
     * The unique identifier for the character.
     */
    private int myID;
    /**
     * The name of the character.
     */
    private String myName;
    /**
     * The type of character (Player or Enemy).
     */
    private CharacterType myCharacterType;
    /**
     * The class of the character (e.g., Warrior, Rogue, etc.).
     */
    private String myCharacterClass;
    /**
     * The base health points of the character.
     */
    private int myBaseHP;
    /**
     * The base attack speed of the character.
     */
    private int myBaseAttackSpeed;
    /**
     * The base chance to hit a target.
     */
    private double myBaseChanceToHit;
    /**
     * The base minimum damage the character can deal.
     */
    private int myBaseMinDamage;
    /**
     * The base maximum damage the character can deal.
     */
    private int myBaseMaxDamage;
    /**
     * The movement speed of the character.
     */
    private int myMoveSpeed;
    /**
     * The vision range of the character.
     */
    private int myVisionStat;
    /**
     * The level of the character.
     */
    private int myLevel;
    /**
     * The preferred biome for the character (for enemies).
     */
    private int myPreferredBiome;

    /**
     * Default constructor.
     */
    public Character() {
    }

    /**
     * Constructs a Character object with all specified attributes.
     *
     * @param theName            The name of the character.
     * @param theCharacterType   The type of character.
     * @param theCharacterClass  The class of the character.
     * @param theBaseHP          The base health points.
     * @param theBaseAttackSpeed The base attack speed.
     * @param theBaseChanceToHit The base chance to hit.
     * @param theBaseMinDamage   The base minimum damage.
     * @param theBaseMaxDamage   The base maximum damage.
     * @param theMoveSpeed       The movement speed.
     * @param theVisionStat      The vision range.
     * @param theLevel           The level.
     */
    public Character(final String theName, final CharacterType theCharacterType, final String theCharacterClass,
                     final int theBaseHP, final int theBaseAttackSpeed, final double theBaseChanceToHit,
                     final int theBaseMinDamage, final int theBaseMaxDamage, final int theMoveSpeed,
                     final int theVisionStat, final int theLevel) {
        myName = theName;
        myCharacterType = theCharacterType;
        myCharacterClass = theCharacterClass;
        myBaseHP = theBaseHP;
        myBaseAttackSpeed = theBaseAttackSpeed;
        myBaseChanceToHit = theBaseChanceToHit;
        myBaseMinDamage = theBaseMinDamage;
        myBaseMaxDamage = theBaseMaxDamage;
        myMoveSpeed = theMoveSpeed;
        myVisionStat = theVisionStat;
        myLevel = theLevel;
        myPreferredBiome = 0; // Default to no biome preference
    }

    // Getters and Setters

    /**
     * Sets the unique identifier.
     *
     * @param theID The new ID.
     */
    public void setId(final int theID) {
        myID = theID;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getMyName() {
        return myName;
    }

    /**
     * Sets the name.
     *
     * @param theName The new name.
     */
    public void setMyName(final String theName) {
        myName = theName;
    }

    /**
     * Sets the character type.
     *
     * @param theCharacterType The new character type.
     */
    public void setCharacterType(final CharacterType theCharacterType) {
        myCharacterType = theCharacterType;
    }

    /**
     * Gets the character class.
     *
     * @return The character class.
     */
    public String getCharacterClass() {
        return myCharacterClass;
    }

    /**
     * Sets the character class.
     *
     * @param theCharacterClass The new character class.
     */
    public void setCharacterClass(final String theCharacterClass) {
        myCharacterClass = theCharacterClass;
    }

    /**
     * Gets the base health points.
     *
     * @return The base HP.
     */
    public int getBaseHP() {
        return myBaseHP;
    }

    /**
     * Sets the base health points.
     *
     * @param theBaseHP The new base HP.
     */
    public void setBaseHP(final int theBaseHP) {
        myBaseHP = theBaseHP;
    }

    /**
     * Gets the base attack speed.
     *
     * @return The base attack speed.
     */
    public int getBaseAttackSpeed() {
        return myBaseAttackSpeed;
    }

    /**
     * Sets the base attack speed.
     *
     * @param theBaseAttackSpeed The new base attack speed.
     */
    public void setBaseAttackSpeed(final int theBaseAttackSpeed) {
        myBaseAttackSpeed = theBaseAttackSpeed;
    }

    /**
     * Gets the base chance to hit.
     *
     * @return The base chance to hit.
     */
    public double getBaseChanceToHit() {
        return myBaseChanceToHit;
    }

    /**
     * Sets the base chance to hit.
     *
     * @param theBaseChanceToHit The new base chance to hit.
     */
    public void setBaseChanceToHit(final double theBaseChanceToHit) {
        myBaseChanceToHit = theBaseChanceToHit;
    }

    /**
     * Gets the base minimum damage.
     *
     * @return The base minimum damage.
     */
    public int getBaseMinDamage() {
        return myBaseMinDamage;
    }

    /**
     * Sets the base minimum damage.
     *
     * @param theBaseMinDamage The new base minimum damage.
     */
    public void setBaseMinDamage(final int theBaseMinDamage) {
        myBaseMinDamage = theBaseMinDamage;
    }

    /**
     * Gets the base maximum damage.
     *
     * @return The base maximum damage.
     */
    public int getBaseMaxDamage() {
        return myBaseMaxDamage;
    }

    /**
     * Sets the base maximum damage.
     *
     * @param theBaseMaxDamage The new base maximum damage.
     */
    public void setBaseMaxDamage(final int theBaseMaxDamage) {
        myBaseMaxDamage = theBaseMaxDamage;
    }

    /**
     * Gets the movement speed.
     *
     * @return The movement speed.
     */
    public int getMoveSpeed() {
        return myMoveSpeed;
    }

    /**
     * Sets the movement speed.
     *
     * @param theMoveSpeed The new movement speed.
     */
    public void setMoveSpeed(final int theMoveSpeed) {
        myMoveSpeed = theMoveSpeed;
    }

    /**
     * Gets the vision range.
     *
     * @return The vision range.
     */
    public int getVisionStat() {
        return myVisionStat;
    }

    /**
     * Sets the vision range.
     *
     * @param theVisionStat The new vision range.
     */
    public void setVisionStat(final int theVisionStat) {
        myVisionStat = theVisionStat;
    }

    /**
     * Gets the level.
     *
     * @return The level.
     */
    public int getLevel() {
        return myLevel;
    }

    /**
     * Sets the level.
     *
     * @param theLevel The new level.
     */
    public void setLevel(final int theLevel) {
        myLevel = theLevel;
    }

    /**
     * Sets the preferred biome.
     *
     * @param thePreferredBiome The new preferred biome.
     */
    public void setPreferredBiome(final int thePreferredBiome) {
        myPreferredBiome = thePreferredBiome;
    }

    @Override
    public String toString() {
        return String.format("Character{id=%d, name='%s', type=%s, class='%s', level=%d, biome=%d}",
                myID, myName, myCharacterType, myCharacterClass, myLevel, myPreferredBiome);
    }

    /**
     * An enumeration for the types of characters.
     */
    public enum CharacterType {
        PLAYER, ENEMY
    }
}