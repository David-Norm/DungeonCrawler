package drippyspelunk.view.title;

import drippyspelunk.model.menu.SettingsData;
import drippyspelunk.model.menu.TitleScreenLogic;
import drippyspelunk.view.GUIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A panel that uses a CardLayout to show different panels.
 * This class serves as a dynamic container for various menu subpanels
 * on the title screen, such as New Game, Settings, and Credits.
 *
 * @author Devin Arroyo
 * @version 1.2
 */
public class TitleExpandedMenu extends JPanel {

    /**
     * Constant string identifiers for the CardLayout.
     */
    private static final String EMPTY = "EMPTY";
    private static final String NEW_GAME = "NEW_GAME";
    private static final String LOAD_GAME = "LOAD_GAME";
    private static final String SETTINGS = "SETTINGS";
    private static final String CREDITS = "CREDITS";
    private static final String CLOSE_APPLICATION = "CLOSE_APPLICATION";
    /**
     * The font size for styled buttons.
     */
    private static final int STYLED_BUTTON_FONT_SIZE = 14;
    /**
     * The width of styled buttons.
     */
    private static final int STYLED_BUTTON_WIDTH = 160;
    /**
     * The height of styled buttons.
     */
    private static final int STYLED_BUTTON_HEIGHT = 40;
    /**
     * The CardLayout manager for this panel.
     */
    private final CardLayout myCardLayout;
    /**
     * The settings panel.
     */
    private final SettingsPanel mySettingsPanel;
    /**
     * The GUI scaling factor.
     */
    private final int myGuiScale;
    /**
     * The button to start a new game.
     */
    private final JButton myNewGameStartButton;
    /**
     * The button to apply settings changes.
     */
    private final JButton mySettingsApplyButton;
    /**
     * The 'Yes' button for closing the application.
     */
    private final JButton myCloseAppYesButton;
    /**
     * The 'No' button for closing the application.
     */
    private final JButton myCloseAppNoButton;

    /**
     * Constructs a TitleExpandedMenu.
     *
     * @param theLogicalWidth    The logical width of the panel.
     * @param theLogicalHeight   The logical height of the panel.
     * @param theGuiScale        The GUI scaling factor.
     * @param theInitialSettings The initial settings data to populate the settings panel.
     * @param theLogic           The title screen logic instance.
     */
    public TitleExpandedMenu(final int theLogicalWidth,
                             final int theLogicalHeight,
                             final int theGuiScale,
                             final SettingsData theInitialSettings,
                             final TitleScreenLogic theLogic) {

        myGuiScale = theGuiScale;
        myCardLayout = new CardLayout();
        setLayout(myCardLayout);
        setBackground(GUIConstants.BACKGROUND_COLOR);
        setPreferredSize(new Dimension(theLogicalWidth, theLogicalHeight));
        setMaximumSize(new Dimension(theLogicalWidth, theLogicalHeight));

        myNewGameStartButton = createStyledButton("Start New Game");
        mySettingsApplyButton = createStyledButton("Apply");
        myCloseAppYesButton = createStyledButton("Yes");
        myCloseAppNoButton = createStyledButton("No");

        final EmptyPanel myEmptyPanel = new EmptyPanel();
        add(myEmptyPanel, EMPTY);
        add(new NewGamePanel(theGuiScale, theLogic, myNewGameStartButton), NEW_GAME);
        add(new LoadGamePanel(theGuiScale), LOAD_GAME);
        mySettingsPanel = new SettingsPanel(theInitialSettings, mySettingsApplyButton);
        add(mySettingsPanel, SETTINGS);
        add(new CreditsPanel(theGuiScale), CREDITS);
        add(new CloseApplicationPanel(theGuiScale, myCloseAppYesButton, myCloseAppNoButton), CLOSE_APPLICATION);

        getExpandedMenuPanel(PanelType.EMPTY);
    }

    /**
     * Creates a styled JButton with consistent look and feel.
     *
     * @param theText The text to display on the button.
     * @return A styled JButton instance.
     */
    private JButton createStyledButton(final String theText) {
        final JButton button = new JButton(theText);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);

        button.setFont(new Font("SansSerif", Font.BOLD, STYLED_BUTTON_FONT_SIZE * myGuiScale));
        final Dimension buttonSize = new Dimension(STYLED_BUTTON_WIDTH * myGuiScale, STYLED_BUTTON_HEIGHT * myGuiScale);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);

        return button;
    }

    /**
     * Adds an ActionListener to all interactive buttons.
     *
     * @param theListener The ActionListener to be added.
     */
    public void setActionListener(final ActionListener theListener) {
        myNewGameStartButton.addActionListener(theListener);
        mySettingsApplyButton.addActionListener(theListener);
        myCloseAppYesButton.addActionListener(theListener);
        myCloseAppNoButton.addActionListener(theListener);
    }

    /**
     * Removes an ActionListener from all interactive buttons.
     *
     * @param theListener The ActionListener to be removed.
     */
    public void removeActionListener(final ActionListener theListener) {
        myNewGameStartButton.removeActionListener(theListener);
        mySettingsApplyButton.removeActionListener(theListener);
        myCloseAppYesButton.removeActionListener(theListener);
        myCloseAppNoButton.removeActionListener(theListener);
    }

    /**
     * Switches the currently visible panel in the CardLayout.
     *
     * @param thePanelType The type of panel to display.
     */
    public void getExpandedMenuPanel(final PanelType thePanelType) {
        myCardLayout.show(this, thePanelType.name());
    }

    /**
     * Gets the settings panel instance.
     *
     * @return The SettingsPanel object.
     */
    public SettingsPanel getSettingsPanel() {
        return mySettingsPanel;
    }

    /**
     * An enumeration representing the different types of panels that can be displayed.
     */
    public enum PanelType {
        EMPTY,
        NEW_GAME,
        LOAD_GAME,
        SETTINGS,
        CREDITS,
        CLOSE_APPLICATION
    }
}