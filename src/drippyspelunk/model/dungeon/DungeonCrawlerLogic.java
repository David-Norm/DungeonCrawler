package drippyspelunk.model.dungeon;

import drippyspelunk.model.dungeon.entity.GameObject;
import drippyspelunk.model.dungeon.entity.Inventory;
import drippyspelunk.model.dungeon.entity.asset.*;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Character;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Enemy;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;
import drippyspelunk.model.dungeon.factory.GameObjectFactory;
import drippyspelunk.model.dungeon.factory.PotionFactory;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Logic for the Dungeon Crawler with integrated combat system and database character selection.
 *
 * @author Devin Arroyo
 * @author Mark Malyshev
 * @author David Norman
 * @version 1.24
 */
public class DungeonCrawlerLogic {

    /**
     * Property constant for when the player's position changes.
     */
    public static final String PLAYER_POSITION_PROPERTY = "playerPosition";
    /**
     * Property constant for when the current room changes.
     */
    public static final String CURRENT_ROOM_PROPERTY = "myCurrentRoom";
    /**
     * Property constant for each game tick.
     */
    public static final String GAME_TICK_PROPERTY = "gameTick";
    /**
     * Property constant for when the player starts moving.
     */
    public static final String PLAYER_MOVED_PROPERTY = "playerMoved";
    /**
     * Property constant for when the player stops moving.
     */
    public static final String PLAYER_STOPPED_PROPERTY = "playerStopped";
    /**
     * Property constant for when the combat state changes.
     */
    public static final String COMBAT_STATE_CHANGED_PROPERTY = "combatStateChanged";
    /**
     * Property constant for when the player transitions through a door.
     */
    public static final String PLAYER_DOOR_PROPERTY = "playerDoor";
    /**
     * Property constant for when a combat enemy is selected.
     */
    public static final String COMBAT_ENEMY_PROPERTY = "combatEnemy";
    /**
     * Property constant for when the game ends.
     */
    public static final String GAME_OVER_PROPERTY = "gameOver";
    /**
     * Property constant for when the player wins the game.
     */
    public static final String WIN_GAME_PROPERTY = "winGame";
    /**
     * Property constant for when the player levels up.
     */
    public static final String PLAYER_LEVEL_UP_PROPERTY = "playerLevelUp";
    /**
     * Property constant for when the player's inventory changes.
     */
    public static final String INVENTORY_CHANGED_PROPERTY = "inventoryChanged";
    /**
     * Property constant for when a pillar is collected.
     */
    public static final String PILLARS_COLLECTED_PROPERTY = "pillarsCollected";
    /**
     * Property constant for a new combat message.
     */
    public static final String COMBAT_MESSAGE_PROPERTY = "combatMessage";
    /**
     * Property constant for when the player's health changes.
     */
    public static final String PLAYER_HEALTH_CHANGED_PROPERTY = "playerHealthChanged";
    /**
     * Property constant for when an enemy's health changes.
     */
    public static final String ENEMY_HEALTH_CHANGED_PROPERTY = "enemyHealthChanged";

    /**
     * The logical width of the game screen.
     */
    public static final int LOGICAL_GAME_WIDTH = 480;
    /**
     * The logical height of the game screen.
     */
    public static final int LOGICAL_GAME_HEIGHT = 360;

    /**
     * The size of the small grid used for dungeon generation.
     */
    private static final int SMALL_GRID_SIZE = 8;
    /**
     * The size of the big grid used for dungeon generation.
     */
    private static final int BIG_GRID_SIZE = 16;
    /**
     * The percentage of inner rooms to generate.
     */
    private static final double ROOM_PERCENT_INNER = 0.8;
    /**
     * The percentage of total rooms to generate.
     */
    private static final double ROOM_PERCENT = 0.8;
    /**
     * The number of pillars to collect.
     */
    private static final int PILLAR_AMOUNT = 4;
    /**
     * The ID of the starting room.
     */
    private static final int START_ROOM_ID = 9;
    /**
     * The ID of the ending room.
     */
    private static final int END_ROOM_ID = 10;
    /**
     * The initial ID for the starting room during generation.
     */
    private static final int INIT_START_ROOM_ID = -2;

