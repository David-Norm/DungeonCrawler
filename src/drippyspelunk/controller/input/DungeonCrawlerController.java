package drippyspelunk.controller.input;

import drippyspelunk.controller.audio.AudioEvent;
import drippyspelunk.controller.audio.AudioManager;
import drippyspelunk.controller.core.StateManager;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic.AttackType;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic.GameEndingType;
import drippyspelunk.model.dungeon.entity.GameObject;
import drippyspelunk.model.dungeon.entity.asset.ActiveBomb;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Enemy;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;
import drippyspelunk.view.dungeon.CombatOverlay;
import drippyspelunk.view.dungeon.DungeonContentPanel;
import drippyspelunk.view.dungeon.DungeonOverlayPanel;
import drippyspelunk.view.dungeon.DungeonScreenPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Controls for the dungeon crawler, including combat controls.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.13
 */
public class DungeonCrawlerController implements IController, PropertyChangeListener, ActionListener {

    /**
     * Default value for render timestamp.
     */
    private static final int RENDER_TIMESTAMP_DEFAULT = 0;

    /**
     * Animation update rate for general sprites.
     */
    private static final int ANIMATION_UPDATE_RATE = 10;

    /**
     * Number of frames in a general animation cycle.
     */
    private static final int ANIMATION_FRAME_COUNT = 5;

    /**
     * Animation update rate for UI controls.
     */
    private static final int CONTROLS_ANIMATION_UPDATE_RATE = 12;

    /**
     * Number of frames for controls animation.
     */
    private static final int CONTROLS_ANIMATION_FRAME_COUNT = 3;

    /**
     * Number of frames for bomb ticking animation.
     */
    private static final int BOMB_TICKING_FRAME_COUNT = 19;

    /**
     * Animation update rate for bomb ticking.
     */
    private static final int BOMB_TICKING_UPDATE_RATE = 38;

    /**
     * Number of frames for bomb explosion animation.
     */
    private static final int BOMB_EXPLOSION_FRAME_COUNT = 15;

    /**
     * Animation update rate for bomb explosion.
     */
    private static final int BOMB_EXPLOSION_UPDATE_RATE = 9;

    /**
     * Number of frames for overlay animation.
     */
    private static final int OVERLAY_ANIMATION_FRAME_COUNT = 12;

    /**
     * Animation update rate for overlay.
     */
    private static final int OVERLAY_ANIMATION_UPDATE_RATE = 17;

    /**
     * The number of game ticks before a player is considered "idle".
     */
    private static final int IDLE_THRESHOLD = 1800;

    /**
     * Action command for resuming the game.
     */
    private static final String RESUME_GAME_COMMAND = "RESUME_GAME";

    /**
     * Action command for quick saving.
     */
    private static final String QUICK_SAVE_COMMAND = "QUICK_SAVE";

    /**
     * Action command for saving the game.
     */
    private static final String SAVE_GAME_COMMAND = "SAVE_GAME";

    /**
     * Action command for returning to the title screen.
     */
    private static final String TITLE_COMMAND = "TITLE";

    /**
     * Action command for quitting the game.
     */
    private static final String QUIT_COMMAND = "QUIT";

    /**
     * Action command for combat: block.
     */
    private static final String COMBAT_BLOCK_COMMAND = "BLOCK";

    /**
     * Action command for combat: run.
     */
    private static final String COMBAT_RUN_COMMAND = "RUN";

    /**
     * Action command for combat: light attack.
     */
    private static final String COMBAT_LIGHT_ATTACK_COMMAND = "LIGHT_ATTACK";

    /**
     * Action command for combat: heavy attack.
     */
    private static final String COMBAT_HEAVY_ATTACK_COMMAND = "HEAVY_ATTACK";

    /**
     * Action command for combat: Hail Mary attack.
     */
    private static final String COMBAT_HAIL_MARY_COMMAND = "HAIL_MARY";

    /**
     * Action command for combat: class attack.
     */
    private static final String COMBAT_CLASS_ATTACK_COMMAND = "CLASS_ATTACK";

    /**
     * Dialog message for quick save.
     */
    private static final String DIALOG_QUICK_SAVE_PRESSED = "Quick Save button pressed";

