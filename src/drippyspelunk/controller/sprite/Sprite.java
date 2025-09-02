package drippyspelunk.controller.sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class to hold and manage a single sprite.
 * It provides methods for preloading sprites from file paths or existing
 * BufferedImage objects and retrieving them by a unique identifier.
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public final class Sprite {

    /**
     * A thread-safe map to store all preloaded sprites,
     * with their unique identifiers as keys.
     */
    private static final Map<String, Sprite> mySprites = new ConcurrentHashMap<>();

    /**
     * The graphical representation of the sprite.
     */
    private final ImageIcon myImage;

    /**
     * Private constructor to create a Sprite from a file path.
     *
     * @param theFilepath The path to the image file.
     * @throws RuntimeException if the image cannot be loaded.
     */
    private Sprite(final String theFilepath) {
        try (InputStream inputStream = getClass().getResourceAsStream(theFilepath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + theFilepath);
            }
            Image image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("Failed to read image from " + theFilepath);
            }
            myImage = new ImageIcon(image);

        } catch (IOException exception) {
            throw new RuntimeException("Failed to initialize sprite from " + theFilepath, exception);
        }
    }

    /**
     * Private constructor to create a Sprite from a BufferedImage.
     *
     * @param theImage The BufferedImage to create the sprite from.
     * @throws IllegalArgumentException if the BufferedImage is null.
     */
    private Sprite(final BufferedImage theImage) {
        if (theImage == null) {
            throw new IllegalArgumentException("BufferedImage cannot be null.");
        }
        myImage = new ImageIcon(theImage);
    }

    /**
     * Preloads a sprite into the manager from a file path.
     * If a sprite with the same identifier already exists, it will be overwritten.
     *
     * @param theIdentifier The unique identifier for the sprite.
     * @param theFilepath   The file path to the sprite image.
     */
    public static synchronized void preloadSprite(final String theIdentifier, final String theFilepath) {
        try {
            final Sprite sprite = new Sprite(theFilepath);
            mySprites.put(theIdentifier, sprite);
        } catch (final RuntimeException ignored) {
        }
    }

    /**
     * Preloads a sprite into the manager from a BufferedImage.
     * If a sprite with the same identifier already exists, it will be overwritten.
     *
     * @param theIdentifier The unique identifier for the sprite.
     * @param theImage      The BufferedImage of the sprite.
     */
    public static synchronized void preloadSpriteFromImage(final String theIdentifier, final BufferedImage theImage) {
        try {
            final Sprite sprite = new Sprite(theImage);
            mySprites.put(theIdentifier, sprite);
        } catch (final RuntimeException ignored) {
        }
    }

    /**
     * Retrieves a preloaded Sprite from the manager using its identifier.
     *
     * @param theIdentifier The unique identifier of the sprite.
     * @return The Sprite object, or null if not found.
     */
    public static Sprite getSprite(final String theIdentifier) {
        return mySprites.get(theIdentifier);
    }

    /**
     * Clears all preloaded sprites from memory.
     */
    public static void cleanupSprites() {
        mySprites.clear();
    }

    /**
     * Gets the ImageIcon representation of this sprite.
     *
     * @return The ImageIcon.
     */
    public ImageIcon getImageIcon() {
        return myImage;
    }
}