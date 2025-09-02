package drippyspelunk.model.dungeon.entity.asset;

import drippyspelunk.model.dungeon.entity.GameObject;

import java.awt.*;

/**
 * Represents an active bomb in the dungeon, managing its fuse, explosion, and animation state.
 *
 * @author Devin Arroyo
 * @version 1.1
 */
public class ActiveBomb extends GameObject {
    /**
     * The initial duration of the bomb's fuse in game ticks.
     */
    private static final int FUSE_DURATION = 185;
    /**
     * The duration of the explosion animation in game ticks.
     */
    private static final int EXPLOSION_DURATION = 32;
    /**
     * The radius of the explosion's bounding box.
     */
    private static final int EXPLOSION_RADIUS = 96;

    /**
     * The remaining time on the bomb's fuse.
     */
    private int myFuseTimer;
    /**
     * The remaining time for the explosion animation.
     */
    private int myExplosionTimer;
    /**
     * A flag indicating if the bomb has exploded.
     */
    private boolean myExploded;

    /**
     * Constructs a new ActiveBomb object at a specific position.
     * The bomb is initialized with a fuse timer and is not yet exploded.
     *
     * @param theX The x-coordinate where the bomb is placed.
     * @param theY The y-coordinate where the bomb is placed.
     */
    public ActiveBomb(final int theX, final int theY) {
        super(theX, theY, 32, 32);
        myFuseTimer = FUSE_DURATION;
        myExplosionTimer = EXPLOSION_DURATION;
        myExploded = false;
    }

    /**
     * Updates the bomb's state, decrementing the fuse timer.
     * When the fuse timer reaches zero, the bomb explodes.
     */
    @Override
    public void update() {
        if (!myExploded) {
            myFuseTimer--;
            if (myFuseTimer <= 0) {
                myExploded = true;
            }
        }
    }

    /**
     * Checks if the bomb has exploded.
     *
     * @return true if the bomb has exploded, false otherwise.
     */
    public boolean getMyExploded() {
        return myExploded;
    }

    /**
     * Checks if the bomb's explosion animation is finished.
     *
     * @return true, if the bomb has exploded and its explosion timer has run out, false otherwise.
     */
    public boolean isFinished() {
        return myExploded && myExplosionTimer <= 0;
    }

    /**
     * Decrements the explosion timer if the bomb has exploded.
     */
    public void tickExplosion() {
        if (myExploded) {
            myExplosionTimer--;
        }
    }

    /**
     * Gets the bounding rectangle for the explosion.
     * The explosion is a square centered on the bomb's position.
     *
     * @return A Rectangle representing the explosion's area, or an empty Rectangle if the bomb hasn't exploded yet.
     */
    public Rectangle getExplosionBounds() {
        if (myExploded) {
            final int explosionX = getX() - (EXPLOSION_RADIUS / 2);
            final int explosionY = getY() - (EXPLOSION_RADIUS / 2);
            return new Rectangle(explosionX, explosionY, EXPLOSION_RADIUS, EXPLOSION_RADIUS);
        }
        return new Rectangle();
    }
}