    /**
     * Dialog message for saving the game.
     */
    private static final String DIALOG_SAVE_GAME_PRESSED = "Save Game button pressed";

    /**
     * Dialog message for an unknown button.
     */
    private static final String DIALOG_UNKNOWN_BUTTON = "Unknown button pressed: ";

    /**
     * The state of a bomb when it is ticking.
     */
    private static final String BOMB_STATE_TICKING = "ticking";

    /**
     * The state of a bomb when it is exploding.
     */
    private static final String BOMB_STATE_EXPLOSION = "explosion";

    /**
     * Starting frame index for animations.
     */
    private static final int STARTING_FRAME_INDEX = 0;

    /**
     * Step for incrementing animation frames.
     */
    private static final int INCREMENT_STEP = 1;

    /**
     * The main game logic instance.
     */
    private final DungeonCrawlerLogic myLogic;

    /**
     * Manages application state transitions.
     */
    private final StateManager myStateManager;
    /**
     * Maps active bombs to their animation tick counters.
     */
    private final Map<ActiveBomb, Integer> myBombAnimationTickCounters = new HashMap<>();
    /**
     * Maps active bombs to their current animation frames.
     */
    private final Map<ActiveBomb, Integer> myBombAnimationFrames = new HashMap<>();
    /**
     * Timer for the overlay animation.
     */
    private final Timer myOverlayAnimationTimer;
    /**
     * Random number generator for idle animations.
     */
    private final Random myRandom = new Random();
    /**
     * The main view for the dungeon screen.
     */
    private DungeonScreenPanel myView;
    /**
     * The panel that displays the dungeon content.
     */
    private DungeonContentPanel myContentPanel;
    /**
     * The panel that displays the pause/menu overlay.
     */
    private DungeonOverlayPanel myOverlayPanel;
    /**
     * The overlay for combat-related UI.
     */
    private CombatOverlay myCombatOverlay;
    /**
     * Counter for player animation ticks.
     */
    private int myPlayerAnimationTickCounter = 0;
    /**
     * Index of the current player animation frame.
     */
    private int myPlayerAnimationFrameIndex = 0;
    /**
     * Counter for enemy animation ticks.
     */
    private int myEnemyAnimationTickCounter = 0;
    /**
     * Counter for controls animation ticks.
     */
    private int myControlsAnimationTickCounter = 0;
    /**
     * Index of the current controls animation frame.
     */
    private int myControlsAnimationFrameIndex = 0;
    /**
     * Counter for player idle ticks.
     */
    private int myIdleTickCounter = 0;
    /**
     * Index of the current overlay animation frame.
     */
    private int myOverlayAnimationFrame = 0;

    /**
     * Constructs a DungeonCrawlerController.
     *
     * @param theLogic        The game logic instance.
     * @param theView         The dungeon screen panel view.
     * @param theStateManager The state manager for game state transitions.
     */
    public DungeonCrawlerController(final DungeonCrawlerLogic theLogic,
                                    final DungeonScreenPanel theView,
                                    final StateManager theStateManager) {
        super();
        myLogic = theLogic;
        myView = theView;
        myStateManager = theStateManager;
        myLogic.addPropertyChangeListener(this);
        if (myView != null) {
            myContentPanel = theView.getDungeonScreen().getDungeonContentPanel();
            myOverlayPanel = theView.getDungeonScreen().getOverlayPanel();
            myCombatOverlay = theView.getDungeonScreen().getCombatOverlay();

            myCombatOverlay.getBlockButton().setActionCommand(COMBAT_BLOCK_COMMAND);
            myCombatOverlay.getRunButton().setActionCommand(COMBAT_RUN_COMMAND);
            myCombatOverlay.getLightAttackButton().setActionCommand(COMBAT_LIGHT_ATTACK_COMMAND);
            myCombatOverlay.getHeavyAttackButton().setActionCommand(COMBAT_HEAVY_ATTACK_COMMAND);
            myCombatOverlay.getHailMaryButton().setActionCommand(COMBAT_HAIL_MARY_COMMAND);
            myCombatOverlay.getClassAttackButton().setActionCommand(COMBAT_CLASS_ATTACK_COMMAND);
            myCombatOverlay.addActionListener(this);
        }

        myOverlayAnimationTimer = new Timer(OVERLAY_ANIMATION_UPDATE_RATE, theEvent -> {
            myOverlayAnimationFrame = (myOverlayAnimationFrame + INCREMENT_STEP) % OVERLAY_ANIMATION_FRAME_COUNT;
            if (myOverlayPanel != null) {
                myOverlayPanel.setOverlayAnimationFrame(myOverlayAnimationFrame);
                myView.render(RENDER_TIMESTAMP_DEFAULT);
            }
        });
        myOverlayAnimationTimer.setInitialDelay(RENDER_TIMESTAMP_DEFAULT);
    }

