package drippyspelunk.view.gameover;

import drippyspelunk.controller.sprite.SpriteManager;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic.GameEndingType;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The view for the game over screen, which displays a single unique background image based on the ending type.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.2
 */
public class GameOverView extends JPanel {

    /**
     * The logical width of the game screen before scaling.
     */
    private final int myLogicalWidth;
    /**
     * The logical height of the game screen before scaling.
     */
    private final int myLogicalHeight;
    /**
     * The GUI scaling factor.
     */
    private final int myGuiScale;
    /**
     * A map to associate GameEndingType enums with their corresponding sprite identifiers.
     */
    private final Map<GameEndingType, String> myImageIdentifiers;
    /**
     * The image to be displayed for the game ending.
     */
    private Image myEndingImage;

    /**
     * Constructs the GameOverView.
     *
     * @param theLogicalWidth  The logical width of the game screen.
     * @param theLogicalHeight The logical height of the game screen.
     * @param theGuiScale      The GUI scaling factor.
     * @param theEndingType    The type of game ending that occurred.
     */
    public GameOverView(final int theLogicalWidth, final int theLogicalHeight, final int theGuiScale, final GameEndingType theEndingType) {
        myLogicalWidth = theLogicalWidth;
        myLogicalHeight = theLogicalHeight;
        myGuiScale = theGuiScale;

        myImageIdentifiers = new HashMap<>();
        myImageIdentifiers.put(GameEndingType.WIN, "ending_win");
        myImageIdentifiers.put(GameEndingType.FAKE_WIN, "ending_fake_win");
        myImageIdentifiers.put(GameEndingType.BOMB, "ending_death_bomb");
        myImageIdentifiers.put(GameEndingType.TRAP, "ending_death_trap");
        myImageIdentifiers.put(GameEndingType.POISON, "ending_death_poison");
        myImageIdentifiers.put(GameEndingType.MYSTERY_POTION, "ending_death_mystery");
        myImageIdentifiers.put(GameEndingType.ENEMY, "ending_death_enemy");

        setPreferredSize(new Dimension(theLogicalWidth * theGuiScale, theLogicalHeight * theGuiScale));
        loadEndingImage(theEndingType);
    }

    /**
     * Loads the appropriate ending image based on the game ending type.
     *
     * @param theEndingType The type of game ending.
     */
    private void loadEndingImage(final GameEndingType theEndingType) {
        final String identifier = myImageIdentifiers.get(theEndingType);
        if (identifier != null) {
            final ImageIcon spriteIcon = SpriteManager.getSprite(identifier);
            if (spriteIcon != null) {
                myEndingImage = spriteIcon.getImage();
            } else {
                myEndingImage = null;
            }
        } else {
            myEndingImage = null;
        }
    }

    /**
     * Requests a repaint of the panel to render the image.
     */
    public void render() {
        repaint();
    }

    /**
     * Paints the component, drawing the loaded ending image scaled to fit the panel.
     *
     * @param theGraphics The Graphics object to protect.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2 = (Graphics2D) theGraphics.create();

        if (myEndingImage != null) {
            final int scaledWidth = myLogicalWidth * myGuiScale;
            final int scaledHeight = myLogicalHeight * myGuiScale;
            g2.drawImage(myEndingImage, 0, 0, scaledWidth, scaledHeight, this);
        }

        g2.dispose();
    }
}