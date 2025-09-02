package drippyspelunk.view.dungeon;

import drippyspelunk.model.dungeon.DungeonCrawlerLogic;

import javax.swing.*;
import java.awt.*;

/**
 * The view of the dungeon crawler. This is a layered panel for drawing different visual elements
 * on top of each other. It coordinates different visual layers, delegating game content drawing
 * to a specialized content panel.
 *
 * @author Devin Arroyo
 * @version 1.11
 */
public class DungeonScreen extends JLayeredPane {

    /**
     * The amount to draw walls off-screen to create a seamless view.
     */
    private static final int WALL_OFFSCREEN_AMOUNT = 28;

    /**
     * The panel that renders the main game content, such as rooms, hallways, and entities.
     */
    private final DungeonContentPanel myGameContentPanel;
    /**
     * The panel for general overlays, like the pause menu.
     */
    private final DungeonOverlayPanel myOverlayPanel;
    /**
     * The panel for the combat-specific overlay.
     */
    private final CombatOverlay myCombatOverlay;

    /**
     * Constructs the DungeonScreen.
     *
     * @param theLogic         The main dungeon crawler game logic.
     * @param theLogicalWidth  The logical width of the game area before scaling.
     * @param theLogicalHeight The logical height of the game area before scaling.
     * @param theGuiScale      The GUI scaling factor.
     */
    public DungeonScreen(final DungeonCrawlerLogic theLogic, final int theLogicalWidth, final int theLogicalHeight, final int theGuiScale) {
        final int scaledWidth = theLogicalWidth * theGuiScale;
        final int scaledHeight = theLogicalHeight * theGuiScale;

        setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        setOpaque(true);
        setBackground(Color.PINK.darker());

        // Initialize and add the main game content panel
        myGameContentPanel = new DungeonContentPanel(theLogic, theGuiScale, WALL_OFFSCREEN_AMOUNT);
        myGameContentPanel.setBounds(0, 0, scaledWidth, scaledHeight);
        add(myGameContentPanel, JLayeredPane.DEFAULT_LAYER);

        // Initialize and add the combat overlay, initially hidden
        myCombatOverlay = new CombatOverlay(theGuiScale);
        myCombatOverlay.setBounds(0, 0, scaledWidth, scaledHeight);
        myCombatOverlay.setVisible(false);
        add(myCombatOverlay, JLayeredPane.PALETTE_LAYER);

        // Initialize and add the general overlay panel, initially hidden
        myOverlayPanel = new DungeonOverlayPanel(theLogicalWidth, theLogicalHeight, theGuiScale);
        myOverlayPanel.setBounds(0, 0, scaledWidth, scaledHeight);
        myOverlayPanel.setVisible(false);
        add(myOverlayPanel, JLayeredPane.MODAL_LAYER);
    }

    /**
     * Toggles the visibility of the general overlay panel.
     */
    public void toggleOverlayVisibility() {
        myOverlayPanel.setVisible(!myOverlayPanel.isVisible());
    }

    /**
     * Gets the main content panel for rendering game elements.
     *
     * @return The DungeonContentPanel instance.
     */
    public DungeonContentPanel getDungeonContentPanel() {
        return myGameContentPanel;
    }

    /**
     * Gets the general overlay panel.
     *
     * @return The DungeonOverlayPanel instance.
     */
    public DungeonOverlayPanel getOverlayPanel() {
        return myOverlayPanel;
    }

    /**
     * Gets the combat overlay panel.
     *
     * @return The CombatOverlay instance.
     */
    public CombatOverlay getCombatOverlay() {
        return myCombatOverlay;
    }
}