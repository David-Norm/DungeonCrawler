package drippyspelunk.view.dungeon;

import drippyspelunk.controller.sprite.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * A semi-transparent overlay panel with a gradient background and animated image,
 * designed to be displayed on top of other layers in a JLayeredPane for the Dungeon Crawler.
 * It contains interactive buttons for game control, such as saving and quitting.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.4
 */
public class DungeonOverlayPanel extends JPanel {

    /**
     * The base font size for the buttons.
     */
    private static final int BASE_FONT_SIZE = 14;
    /**
     * The base width of the buttons.
     */
    private static final int BASE_BUTTON_WIDTH = 100;
    /**
     * The base height of the buttons.
     */
    private static final int BASE_BUTTON_HEIGHT = 40;
    /**
     * The vertical spacing between buttons.
     */
    private static final int BUTTON_SPACING = 10;
    /**
     * The vertical padding at the top and bottom of the button list.
     */
    private static final int VERTICAL_PADDING = 20;
    /**
     * The base size (width and height) for the animated image.
     */
    private static final int IMAGE_BASE_SIZE = 360;
    /**
     * The font name used for text.
     */
    private static final String FONT_NAME = "SansSerif";
    /**
     * The default background color for the buttons.
     */
    private static final Color DEFAULT_BUTTON_COLOR = new Color(50, 50, 50, 150);
    /**
     * The background color for buttons on hover.
     */
    private static final Color HOVER_BUTTON_COLOR = new Color(70, 70, 70, 200);
    /**
     * The background color for buttons when pressed.
     */
    private static final Color PRESSED_BUTTON_COLOR = new Color(30, 30, 30, 220);
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
     * The button to resume the game.
     */
    private final JButton myResumeGameButton;
    /**
     * The button to quick-save the game.
     */
    private final JButton myQuickSaveButton;
    /**
     * The button to open the save game menu.
     */
    private final JButton mySaveGameButton;
    /**
     * The button to return to the main title screen.
     */
    private final JButton myTitleButton;
    /**
     * The button to quit the application.
     */
    private final JButton myQuitButton;
    /**
     * The current animation frame index for the overlay's animated image.
     */
    private int myOverlayAnimationFrame;

    /**
     * Constructs the DungeonOverlayPanel.
     *
     * @param theLogicalWidth  The logical width of the game view.
     * @param theLogicalHeight The logical height of the game view.
     * @param theGuiScale      The GUI scaling factor.
     */
    public DungeonOverlayPanel(final int theLogicalWidth, final int theLogicalHeight, final int theGuiScale) {
        myLogicalWidth = theLogicalWidth;
        myLogicalHeight = theLogicalHeight;
        myGuiScale = theGuiScale;

        setOpaque(false);
        setLayout(new BorderLayout());

        // Panel to hold the buttons, positioned on the left side
        final JPanel textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension((theLogicalWidth / 4) * theGuiScale, theLogicalHeight * theGuiScale));
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        // Create the buttons
        myResumeGameButton = createStyledButton(OverlayButtonAction.RESUME_GAME);
        myQuickSaveButton = createStyledButton(OverlayButtonAction.QUICK_SAVE);
        mySaveGameButton = createStyledButton(OverlayButtonAction.SAVE_GAME);
        myTitleButton = createStyledButton(OverlayButtonAction.TITLE);
        myQuitButton = createStyledButton(OverlayButtonAction.QUIT);

        final int buttonSpacing = BUTTON_SPACING * myGuiScale;
        final int verticalPadding = VERTICAL_PADDING * myGuiScale;

        // Add padding and buttons to the text panel
        textPanel.add(Box.createVerticalStrut(verticalPadding));
        textPanel.add(myResumeGameButton);
        textPanel.add(Box.createVerticalStrut(buttonSpacing));
        textPanel.add(myQuickSaveButton);
        textPanel.add(Box.createVerticalStrut(buttonSpacing));
        textPanel.add(mySaveGameButton);
        textPanel.add(Box.createVerticalStrut(buttonSpacing));
        textPanel.add(myTitleButton);
        textPanel.add(Box.createVerticalStrut(buttonSpacing));
        textPanel.add(myQuitButton);
        textPanel.add(Box.createVerticalStrut(verticalPadding));

        add(textPanel, BorderLayout.WEST);

        // Placeholder panel for the animated image, on the right side
        final JPanel animationPlaceholder = new JPanel();
        animationPlaceholder.setPreferredSize(new Dimension((theLogicalWidth * 3) / 4, theLogicalHeight));
        animationPlaceholder.setOpaque(false);
        add(animationPlaceholder, BorderLayout.CENTER);

