package drippyspelunk.controller.sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Manages all sprite loadings for the application.
 *
 * @author Devin Arroyo
 * @version 1.4
 */
public final class SpriteManager {

    /**
     * The base path for all sprite resources.
     */
    private static final String SPRITES_PATH = "/sprite/";

    /**
     * The singleton instance of the SpriteManager.
     */
    private static final SpriteManager INSTANCE = new SpriteManager();

    /**
     * Private constructor to enforce a singleton pattern.
     */
    private SpriteManager() {
    }

    /**
     * Initializes the SpriteManager by loading all necessary sprites and sprite sheets from the resources.
     * This method should be called once at the start of the application.
     */
    public static synchronized void initialize() {
        new SpriteBuilder().setIdentifier("exampleSheet").setPath(SPRITES_PATH + "example.png").load();

        new SpriteBuilder().setIdentifier("floor_sprite_biome1").setPath(SPRITES_PATH + "floor_sprite_biome1.png").loadFromSheetWithCharacters(32, 32, 9, 'a');
        new SpriteBuilder().setIdentifier("floor_sprite_biome2").setPath(SPRITES_PATH + "floor_sprite_biome2.png").loadFromSheetWithCharacters(32, 32, 9, 'a');
        new SpriteBuilder().setIdentifier("floor_sprite_biome3").setPath(SPRITES_PATH + "floor_sprite_biome3.png").loadFromSheetWithCharacters(32, 32, 9, 'a');
        new SpriteBuilder().setIdentifier("floor_sprite_biome4").setPath(SPRITES_PATH + "floor_sprite_biome4.png").loadFromSheetWithCharacters(32, 32, 9, 'a');
        new SpriteBuilder().setIdentifier("floor_sprite_biome5").setPath(SPRITES_PATH + "floor_sprite_biome5.png").loadFromSheetWithCharacters(32, 32, 9, 'a');

        new SpriteBuilder().setIdentifier("wall_sprite_biome1").setPath(SPRITES_PATH + "wall_sprite_biome1.png").loadFromSheetWithCharacters(32, 32, 21, 'A');
        new SpriteBuilder().setIdentifier("wall_sprite_biome2").setPath(SPRITES_PATH + "wall_sprite_biome2.png").loadFromSheetWithCharacters(32, 32, 21, 'A');
        new SpriteBuilder().setIdentifier("wall_sprite_biome3").setPath(SPRITES_PATH + "wall_sprite_biome3.png").loadFromSheetWithCharacters(32, 32, 21, 'A');
        new SpriteBuilder().setIdentifier("wall_sprite_biome4").setPath(SPRITES_PATH + "wall_sprite_biome4.png").loadFromSheetWithCharacters(32, 32, 21, 'A');
        new SpriteBuilder().setIdentifier("wall_sprite_biome5").setPath(SPRITES_PATH + "wall_sprite_biome5.png").loadFromSheetWithCharacters(32, 32, 21, 'A');

        // Player sprite configuration (4 directions, 9 frames: 1 standing + 8 walking)
        final String[] playerStates = new String[]{
                "standing_north", "standing_west", "standing_south", "standing_east",
                "walking_north", "walking_west", "walking_south", "walking_east"
        };
        final int playerFrameCount = 9; // Standing frame + 8 walking frames

        new SpriteBuilder().setIdentifier("player").setPath(SPRITES_PATH + "player_animated_spritesheet.png")
                .loadEnemyAnimatedSheet(32, 64, playerFrameCount, playerStates);

        // Enemy sprite configuration (4 directions, 9 frames: 1 standing + 8 walking)
        final String[] enemyStates = new String[]{
                "standing_north", "standing_west", "standing_south", "standing_east",
                "walking_north", "walking_west", "walking_south", "walking_east"
        };
        final int enemyFrameCount = 9; // Standing frame + 8 walking frames

        new SpriteBuilder().setIdentifier("skeleton").setPath(SPRITES_PATH + "skeleton_animated_spritesheet.png")
                .loadEnemyAnimatedSheet(32, 64, enemyFrameCount, enemyStates);

        new SpriteBuilder().setIdentifier("goblin").setPath(SPRITES_PATH + "goblin_animated_spritesheet.png")
                .loadEnemyAnimatedSheet(32, 64, enemyFrameCount, enemyStates);

        new SpriteBuilder().setIdentifier("orc").setPath(SPRITES_PATH + "orc_animated_spritesheet.png")
                .loadEnemyAnimatedSheet(32, 64, enemyFrameCount, enemyStates);

        new SpriteBuilder().setIdentifier("dragon").setPath(SPRITES_PATH + "dragon_animated_spritesheet.png")
                .loadEnemyAnimatedSheet(64, 64, enemyFrameCount, enemyStates);

        new SpriteBuilder().setIdentifier("bomb_consumable").setPath(SPRITES_PATH + "bomb_consumable.png").load();

        final String[] bombStates = new String[]{
                "ticking", "explosion"
        };
        new SpriteBuilder().setIdentifier("bomb").setPath(SPRITES_PATH + "bomb_animated_spritesheet.png")
                .loadAnimatedSheet(128, 96, 19, 2, bombStates);

        new SpriteBuilder().setIdentifier("health_potion").setPath(SPRITES_PATH + "health_potion.png").load();
        new SpriteBuilder().setIdentifier("poison_potion").setPath(SPRITES_PATH + "poison_potion.png").load();
        new SpriteBuilder().setIdentifier("speed_potion").setPath(SPRITES_PATH + "speed_potion.png").load();
        new SpriteBuilder().setIdentifier("mystery_potion").setPath(SPRITES_PATH + "mystery_potion.png").load();
        new SpriteBuilder().setIdentifier("vision_potion").setPath(SPRITES_PATH + "vision_potion.png").load();

        new SpriteBuilder().setIdentifier("chest_closed").setPath(SPRITES_PATH + "chest_closed.png").load();
        new SpriteBuilder().setIdentifier("chest_open").setPath(SPRITES_PATH + "chest_open.png").load();
        new SpriteBuilder().setIdentifier("breakable_wall").setPath(SPRITES_PATH + "breakable_wall.png").load();
        new SpriteBuilder().setIdentifier("trap").setPath(SPRITES_PATH + "trap.png").load();
        new SpriteBuilder().setIdentifier("pillar").setPath(SPRITES_PATH + "pillar.png").load();
        new SpriteBuilder().setIdentifier("exit").setPath(SPRITES_PATH + "exit.png").load();

        new SpriteBuilder().setIdentifier("controls").setPath(SPRITES_PATH + "controls_animated_spritesheet.png")
                .loadAnimatedSheet(80, 360, 3, 2,
                        "normal", "combat");

        new SpriteBuilder().setIdentifier("overlay_background_left").setPath(SPRITES_PATH + "overlay_background_left.png").load();
        new SpriteBuilder().setIdentifier("overlay_background_right").setPath(SPRITES_PATH + "overlay_background_right.png")
                .loadAnimatedSheet(360, 360, 12, 1,
                        "normal");

        new SpriteBuilder().setIdentifier("resume_game_sprite").setPath(SPRITES_PATH + "resume_game_sprite.png").load();
        new SpriteBuilder().setIdentifier("save_game_sprite").setPath(SPRITES_PATH + "save_game_sprite.png").load();
        new SpriteBuilder().setIdentifier("main_menu_sprite").setPath(SPRITES_PATH + "main_menu_sprite.png").load();
        new SpriteBuilder().setIdentifier("quit_game_sprite").setPath(SPRITES_PATH + "quit_game_sprite.png").load();

        new SpriteBuilder().setIdentifier("ending_win").setPath(SPRITES_PATH + "ending_win.png").load();
        new SpriteBuilder().setIdentifier("ending_fake_win").setPath(SPRITES_PATH + "ending_fake_win.png").load();
        new SpriteBuilder().setIdentifier("ending_death_bomb").setPath(SPRITES_PATH + "ending_death_bomb.png").load();
        new SpriteBuilder().setIdentifier("ending_death_trap").setPath(SPRITES_PATH + "ending_death_trap.png").load();
        new SpriteBuilder().setIdentifier("ending_death_poison").setPath(SPRITES_PATH + "ending_death_poison.png").load();
        new SpriteBuilder().setIdentifier("ending_death_mystery").setPath(SPRITES_PATH + "ending_death_mystery.png").load();
        new SpriteBuilder().setIdentifier("ending_death_enemy").setPath(SPRITES_PATH + "ending_death_enemy.png").load();
    }