    /**
     * Sets the view for this controller.
     *
     * @param theView The dungeon screen panel view.
     */
    public void setMyView(final DungeonScreenPanel theView) {
        myView = theView;
        myContentPanel = theView.getDungeonScreen().getDungeonContentPanel();
        myOverlayPanel = theView.getDungeonScreen().getOverlayPanel();
        myCombatOverlay = theView.getDungeonScreen().getCombatOverlay();
        myOverlayPanel.setActionListener(this);

        myCombatOverlay.getBlockButton().setActionCommand(COMBAT_BLOCK_COMMAND);
        myCombatOverlay.getRunButton().setActionCommand(COMBAT_RUN_COMMAND);
        myCombatOverlay.getLightAttackButton().setActionCommand(COMBAT_LIGHT_ATTACK_COMMAND);
        myCombatOverlay.getHeavyAttackButton().setActionCommand(COMBAT_HEAVY_ATTACK_COMMAND);
        myCombatOverlay.getHailMaryButton().setActionCommand(COMBAT_HAIL_MARY_COMMAND);
        myCombatOverlay.getClassAttackButton().setActionCommand(COMBAT_CLASS_ATTACK_COMMAND);
        myCombatOverlay.addActionListener(this);
    }

    @Override
    public void keyTyped(final KeyEvent theEvent) {
        // Not used
    }

    @Override
    public void keyPressed(final KeyEvent theEvent) {
        final int key = theEvent.getKeyCode();

        if (myStateManager.isGamePaused()) {
            handlePausedInput(key);
        } else if (myLogic.isInCombat()) {
            handleCombatInput(key);
        } else {
            handleNormalInput(key);
        }
    }

