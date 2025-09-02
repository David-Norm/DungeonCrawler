package drippyspelunk.view.dungeon;

import drippyspelunk.controller.sprite.SpriteManager;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic;
import drippyspelunk.model.dungeon.entity.GameObject;
import drippyspelunk.model.dungeon.entity.asset.*;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Enemy;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Player;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A panel responsible for drawing the game content (dungeon, characters, items, background)
 * within the DungeonCrawlerView's layered pane. It iterates through all game objects
 * and renders the corresponding sprite based on the object's type and state.
 *
 * @author Devin Arroyo
 * @author Mark Malyshev
 * @version 1.8
 */
public class DungeonContentPanel extends JPanel {

    // Sprite dimension constants
    /**
     * The fixed Y dimension for a bomb sprite.
     */
    private static final int BOMB_SPRITE_SIZE_Y = 96;
    /**
     * The fixed X dimension for a bomb sprite.
     */
    private static final int BOMB_SPRITE_SIZE_X = 128;
    /**
     * The fixed width for a character sprite.
     */
    private static final int CHARACTER_SPRITE_WIDTH = 32;
    /**
     * The fixed height for a character sprite.
     */
    private static final int CHARACTER_SPRITE_HEIGHT = 64;
    /**
     * The fixed size for a consumable item sprite.
     */
    private static final int CONSUMABLE_SPRITE_SIZE = 32;

    /**
     * The main game logic model.
     */
    private final DungeonCrawlerLogic myModel;
    /**
     * The GUI scaling factor.
     */
    private final int myGuiScale;
    /**
     * The amount to draw walls off-screen to create a seamless view.
     */
    private final int myWallOffscreenAmount;
    /**
     * A map to track the current animation frame for each enemy.
     */
    private final Map<Enemy, Integer> myEnemyAnimationFrames;
    /**
     * A map to track the current animation frame for each active bomb.
     */
    private final Map<ActiveBomb, Integer> myActiveBombAnimationFrames;
    /**
     * The current animation frame for the player.
     */
    private int myPlayerAnimationFrame;

    /**
     * Constructs the DungeonContentPanel.
     *
     * @param theLogic               The main dungeon game logic.
     * @param theGuiScale            The GUI scaling factor.
     * @param theWallOffscreenAmount The amount to draw walls off-screen.
     */
    public DungeonContentPanel(final DungeonCrawlerLogic theLogic, final int theGuiScale, final int theWallOffscreenAmount) {
        myModel = theLogic;
        myGuiScale = theGuiScale;
        myWallOffscreenAmount = theWallOffscreenAmount;
        setOpaque(false);
        myPlayerAnimationFrame = 0;
        myEnemyAnimationFrames = new HashMap<>();
        myActiveBombAnimationFrames = new HashMap<>();
    }

    /**
     * Sets the player's current animation frame.
     *
     * @param theFrame The frame index.
     */
    public void setPlayerAnimationFrame(final int theFrame) {
        myPlayerAnimationFrame = theFrame;
    }

    /**
     * Sets the animation frame for a specific enemy.
     *
     * @param theEnemy The enemy object.
     * @param theFrame The frame index.
     */
    public void setEnemyAnimationFrame(final Enemy theEnemy, final int theFrame) {
        myEnemyAnimationFrames.put(theEnemy, theFrame);
    }

    /**
     * Sets the animation frame for a specific active bomb.
     *
     * @param theActiveBomb The active bomb object.
     * @param theState      The state of the bomb ("ticking" or "explosion").
     * @param theFrame      The frame index.
     */
    public void setActiveBombAnimationFrame(final ActiveBomb theActiveBomb, final String theState, final int theFrame) {
        myActiveBombAnimationFrames.put(theActiveBomb, theFrame);
    }