    /**
     * The factor to reduce speed for diagonal movement.
     */
    private static final double DIAGONAL_SPEED_FACTOR = 0.707;
    /**
     * The damage dealt by a trap.
     */
    private static final int TRAP_DAMAGE = 20;
    /**
     * The minimum number of items in a chest.
     */
    private static final int CHEST_MIN_ITEMS = 1;
    /**
     * The maximum number of items in a chest.
     */
    private static final int CHEST_MAX_ITEMS = 3;
    /**
     * The type index for a bomb consumable.
     */
    private static final int CONSUMABLE_TYPE_BOMB = 4;
    /**
     * The total number of consumable types.
     */
    private static final int CONSUMABLE_TYPE_COUNT = 5;
    /**
     * The offset for player position when transitioning rooms.
     */
    private static final int DOOR_OFFSET = 2;

    /**
     * The string representation of a Poison Potion.
     */
    private static final String ITEM_TYPE_POISON_POTION = "PoisonPotion";
    /**
     * The string representation of a Mystery Potion.
     */
    private static final String ITEM_TYPE_MYSTERY_POTION = "MysteryPotion";

    /**
     * The character selected by the player.
     */
    private final Character mySelectedPlayerCharacter;
    /**
     * The name entered by the player.
     */
    private final String myPlayerName;
    /**
     * Supports firing property change events.
     */
    private final PropertyChangeSupport myPCS;
    /**
     * The difficulty level of the game.
     */
    private final int myDifficulty;
    /**
     * A map to track the current animation frame of each enemy.
     */
    private final Map<Enemy, Integer> myEnemyAnimationFrames;
    /**
     * A random number generator.
     */
    private final Random myRandom = new Random();
    /**
     * A queue for handling game ending events.
     */
    private final Queue<GameEndingType> myGameEndingQueue;
    /**
     * The player character object.
     */
    private Player myPlayer;
    /**
     * The player's inventory.
     */
    private Inventory<Consumable> myInventory;
    /**
     * The 2D array representing the dungeon grid.
     */
    private int[][] myDungeonGrid;
    /**
     * A map of all rooms in the dungeon.
     */
    private Map<Integer, Room> myRooms;
    /**
     * The room the player is currently in.
     */
    private Room myCurrentRoom;
    /**
     * The logic handler for combat.
     */
    private CombatLogic myCombatLogic;
    /**
     * A flag indicating if the player is currently moving.
     */
    private boolean myPlayerIsCurrentlyMoving = false;
    /**
     * The number of pillars remaining to be collected.
     */
    private int myPillarsToCollect = PILLAR_AMOUNT;
    /**
     * The number of game ticks that have passed.
     */
    private long gameTicks = 0;
    /**
     * A flag to prevent multiple game over events.
     */
    private boolean myGameOverTriggered = false;

    /**
     * Constructs the DungeonCrawlerLogic with the specified character, name, and difficulty.
     *
     * @param theSelectedPlayerCharacter The character selected by the player.
     * @param thePlayerName              The name of the player.
     * @param theDifficulty              The difficulty level of the game.
     */
    public DungeonCrawlerLogic(final Character theSelectedPlayerCharacter, final String thePlayerName, final int theDifficulty) {
        myPCS = new PropertyChangeSupport(this);
        myRooms = new HashMap<>();
        mySelectedPlayerCharacter = theSelectedPlayerCharacter;
        myPlayerName = thePlayerName;
        myEnemyAnimationFrames = new HashMap<>();
        myGameEndingQueue = new ConcurrentLinkedQueue<>();
        myDifficulty = theDifficulty;
    }

