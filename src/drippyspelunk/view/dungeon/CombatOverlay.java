package drippyspelunk.view.dungeon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A semi-transparent overlay that appears over the main dungeon screen during combat.
 * It is responsible for displaying health bars for both the player and the enemy,
 * and a set of buttons for the player's combat actions.
 *
 * @author David Norman
 * @author Devin Arroyo
 * @version 1.5
 */
public class CombatOverlay extends JPanel {

    // Health bar visual constants
    /**
     * The background color of the health bars.
     */
    private static final Color HEALTH_BAR_BACKGROUND = Color.DARK_GRAY;
    /**
     * The color of the player's health portion of the bar.
     */
    private static final Color PLAYER_HEALTH_COLOR = Color.GREEN;
    /**
     * The color of the enemy's health portion of the bar.
     */
    private static final Color ENEMY_HEALTH_COLOR = Color.RED;
    /**
     * The fixed width of the health bars.
     */
    private static final int HEALTH_BAR_WIDTH = 80;
    /**
     * The fixed height of the health bars.
     */
    private static final int HEALTH_BAR_HEIGHT = 8;
    /**
     * The vertical offset for drawing the health bars above the characters.
     */
    private static final int HEALTH_BAR_OFFSET_Y = -35;
    // Combat positioning constants (should align with DungeonCrawlerLogic)
    /**
     * The fixed x-coordinate for the player's combat sprite.
     */
    private static final int COMBAT_PLAYER_X = 170;
    /**
     * The fixed y-coordinate for the player's combat sprite.
     */
    private static final int COMBAT_PLAYER_Y = 160;
    /**
     * The fixed x-coordinate for the enemy's combat sprite.
     */
    private static final int COMBAT_ENEMY_X = 330;
    /**
     * The fixed y-coordinate for the enemy's combat sprite.
     */
    private static final int COMBAT_ENEMY_Y = 120;
    /**
     * The fixed width of the character sprites.
     */
    private static final int CHARACTER_WIDTH = 50;
    /**
     * The GUI scaling factor.
     */
    private final int myGuiScale;
    // Combat action buttons
    /**
     * The button for the "Block" action.
     */
    private JButton myBlockButton;
    /**
     * The button for the "Run" action.
     */
    private JButton myRunButton;
    /**
     * The button for the "Light Attack" action.
     */
    private JButton myLightAttackButton;
    /**
     * The button for the "Heavy Attack" action.
     */
    private JButton myHeavyAttackButton;
    /**
     * The button for the "Hail Mary" special attack.
     */
    private JButton myHailMaryButton;
    /**
     * The button for the character's class-specific attack.
     */
    private JButton myClassAttackButton;
    /**
     * A label to display combat-related messages.
     */
    private JLabel myCombatMessageLabel;
    // Health bar data
    /**
     * The name of the player, displayed above the health bar.
     */
    private String myPlayerName = "Player";
    /**
     * The player's current health.
     */
    private int myCurrentPlayerHealth = 0;
    /**
     * The player's maximum health.
     */
    private int myMaxPlayerHealth = 1;
    /**
     * The name of the enemy, displayed above the health bar.
     */
    private String myEnemyName = "Enemy";
    /**
     * The enemy's current health.
     */
    private int myCurrentEnemyHealth = 0;
    /**
     * The enemy's maximum health.
     */
    private int myMaxEnemyHealth = 1;

    /**
     * Constructs the CombatOverlay.
     *
     * @param theGuiScale The GUI scaling factor.
     */
    public CombatOverlay(final int theGuiScale) {
        myGuiScale = theGuiScale;
        setOpaque(false);
        setLayout(new BorderLayout());

        initializeButtons();
        setupLayout();
    }

    /**
     * Gets the "Block" button.
     *
     * @return The Block JButton.
     */
    public JButton getBlockButton() {
        return myBlockButton;
    }

    /**
     * Gets the "Run" button.
     *
     * @return The Run JButton.
     */
    public JButton getRunButton() {
        return myRunButton;
    }

    /**
     * Gets the "Light Attack" button.
     *
     * @return The Light Attack JButton.
     */
    public JButton getLightAttackButton() {
        return myLightAttackButton;
    }

    /**
     * Gets the "Heavy Attack" button.
     *
     * @return The Heavy Attack JButton.
     */
    public JButton getHeavyAttackButton() {
        return myHeavyAttackButton;
    }

    /**
     * Gets the "Hail Mary" button.
     *
     * @return The Hail Mary JButton.
     */
    public JButton getHailMaryButton() {
        return myHailMaryButton;
    }

    /**
     * Gets the "Class Attack" button.
     *
     * @return The Class Attack JButton.
     */
    public JButton getClassAttackButton() {
        return myClassAttackButton;
    }