    /**
     * Gets the singleton instance of the SpriteManager.
     *
     * @return The singleton SpriteManager instance.
     */
    public static SpriteManager getInstance() {
        return INSTANCE;
    }

    /**
     * Gets an ImageIcon for the sprite with the given identifier.
     *
     * @param theIdentifier The unique identifier of the sprite.
     * @return An ImageIcon for the requested sprite.
     * @throws IllegalArgumentException if the sprite is not found.
     */
    public static ImageIcon getSprite(final String theIdentifier) {
        final Sprite sprite = Sprite.getSprite(theIdentifier);
        if (sprite == null) {
            throw new IllegalArgumentException("Sprite not found " + theIdentifier);
        }
        return sprite.getImageIcon();
    }

    /**
     * A helper class for building and loading sprites.
     */
    public static class SpriteBuilder {
        /**
         * The unique identifier for the sprite being built.
         */
        private String myIdentifier;

        /**
         * The file path of the sprite or sprite sheet.
         */
        private String myPath;

        /**
         * Sets the unique identifier for the sprite.
         *
         * @param theIdentifier The identifier.
         * @return This SpriteBuilder instance for chaining.
         */
        public SpriteBuilder setIdentifier(final String theIdentifier) {
            myIdentifier = theIdentifier;
            return this;
        }

