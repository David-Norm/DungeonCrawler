package drippyspelunk.model.dungeon;

import drippyspelunk.model.dungeon.DungeonCrawlerLogic.AttackType;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Enemy;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

import java.util.Random;

/**
 * Manages combat mechanics including damage calculation, hit chances, and blocking.
 * Now uses database-driven enemy stats for more dynamic combat.
 *
 * @author David Norman
 * @author Devin Arroyo
 * @version 1.6
 */
public class CombatManager {

    /**
     * A random number generator.
     */
    private static final Random myRandom = new Random();
    /**
     * The initial chance to successfully block an attack.
     */
    private static final int INITIAL_BLOCK_CHANCE = 90;
    /**
     * The minimum chance to block an attack.
     */
    private static final int MIN_BLOCK_CHANCE = 30;
    /**
     * The reduction in block chance with each successful block.
     */
    private static final int BLOCK_CHANCE_REDUCTION = 20;
    /**
     * The number of times a block has been used in the current combat.
     */
    private int myBlockUseCount = 0;

    /**
     * Performs a player attack on an enemy.
     *
     * @param theAttackType The type of attack to perform.
     * @param theTarget     The enemy being attacked.
     * @param thePlayer     The player performing the attack.
     * @return The result of the attack.
     */
    public CombatResult performPlayerAttack(final AttackType theAttackType, final Enemy theTarget, final Player thePlayer) {
        // Handle null parameters
        if (theAttackType == null || theTarget == null || thePlayer == null) {
            return new CombatResult(false, 0, "Invalid attack parameters");
        }

        int hitChance;
        int minDamage;
        int maxDamage;

        if (theAttackType == AttackType.CLASS_ATTACK) {
            // Use player's database stats for class attack
            hitChance = (int) (thePlayer.getBaseHitChance() * 100);
            minDamage = thePlayer.getBaseMinDamage();
            maxDamage = thePlayer.getBaseMaxDamage();
        } else {
            // Use attack type's predefined stats
            hitChance = myRandom.nextInt(theAttackType.getMaxHitChance() - theAttackType.getMyMinHitChance() + 1)
                    + theAttackType.getMyMinHitChance();
            minDamage = theAttackType.getMyMinDamage();
            maxDamage = theAttackType.getMyMaxDamage();
        }

        final boolean hits = myRandom.nextInt(100) < hitChance;

        if (hits) {
            // Calculate damage
            final int damage = myRandom.nextInt(maxDamage - minDamage + 1) + minDamage;

            // Apply damage
            theTarget.takeDamage(damage);

            final String attackName = theAttackType == AttackType.CLASS_ATTACK ?
                    thePlayer.getCharacterClass() + " Attack" : theAttackType.getMyName();

            String message = String.format("%s hits %s for %d damage!",
                    attackName, theTarget.getName(), damage);

            if (!theTarget.isAlive()) {
                message += " " + theTarget.getName() + " is defeated!";
            }

            return new CombatResult(true, damage, message);
        } else {
            final String attackName = theAttackType == AttackType.CLASS_ATTACK ?
                    thePlayer.getCharacterClass() + " Attack" : theAttackType.getMyName();
            final String message = String.format("%s missed!", attackName);

            // Create special result for class attack misses to trigger popup
            if (theAttackType == AttackType.CLASS_ATTACK) {
                return new CombatResult(false, 0, message, true); // true indicates class attack miss
            }

            return new CombatResult(false, 0, message);
        }
    }

