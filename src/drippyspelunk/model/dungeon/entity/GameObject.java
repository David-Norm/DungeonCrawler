package drippyspelunk.model.dungeon.entity;

import java.awt.*;

/**
 * Base object for all Dungeon Crawler instances.
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public abstract class GameObject {
    /**
     * The x-coordinate of the game object.
     */
    protected int myX;
    /**
     * The y-coordinate of the game object.
     */
    protected int myY;
    /**
     * The width of the game object.
     */
    protected int myWidth;
    /**
     * The height of the game object.
     */
    protected int myHeight;

    /**
     * Constructs a GameObject with specified position and dimensions.
     *
     * @param theX      The x-coordinate of the object.
     * @param theY      The y-coordinate of the object.
     * @param theWidth  The width of the object.
     * @param theHeight The height of the object.
     */
    public GameObject(final int theX, final int theY, final int theWidth, final int theHeight) {
        myX = theX;
        myY = theY;
        myWidth = theWidth;
        myHeight = theHeight;
    }

    /**
     * Gets the x-coordinate of the object.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return myX;
    }

    /**
     * Sets the x-coordinate of the object.
     *
     * @param theX The new x-coordinate.
     */
    public void setX(final int theX) {
        myX = theX;
    }

    /**
     * Gets the y-coordinate of the object.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return myY;
    }

    /**
     * Sets the y-coordinate of the object.
     *
     * @param theY The new y-coordinate.
     */
    public void setY(final int theY) {
        myY = theY;
    }

    /**
     * Gets the width of the object.
     *
     * @return The width.
     */
    public int getWidth() {
        return myWidth;
    }

    /**
     * Gets the height of the object.
     *
     * @return The height.
     */
    public int getHeight() {
        return myHeight;
    }

    /**
     * Returns a Rectangle object representing the bounds of the GameObject.
     *
     * @return The bounding Rectangle.
     */
    public Rectangle getBounds() {
        return new Rectangle(myX, myY, myWidth, myHeight);
    }

    /**
     * Abstract method to update the state of the GameObject.
     */
    public abstract void update();
}