    /**
     * Initializes the dungeon, player, and combat system.
     */
    public void init() {
        final int smallGridSize = SMALL_GRID_SIZE * myDifficulty;
        final int bigGridSize = BIG_GRID_SIZE * myDifficulty;

        final int startingPosX = myDifficulty * 2;
        final int startingPosY = myDifficulty * 2;
        final int endingPosX = smallGridSize - startingPosX;
        final int endingPosY = smallGridSize - startingPosY;
        final DungeonCrawlerRoomConnectionsGenerator generator = new DungeonCrawlerRoomConnectionsGenerator(myDifficulty);
        final DungeonCrawlerLabyrinthGenerator DT = new DungeonCrawlerLabyrinthGenerator();

        do {
            final int[][] tempGrid = new int[smallGridSize][smallGridSize];
            tempGrid[endingPosX][endingPosY] = INIT_START_ROOM_ID;
            myDungeonGrid = DT.generateGridPillars(tempGrid, startingPosX, startingPosY, PILLAR_AMOUNT, bigGridSize, myDifficulty);
        } while (!(DT.innerSumGrid(myDungeonGrid) <= smallGridSize * smallGridSize * ROOM_PERCENT_INNER / myDifficulty &&
                DT.sumGrid(myDungeonGrid) <= bigGridSize * bigGridSize * ROOM_PERCENT / myDifficulty * 1.5));

        myDungeonGrid[endingPosX + (bigGridSize - smallGridSize) / 2][endingPosY + (bigGridSize - smallGridSize) / 2] = END_ROOM_ID;
        myDungeonGrid[startingPosX + (bigGridSize - smallGridSize) / 2][startingPosY + (bigGridSize - smallGridSize) / 2] = START_ROOM_ID;
        DT.asciiGrid(myDungeonGrid);

        myRooms = generator.createDungeonFromGrid(myDungeonGrid);
        setCurrentRoom(myRooms.get((startingPosX + (bigGridSize - smallGridSize) / 2) * myDungeonGrid[1].length + startingPosY + (bigGridSize - smallGridSize) / 2));

        myEnemyAnimationFrames.clear();
        for (final Enemy enemy : myCurrentRoom.getEnemies()) {
            myEnemyAnimationFrames.put(enemy, 0);
        }

        // Create player with selected character
        createPlayerWithSelectedCharacter();
        myInventory = new Inventory<>();

        myCombatLogic = new CombatLogic(myPCS, myPlayer, myInventory, this);
    }

    /**
     * Creates the player using the selected character from the database.
     */
    private void createPlayerWithSelectedCharacter() {
        final Point spawnPoint = myCurrentRoom.getSpawnPoint();

        if (mySelectedPlayerCharacter != null) {
            myPlayer = Player.createFromCharacterData(spawnPoint.x, spawnPoint.y, mySelectedPlayerCharacter);
        } else {
            myPlayer = (Player) GameObjectFactory.createGameObject(
                    GameObjectFactory.TYPE_PLAYER,
                    spawnPoint.x,
                    spawnPoint.y,
                    0,
                    0);
        }
    }