        myOverlayAnimationFrame = 0;
    }

    /**
     * Creates and styles a JButton for the overlay.
     *
     * @param theAction The enum action associated with this button.
     * @return A styled JButton.
     */
    private JButton createStyledButton(final OverlayButtonAction theAction) {
        final JButton button = new JButton(theAction.getButtonText());

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.WHITE);
        button.setBackground(DEFAULT_BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setOpaque(true);

        button.setFont(new Font(FONT_NAME, Font.BOLD, BASE_FONT_SIZE * myGuiScale));
        final Dimension buttonSize = new Dimension(BASE_BUTTON_WIDTH * myGuiScale, BASE_BUTTON_HEIGHT * myGuiScale);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);

        // Add mouse listener for visual feedback
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent theEvent) {
                button.setBackground(HOVER_BUTTON_COLOR);
            }

            @Override
            public void mouseExited(final MouseEvent theEvent) {
                button.setBackground(DEFAULT_BUTTON_COLOR);
            }

            @Override
            public void mousePressed(final MouseEvent theEvent) {
                button.setBackground(PRESSED_BUTTON_COLOR);
            }

            @Override
            public void mouseReleased(final MouseEvent theEvent) {
                if (button.contains(theEvent.getPoint())) {
                    button.setBackground(HOVER_BUTTON_COLOR);
                } else {
                    button.setBackground(DEFAULT_BUTTON_COLOR);
                }
            }
        });

        button.setActionCommand(theAction.name());

        return button;
    }

    /**
     * Sets the ActionListener for all buttons on the overlay.
     *
     * @param theController The ActionListener, typically a controller class, to handle button clicks.
     */
    public void setActionListener(final ActionListener theController) {
        myResumeGameButton.addActionListener(theController);
        myQuickSaveButton.addActionListener(theController);
        mySaveGameButton.addActionListener(theController);
        myTitleButton.addActionListener(theController);
        myQuitButton.addActionListener(theController);
    }

    /**
     * Sets the current animation frame index for the overlay's animated image.
     *
     * @param theFrame The frame index.
     */
    public void setOverlayAnimationFrame(final int theFrame) {
        myOverlayAnimationFrame = theFrame;
    }

    /**
     * Overrides the paintComponent method to draw the custom background and animated image.
     *
     * @param theGraphics The Graphics object to draw on.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics.create();

        renderLeftPanelBackground(g2d);
        renderAnimatedImage(g2d);

        g2d.dispose();
    }

    /**
     * Renders the background for the left panel, including a gradient.
     *
     * @param theGraphics The Graphics2D object to draw on.
     */
    private void renderLeftPanelBackground(final Graphics2D theGraphics) {
        final int leftPanelWidth = myLogicalWidth / 4;
        final ImageIcon backgroundSprite = SpriteManager.getSprite("overlay_background_left");
        if (backgroundSprite != null) {
            final Image backgroundImage = backgroundSprite.getImage();
            theGraphics.drawImage(backgroundImage, 0, 0, leftPanelWidth * myGuiScale, myLogicalHeight * myGuiScale, this);
        }
        final Color startColor = new Color(0, 0, 0, 255);
        final Color endColor = new Color(50, 50, 50, 150);
        final GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, (float) leftPanelWidth * myGuiScale, 0, endColor);
        theGraphics.setPaint(gradientPaint);
        theGraphics.fillRect(0, 0, leftPanelWidth * myGuiScale, myLogicalHeight * myGuiScale);
    }

    /**
     * Renders the animated image on the right side of the panel.
     *
     * @param theGraphics The Graphics2D object to draw on.
     */
    private void renderAnimatedImage(final Graphics2D theGraphics) {
        final String spriteIdentifier = "overlay_background_right_normal_" + myOverlayAnimationFrame;
        final ImageIcon spriteIcon = SpriteManager.getSprite(spriteIdentifier);
        if (spriteIcon != null) {
            final int imageWidth = IMAGE_BASE_SIZE * myGuiScale;
            final int imageHeight = IMAGE_BASE_SIZE * myGuiScale;
            final int x = getWidth() - imageWidth;
            final int y = (getHeight() - imageHeight) / 2;
            theGraphics.drawImage(spriteIcon.getImage(), x, y, imageWidth, imageHeight, this);
        } else {
            theGraphics.setColor(Color.RED);
            theGraphics.fill(new Rectangle2D.Double(myLogicalWidth / 4.0 * myGuiScale, 0, myLogicalWidth * 3.0 / 4.0 * myGuiScale, myLogicalHeight * myGuiScale));
        }
    }

    /**
     * Lists the actions for the overlay buttons.
     */
    public enum OverlayButtonAction {
        RESUME_GAME("Resume"),
        QUICK_SAVE("Quick Save"),
        SAVE_GAME("Save Game"),
        TITLE("Title"),
        QUIT("Quit");

        /**
         * The text to display on the button.
         */
        private final String myButtonText;

        /**
         * Constructs a button action.
         *
         * @param theButtonText The text for the button.
         */
        OverlayButtonAction(final String theButtonText) {
            myButtonText = theButtonText;
        }

        /**
         * Gets the button's display text.
         *
         * @return The display text.
         */
        public String getButtonText() {
            return myButtonText;
        }
    }
}