    /**
     * Performs an enemy attack on the player using database-driven stats.
     *
     * @param theAttacker The enemy performing the attack.
     * @param theTarget   The player being attacked.
     * @return The result of the attack.
     */
    public CombatResult performEnemyAttack(final Enemy theAttacker, final Player theTarget) {
        // Handle null parameters
        if (theAttacker == null || theTarget == null) {
            return new CombatResult(false, 0, "Invalid attack parameters");
        }

        // Use enemy's database stats for hit chance (convert to percentage)
        final int enemyHitChance = (int) (theAttacker.getHitChance() * 100);
        final boolean hits = myRandom.nextInt(100) < enemyHitChance;

        if (hits) {
            // Use enemy's database damage range
            final int minDamage = theAttacker.getMinDamage();
            final int maxDamage = theAttacker.getMaxDamage();
            final int damage = myRandom.nextInt(maxDamage - minDamage + 1) + minDamage;

            theTarget.takeDamage(damage);

            String message = String.format("%s attacks for %d damage!",
                    theAttacker.getName(), damage);

            if (!theTarget.isAlive()) {
                message += " You have been defeated!";
            }

            return new CombatResult(true, damage, message);
        } else {
            return new CombatResult(false, 0, theAttacker.getName() + " missed!");
        }
    }

    /**
     * Attempts to block an incoming attack.
     *
     * @return True if the block is successful, false otherwise.
     */
    public boolean attemptBlock() {
        final int blockChance = calculateCurrentBlockChance();
        final boolean blocked = myRandom.nextInt(100) < blockChance;

        if (blocked) {
            myBlockUseCount++;
        }

        return blocked;
    }

    /**
     * Performs a blocked enemy attack (reduced damage).
     *
     * @param theAttacker The enemy performing the attack.
     * @param theTarget   The player being attacked.
     * @return The result of the blocked attack.
     */
    public CombatResult performBlockedEnemyAttack(final Enemy theAttacker, final Player theTarget) {
        // Handle null parameters
        if (theAttacker == null || theTarget == null) {
            return new CombatResult(false, 0, "Invalid attack parameters");
        }

        // Enemy still needs to hit even when blocked
        final int enemyHitChance = (int) (theAttacker.getHitChance() * 100);
        final boolean hits = myRandom.nextInt(100) < enemyHitChance;

        if (hits) {
            // Blocked attacks do reduced damage (25% of normal)
            final int minDamage = Math.max(1, theAttacker.getMinDamage() / 4);
            final int maxDamage = Math.max(2, theAttacker.getMaxDamage() / 4);
            final int damage = myRandom.nextInt(maxDamage - minDamage + 1) + minDamage;

            theTarget.takeDamage(damage);

            final String message = String.format("You blocked %s's attack! Only took %d damage.",
                    theAttacker.getName(), damage);

            return new CombatResult(true, damage, message);
        } else {
            return new CombatResult(false, 0, "You blocked " + theAttacker.getName() + "'s missed attack!");
        }
    }

    /**
     * Calculates experience points gained from defeating an enemy.
     *
     * @param theDefeatedEnemy The defeated enemy.
     * @return The amount of experience gained.
     */
    public int calculateExperienceGain(final Enemy theDefeatedEnemy) {
        // Handle null enemy
        if (theDefeatedEnemy == null) {
            return 0;
        }

        // Base XP based on enemy level and max health
        final int baseXP = theDefeatedEnemy.getLevel() * 10;
        final int healthBonus = theDefeatedEnemy.getMaxHealth() / 10;
        return baseXP + healthBonus;
    }

    /**
     * Gets the current block chance percentage.
     *
     * @return The current block chance.
     */
    public int getCurrentBlockChance() {
        return calculateCurrentBlockChance();
    }

    /**
     * Calculates the current block chance based on use count.
     *
     * @return The calculated block chance.
     */
    private int calculateCurrentBlockChance() {
        if (myBlockUseCount == 0) {
            return INITIAL_BLOCK_CHANCE;
        }

        final int currentChance = INITIAL_BLOCK_CHANCE - (myBlockUseCount * BLOCK_CHANCE_REDUCTION);
        return Math.max(currentChance, MIN_BLOCK_CHANCE);
    }

    /**
     * Resets combat state for a new battle.
     */
    public void resetCombat() {
        myBlockUseCount = 0;
    }

