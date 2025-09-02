package drippyspelunk.view.title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A panel containing the buttons for the title screen.
 * This class organizes and styles the main menu buttons, handling their appearance
 * and click events. When a button is clicked, its background color changes to indicate it's selected.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.2
 */
public class TitleButtonPanel extends JPanel {

    /**
     * The default color for unselected buttons.
     */
    private static final Color DEFAULT_BUTTON_COLOR = Color.WHITE;
    /**
     * The color for the selected button.
     */
    private static final Color SELECTED_BUTTON_COLOR = Color.YELLOW;
    /**
     * The base font size for button text.
     */
    private static final int BASE_FONT_SIZE = 14;
    /**
     * The base width of the buttons.
     */
    private static final int BASE_BUTTON_WIDTH = 160;
    /**
     * The base height of the buttons.
     */
    private static final int BASE_BUTTON_HEIGHT = 40;
    /**
     * The vertical spacing between buttons.
     */
    private static final int BUTTON_SPACING = 10;
    /**
     * The vertical padding at the top and bottom of the panel.
     */
    private static final int VERTICAL_PADDING = 20;
    /**
     * Constant strings for button text.
     */
    private static final String NEW_GAME_TEXT = "New Game";
    private static final String LOAD_GAME_TEXT = "Load Game";
    private static final String SETTINGS_TEXT = "Settings";
    private static final String CREDITS_TEXT = "Credits";
    private static final String CLOSE_APPLICATION_TEXT = "Close Application";
    /**
     * The name of the font used for button text.
     */
    private static final String FONT_NAME = "SansSerif";
    /**
     * The GUI scaling factor.
     */
    private final int myGuiScale;
    /**
     * The New Game button.
     */
    private final JButton myNewGameButton;
    /**
     * The Load Game button.
     */
    private final JButton myLoadGameButton;
    /**
     * The Settings button.
     */
    private final JButton mySettingsButton;
    /**
     * The Credits button.
     */
    private final JButton myCreditsButton;
    /**
     * The Close Application button.
     */
    private final JButton myCloseApplicationButton;
    /**
     * The last button that was pressed. Used to manage the selected button's color.
     */
    private JButton myLastPressedButton;

    /**
     * Constructs a TitleButtonPanel.
     *
     * @param theLogicalWidth  The logical width of the panel.
     * @param theLogicalHeight The logical height of the panel.
     * @param theGuiScale      The GUI scaling factor.
     */
    public TitleButtonPanel(final int theLogicalWidth,
                            final int theLogicalHeight,
                            final int theGuiScale) {

        myGuiScale = theGuiScale;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(theLogicalWidth, theLogicalHeight));
        setBackground(Color.PINK.darker());

        myNewGameButton = createStyledButton(NEW_GAME_TEXT);
        myLoadGameButton = createStyledButton(LOAD_GAME_TEXT);
        mySettingsButton = createStyledButton(SETTINGS_TEXT);
        myCreditsButton = createStyledButton(CREDITS_TEXT);
        myCloseApplicationButton = createStyledButton(CLOSE_APPLICATION_TEXT);

        myLastPressedButton = null;

        final int buttonSpacing = BUTTON_SPACING * myGuiScale;
        final int verticalPadding = VERTICAL_PADDING * myGuiScale;

        add(Box.createVerticalStrut(verticalPadding));
        add(myNewGameButton);
        add(Box.createVerticalStrut(buttonSpacing));
        add(myLoadGameButton);
        add(Box.createVerticalStrut(buttonSpacing));
        add(mySettingsButton);
        add(Box.createVerticalStrut(buttonSpacing));
        add(myCreditsButton);
        add(Box.createVerticalStrut(buttonSpacing));
        add(myCloseApplicationButton);
        add(Box.createVerticalStrut(verticalPadding));
    }

    /**
     * Creates a styled JButton with consistent appearance and behavior.
     *
     * @param theText The text to display on the button.
     * @return A styled JButton instance.
     */
    private JButton createStyledButton(final String theText) {
        final JButton button = new JButton(theText);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.BLACK);
        button.setBackground(DEFAULT_BUTTON_COLOR);
        button.setFocusPainted(false);

        button.addActionListener(theEvent -> {
            if (myLastPressedButton != null) {
                myLastPressedButton.setBackground(DEFAULT_BUTTON_COLOR);
            }
            button.setBackground(SELECTED_BUTTON_COLOR);
            myLastPressedButton = button;
        });

        button.setFont(new Font(FONT_NAME, Font.BOLD, BASE_FONT_SIZE * myGuiScale));
        final Dimension buttonSize = new Dimension(BASE_BUTTON_WIDTH * myGuiScale, BASE_BUTTON_HEIGHT * myGuiScale);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);

        return button;
    }

    /**
     * Adds an ActionListener to all interactive buttons on the panel.
     *
     * @param theController The ActionListener to be added.
     */
    public void setActionListener(final ActionListener theController) {
        myNewGameButton.addActionListener(theController);
        myLoadGameButton.addActionListener(theController);
        mySettingsButton.addActionListener(theController);
        myCreditsButton.addActionListener(theController);
        myCloseApplicationButton.addActionListener(theController);
    }

    /**
     * Removes an ActionListener from all interactive buttons on the panel.
     *
     * @param theListener The ActionListener to be removed.
     */
    public void removeActionListener(final ActionListener theListener) {
        myNewGameButton.removeActionListener(theListener);
        myLoadGameButton.removeActionListener(theListener);
        mySettingsButton.removeActionListener(theListener);
        myCreditsButton.removeActionListener(theListener);
        myCloseApplicationButton.removeActionListener(theListener);
    }
}