        /**
         * Sets the file path of the sprite or sprite sheet.
         *
         * @param thePath The file path relative to the resources' directory.
         * @return This SpriteBuilder instance for chaining.
         */
        public SpriteBuilder setPath(final String thePath) {
            myPath = thePath;
            return this;
        }

        /**
         * Loads a single static sprite image.
         */
        public void load() {
            if (myIdentifier == null || myPath == null) {
                return;
            }
            Sprite.preloadSprite(myIdentifier, myPath);
        }

        /**
         * Loads a series of sprites from a single sheet, identifying them with a base identifier
         * and a character suffix (e.g., "sprite_a", "sprite_b").
         *
         * @param theSpriteWidth  The width of each sprite.
         * @param theSpriteHeight The height of each sprite.
         * @param theSpriteCount  The number of sprites to load.
         * @param theStartChar    The starting character for the identifier suffix.
         */
        public void loadFromSheetWithCharacters(final int theSpriteWidth, final int theSpriteHeight, final int theSpriteCount, final char theStartChar) {
            try (InputStream inputStream = getClass().getResourceAsStream(myPath)) {
                if (inputStream == null) {
                    throw new IOException("Resource not found " + myPath);
                }
                BufferedImage sheet = ImageIO.read(inputStream);

                for (int i = 0; i < theSpriteCount; i++) {
                    BufferedImage subImage = sheet.getSubimage(0, i * theSpriteHeight, theSpriteWidth, theSpriteHeight);
                    String spriteIdentifier;
                    if (theStartChar == 'A' && i < 26) {
                        spriteIdentifier = myIdentifier + "_" + (char) ('A' + i);
                    } else if (theStartChar == 'A') {
                        spriteIdentifier = myIdentifier + "_" + (char) ('a' + (i - 26));
                    } else {
                        spriteIdentifier = myIdentifier + "_" + (char) (theStartChar + i);
                    }
                    Sprite.preloadSpriteFromImage(spriteIdentifier, subImage);
                }
            } catch (IOException exception) {
                throw new RuntimeException("Failed to initialize spritesheet " + myPath, exception);
            }
        }