    /**
     * Adds an ActionListener to all combat buttons.
     *
     * @param theListener The listener to be added.
     */
    public void addActionListener(final ActionListener theListener) {
        myBlockButton.addActionListener(theListener);
        myRunButton.addActionListener(theListener);
        myLightAttackButton.addActionListener(theListener);
        myHeavyAttackButton.addActionListener(theListener);
        myHailMaryButton.addActionListener(theListener);
        myClassAttackButton.addActionListener(theListener);
    }

    /**
     * Displays a message on the combat message label.
     *
     * @param theMessage The message to display.
     */
    public void displayMessage(final String theMessage) {
        // Use SwingUtilities.invokeLater to ensure thread safety when updating GUI components.
        SwingUtilities.invokeLater(() -> myCombatMessageLabel.setText(theMessage));
    }

    /**
     * Updates the player's health information for rendering.
     *
     * @param theName          The player's name.
     * @param theCurrentHealth The player's current health.
     * @param theMaxHealth     The player's maximum health.
     */
    public void setPlayerHealth(final String theName, final int theCurrentHealth, final int theMaxHealth) {
        myPlayerName = theName;
        myCurrentPlayerHealth = theCurrentHealth;
        myMaxPlayerHealth = theMaxHealth;
    }

    /**
     * Updates the enemy's health information for rendering.
     *
     * @param theName          The enemy's name.
     * @param theCurrentHealth The enemy's current health.
     * @param theMaxHealth     The enemy's maximum health.
     */
    public void setEnemyHealth(final String theName, final int theCurrentHealth, final int theMaxHealth) {
        myEnemyName = theName;
        myCurrentEnemyHealth = theCurrentHealth;
        myMaxEnemyHealth = theMaxHealth;
    }

    /**
     * Initializes and styles all the combat buttons and the message label.
     */
    private void initializeButtons() {
        // Create buttons with hardcoded labels
        myBlockButton = new JButton("Block");
        myRunButton = new JButton("Run");
        myLightAttackButton = new JButton("Light Attack");
        myHeavyAttackButton = new JButton("Heavy Attack");
        myHailMaryButton = new JButton("Hail Mary");
        myClassAttackButton = new JButton("Class Attack");

        // Set preferred size for consistency
        final Dimension buttonSize = new Dimension(90 * myGuiScale, 30 * myGuiScale);
        myBlockButton.setPreferredSize(buttonSize);
        myRunButton.setPreferredSize(buttonSize);
        myLightAttackButton.setPreferredSize(buttonSize);
        myHeavyAttackButton.setPreferredSize(buttonSize);
        myHailMaryButton.setPreferredSize(buttonSize);
        myClassAttackButton.setPreferredSize(buttonSize);

        // Style the buttons
        styleButton(myBlockButton, new Color(100, 100, 255));
        styleButton(myRunButton, new Color(120, 80, 200));
        styleButton(myLightAttackButton, Color.RED);
        styleButton(myHeavyAttackButton, new Color(200, 50, 50));
        styleButton(myHailMaryButton, new Color(150, 30, 30));
        styleButton(myClassAttackButton, new Color(100, 10, 10));

        // Initialize and style the message label
        myCombatMessageLabel = new JLabel("", SwingConstants.CENTER);
        myCombatMessageLabel.setFont(new Font("Arial", Font.BOLD, 12 * myGuiScale));
        myCombatMessageLabel.setForeground(Color.WHITE);
        myCombatMessageLabel.setBackground(new Color(0, 0, 0, 150));
        myCombatMessageLabel.setOpaque(true);
        myCombatMessageLabel.setBorder(BorderFactory.createEmptyBorder(5 * myGuiScale, 10 * myGuiScale, 5 * myGuiScale, 10 * myGuiScale));
    }

    /**
     * Applies consistent styling to a JButton.
     *
     * @param theButton The button to style.
     * @param theColor  The background color for the button.
     */
    private void styleButton(final JButton theButton, final Color theColor) {
        theButton.setBackground(theColor);
        theButton.setForeground(Color.WHITE);
        theButton.setFont(new Font("Arial", Font.BOLD, Math.max(1, 10 * myGuiScale)));
        theButton.setBorder(BorderFactory.createRaisedBevelBorder());
        theButton.setFocusPainted(false);
        theButton.setOpaque(true);
    }

    /**
     * Sets up the layout of the panel, arranging buttons and the message label.
     */
    private void setupLayout() {
        // Add a combat message at the top
        add(myCombatMessageLabel, BorderLayout.NORTH);

        // Create a main panel to hold both attack buttons and action buttons
        final JPanel mainButtonPanel = new JPanel(new BorderLayout(10 * myGuiScale, 5 * myGuiScale));
        mainButtonPanel.setOpaque(false);

        // Create a 2x2 grid for the attack buttons
        final JPanel attackSquarePanel = new JPanel(new GridLayout(2, 2, 5 * myGuiScale, 5 * myGuiScale));
        attackSquarePanel.setOpaque(false);

        // Add attack buttons in a square formation
        attackSquarePanel.add(myLightAttackButton);
        attackSquarePanel.add(myHeavyAttackButton);
        attackSquarePanel.add(myHailMaryButton);
        attackSquarePanel.add(myClassAttackButton);

        // Create a panel for the Block and Run buttons
        final JPanel actionPanel = new JPanel(new GridLayout(2, 1, 5 * myGuiScale, 8 * myGuiScale));
        actionPanel.setOpaque(false);
        actionPanel.add(myBlockButton);
        actionPanel.add(myRunButton);

        // Add button panels to the main panel
        mainButtonPanel.add(attackSquarePanel, BorderLayout.CENTER);
        mainButtonPanel.add(actionPanel, BorderLayout.EAST);

        add(mainButtonPanel, BorderLayout.SOUTH);
    }