    /**
     * Uses a health potion on the player.
     *
     * @param thePlayer The player to heal.
     * @return The result of using the potion.
     */
    public CombatResult useHealthPotion(final Player thePlayer) {
        // Handle null player
        if (thePlayer == null) {
            return new CombatResult(false, 0, "Invalid player");
        }

        final int healAmount = 30; // Standard heal amount
        final int oldHealth = thePlayer.getHealth();
        thePlayer.heal(healAmount);
        final int actualHeal = thePlayer.getHealth() - oldHealth;

        final String message = String.format("Used Health Potion! Healed %d HP.", actualHeal);
        return new CombatResult(true, actualHeal, message);
    }

    /**
     * Uses a poison potion on an enemy.
     *
     * @param theEnemy The enemy to poison.
     * @return The result of using the potion.
     */
    public CombatResult usePoisonPotion(final Enemy theEnemy) {
        // Handle null enemy
        if (theEnemy == null) {
            return new CombatResult(false, 0, "Invalid enemy");
        }

        theEnemy.setPoisoned(true);
        final int harmAmount = 10; // Standard Harm amount per turn
        final String message = String.format("Used Poison potion! Enemy will take %d HP every turn.", harmAmount);
        return new CombatResult(true, harmAmount, message);
    }

    /**
     * Uses a bomb on an enemy.
     *
     * @param theEnemy The enemy to attack with the bomb.
     * @return The result of using the bomb.
     */
    public CombatResult useBomb(final Enemy theEnemy) {
        // Handle null enemy
        if (theEnemy == null) {
            return new CombatResult(false, 0, "Invalid enemy");
        }

        final int harmAmount = 50; // Standard Harm amount
        final int oldHealth = theEnemy.getHealth();
        theEnemy.takeDamage(harmAmount);
        final int actualHarm = oldHealth - theEnemy.getHealth();

        final String message = String.format("Used Bomb! Enemy took %d HP.", actualHarm);
        return new CombatResult(true, actualHarm, message);
    }

    /**
     * Determines if the player can run from combat based on enemy stats.
     *
     * @param theEnemy The enemy in combat.
     * @return True if the escape is successful, false otherwise.
     */
    public boolean canRunFromCombat(final Enemy theEnemy) {
        // Handle null enemy - allow escape if no enemy
        if (theEnemy == null) {
            return true;
        }

        // Higher level enemies are harder to run from
        final int escapeChance = Math.max(30, 80 - (theEnemy.getLevel() * 10));
        return myRandom.nextInt(100) < escapeChance;
    }

    /**
     * A class to hold the results of a combat action.
     */
    public static class CombatResult {
        /**
         * A flag indicating if the action resulted in a hit.
         */
        public final boolean myHit;
        /**
         * The amount of damage dealt or healed.
         */
        public final int myDamage;
        /**
         * A message describing the combat result.
         */
        public final String myMessage;
        /**
         * A flag indicating if a class attack missed.
         */
        public final boolean myIsClassAttackMiss;

        /**
         * Constructs a CombatResult with hit, damage, and a message.
         *
         * @param theHit     A flag indicating if the attack was a hit.
         * @param theDamage  The damage dealt.
         * @param theMessage The message to display.
         */
        public CombatResult(final boolean theHit, final int theDamage, final String theMessage) {
            myHit = theHit;
            myDamage = theDamage;
            myMessage = theMessage;
            myIsClassAttackMiss = false;
        }

        /**
         * Constructs a CombatResult with hit, damage, a message, and a class attack miss flag.
         *
         * @param theHit               A flag indicating if the attack was a hit.
         * @param theDamage            The damage dealt.
         * @param theMessage           The message to display.
         * @param theIsClassAttackMiss A flag indicating if a class attack missed.
         */
        public CombatResult(final boolean theHit, final int theDamage, final String theMessage, final boolean theIsClassAttackMiss) {
            myHit = theHit;
            myDamage = theDamage;
            myMessage = theMessage;
            myIsClassAttackMiss = theIsClassAttackMiss;
        }
    }
}