    /**
     * Updates the game state, including player movement, combat, and object interactions.
     *
     * @param theElapsedTime The time elapsed since the last update.
     */
    public void update(final long theElapsedTime) {
        gameTicks++;
        if (!myGameOverTriggered && !myGameEndingQueue.isEmpty()) {
            final GameEndingType myGameEndingType = myGameEndingQueue.poll();

            if (myGameEndingType != null) {
                myGameOverTriggered = true;
                myPCS.firePropertyChange(GAME_OVER_PROPERTY, null, myGameEndingType);
                return;
            }
        }

        if (myGameOverTriggered) {
            return;
        }

        if (!myPlayer.isAlive()) {
            myGameEndingQueue.offer(GameEndingType.ENEMY);
            return;
        }

        if (myPlayer.isSpeedBoostActive()) {
            myPlayer.tickSpeedBoost();
        }
        if (myPlayer.isSlowed()) {
            myPlayer.tickSlow();
        }
        if (myPlayer.isPoisoned()) {
            myPlayer.tickPoisoned();
        }
        if (myPlayer.isVisionBoostActive()) {
            myPlayer.tickVisionBoost();
        }
        if (myPlayer.isBlinded()) {
            myPlayer.tickBlind();
        }

        final List<GameObject> objectsToRemove = new ArrayList<>();
        final List<ActiveBomb> bombsToRemove = new ArrayList<>();
        for (final ActiveBomb bomb : new ArrayList<>(myCurrentRoom.getActiveBombs())) {
            bomb.update();
            if (bomb.getMyExploded()) {
                handleExplosion(bomb, objectsToRemove);
                bomb.tickExplosion();
                if (bomb.isFinished()) {
                    bombsToRemove.add(bomb);
                    objectsToRemove.add(bomb);
                }
            }
        }

        final List<GameObject> combinedObjectsToRemove = new ArrayList<>();
        combinedObjectsToRemove.addAll(bombsToRemove);
        combinedObjectsToRemove.addAll(objectsToRemove);

        myCurrentRoom.removeObjects(combinedObjectsToRemove);

        final int oldPlayerX = myPlayer.getX();
        final int oldPlayerY = myPlayer.getY();

        // Only update player movement if not in combat
        if (!myCombatLogic.isInCombat()) {
            int dx = 0;
            int dy = 0;
            final int speed = myPlayer.getMoveSpeed();

            final boolean isDiagonal = (myPlayer.isMovingLeft() || myPlayer.isMovingRight()) &&
                    (myPlayer.isMovingUp() || myPlayer.isMovingDown());

            final int effectiveSpeed = isDiagonal ? (int) (speed * DIAGONAL_SPEED_FACTOR) : speed;

            if (myPlayer.isMovingLeft()) {
                dx -= effectiveSpeed;
            }
            if (myPlayer.isMovingRight()) {
                dx += effectiveSpeed;
            }
            if (myPlayer.isMovingUp()) {
                dy -= effectiveSpeed;
            }
            if (myPlayer.isMovingDown()) {
                dy += effectiveSpeed;
            }

            moveAndCollide(myPlayer, dx, dy);
        }

        // Only update enemies if NOT in combat at all
        if (myCurrentRoom.getEnemies() != null && !myCombatLogic.isInCombat()) {
            for (final Enemy enemy : myCurrentRoom.getEnemies()) {
                if (enemy.isAlive()) {
                    final double radianAngle = Math.atan2(myPlayer.getY() - enemy.getY(), myPlayer.getX() - enemy.getX());
                    final int dx = (int) Math.round(Math.cos(radianAngle) * enemy.getSpeed());
                    final int dy = (int) Math.round(Math.sin(radianAngle) * enemy.getSpeed());
                    if (enemy.getCharacterClass().equals("dragon")) {
                        if (gameTicks % 400 > 200) {
                            moveAndCollide(enemy, dx * 2, dy * 2);
                        }
                    } else {
                        moveAndCollide(enemy, dx, dy);
                    }
                    if (dx > 0) enemy.setCurrentState(Enemy.EnemyState.WALKING_EAST);
                    else if (dx < 0) enemy.setCurrentState(Enemy.EnemyState.WALKING_WEST);
                    else if (dy > 0) enemy.setCurrentState(Enemy.EnemyState.WALKING_SOUTH);
                    else if (dy < 0) enemy.setCurrentState(Enemy.EnemyState.WALKING_NORTH);
                    else enemy.setCurrentState(Enemy.EnemyState.STANDING_SOUTH);
                }
            }
        }


        if (!myCombatLogic.isInCombat()) {
            handlePlayerCollision();
            checkDoorTransition();
        }

        final boolean playerActuallyMoved = (myPlayer.getX() != oldPlayerX || myPlayer.getY() != oldPlayerY);

        if (playerActuallyMoved && !myPlayerIsCurrentlyMoving && !myCombatLogic.isInCombat()) {
            myPlayerIsCurrentlyMoving = true;
            myPCS.firePropertyChange(PLAYER_MOVED_PROPERTY, null, null);
        } else if (!playerActuallyMoved && myPlayerIsCurrentlyMoving) {
            myPlayerIsCurrentlyMoving = false;
            myPCS.firePropertyChange(PLAYER_STOPPED_PROPERTY, null, null);
        }

        final List<Enemy> enemies = getCurrentRoom().getEnemies();
        for (final Enemy enemy : enemies) {
            if (enemy.isAlive() && myPlayer.getBounds().intersects(enemy.getBounds())) {
                myCombatLogic.startCombat(enemy);
                break;
            }
        }

        myPCS.firePropertyChange(PLAYER_POSITION_PROPERTY, null, myPlayer.getBounds());
        myPCS.firePropertyChange(GAME_TICK_PROPERTY, null, null);
    }

