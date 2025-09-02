package drippyspelunk.model.dungeon;

import drippyspelunk.model.dungeon.DungeonCrawlerLogic.AttackType;
import drippyspelunk.model.dungeon.entity.Inventory;
import drippyspelunk.model.dungeon.entity.asset.Consumable;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Enemy;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeSupport;

/**
 * Handles all combat-related logic for the Dungeon Crawler game.
 *
 * @author David Norman
 * @author Devin Arroyo
 * @version 1.6
 */
public class CombatLogic {

    /**
     * The X coordinate for the player's combat position.
     */
    private static final int COMBAT_PLAYER_X = 130;
    /**
     * The Y coordinate for the player's combat position.
     */
    private static final int COMBAT_PLAYER_Y = 200;
    /**
     * The X coordinate for the enemy's combat position.
     */
    private static final int COMBAT_ENEMY_X = 310;
    /**
     * The Y coordinate for the enemy's combat position.
     */
    private static final int COMBAT_ENEMY_Y = 160;

    /**
     * The player's initiative advantage on a speed tie.
     */
    private static final double PLAYER_INITIATIVE_ADVANTAGE = 0.6; // 60% chance for player on ties
    /**
     * The base delay for the enemy's turn in milliseconds.
     */
    private static final int ENEMY_TURN_BASE_DELAY_MS = 1500;
    /**
     * The minimum delay for the enemy's turn in milliseconds.
     */
    private static final int ENEMY_TURN_MIN_DELAY_MS = 500;
    /**
     * The delay modifier based on the enemy's speed.
     */
    private static final int ENEMY_SPEED_DELAY_MODIFIER_MS = 100;
    /**
     * The amount of damage dealt by poison each turn.
     */
    private static final int ENEMY_POISON_DAMAGE = 10;

    /**
     * The health value for a defeated enemy.
     */
    private static final int DEFEATED_ENEMY_HEALTH = 0;
    /**
     * An off-screen coordinate for defeated enemies.
     */
    private static final int OFFSCREEN_POSITION = -1000;

    /**
     * The property name for combat messages.
     */
    private static final String COMBAT_MESSAGE_PROPERTY = "combatMessage";
    /**
     * The property name for inventory changes.
     */
    private static final String INVENTORY_CHANGED_PROPERTY = "inventoryChanged";
    /**
     * The type name for a health potion item.
     */
    private static final String ITEM_TYPE_HEALTH_POTION = "HealthPotion";
    /**
     * The type name for a poison potion item.
     */
    private static final String ITEM_TYPE_POISON_POTION = "PoisonPotion";
    /**
     * The type name for a bomb item.
     */
    private static final String ITEM_TYPE_BOMB = "Bomb";