    /**
     * Overrides the paintComponent method to draw all game objects.
     *
     * @param theGraphics The Graphics object to draw on.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        // Apply a global Y offset to create a 3D effect for walls
        final int globalYOffset = myWallOffscreenAmount * myGuiScale;

        final List<GameObject> allObjectsToDraw = myModel.getAllObjects();

        // Iterate through all objects and render them
        for (final GameObject obj : allObjectsToDraw) {
            final int renderX = obj.getX() * myGuiScale;
            final int renderY = obj.getY() * myGuiScale - globalYOffset;
            final int renderWidth = obj.getWidth() * myGuiScale;
            final int renderHeight = obj.getHeight() * myGuiScale;

            switch (obj) {
                case Wall wall ->
                        renderMatchingSprite(g2d, renderX, renderY, renderWidth, renderHeight, wall.getSpriteIdentifier());
                case Floor floorTile ->
                        renderMatchingSprite(g2d, renderX, renderY, renderWidth, renderHeight, floorTile.getSpriteIdentifier());
                case Player player -> renderPlayerSprite(g2d, renderX, renderY, player);
                case Enemy enemy -> renderEnemySprite(g2d, renderX, renderY, enemy);
                case HealthPotion ignored -> renderConsumableSprite(g2d, renderX, renderY, "health_potion");
                case PoisonPotion ignored -> renderConsumableSprite(g2d, renderX, renderY, "poison_potion");
                case SpeedPotion ignored -> renderConsumableSprite(g2d, renderX, renderY, "speed_potion");
                case MysteryPotion ignored -> renderConsumableSprite(g2d, renderX, renderY, "mystery_potion");
                case VisionPotion ignored -> renderConsumableSprite(g2d, renderX, renderY, "vision_potion");
                case Bomb ignored -> renderConsumableSprite(g2d, renderX, renderY, "bomb_consumable");
                case ActiveBomb activeBomb -> renderActiveBombSprite(g2d, renderX, renderY, activeBomb);
                case BreakableWall ignored ->
                        renderMatchingSprite(g2d, renderX, renderY, renderWidth, renderHeight, "breakable_wall");
                case Chest chest -> {
                    final String spriteIdentifier = chest.isOpen() ? "chest_open" : "chest_closed";
                    renderMatchingSprite(g2d, renderX, renderY, renderWidth, renderHeight, spriteIdentifier);
                }
                case Trap ignored -> renderMatchingSprite(g2d, renderX, renderY, renderWidth, renderHeight, "trap");
                case Pillar ignored -> renderMatchingSprite(g2d, renderX, renderY, renderWidth, renderHeight, "pillar");
                case Exit ignored -> renderMatchingSprite(g2d, renderX, renderY, renderWidth, renderHeight, "exit");
                default -> {
                    g2d.setColor(Color.MAGENTA);
                    g2d.fillRect(renderX, renderY, renderWidth, renderHeight);
                }
            }
        }
    }

    /**
     * Renders a consumable sprite with its fixed dimensions.
     *
     * @param theGraphics         The Graphics2D object.
     * @param theRenderX          The x-coordinate.
     * @param theRenderY          The y-coordinate.
     * @param theSpriteIdentifier The sprite identifier.
     */
    private void renderConsumableSprite(final Graphics2D theGraphics, final int theRenderX, final int theRenderY, final String theSpriteIdentifier) {
        renderMatchingSprite(theGraphics, theRenderX, theRenderY, CONSUMABLE_SPRITE_SIZE * myGuiScale, CONSUMABLE_SPRITE_SIZE * myGuiScale, theSpriteIdentifier);
    }

    /**
     * Renders an animated bomb sprite, either ticking or exploding.
     *
     * @param theGraphics   The Graphics2D object.
     * @param theRenderX    The x-coordinate.
     * @param theRenderY    The y-coordinate.
     * @param theActiveBomb The active bomb object.
     */
    private void renderActiveBombSprite(final Graphics2D theGraphics, final int theRenderX, final int theRenderY, final ActiveBomb theActiveBomb) {
        final int frame = myActiveBombAnimationFrames.getOrDefault(theActiveBomb, 0);
        final String spriteIdentifier;

        if (theActiveBomb.getMyExploded()) {
            spriteIdentifier = "bomb_explosion_" + frame;
        } else {
            spriteIdentifier = "bomb_ticking_" + frame;
        }

        final int spriteSizeY = BOMB_SPRITE_SIZE_Y * myGuiScale;
        final int spriteSizeX = BOMB_SPRITE_SIZE_X * myGuiScale;

        // Adjust position to center the larger sprite on the object's hitbox
        final int adjustedRenderX = theRenderX - (spriteSizeY - (theActiveBomb.getWidth() * myGuiScale)) / 2;
        final int adjustedRenderY = theRenderY - (spriteSizeX - (theActiveBomb.getHeight() * myGuiScale)) / 2;

        renderMatchingSprite(theGraphics, adjustedRenderX, adjustedRenderY, spriteSizeX, spriteSizeY, spriteIdentifier);
    }

    /**
     * Renders the player's animated sprite based on their current state.
     *
     * @param theGraphics The Graphics2D object.
     * @param theRenderX  The x-coordinate.
     * @param theRenderY  The y-coordinate.
     * @param thePlayer   The player object.
     */
    private void renderPlayerSprite(final Graphics2D theGraphics, final int theRenderX, final int theRenderY, final Player thePlayer) {
        final String spriteIdentifier;
        spriteIdentifier = switch (thePlayer.getCurrentState()) {
            case WALKING_NORTH -> "player_walking_north_" + myPlayerAnimationFrame;
            case WALKING_SOUTH -> "player_walking_south_" + myPlayerAnimationFrame;
            case WALKING_EAST -> "player_walking_east_" + myPlayerAnimationFrame;
            case WALKING_WEST -> "player_walking_west_" + myPlayerAnimationFrame;
            case STANDING_NORTH -> "player_standing_north_0";
            case STANDING_SOUTH -> "player_standing_south_0";
            case STANDING_EAST -> "player_standing_east_0";
            case STANDING_WEST -> "player_standing_west_0";
            case IDLE_1, IDLE_2 -> "player_standing_south_0"; // Default to standing south for idle states
        };

        // Adjust position to center the sprite over the hitbox
        final int visualSpriteHeight = CHARACTER_SPRITE_HEIGHT * myGuiScale;
        final int hitboxHeight = thePlayer.getHeight() * myGuiScale;
        final int verticalOffset = visualSpriteHeight - hitboxHeight;
        final int adjustedRenderY = theRenderY - verticalOffset;

        renderMatchingSprite(theGraphics, theRenderX, adjustedRenderY, CHARACTER_SPRITE_WIDTH * myGuiScale, visualSpriteHeight, spriteIdentifier);
    }