    /**
     * Overrides the paintComponent method to draw the semi-transparent background and health bars.
     *
     * @param theGraphics The Graphics object to draw on.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        Graphics2D g2d = (Graphics2D) theGraphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a semi-transparent black background
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawHealthBars(g2d);
        g2d.dispose();
    }

    /**
     * Draws the player and enemy health bars.
     *
     * @param theGraphics The Graphics2D object to draw on.
     */
    private void drawHealthBars(final Graphics2D theGraphics) {
        // Calculate scaled positions for the health bars
        int playerRenderX = (COMBAT_PLAYER_X - CHARACTER_WIDTH / 2) * myGuiScale;
        int playerRenderY = COMBAT_PLAYER_Y * myGuiScale;
        drawHealthBar(theGraphics, playerRenderX, playerRenderY + HEALTH_BAR_OFFSET_Y * myGuiScale,
                myCurrentPlayerHealth, myMaxPlayerHealth, PLAYER_HEALTH_COLOR, myPlayerName);

        int enemyRenderX = (COMBAT_ENEMY_X - CHARACTER_WIDTH / 2) * myGuiScale + (CHARACTER_WIDTH * myGuiScale) / 2;
        int enemyRenderY = COMBAT_ENEMY_Y * myGuiScale;
        drawHealthBar(theGraphics, enemyRenderX, enemyRenderY + HEALTH_BAR_OFFSET_Y * myGuiScale,
                myCurrentEnemyHealth, myMaxEnemyHealth, ENEMY_HEALTH_COLOR, myEnemyName);
    }

    /**
     * Draws a single health bar with its name and current health text.
     *
     * @param theGraphics      The Graphics2D object.
     * @param theX             The base x-coordinate.
     * @param theY             The base y-coordinate.
     * @param theCurrentHealth The current health value.
     * @param theMaxHealth     The maximum health value.
     * @param theHealthColor   The color of the health portion of the bar.
     * @param theName          The name to display above the health bar.
     */
    private void drawHealthBar(final Graphics2D theGraphics, final int theX, final int theY, final int theCurrentHealth, final int theMaxHealth,
                               final Color theHealthColor, final String theName) {
        int barWidth = HEALTH_BAR_WIDTH * myGuiScale;
        int barHeight = HEALTH_BAR_HEIGHT * myGuiScale;

        // Center the health bar above the character
        int barX = theX - barWidth / 2;

        // Draw background
        theGraphics.setColor(HEALTH_BAR_BACKGROUND);
        theGraphics.fillRect(barX, theY, barWidth, barHeight);

        // Draw a health portion
        if (theMaxHealth > 0) {
            int healthWidth = (int) ((double) theCurrentHealth / theMaxHealth * barWidth);
            theGraphics.setColor(theHealthColor);
            theGraphics.fillRect(barX, theY, healthWidth, barHeight);
        }

        // Draw border
        theGraphics.setColor(Color.WHITE);
        theGraphics.drawRect(barX, theY, barWidth, barHeight);

        // Draw name with outline for better visibility
        int fontSize = Math.max(8, 10 * myGuiScale);
        theGraphics.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics fm = theGraphics.getFontMetrics();
        int nameWidth = fm.stringWidth(theName);
        int nameX = barX + (barWidth - nameWidth) / 2;
        int nameY = theY - Math.max(2, 3 * myGuiScale);

        theGraphics.setColor(Color.BLACK);
        theGraphics.drawString(theName, nameX + 1, nameY + 1);
        theGraphics.setColor(Color.WHITE);
        theGraphics.drawString(theName, nameX, nameY);

        // Draw health text with outline
        String healthText = theCurrentHealth + "/" + theMaxHealth;
        int healthTextWidth = fm.stringWidth(healthText);
        int healthTextX = barX + (barWidth - healthTextWidth) / 2;
        int healthTextY = theY + barHeight + Math.max(10, 12 * myGuiScale);

        theGraphics.setColor(Color.BLACK);
        theGraphics.drawString(healthText, healthTextX + 1, healthTextY + 1);
        theGraphics.setColor(Color.WHITE);
        theGraphics.drawString(healthText, healthTextX, healthTextY);
    }
}