    /**
     * The first part of the combat started a message.
     */
    private static final String MESSAGE_COMBAT_STARTED_PART1 = "Combat started with ";
    /**
     * The first part of the level message.
     */
    private static final String MESSAGE_LEVEL_PART1 = " (Level ";
    /**
     * The second part of the level message.
     */
    private static final String MESSAGE_LEVEL_PART2 = " ";
    /**
     * The third part of the level message.
     */
    private static final String MESSAGE_LEVEL_PART3 = ")";
    /**
     * The first part of the player faster message.
     */
    private static final String MESSAGE_PLAYER_FASTER_PART1 = "You attack first! (Your speed: ";
    /**
     * The second part of the player faster message.
     */
    private static final String MESSAGE_PLAYER_FASTER_PART2 = " vs ";
    /**
     * The third part of the player faster message.
     */
    private static final String MESSAGE_PLAYER_FASTER_PART3 = "'s speed: ";
    /**
     * The fourth part of the player faster message.
     */
    private static final String MESSAGE_PLAYER_FASTER_PART4 = ")";
    /**
     * The first part of the enemy faster message.
     */
    private static final String MESSAGE_ENEMY_FASTER_PART1 = " attacks first! (Their speed: ";
    /**
     * The second part of the enemy faster message.
     */
    private static final String MESSAGE_ENEMY_FASTER_PART2 = " vs your speed: ";
    private static final String MESSAGE_PLAYER_TURN_WAIT_SHORT = "Wait for your turn!";
    /**
     * The first part of the victory message.
     */
    private static final String MESSAGE_VICTORY_PART1 = "Victory! Gained ";
    /**
     * The second part of the victory message.
     */
    private static final String MESSAGE_VICTORY_PART2 = " XP.";
    /**
     * The first part of the victory level up message.
     */
    private static final String MESSAGE_VICTORY_LEVEL_UP_PART1 = " LEVEL UP! Now level ";
    /**
     * The second part of the victory level up message.
     */
    private static final String MESSAGE_VICTORY_LEVEL_UP_PART2 = "!";
    /**
     * The prefix for a failed defense message.
     */
    private static final String MESSAGE_DEFENSE_FAILED_PREFIX = "Block failed! ";
    /**
     * The first part of the defeat message.
     */
    private static final String MESSAGE_DEFEAT_PART1 = "Defeat! You have been slain by ";
    /**
     * The message for an invalid combat item.
     */
    private static final String MESSAGE_INVALID_ITEM_COMBAT = "Selected Item cannot be used in combat!";
    /**
     * The second part of the block stance message.
     */
    private static final String MESSAGE_BLOCK_STANCE_PART2 = "%)";
    /**
     * A short message for block stance.
     */
    private static final String MESSAGE_BLOCK_STANCE_SHORT_PART1 = "Blocking stance! (Block chance: ";
    /**
     * The UI message for a successful fleeing.
     */
    private static final String MESSAGE_FLEE_SUCCESS_UI = "You successfully flee from combat!";
    /**
     * The first part of the fled fail message.
     */
    private static final String MESSAGE_FLEE_FAIL_PART1 = "Failed to escape! ";
    /**
     * The second part of the fled fail message.
     */
    private static final String MESSAGE_FLEE_FAIL_PART2 = " blocks your path!";
    /**
     * The combat manager handling damage and other combat mechanics.
     */
    private final CombatManager myCombatManager;
    /**
     * The property change support for firing events.
     */
    private final PropertyChangeSupport myPCS;
    /**
     * The player character.
     */
    private final Player myPlayer;
    /**
     * The player's inventory.
     */
    private final Inventory<Consumable> myInventory;
    /**
     * The main game logic handler.
     */
    private final DungeonCrawlerLogic myMainLogic;
    /**
     * A flag indicating if the player is currently in combat.
     */
    private boolean myInCombat = false;
    /**
     * The enemy currently in combat.
     */
    private Enemy myCurrentCombatEnemy = null;
    /**
     * The player's position before combat started.
     */
    private Point myPlayerPreCombatPosition = null;
    /**
     * The enemy's position before combat started.
     */
    private Point myEnemyPreCombatPosition = null;
    /**
     * The room the player was in before combat started.
     */
    private Room myPreCombatRoom = null; // Store the room before combat
    /**
     * A flag indicating if the player ran from combat.
     */
    private boolean myPlayerRanFromCombat = false; // Track if player fled
    /**
     * A flag indicating if it's the player's turn.
     */
    private boolean myIsPlayerTurn = true;
    /**
     * A flag indicating if the player is currently blocking.
     */
    private boolean myIsBlocking = false;

    /**
     * Constructor for CombatLogic.
     *
     * @param thePcs       The property change support.
     * @param thePlayer    The player character.
     * @param theInventory The player's inventory.
     * @param theMainLogic The main game logic.
     */
    public CombatLogic(final PropertyChangeSupport thePcs, final Player thePlayer,
                       final Inventory<Consumable> theInventory,
                       final DungeonCrawlerLogic theMainLogic) {
        myPCS = thePcs;
        myPlayer = thePlayer;
        myInventory = theInventory;
        myMainLogic = theMainLogic;
        myCombatManager = new CombatManager();
    }