    /**
     * Handles collisions between the player and various game objects.
     */
    private void handlePlayerCollision() {
        final Rectangle playerBounds = myPlayer.getBounds();

        for (final GameObject obj : myCurrentRoom.getObjectsInRoom()) {
            if (obj instanceof Enemy enemy) {
                if (enemy.isAlive() && !myCombatLogic.isInCombat() && playerBounds.intersects(obj.getBounds())) {
                    myCombatLogic.startCombat(enemy);
                    return;
                }
            } else if (obj instanceof Trap) {
                if (playerBounds.intersects(obj.getBounds())) {
                    myPlayer.takeDamage(TRAP_DAMAGE);
                    myCurrentRoom.removeObject(obj);
                    if (!myPlayer.isAlive()) {
                        myGameEndingQueue.offer(GameEndingType.TRAP);
                    }
                    return;
                }
            } else if (obj instanceof Consumable) {
                if (playerBounds.intersects(obj.getBounds())) {
                    if (myInventory.add((Consumable) obj)) {
                        myCurrentRoom.removeObject(obj);
                        myPCS.firePropertyChange(INVENTORY_CHANGED_PROPERTY, null, myInventory);
                    }
                    return;
                }
            } else if (obj instanceof Chest chest) {
                if (playerBounds.intersects(obj.getBounds()) && !chest.isOpen()) {
                    chest.open();
                    final int numItems = myRandom.nextInt(CHEST_MAX_ITEMS) + CHEST_MIN_ITEMS;
                    for (int i = 0; i < numItems; i++) {
                        final Consumable randomItem = getRandomConsumable();
                        myInventory.add(randomItem);

                    }
                    myPCS.firePropertyChange(INVENTORY_CHANGED_PROPERTY, null, myInventory);
                    return;
                }
            } else if (obj instanceof Pillar) {
                if (playerBounds.intersects(obj.getBounds())) {
                    myCurrentRoom.removeObject(obj);
                    myPillarsToCollect--;
                    myPCS.firePropertyChange(PILLARS_COLLECTED_PROPERTY, null, myPillarsToCollect);
                    return;
                }
            } else if (obj instanceof Exit) {
                if (playerBounds.intersects(obj.getBounds())) {
                    if (myPillarsToCollect == 0) {
                        myGameEndingQueue.offer(GameEndingType.WIN);
                        return;
                    } else {
                        myGameEndingQueue.offer(GameEndingType.FAKE_WIN);
                    }
                }
            }
        }
    }

    /**
     * Gets a random consumable item.
     *
     * @return A random consumable.
     */
    private Consumable getRandomConsumable() {
        final int itemType = myRandom.nextInt(CONSUMABLE_TYPE_COUNT);
        return (itemType == CONSUMABLE_TYPE_BOMB) ? new Bomb(0, 0, 0, 0) : PotionFactory.createRandomPotion(0, 0, 0, 0);
    }

    /**
     * Handles the effects of an explosion.
     *
     * @param theBomb            The active bomb that exploded.
     * @param theObjectsToRemove A list to add objects to be removed.
     */
    private void handleExplosion(final ActiveBomb theBomb, final List<GameObject> theObjectsToRemove) {
        final Rectangle explosionBounds = theBomb.getExplosionBounds();

        if (explosionBounds.intersects(myPlayer.getBounds())) {
            myPlayer.takeDamage(myPlayer.getHealth());
            myGameEndingQueue.offer(GameEndingType.BOMB);
        }

        for (final GameObject obj : myCurrentRoom.getObjectsInRoom()) {
            if (explosionBounds.intersects(obj.getBounds())) {
                if (obj instanceof BreakableWall || obj instanceof Trap) {
                    theObjectsToRemove.add(obj);
                }
            }
        }
    }

    /**
     * Moves a game object and handles collisions with walls.
     *
     * @param theObject The object to move.
     * @param theDx     The change in the x-direction.
     * @param theDy     The change in the y-direction.
     */
    private void moveAndCollide(final GameObject theObject, final int theDx, final int theDy) {
        for (int i = 0; i < Math.abs(theDx); i++) {
            final int newX = theObject.getX() + (theDx > 0 ? 1 : -1);
            final Rectangle proposedBounds = new Rectangle(newX, theObject.getY(), theObject.getWidth(), theObject.getHeight());

            if (checkWallCollisionAtPosition(proposedBounds)) {
                theObject.setX(newX);
            } else {
                break;
            }
        }

        for (int i = 0; i < Math.abs(theDy); i++) {
            final int newY = theObject.getY() + (theDy > 0 ? 1 : -1);
            final Rectangle proposedBounds = new Rectangle(theObject.getX(), newY, theObject.getWidth(), theObject.getHeight());

            if (checkWallCollisionAtPosition(proposedBounds)) {
                theObject.setY(newY);
            } else {
                break;
            }
        }
    }