    /**
     * Handles keyboard input when the game is paused.
     *
     * @param theKey The key code of the pressed key.
     */
    private void handlePausedInput(final int theKey) {
        switch (theKey) {
            case KeyEvent.VK_P, KeyEvent.VK_ESCAPE -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myView.getDungeonScreen().toggleOverlayVisibility();
                myStateManager.unpauseGame();
                myOverlayAnimationTimer.stop();
            }
        }
    }

    /**
     * Handles keyboard input during combat.
     *
     * @param theKey The key code of the pressed key.
     */
    private void handleCombatInput(final int theKey) {
        switch (theKey) {
            case KeyEvent.VK_1 -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myLogic.performAttack(AttackType.LIGHT);
            }
            case KeyEvent.VK_2 -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myLogic.performAttack(AttackType.HEAVY);
            }
            case KeyEvent.VK_3 -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myLogic.performAttack(AttackType.HAIL_MARY);
            }
            case KeyEvent.VK_4 -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myLogic.performAttack(AttackType.CLASS_ATTACK);
            }
            case KeyEvent.VK_B, KeyEvent.VK_5 -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myLogic.performBlock();
            }
            case KeyEvent.VK_R, KeyEvent.VK_6 -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myLogic.runFromCombat();
            }
            case KeyEvent.VK_ESCAPE, KeyEvent.VK_P -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myView.getDungeonScreen().toggleOverlayVisibility();
                myStateManager.pauseGame();
                myOverlayAnimationTimer.start();
            }
        }
    }

    /**
     * Handles keyboard input during normal gameplay (not paused or in combat).
     *
     * @param theKey The key code of the pressed key.
     */
    private void handleNormalInput(final int theKey) {
        switch (theKey) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> myLogic.playerMoveLeft(true);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> myLogic.playerMoveRight(true);
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> myLogic.playerMoveDown(true);
            case KeyEvent.VK_W, KeyEvent.VK_UP -> myLogic.playerMoveUp(true);
            case KeyEvent.VK_ESCAPE, KeyEvent.VK_P -> {
                AudioManager.dispatch(AudioEvent.MENU_SELECT);
                myView.getDungeonScreen().toggleOverlayVisibility();
                myStateManager.pauseGame();
                myOverlayAnimationTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(final KeyEvent theEvent) {
        final int key = theEvent.getKeyCode();

        if (!myLogic.isInCombat()) {
            switch (key) {
                case KeyEvent.VK_A, KeyEvent.VK_LEFT -> myLogic.playerMoveLeft(false);
                case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> myLogic.playerMoveRight(false);
                case KeyEvent.VK_S, KeyEvent.VK_DOWN -> myLogic.playerMoveDown(false);
                case KeyEvent.VK_W, KeyEvent.VK_UP -> myLogic.playerMoveUp(false);
            }
        }
    }

    /**
     * Handles a click on an inventory item.
     *
     * @param theSlotIndex The index of the inventory slot.
     */
    public void handleInventoryItemClick(final int theSlotIndex) {
        myLogic.useInventoryItem(theSlotIndex);
    }

    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        final String actionCommand = theEvent.getActionCommand();
        if (actionCommand != null) {
            switch (actionCommand) {
                case RESUME_GAME_COMMAND -> {
                    myView.getDungeonScreen().toggleOverlayVisibility();
                    myStateManager.unpauseGame();
                    myOverlayAnimationTimer.stop();
                }
                case TITLE_COMMAND -> {
                    myStateManager.unpauseGame();
                    myStateManager.goToTitleScreen();
                    myOverlayAnimationTimer.stop();
                }
                case QUIT_COMMAND -> System.exit(0);
                case COMBAT_BLOCK_COMMAND -> myLogic.performBlock();
                case COMBAT_RUN_COMMAND -> myLogic.runFromCombat();
                case COMBAT_LIGHT_ATTACK_COMMAND -> myLogic.performAttack(AttackType.LIGHT);
                case COMBAT_HEAVY_ATTACK_COMMAND -> myLogic.performAttack(AttackType.HEAVY);
                case COMBAT_HAIL_MARY_COMMAND -> myLogic.performAttack(AttackType.HAIL_MARY);
                case COMBAT_CLASS_ATTACK_COMMAND -> myLogic.performAttack(AttackType.CLASS_ATTACK);
                default -> {

                }
            }
        }
    }

    /**
     * Updates the player's animation state and frame.
     */
    private void updatePlayerAnimation() {
        Player player = myLogic.getPlayer();
        Player.PlayerState oldState = player.getCurrentState();
        Player.PlayerState newState = oldState;
        if (player.isMoving()) {
            myIdleTickCounter = RENDER_TIMESTAMP_DEFAULT;
            if (player.isMovingUp()) {
                newState = Player.PlayerState.WALKING_NORTH;
                player.setLastDirection(Player.NORTH);
            } else if (player.isMovingDown()) {
                newState = Player.PlayerState.WALKING_SOUTH;
                player.setLastDirection(Player.SOUTH);
            } else if (player.isMovingLeft()) {
                newState = Player.PlayerState.WALKING_WEST;
                player.setLastDirection(Player.WEST);
            } else if (player.isMovingRight()) {
                newState = Player.PlayerState.WALKING_EAST;
                player.setLastDirection(Player.EAST);
            }
        } else {
            myIdleTickCounter++;
            if (oldState == Player.PlayerState.WALKING_NORTH) {
                newState = Player.PlayerState.STANDING_NORTH;
                myPlayerAnimationFrameIndex = STARTING_FRAME_INDEX;
            } else if (oldState == Player.PlayerState.WALKING_SOUTH) {
                newState = Player.PlayerState.STANDING_SOUTH;
                myPlayerAnimationFrameIndex = STARTING_FRAME_INDEX;
            } else if (oldState == Player.PlayerState.WALKING_EAST) {
                newState = Player.PlayerState.STANDING_EAST;
                myPlayerAnimationFrameIndex = STARTING_FRAME_INDEX;
            } else if (oldState == Player.PlayerState.WALKING_WEST) {
                newState = Player.PlayerState.STANDING_WEST;
                myPlayerAnimationFrameIndex = STARTING_FRAME_INDEX;
            }
            if (myIdleTickCounter >= IDLE_THRESHOLD && (
                    oldState == Player.PlayerState.STANDING_NORTH ||
                            oldState == Player.PlayerState.STANDING_SOUTH ||
                            oldState == Player.PlayerState.STANDING_EAST ||
                            oldState == Player.PlayerState.STANDING_WEST)) {
                if (myRandom.nextBoolean()) {
                    newState = Player.PlayerState.IDLE_1;
                } else {
                    newState = Player.PlayerState.IDLE_2;
                }
                myPlayerAnimationFrameIndex = STARTING_FRAME_INDEX;
            }
        }
        if (newState != oldState || player.isMoving() ||
                newState == Player.PlayerState.IDLE_1 ||
                newState == Player.PlayerState.IDLE_2 ||
                newState == Player.PlayerState.STANDING_NORTH ||
                newState == Player.PlayerState.STANDING_SOUTH ||
                newState == Player.PlayerState.STANDING_EAST ||
                newState == Player.PlayerState.STANDING_WEST) {
            myPlayerAnimationTickCounter++;
            if (myPlayerAnimationTickCounter >= ANIMATION_UPDATE_RATE) {
                myPlayerAnimationTickCounter = RENDER_TIMESTAMP_DEFAULT;
                myPlayerAnimationFrameIndex = (myPlayerAnimationFrameIndex + INCREMENT_STEP) % ANIMATION_FRAME_COUNT;
                myContentPanel.setPlayerAnimationFrame(myPlayerAnimationFrameIndex);
            }
        }

        if (newState != oldState) {
            player.setCurrentState(newState);
        }
    }

    /**
     * Updates the animation frames for all enemies in the current room.
     */
    private void updateEnemyAnimations() {
        myEnemyAnimationTickCounter++;
        if (myEnemyAnimationTickCounter >= ANIMATION_UPDATE_RATE) {
            myEnemyAnimationTickCounter = RENDER_TIMESTAMP_DEFAULT;
            for (final Enemy enemy : myLogic.getCurrentRoom().getEnemies()) {
                if (enemy.isAlive()) {
                    final int oldFrame = myLogic.getEnemyAnimationFrame(enemy);
                    final int newFrame = (oldFrame + INCREMENT_STEP) % ANIMATION_FRAME_COUNT;
                    myLogic.setEnemyAnimationFrame(enemy, newFrame);
                    myContentPanel.setEnemyAnimationFrame(enemy, newFrame);
                }
            }
        }
    }

    /**
     * Updates the animation frame for the player controls panel.
     */
    private void updateControlsAnimation() {
        myControlsAnimationTickCounter++;
        if (myControlsAnimationTickCounter >= CONTROLS_ANIMATION_UPDATE_RATE) {
            myControlsAnimationTickCounter = RENDER_TIMESTAMP_DEFAULT;
            myControlsAnimationFrameIndex = (myControlsAnimationFrameIndex + INCREMENT_STEP) % CONTROLS_ANIMATION_FRAME_COUNT;
            myView.getPlayerControlsPanel().setAnimationFrame(myControlsAnimationFrameIndex);
        }
    }

    /**
     * Updates the animation frames for all active bombs.
     */
    private void updateActiveBombAnimations() {
        for (final GameObject obj : myLogic.getAllObjects()) {
            if (obj instanceof ActiveBomb activeBomb) {
                myBombAnimationTickCounters.putIfAbsent(activeBomb, RENDER_TIMESTAMP_DEFAULT);
                myBombAnimationFrames.putIfAbsent(activeBomb, RENDER_TIMESTAMP_DEFAULT);
                int tickCounter = myBombAnimationTickCounters.get(activeBomb);
                int frameIndex = myBombAnimationFrames.get(activeBomb);
                String animationState;
                if (!activeBomb.getMyExploded()) {
                    animationState = BOMB_STATE_TICKING;
                    tickCounter++;
                    if (tickCounter >= BOMB_TICKING_UPDATE_RATE) {
                        tickCounter = RENDER_TIMESTAMP_DEFAULT;
                        frameIndex = (frameIndex + INCREMENT_STEP) % BOMB_TICKING_FRAME_COUNT;
                    }
                } else {
                    animationState = BOMB_STATE_EXPLOSION;
                    tickCounter++;
                    if (tickCounter >= BOMB_EXPLOSION_UPDATE_RATE) {
                        tickCounter = RENDER_TIMESTAMP_DEFAULT;
                        frameIndex = (frameIndex + INCREMENT_STEP) % BOMB_EXPLOSION_FRAME_COUNT;
                    }
                }
                myBombAnimationTickCounters.put(activeBomb, tickCounter);
                myBombAnimationFrames.put(activeBomb, frameIndex);
                myContentPanel.setActiveBombAnimationFrame(activeBomb, animationState, frameIndex);
            }
        }
    }

    @Override
    public void cleanup() {
        myLogic.removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propertyName = theEvent.getPropertyName();
        if (DungeonCrawlerLogic.PLAYER_MOVED_PROPERTY.equals(propertyName)) {
            AudioManager.dispatch(AudioEvent.PLAYER_FOOTSTEP_START);
        } else if (DungeonCrawlerLogic.PLAYER_STOPPED_PROPERTY.equals(propertyName)) {
            AudioManager.dispatch(AudioEvent.PLAYER_FOOTSTEP_STOP);
        } else if (DungeonCrawlerLogic.PLAYER_DOOR_PROPERTY.equals(propertyName)) {
            AudioManager.dispatch(AudioEvent.PLAYER_DOOR_OPEN);
        } else if (DungeonCrawlerLogic.GAME_OVER_PROPERTY.equals(propertyName)) {
            final GameEndingType endingType = (GameEndingType) theEvent.getNewValue();
            myStateManager.goToGameOver(endingType);
        } else if (DungeonCrawlerLogic.WIN_GAME_PROPERTY.equals(propertyName)) {
            final GameEndingType endingType = (GameEndingType) theEvent.getNewValue();
            myStateManager.goToGameOver(endingType);
        } else if (DungeonCrawlerLogic.COMBAT_STATE_CHANGED_PROPERTY.equals(propertyName)) {
            final boolean inCombat = (Boolean) theEvent.getNewValue();
            myView.getDungeonScreen().getCombatOverlay().setVisible(inCombat);
            if (inCombat) {
                myCombatOverlay.displayMessage("Combat Started!");
                Player player = myLogic.getPlayer();
                Enemy enemy = myLogic.getCurrentCombatEnemy();
                // Use player's username instead of character name
                myCombatOverlay.setPlayerHealth(myLogic.getPlayerName(), player.getHealth(), player.getMaxHealth());
                myCombatOverlay.setEnemyHealth(enemy.getName(), enemy.getHealth(), enemy.getMaxHealth());
            }
        } else if (DungeonCrawlerLogic.COMBAT_MESSAGE_PROPERTY.equals(propertyName)) {
            myCombatOverlay.displayMessage((String) theEvent.getNewValue());
        } else if (DungeonCrawlerLogic.PLAYER_HEALTH_CHANGED_PROPERTY.equals(propertyName)) {
            Player player = myLogic.getPlayer();
            // Use player's username instead of character name
            myCombatOverlay.setPlayerHealth(myLogic.getPlayerName(), player.getHealth(), player.getMaxHealth());
        } else if (DungeonCrawlerLogic.ENEMY_HEALTH_CHANGED_PROPERTY.equals(propertyName)) {
            Enemy enemy = myLogic.getCurrentCombatEnemy();
            if (enemy != null) {
                myCombatOverlay.setEnemyHealth(enemy.getName(), enemy.getHealth(), enemy.getMaxHealth());
            }
        } else if (DungeonCrawlerLogic.GAME_TICK_PROPERTY.equals(propertyName)) {
            updatePlayerAnimation();
            updateEnemyAnimations();
            updateControlsAnimation();
            updateActiveBombAnimations();
            if (myView != null) {
                myView.render(RENDER_TIMESTAMP_DEFAULT);
            }
        }
    }
}