    /**
     * Returns true if the player is in combat, false otherwise.
     *
     * @return A boolean indicating if the player is in combat.
     */
    public boolean isInCombat() {
        return myInCombat;
    }

    /**
     * Returns the enemy currently in combat.
     *
     * @return The current combat enemy.
     */
    public Enemy getCurrentCombatEnemy() {
        return myCurrentCombatEnemy;
    }

    /**
     * Returns true if it is the player's turn, false otherwise.
     *
     * @return A boolean indicating if it is the player's turn.
     */
    public boolean isPlayerTurn() {
        return myIsPlayerTurn;
    }

    /**
     * Starts combat with the given enemy.
     *
     * @param theEnemy The enemy to start combat with.
     */
    public void startCombat(final Enemy theEnemy) {
        // Handle null enemy
        if (theEnemy == null) {
            if (myPCS != null) {
                myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, "null enemy");
            }
            return;
        }
        myInCombat = true;
        myCurrentCombatEnemy = theEnemy;
        myIsBlocking = false;
        myPlayerRanFromCombat = false; // Reset flee flag
        myCombatManager.resetCombat();
        myIsPlayerTurn = determineFirstTurn(myPlayer, theEnemy);

        myPlayer.setMovingDown(false);
        myPlayer.setMovingUp(false);
        myPlayer.setMovingLeft(false);
        myPlayer.setMovingRight(false);

        // Store original positions and current room
        myPlayerPreCombatPosition = new Point(myPlayer.getX(), myPlayer.getY());
        myEnemyPreCombatPosition = new Point(theEnemy.getX(), theEnemy.getY());
        myPreCombatRoom = myMainLogic.getCurrentRoom(); // Store the current room

        // Move to combat positions
        myPlayer.setX(COMBAT_PLAYER_X);
        myPlayer.setY(COMBAT_PLAYER_Y);
        theEnemy.setX(COMBAT_ENEMY_X);
        theEnemy.setY(COMBAT_ENEMY_Y);

        String combatStartMsg = MESSAGE_COMBAT_STARTED_PART1 + theEnemy.getName();
        if (theEnemy.getLevel() > 1) {
            combatStartMsg += MESSAGE_LEVEL_PART1 + theEnemy.getLevel() + MESSAGE_LEVEL_PART2 + theEnemy.getCharacterClass() + MESSAGE_LEVEL_PART3;
        }

        // Add turn order information
        String turnOrderMsg;
        if (myIsPlayerTurn) {
            turnOrderMsg = MESSAGE_PLAYER_FASTER_PART1 + myPlayer.getAttackSpeed() +
                    MESSAGE_PLAYER_FASTER_PART2 + theEnemy.getName() + MESSAGE_PLAYER_FASTER_PART3 + theEnemy.getAttackSpeed() + MESSAGE_PLAYER_FASTER_PART4;
        } else {
            turnOrderMsg = theEnemy.getName() + MESSAGE_ENEMY_FASTER_PART1 + theEnemy.getAttackSpeed() +
                    MESSAGE_ENEMY_FASTER_PART2 + myPlayer.getAttackSpeed() + MESSAGE_PLAYER_FASTER_PART4;
        }