    /**
     * Checks for collisions with walls at a specific position.
     *
     * @param theProposedBounds The proposed bounds for the object.
     * @return True if there is no wall collision, false otherwise.
     */
    private boolean checkWallCollisionAtPosition(final Rectangle theProposedBounds) {
        for (final GameObject obj : myCurrentRoom.getGameObjects()) {
            if (obj instanceof Wall || obj instanceof BreakableWall) {
                if (theProposedBounds.intersects(obj.getBounds())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the player has entered a door and transitions to the new room.
     */
    private void checkDoorTransition() {
        final Rectangle playerBounds = myPlayer.getBounds();

        for (final Door door : myCurrentRoom.getDoors()) {
            if (playerBounds.intersects(door.getBounds())) {

                final int targetRoomId = door.getTargetRoomId();
                final Room targetRoom = myRooms.get(targetRoomId);

                if (targetRoom != null) {
                    final Point newPlayerPos = calculateNewPlayerPosition(door);
                    myPlayer.setX(newPlayerPos.x);
                    myPlayer.setY(newPlayerPos.y);

                    setCurrentRoom(targetRoom);
                    myPCS.firePropertyChange(PLAYER_DOOR_PROPERTY, null, null);
                    myEnemyAnimationFrames.clear();
                    for (final Enemy enemy : myCurrentRoom.getEnemies()) {
                        myEnemyAnimationFrames.put(enemy, 0);
                    }
                }
            }
        }
    }

    /**
     * Gets the current number of game ticks.
     *
     * @return The number of game ticks.
     */
    public long getGameTicks() {
        return gameTicks;
    }

    /**
     * Calculates the player's new position after entering a door.
     *
     * @param theEnteredDoor The door the player entered.
     * @return The new point for the player's position.
     */
    private Point calculateNewPlayerPosition(final Door theEnteredDoor) {
        final int newX;
        final int newY;

        switch (theEnteredDoor.getDirection()) {
            case Door.NORTH -> {
                newY = (Room.GRID_HEIGHT - DOOR_OFFSET) * Room.TILE_SIZE;
                newX = myPlayer.getX();
            }
            case Door.SOUTH -> {
                newY = Room.TILE_SIZE;
                newX = myPlayer.getX();
            }
            case Door.EAST -> {
                newX = Room.TILE_SIZE;
                newY = myPlayer.getY();
            }
            case Door.WEST -> {
                newX = (Room.GRID_WIDTH - DOOR_OFFSET) * Room.TILE_SIZE;
                newY = myPlayer.getY();
            }
            default -> {
                newX = myPlayer.getX();
                newY = myPlayer.getY();
            }
        }
        return new Point(newX, newY);
    }

    /**
     * Gets a list of all doors in the current room.
     *
     * @return A list of doors.
     */
    public List<Door> getCurrentDoors() {
        if (myCurrentRoom != null) {
            return myCurrentRoom.getDoors();
        }
        return Collections.emptyList();
    }

    /**
     * Gets the current room.
     *
     * @return The current room.
     */
    public Room getCurrentRoom() {
        return myCurrentRoom;
    }

    /**
     * Sets the current room and fires a property change event.
     *
     * @param theNewRoom The new room.
     */
    public void setCurrentRoom(final Room theNewRoom) {
        final Room oldRoom = myCurrentRoom;
        myCurrentRoom = theNewRoom;
        myPCS.firePropertyChange(CURRENT_ROOM_PROPERTY, oldRoom, theNewRoom);
    }

    /**
     * Gets the logical screen width.
     *
     * @return The screen width.
     */
    public int getScreenWidth() {
        return LOGICAL_GAME_WIDTH;
    }

    /**
     * Gets the logical screen height.
     *
     * @return The screen height.
     */
    public int getScreenHeight() {
        return LOGICAL_GAME_HEIGHT;
    }

    /**
     * Sets the player's movement to the left.
     *
     * @param thePressed True if the left key is pressed, false otherwise.
     */
    public void playerMoveLeft(final boolean thePressed) {
        if (myPlayer != null && !myCombatLogic.isInCombat()) {
            myPlayer.setMovingLeft(thePressed);
        }
    }

    /**
     * Sets the player's movement to the right.
     *
     * @param thePressed True if the right key is pressed, false otherwise.
     */
    public void playerMoveRight(final boolean thePressed) {
        if (myPlayer != null && !myCombatLogic.isInCombat()) {
            myPlayer.setMovingRight(thePressed);
        }
    }

    /**
     * Sets the player's movement to up.
     *
     * @param thePressed True if the up key is pressed, false otherwise.
     */
    public void playerMoveUp(final boolean thePressed) {
        if (myPlayer != null && !myCombatLogic.isInCombat()) {
            myPlayer.setMovingUp(thePressed);
        }
    }

    /**
     * Sets the player's movement to down.
     *
     * @param thePressed True if the down key is pressed, false otherwise.
     */
    public void playerMoveDown(final boolean thePressed) {
        if (myPlayer != null && !myCombatLogic.isInCombat()) {
            myPlayer.setMovingDown(thePressed);
        }
    }

    /**
     * Performs an attack in combat.
     *
     * @param theAttackType The type of attack to perform.
     */
    public void performAttack(final AttackType theAttackType) {
        myCombatLogic.performAttack(theAttackType);
    }

    /**
     * Performs a block in combat.
     */
    public void performBlock() {
        myCombatLogic.performBlock();
    }

    /**
     * Attempts to run from combat.
     */
    public void runFromCombat() {
        myCombatLogic.runFromCombat();
    }

    /**
     * Uses an item from the player's inventory.
     *
     * @param theSlotIndex The index of the item slot to use.
     */
    public void useInventoryItem(final int theSlotIndex) {
        if (myInventory == null || theSlotIndex < 0 || theSlotIndex >= myInventory.getSize()) {
            return;
        }

        final Consumable item = myInventory.getItems().get(theSlotIndex);

        if (myCombatLogic.isInCombat()) {
            myCombatLogic.useSpecificInventoryItem(item);
            myPCS.firePropertyChange(INVENTORY_CHANGED_PROPERTY, null, myInventory);
        } else {
            if (item instanceof Bomb) { // Check if the item is a bomb
                placeBomb(); // Call the dedicated method to place the bomb
            } else {
                useItemOutsideCombat(item);
            }
        }
    }

    /**
     * Uses a consumable item outside of combat.
     *
     * @param theItem The item to use.
     */
    public void useItemOutsideCombat(final Consumable theItem) {
        final String itemType = theItem.getClass().getSimpleName();

        theItem.applyEffect(myPlayer);
        myInventory.remove(theItem);

        // Check for immediate death from instant damage potions and add to queue
        if (!myPlayer.isAlive()) {
            final GameEndingType gameEndingType = switch (itemType) {
                case ITEM_TYPE_POISON_POTION -> GameEndingType.POISON;
                case ITEM_TYPE_MYSTERY_POTION -> GameEndingType.MYSTERY_POTION;
                default -> GameEndingType.ENEMY;
            };
            myGameEndingQueue.offer(gameEndingType);
        }
    }

    /**
     * Places a bomb in the current room.
     */
    public void placeBomb() {
        final Bomb bomb = myInventory.getBomb();
        if (bomb != null) {
            myInventory.remove(bomb);
            final ActiveBomb activeBomb = new ActiveBomb(myPlayer.getX(), myPlayer.getY());
            myCurrentRoom.addObject(activeBomb);
            myPCS.firePropertyChange(INVENTORY_CHANGED_PROPERTY, null, myInventory);
        }
    }

    /**
     * Checks if the game is currently in combat.
     *
     * @return True if in combat, false otherwise.
     */
    public boolean isInCombat() {
        return myCombatLogic.isInCombat();
    }

    /**
     * Gets the current enemy in combat.
     *
     * @return The current combat enemy.
     */
    public Enemy getCurrentCombatEnemy() {
        return myCombatLogic.getCurrentCombatEnemy();
    }

    /**
     * Gets the player object.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     * Gets the player's inventory.
     *
     * @return The inventory.
     */
    public Inventory<Consumable> getInventory() {
        return myInventory;
    }

    /**
     * Gets the dungeon grid.
     *
     * @return The 2D array representing the dungeon grid.
     */
    public int[][] getDungeonGrid() {
        return myDungeonGrid;
    }

    /**
     * Gets the player's chosen username instead of character name.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return myPlayerName;
    }

    /**
     * Gets the selected character name.
     *
     * @return The name of the selected character.
     */
    public String getSelectedCharacterName() {
        if (mySelectedPlayerCharacter != null) {
            return mySelectedPlayerCharacter.getMyName();
        }
        return null;
    }

    /**
     * Gets the current animation frame for an enemy.
     *
     * @param theEnemy The enemy to get the frame for.
     * @return The animation frame index.
     */
    public int getEnemyAnimationFrame(final Enemy theEnemy) {
        return myEnemyAnimationFrames.getOrDefault(theEnemy, 0);
    }

    /**
     * Sets the animation frame for an enemy.
     *
     * @param theEnemy The enemy to set the frame for.
     * @param theFrame The new animation frame index.
     */
    public void setEnemyAnimationFrame(final Enemy theEnemy, final int theFrame) {
        myEnemyAnimationFrames.put(theEnemy, theFrame);
    }

    /**
     * Gets a list of all game objects in the current room to be drawn.
     *
     * @return An unmodifiable list of all objects.
     */
    public List<GameObject> getAllObjects() {
        if (myCurrentRoom == null) {
            return Collections.emptyList();
        }

        final List<GameObject> allObjectsToDraw = new ArrayList<>();
        allObjectsToDraw.addAll(myCurrentRoom.getFloorObjects());
        allObjectsToDraw.addAll(myCurrentRoom.getObjectsInRoom());
        allObjectsToDraw.addAll(myCurrentRoom.getEnemies());
        allObjectsToDraw.add(myPlayer);
        allObjectsToDraw.addAll(myCurrentRoom.getActiveBombs());

        return Collections.unmodifiableList(allObjectsToDraw);
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

    /**
     * Adds a property change listener for a specific property.
     *
     * @param thePropertyName The name of the property to listen for.
     * @param theListener     The listener to add.
     */
    public void addPropertyChangeListener(final String thePropertyName, final PropertyChangeListener theListener) {
        myPCS.addPropertyChangeListener(thePropertyName, theListener);
    }

    /**
     * Removes a property change listener for a specific property.
     *
     * @param thePropertyName The name of the property.
     * @param theListener     The listener to remove.
     */
    public void removePropertyChangeListener(final String thePropertyName, final PropertyChangeListener theListener) {
        myPCS.removePropertyChangeListener(thePropertyName, theListener);
    }

    /**
     * Enum for different types of game endings.
     */
    public enum GameEndingType {
        WIN,
        FAKE_WIN,
        BOMB,
        TRAP,
        POISON,
        MYSTERY_POTION,
        ENEMY
    }

    /**
     * Enum for different attack types.
     */
    public enum AttackType {
        LIGHT("Light Attack", 15, 30, 90, 100),
        HEAVY("Heavy Attack", 40, 65, 40, 60),
        HAIL_MARY("Hail Mary", 80, 100, 20, 30),
        // Values will be set dynamically from player stats
        CLASS_ATTACK("Class Attack", 0, 0, 0, 0);

        private final String myName;
        private final int myMinDamage;
        private final int myMaxDamage;
        private final int myMinHitChance;
        private final int myMaxHitChance;

        /**
         * Constructs an AttackType.
         *
         * @param theName   The name of the attack.
         * @param theMinDmg The minimum damage.
         * @param theMaxDmg The maximum damage.
         * @param theMinHit The minimum hit chance.
         * @param theMaxHit The maximum hit chance.
         */
        AttackType(final String theName, final int theMinDmg, final int theMaxDmg, final int theMinHit, final int theMaxHit) {
            myName = theName;
            myMinDamage = theMinDmg;
            myMaxDamage = theMaxDmg;
            myMinHitChance = theMinHit;
            myMaxHitChance = theMaxHit;
        }

        /**
         * Gets the name of the attack.
         *
         * @return The attack name.
         */
        public String getMyName() {
            return myName;
        }

        /**
         * Gets the minimum damage.
         *
         * @return The minimum damage.
         */
        public int getMyMinDamage() {
            return myMinDamage;
        }

        /**
         * Gets the maximum damage.
         *
         * @return The maximum damage.
         */
        public int getMyMaxDamage() {
            return myMaxDamage;
        }

        /**
         * Gets the minimum hit chance.
         *
         * @return The minimum hit chance.
         */
        public int getMyMinHitChance() {
            return myMinHitChance;
        }

        /**
         * Gets the maximum hit chance.
         *
         * @return The maximum hit chance.
         */
        public int getMaxHitChance() {
            return myMaxHitChance;
        }
    }
}