    /**
     * Renders an enemy's animated sprite based on its current state and class.
     *
     * @param theGraphics The Graphics2D object.
     * @param theRenderX  The x-coordinate.
     * @param theRenderY  The y-coordinate.
     * @param theEnemy    The enemy object.
     */
    private void renderEnemySprite(final Graphics2D theGraphics, final int theRenderX, final int theRenderY, final Enemy theEnemy) {
        final String spriteIdentifier;
        final String enemyClass = theEnemy.getCharacterClass();
        final int enemyAnimationFrame = myEnemyAnimationFrames.getOrDefault(theEnemy, 0);

        // Special handling for dragon's stationary mode
        final boolean isDragonStationary = "dragon".equals(enemyClass) &&
                (myModel.getGameTicks() % 400 <= 200);

        spriteIdentifier = switch (theEnemy.getCurrentState()) {
            case WALKING_NORTH -> enemyClass + "_walking_north_" + (isDragonStationary ? "0" : enemyAnimationFrame);
            case WALKING_SOUTH -> enemyClass + "_walking_south_" + (isDragonStationary ? "0" : enemyAnimationFrame);
            case WALKING_EAST -> enemyClass + "_walking_east_" + (isDragonStationary ? "0" : enemyAnimationFrame);
            case WALKING_WEST -> enemyClass + "_walking_west_" + (isDragonStationary ? "0" : enemyAnimationFrame);
            case STANDING_NORTH -> enemyClass + "_standing_north_0";
            case STANDING_SOUTH -> enemyClass + "_standing_south_0";
            case STANDING_EAST -> enemyClass + "_standing_east_0";
            case STANDING_WEST -> enemyClass + "_standing_west_0";
        };

        // Determine sprite dimensions based on enemy type
        final int spriteWidth, spriteHeight;
        if ("dragon".equals(enemyClass)) {
            spriteWidth = 64;
            spriteHeight = 64;
        } else {
            spriteWidth = CHARACTER_SPRITE_WIDTH;
            spriteHeight = CHARACTER_SPRITE_HEIGHT;
        }

        final int visualSpriteWidth = spriteWidth * myGuiScale;
        final int visualSpriteHeight = spriteHeight * myGuiScale;
        final int hitboxWidth = theEnemy.getWidth() * myGuiScale;
        final int hitboxHeight = theEnemy.getHeight() * myGuiScale;

        // Center the sprite over the hitbox
        final int horizontalOffset = (visualSpriteWidth - hitboxWidth) / 2;
        final int verticalOffset = visualSpriteHeight - hitboxHeight;
        final int adjustedRenderX = theRenderX - horizontalOffset;
        final int adjustedRenderY = theRenderY - verticalOffset;

        renderMatchingSprite(theGraphics, adjustedRenderX, adjustedRenderY, visualSpriteWidth, visualSpriteHeight, spriteIdentifier);
    }

    /**
     * Renders a sprite image from the SpriteManager, scaled to a specified size.
     *
     * @param theGraphics         The Graphics2D object to draw on.
     * @param theRenderX          The x-coordinate for rendering.
     * @param theRenderY          The y-coordinate for rendering.
     * @param theRenderWidth      The width to render the sprite.
     * @param theRenderHeight     The height to render the sprite.
     * @param theSpriteIdentifier The identifier of the sprite to retrieve.
     */
    private void renderMatchingSprite(final Graphics2D theGraphics,
                                      final int theRenderX,
                                      final int theRenderY,
                                      final int theRenderWidth,
                                      final int theRenderHeight,
                                      final String theSpriteIdentifier) {
        if (theSpriteIdentifier != null) {
            final ImageIcon spriteIcon = SpriteManager.getSprite(theSpriteIdentifier);
            if (spriteIcon != null) {
                final Image spriteImage = spriteIcon.getImage();
                theGraphics.drawImage(spriteImage, theRenderX, theRenderY, theRenderWidth, theRenderHeight, this);
            } else {
                theGraphics.setColor(Color.PINK.darker());
                theGraphics.fillRect(theRenderX, theRenderY, theRenderWidth, theRenderHeight);
            }
        } else {
            theGraphics.setColor(Color.MAGENTA.darker());
            theGraphics.fillRect(theRenderX, theRenderY, theRenderWidth, theRenderHeight);
        }
    }
}