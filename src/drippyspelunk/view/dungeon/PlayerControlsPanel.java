package drippyspelunk.view.dungeon;

import drippyspelunk.controller.sprite.SpriteManager;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Panel to display pillar objective tracking and animated controls based on game state.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.4
 */
public class PlayerControlsPanel extends JPanel implements PropertyChangeListener {

    /**
     * The base width of the control sprite.
     */
    private static final int SPRITE_WIDTH = 80;
    /**
     * The base height of the control sprite.
     */
    private static final int SPRITE_HEIGHT = 360;
    /**
     * The total number of pillars to be collected in the game.
     */
    private static final int TOTAL_PILLARS = 4;

    // Text styling constants
    /**
     * The font size for the objective title.
     */
    private static final int OBJECTIVE_FONT_SIZE = 8;
    /**
     * The font size for the pillars collected counter.
     */
    private static final int PILLARS_FONT_SIZE = 9;
    /**
     * The margin for text at the top of the panel.
     */
    private static final int TEXT_MARGIN = 5;
    /**
     * The vertical spacing between lines of text.
     */
    private static final int LINE_SPACING = 12;
    /**
     * The height of the reserved area for text.
     */
    private static final int TEXT_AREA_HEIGHT = 80;

    /**
     * The dungeon game logic model.
     */
    private final DungeonCrawlerLogic myLogic;
    /**
     * The GUI scaling factor.
     */
    private final int myGuiScale;

    /**
     * The current index of the animation frame for the control sprite.
     */
    private int myAnimationFrameIndex = 0;
    /**
     * The number of pillars remaining to be collected.
     */
    private int myPillarsCollected;

    /**
     * Constructs the PlayerControlsPanel.
     *
     * @param theWidth    The logical width of the panel.
     * @param theHeight   The logical height of the panel.
     * @param theLogic    The dungeon game logic.
     * @param theGuiScale The GUI scaling factor.
     */
    public PlayerControlsPanel(final int theWidth, final int theHeight, final DungeonCrawlerLogic theLogic, final int theGuiScale) {
        myLogic = theLogic;
        myGuiScale = theGuiScale;
        myPillarsCollected = TOTAL_PILLARS; // Start with 4 pillars to collect

        setPreferredSize(new Dimension(theWidth, theHeight));
        setBackground(Color.DARK_GRAY);

        // Listen for pillar collection events from the model
        myLogic.addPropertyChangeListener(DungeonCrawlerLogic.PILLARS_COLLECTED_PROPERTY, this);
    }

    /**
     * Sets the current animation frame index for the control sprite.
     *
     * @param theFrame The frame index.
     */
    public void setAnimationFrame(final int theFrame) {
        myAnimationFrameIndex = theFrame;
    }

    /**
     * Overrides the paintComponent method to draw the panel's contents.
     *
     * @param theGraphics The Graphics object to draw on.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Draw objective text at the top
        drawObjectiveText(g2d);

        // Draw controls sprite below the text
        drawControlsSprite(g2d);

        g2d.dispose();
    }

    /**
     * Draws the objective text, including the title, description, and pillar counter.
     *
     * @param theGraphics The Graphics2D object to draw on.
     */
    private void drawObjectiveText(final Graphics2D theGraphics) {
        theGraphics.setColor(Color.WHITE);

        // Objective title
        final Font objectiveFont = new Font("SansSerif", Font.BOLD, OBJECTIVE_FONT_SIZE * myGuiScale);
        theGraphics.setFont(objectiveFont);
        final String objectiveText = "Objective:";
        final FontMetrics objectiveFm = theGraphics.getFontMetrics();
        final int objectiveX = (getWidth() - objectiveFm.stringWidth(objectiveText)) / 2;
        final int objectiveY = TEXT_MARGIN * myGuiScale + objectiveFm.getAscent();
        theGraphics.drawString(objectiveText, objectiveX, objectiveY);

        // Objective description - word wrapped
        final Font descFont = new Font("SansSerif", Font.PLAIN, (OBJECTIVE_FONT_SIZE - 1) * myGuiScale);
        theGraphics.setFont(descFont);
        theGraphics.setColor(Color.LIGHT_GRAY);

        final String[] descLines = {"Find all 4 pillars", "and escape!"};
        final FontMetrics descFm = theGraphics.getFontMetrics();
        int descY = objectiveY + (LINE_SPACING * myGuiScale);

        for (String line : descLines) {
            final int descX = (getWidth() - descFm.stringWidth(line)) / 2;
            theGraphics.drawString(line, descX, descY);
            descY += descFm.getHeight();
        }

        // Pillars found counter
        final Font pillarsFont = new Font("SansSerif", Font.BOLD, PILLARS_FONT_SIZE * myGuiScale);
        theGraphics.setFont(pillarsFont);
        final int pillarsFound = TOTAL_PILLARS - myPillarsCollected;
        final String pillarsText = "Pillars Found: " + pillarsFound + "/" + TOTAL_PILLARS;

        // Color based on progress
        if (pillarsFound == TOTAL_PILLARS) {
            theGraphics.setColor(Color.GREEN);
        } else if (pillarsFound > 0) {
            theGraphics.setColor(Color.YELLOW);
        } else {
            theGraphics.setColor(Color.WHITE);
        }

        final FontMetrics pillarsFm = theGraphics.getFontMetrics();
        final int pillarsX = (getWidth() - pillarsFm.stringWidth(pillarsText)) / 2;
        final int pillarsY = descY + (LINE_SPACING * myGuiScale);
        theGraphics.drawString(pillarsText, pillarsX, pillarsY);
    }

    /**
     * Draws the animated controls sprite.
     * The sprite changes based on whether the player is in combat or not.
     *
     * @param theGraphics The Graphics2D object to draw on.
     */
    private void drawControlsSprite(final Graphics2D theGraphics) {
        final String spriteIdentifier = myLogic.isInCombat()
                ? "controls_combat_" + myAnimationFrameIndex
                : "controls_normal_" + myAnimationFrameIndex;

        final ImageIcon spriteIcon = SpriteManager.getSprite(spriteIdentifier);

        final int scaledWidth = SPRITE_WIDTH * myGuiScale;
        final int scaledHeight = SPRITE_HEIGHT * myGuiScale;
        final int controlsY = TEXT_AREA_HEIGHT * myGuiScale;
        if (spriteIcon != null) {
            // Position controls below the text area
            theGraphics.drawImage(spriteIcon.getImage(), 0, controlsY, scaledWidth, scaledHeight, this);
        } else {
            // Fallback rectangle below a text
            theGraphics.setColor(Color.MAGENTA);
            theGraphics.fillRect(0, controlsY, scaledWidth, scaledHeight);
        }
    }

    /**
     * Handles property changes from the DungeonCrawlerLogic model.
     * This method is called when the number of pillars collected changes.
     *
     * @param theEvent The property change event.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (DungeonCrawlerLogic.PILLARS_COLLECTED_PROPERTY.equals(theEvent.getPropertyName())) {
            myPillarsCollected = (Integer) theEvent.getNewValue();
        }
    }
}