        /**
         * Loads an animated sprite sheet with multiple states and frames per state.
         *
         * @param theSpriteWidth      The width of each sprite frame.
         * @param theSpriteHeight     The height of each sprite frame.
         * @param theFrameCount       The number of frames in each animation state.
         * @param theStateCount       The number of animation states.
         * @param theStateIdentifiers An array of identifiers for each state.
         */
        public void loadAnimatedSheet(final int theSpriteWidth, final int theSpriteHeight, final int theFrameCount, final int theStateCount, final String... theStateIdentifiers) {
            if (theStateIdentifiers.length != theStateCount) {
                throw new IllegalArgumentException("Number of state identifiers must match state count");
            }
            try (InputStream inputStream = getClass().getResourceAsStream(myPath)) {
                if (inputStream == null) {
                    throw new IOException("Resource not found " + myPath);
                }
                BufferedImage sheet = ImageIO.read(inputStream);

                for (int row = 0; row < theStateCount; row++) {
                    for (int col = 0; col < theFrameCount; col++) {
                        BufferedImage subImage = sheet.getSubimage(col * theSpriteWidth, row * theSpriteHeight, theSpriteWidth, theSpriteHeight);
                        String spriteIdentifier = myIdentifier + "_" + theStateIdentifiers[row] + "_" + col;
                        Sprite.preloadSpriteFromImage(spriteIdentifier, subImage);
                    }
                }
            } catch (IOException exception) {
                throw new RuntimeException("Failed to initialize animated spritesheet " + myPath, exception);
            }
        }

        /**
         * Loads an animated sprite sheet specifically for a character (player or enemy).
         * This handles the specific layout of standing frames and walking frames.
         *
         * @param theSpriteWidth      The width of each sprite frame.
         * @param theSpriteHeight     The height of each sprite frame.
         * @param theFrameCount       The number of frames per walking animation.
         * @param theStateIdentifiers An array of identifiers for each state (standing and walking).
         */
        public void loadEnemyAnimatedSheet(final int theSpriteWidth, final int theSpriteHeight, final int theFrameCount, final String... theStateIdentifiers) {
            try (InputStream inputStream = getClass().getResourceAsStream(myPath)) {
                if (inputStream == null) {
                    throw new IOException("Resource not found " + myPath);
                }
                BufferedImage sheet = ImageIO.read(inputStream);

                // 4 directions: north, west, south, east (rows 0, 1, 2, 3)
                for (int direction = 0; direction < 4; direction++) {
                    // Standing frame (frame 0) for each direction
                    BufferedImage standingFrame = sheet.getSubimage(0, direction * theSpriteHeight, theSpriteWidth, theSpriteHeight);
                    String standingSpriteId = myIdentifier + "_" + theStateIdentifiers[direction] + "_0";
                    Sprite.preloadSpriteFromImage(standingSpriteId, standingFrame);

                    // Walking frames (frames 1-8) for each direction
                    for (int frame = 1; frame < theFrameCount; frame++) {
                        BufferedImage walkingFrame = sheet.getSubimage(frame * theSpriteWidth, direction * theSpriteHeight, theSpriteWidth, theSpriteHeight);
                        String walkingSpriteId = myIdentifier + "_" + theStateIdentifiers[direction + 4] + "_" + (frame - 1);
                        Sprite.preloadSpriteFromImage(walkingSpriteId, walkingFrame);
                    }
                }
            } catch (IOException exception) {
                throw new RuntimeException("Failed to initialize enemy animated spritesheet " + myPath, exception);
            }
        }
    }
}