package drippyspelunk.view.title;

import drippyspelunk.model.menu.TitleScreenLogic;
import drippyspelunk.view.GUIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main view for the title screen.
 * This panel serves as the primary container for all title screen components,
 * including the button panel, expanded menu, and the new game panel.
 * It uses a BorderLayout to arrange its subpanels.
 *
 * @author Devin Arroyo
 * @version 1.5
 */
public class TitleScreenView extends JPanel {

    /**
     * The ratio of the total width allocated to the button panel.
     */
    private static final double BUTTON_PANEL_WIDTH_RATIO = 2.0 / 7.0;
    /**
     * The ratio of the total width allocated to the expanded menu panel.
     */
    private static final double EXPANDED_MENU_WIDTH_RATIO = 5.0 / 7.0;
    /**
     * The panel containing the main menu buttons.
     */
    private final TitleButtonPanel myButtonPanel;
    /**
     * The panel that displays different menus (e.g., New Game, Settings).
     */
    private final TitleExpandedMenu myExpandedMenu;
    /**
     * The panel for new game character selection.
     */
    private final NewGamePanel myNewGamePanel;

    /**
     * Constructs a TitleScreenView with specified logical dimensions and GUI scale.
     *
     * @param theLogic         The title screen logic model.
     * @param theLogicalWidth  The logical width of the window.
     * @param theLogicalHeight The logical height of the window.
     * @param theGuiScale      The scaling factor for GUI components.
     */
    public TitleScreenView(final TitleScreenLogic theLogic,
                           final int theLogicalWidth,
                           final int theLogicalHeight,
                           final int theGuiScale) {

        setLayout(new BorderLayout());
        setBackground(GUIConstants.BACKGROUND_COLOR);

        final int scaledWidth = theLogicalWidth * theGuiScale;
        final int scaledHeight = theLogicalHeight * theGuiScale;

        setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        setMaximumSize(new Dimension(scaledWidth, scaledHeight));

        final JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        final int buttonPanelWidth = (int) (scaledWidth * BUTTON_PANEL_WIDTH_RATIO);
        final int expandedMenuWidth = (int) (scaledWidth * EXPANDED_MENU_WIDTH_RATIO);

        myButtonPanel = new TitleButtonPanel(buttonPanelWidth, scaledHeight, theGuiScale);
        myExpandedMenu = new TitleExpandedMenu(expandedMenuWidth,
                scaledHeight,
                theGuiScale,
                theLogic.getCurrentSettings(),
                theLogic);

        myNewGamePanel = (NewGamePanel) myExpandedMenu.getComponent(1);

        mainContentPanel.add(myButtonPanel, BorderLayout.WEST);
        mainContentPanel.add(myExpandedMenu, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    /**
     * Custom painting method for the panel.
     *
     * @param theGraphics The Graphics object to protect.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
    }

    /**
     * Renders the current state of the view.
     *
     * @param theCurrentTime The current game time.
     */
    public void render(final long theCurrentTime) {
        repaint();
    }

    /**
     * Displays a specific panel within the expanded menu.
     *
     * @param thePanelType The type of panel to show.
     */
    public void showPanel(final TitleExpandedMenu.PanelType thePanelType) {
        myExpandedMenu.getExpandedMenuPanel(thePanelType);
    }

    /**
     * Gets the expanded menu panel.
     *
     * @return The expanded menu panel.
     */
    public TitleExpandedMenu getExpandedMenu() {
        return myExpandedMenu;
    }

    /**
     * Gets the new game panel.
     *
     * @return The new game panel.
     */
    public NewGamePanel getNewGamePanel() {
        return myNewGamePanel;
    }

    /**
     * Sets the action listener for all interactive components.
     *
     * @param theController The action listener to be added.
     */
    public void setActionListener(final ActionListener theController) {
        myButtonPanel.setActionListener(theController);
        myExpandedMenu.setActionListener(theController);
    }

    /**
     * Removes the action listener from all interactive components.
     *
     * @param theController The action listener to be removed.
     */
    public void removeActionListener(final ActionListener theController) {
        myButtonPanel.removeActionListener(theController);
        myExpandedMenu.removeActionListener(theController);
    }
}