        combatStartMsg += "! " + turnOrderMsg;
        // Fire property change to notify UI
        myPCS.firePropertyChange(DungeonCrawlerLogic.COMBAT_STATE_CHANGED_PROPERTY, false, true);
        myPCS.firePropertyChange(DungeonCrawlerLogic.COMBAT_ENEMY_PROPERTY, null, theEnemy);
        myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, combatStartMsg);

        myPCS.firePropertyChange(DungeonCrawlerLogic.PLAYER_HEALTH_CHANGED_PROPERTY, null, myPlayer.getHealth());
        myPCS.firePropertyChange(DungeonCrawlerLogic.ENEMY_HEALTH_CHANGED_PROPERTY, null, theEnemy.getHealth());

        if (!myIsPlayerTurn) {
            performEnemyTurn();
        }
    }

    /**
     * Ends the current combat.
     */
    public void endCombat() {
        if (!myInCombat) return;

        if (myCurrentCombatEnemy != null && !myCurrentCombatEnemy.isAlive() && !myPlayerRanFromCombat) {
            awardExperienceForVictory(myCurrentCombatEnemy);
        }

        myInCombat = false;

        // Restore original positions
        if (myPlayerPreCombatPosition != null) {
            myPlayer.setX(myPlayerPreCombatPosition.x);
            myPlayer.setY(myPlayerPreCombatPosition.y);
        }

        if (myEnemyPreCombatPosition != null && myCurrentCombatEnemy != null && myCurrentCombatEnemy.isAlive()) {
            myCurrentCombatEnemy.setX(myEnemyPreCombatPosition.x);
            myCurrentCombatEnemy.setY(myEnemyPreCombatPosition.y);
        }

        // Restore the original room if it was stored
        if (myPreCombatRoom != null) {
            myMainLogic.setCurrentRoom(myPreCombatRoom);
        }

        // Clear combat references
        myCurrentCombatEnemy = null;
        myPlayerPreCombatPosition = null;
        myEnemyPreCombatPosition = null;
        myPreCombatRoom = null;

        // Fire property change to notify UI
        myPCS.firePropertyChange(DungeonCrawlerLogic.COMBAT_STATE_CHANGED_PROPERTY, true, false);
        myPCS.firePropertyChange(DungeonCrawlerLogic.COMBAT_ENEMY_PROPERTY, null, null);
    }

    /**
     * Determines who attacks first based on attack speed.
     *
     * @param thePlayer The player character.
     * @param theEnemy  The enemy character.
     * @return True if the player goes first, false otherwise.
     */
    private boolean determineFirstTurn(final Player thePlayer, final Enemy theEnemy) {
        final int playerSpeed = thePlayer.getAttackSpeed();
        final int enemySpeed = theEnemy.getAttackSpeed();

        if (playerSpeed > enemySpeed) {
            return true;
        } else if (enemySpeed > playerSpeed) {
            return false;
        } else {
            // Equal speed - random determination with slight player advantage
            return Math.random() < PLAYER_INITIATIVE_ADVANTAGE;
        }
    }

    /**
     * Awards experience to the player for defeating an enemy.
     *
     * @param theDefeatedEnemy The enemy that was defeated.
     */
    private void awardExperienceForVictory(final Enemy theDefeatedEnemy) {
        if (myCombatManager == null) return;

        final int xpGained = myCombatManager.calculateExperienceGain(theDefeatedEnemy);
        final int oldLevel = myPlayer.getLevel();

        myPlayer.addExperience(xpGained);

        String victoryMsg = MESSAGE_VICTORY_PART1 + xpGained + MESSAGE_VICTORY_PART2;

        // Check for level up
        if (myPlayer.getLevel() > oldLevel) {
            victoryMsg += MESSAGE_VICTORY_LEVEL_UP_PART1 + myPlayer.getLevel() + MESSAGE_VICTORY_LEVEL_UP_PART2;
            myPCS.firePropertyChange(DungeonCrawlerLogic.PLAYER_LEVEL_UP_PROPERTY, oldLevel, myPlayer.getLevel());
        }

        myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, victoryMsg);
    }

    /**
     * Performs a player attack action.
     *
     * @param theAttackType The type of attack to perform.
     */
    public void performAttack(final AttackType theAttackType) {
        if (!myInCombat || myCurrentCombatEnemy == null || !myIsPlayerTurn) {
            if (!myIsPlayerTurn) {
                myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, MESSAGE_PLAYER_TURN_WAIT_SHORT);
            }
            return;
        }

        final CombatManager.CombatResult result = myCombatManager.performPlayerAttack(theAttackType, myCurrentCombatEnemy, myPlayer);

        myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, result.myMessage);
        myPCS.firePropertyChange(DungeonCrawlerLogic.ENEMY_HEALTH_CHANGED_PROPERTY, null, myCurrentCombatEnemy.getHealth());

        // Check if this was a class attack miss and trigger popup
        if (result.myIsClassAttackMiss) {
            myPCS.firePropertyChange("classAttackMiss", null, true);
        }

        myIsPlayerTurn = false;
        performEnemyTurn();
    }

    /**
     * Performs an enemy turn.
     */
    private void performEnemyTurn() {
        if (!myInCombat || myCurrentCombatEnemy == null || myIsPlayerTurn) return;

        // Determine delay based on enemy attack speed (faster enemies act quicker)
        final int enemySpeed = myCurrentCombatEnemy.getAttackSpeed();
        final int adjustedDelay = Math.max(ENEMY_TURN_MIN_DELAY_MS, ENEMY_TURN_BASE_DELAY_MS - (enemySpeed * ENEMY_SPEED_DELAY_MODIFIER_MS));

        // Delay enemy attack for better game feel
        new Thread(() -> {
            try {
                Thread.sleep(adjustedDelay);

                SwingUtilities.invokeLater(() -> {
                    CombatManager.CombatResult result;

                    if (myCurrentCombatEnemy.isPoisoned()) {
                        myCurrentCombatEnemy.takeDamage(ENEMY_POISON_DAMAGE);
                        myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, "Enemy poisoned, 10 HP damage");  //This line never activates, we likely need more delay somewhere.
                        myPCS.firePropertyChange(DungeonCrawlerLogic.ENEMY_HEALTH_CHANGED_PROPERTY, null, myCurrentCombatEnemy.getHealth());
                    }

                    if (!myCurrentCombatEnemy.isAlive() || myCurrentCombatEnemy == null) {
                        endCombat();
                    } else {

                        if (myIsBlocking) {
                            final boolean blocked = myCombatManager.attemptBlock();
                            if (blocked) {
                                result = myCombatManager.performBlockedEnemyAttack(myCurrentCombatEnemy, myPlayer);
                            } else {
                                result = myCombatManager.performEnemyAttack(myCurrentCombatEnemy, myPlayer);
                                result = new CombatManager.CombatResult(result.myHit, result.myDamage,
                                        MESSAGE_DEFENSE_FAILED_PREFIX + result.myMessage);
                            }
                            myIsBlocking = false;
                        } else {
                            result = myCombatManager.performEnemyAttack(myCurrentCombatEnemy, myPlayer);
                        }

                        myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, result.myMessage);
                        myPCS.firePropertyChange(DungeonCrawlerLogic.PLAYER_HEALTH_CHANGED_PROPERTY, null, myPlayer.getHealth());

                        // Check if player is defeated
                        if (!myPlayer.isAlive()) {
                            myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, MESSAGE_DEFEAT_PART1 + myCurrentCombatEnemy.getName());
                            // Handle game over
                            endCombat();
                            myPCS.firePropertyChange(DungeonCrawlerLogic.GAME_OVER_PROPERTY, null, DungeonCrawlerLogic.GameEndingType.ENEMY);
                        } else {
                            // Player's turn now
                            myIsPlayerTurn = true;
                            //myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, MESSAGE_YOUR_TURN_ACTION);
                        }
                    }
                });
            } catch (final InterruptedException theE) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Performs a block action.
     */
    public void performBlock() {
        if ((!myInCombat || !myIsPlayerTurn) && (myCurrentCombatEnemy != null)) {
            if (!myIsPlayerTurn) {
                myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, MESSAGE_PLAYER_TURN_WAIT_SHORT);
            }
            return;
        }

        myIsBlocking = true;
        myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null,
                MESSAGE_BLOCK_STANCE_SHORT_PART1 + myCombatManager.getCurrentBlockChance() + MESSAGE_BLOCK_STANCE_PART2);

        // Enemy's turn
        myIsPlayerTurn = false;
        performEnemyTurn();
    }

    /**
     * Attempts to run from combat.
     */
    public void runFromCombat() {
        if (!myInCombat || myCurrentCombatEnemy == null) return;

        // Use the combat manager to determine if escape is successful
        final boolean canEscape = myCombatManager.canRunFromCombat(myCurrentCombatEnemy);

        if (canEscape) {
            myPlayerRanFromCombat = true; // Mark that player fled
            myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, MESSAGE_FLEE_SUCCESS_UI);

            // Mark the enemy as "fled from" so it won't chase or initiate combat
            if (myCurrentCombatEnemy != null) {
                // Set the enemy's health to 0 to effectively "remove" it
                myCurrentCombatEnemy.setHealth(DEFEATED_ENEMY_HEALTH);

                // Move the enemy far off-screen so it won't interfere
                myCurrentCombatEnemy.setX(OFFSCREEN_POSITION);
                myCurrentCombatEnemy.setY(OFFSCREEN_POSITION);
            }

            endCombat();
        } else {
            myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null,
                    MESSAGE_FLEE_FAIL_PART1 + myCurrentCombatEnemy.getName() + MESSAGE_FLEE_FAIL_PART2);

            // Failed escape counts as a turn, enemy attacks
            myIsPlayerTurn = false;
            performEnemyTurn();
        }
    }

    /**
     * Uses a specific inventory item.
     *
     * @param theSelectedItem The item to use.
     */
    public void useSpecificInventoryItem(final Consumable theSelectedItem) {
        if (!myInCombat || !myIsPlayerTurn) {
            if (myInCombat) {
                myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, MESSAGE_PLAYER_TURN_WAIT_SHORT);
            }
            return;
        }

        if (theSelectedItem == null) {
            return;
        }

        // Use the item
        final String itemType = theSelectedItem.getClass().getSimpleName();

        if (!(itemType.equals(ITEM_TYPE_HEALTH_POTION) || itemType.equals(ITEM_TYPE_POISON_POTION) || itemType.equals(ITEM_TYPE_BOMB))) {
            myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, MESSAGE_INVALID_ITEM_COMBAT);
        } else {
            if (itemType.equals(ITEM_TYPE_HEALTH_POTION)) {
                final CombatManager.CombatResult result = myCombatManager.useHealthPotion(myPlayer);
                myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, result.myMessage);
                myPCS.firePropertyChange(DungeonCrawlerLogic.PLAYER_HEALTH_CHANGED_PROPERTY, null, myPlayer.getHealth());
            } else if (itemType.equals(ITEM_TYPE_POISON_POTION)) {
                final CombatManager.CombatResult result = myCombatManager.usePoisonPotion(myCurrentCombatEnemy);
                myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, result.myMessage);
            } else {
                final CombatManager.CombatResult result = myCombatManager.useBomb(myCurrentCombatEnemy);
                myPCS.firePropertyChange(COMBAT_MESSAGE_PROPERTY, null, result.myMessage);
                myPCS.firePropertyChange(DungeonCrawlerLogic.ENEMY_HEALTH_CHANGED_PROPERTY, null, myCurrentCombatEnemy.getHealth());
            }
            // Remove item from inventory
            myInventory.remove(theSelectedItem);
            myMainLogic.getCurrentRoom().removeObject(theSelectedItem);
            // Fire inventory changed event
            myPCS.firePropertyChange(INVENTORY_CHANGED_PROPERTY, null, myInventory);

            // Enemy's turn
            myIsPlayerTurn = false;
            performEnemyTurn();
        }
    }
}