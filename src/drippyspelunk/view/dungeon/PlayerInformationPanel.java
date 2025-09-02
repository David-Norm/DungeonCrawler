package drippyspelunk.view.dungeon;

import drippyspelunk.controller.input.DungeonCrawlerController;
import drippyspelunk.controller.sprite.SpriteManager;
import drippyspelunk.model.dungeon.DungeonCrawlerLogic;
import drippyspelunk.model.dungeon.entity.asset.Bomb;
import drippyspelunk.model.dungeon.entity.asset.Consumable;
import drippyspelunk.model.dungeon.entity.asset.HealthPotion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Player information panel that shows player stats, combat status, and inventory grid.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @author Mark Malyshev
 * @version 2.7
 */
public final class PlayerInformationPanel extends JPanel {

    /**
     * The number of columns in the inventory grid.
     */
    private static final int INVENTORY_COLS = 3;
    /**
     * The number of rows in the inventory grid.
     */
    private static final int INVENTORY_ROWS = 3;
    /**
     * The base size of a single inventory slot.
     */
    private static final int BASE_SLOT_SIZE = 32;
    /**
     * The base spacing between inventory slots.
     */
    private static final int BASE_SLOT_SPACING = 2;
    /**
     * The background color of an inventory slot.
     */
    private static final Color SLOT_BACKGROUND = Color.DARK_GRAY;
    /**
     * The border color of an inventory slot.
     */
    private static final Color SLOT_BORDER = Color.GRAY;
    /**
     * The color used to highlight a selected inventory slot.
     */
    private static final Color SLOT_SELECTED = Color.YELLOW;
    /**
     * The size of consumable sprites within the inventory slots.
     */
    private static final int CONSUMABLE_SPRITE_SIZE = 22;
    // Y-offsets for drawing text and components.
    private static final int Y_OFFSET_1 = 15;
    private static final int Y_OFFSET_2 = 18;
    private static final int Y_OFFSET_3 = 12;
    private static final int Y_OFFSET_4 = 10;
    private static final int Y_OFFSET_5 = 12;
    private static final int Y_OFFSET_6 = 5;
    private static final int Y_OFFSET_7 = 8;
    /**
     * The horizontal padding for text and components.
     */
    private static final int X_PADDING = 10;
    /**
     * The additional spacing for the health bar.
     */
    private static final int HEALTH_BAR_SPACING_OFFSET = 20;
    /**
     * The base width for a 1px border.
     */
    private static final int BORDER_WIDTH_1 = 1;
    /**
     * The base width for a 2px border.
     */
    private static final int BORDER_WIDTH_2 = 2;
    /**
     * The base width of the health bar.
     */
    private static final int HEALTH_BAR_WIDTH = 55;
    /**
     * The base height of the health bar.
     */
    private static final int HEALTH_BAR_HEIGHT = 8;
    /**
     * Health percentage for a high health status.
     */
    private static final double HEALTH_PERCENT_HIGH = 0.6;
    /**
     * Health percentage for a medium health status.
     */
    private static final double HEALTH_PERCENT_MEDIUM = 0.3;
    /**
     * The delay in milliseconds for the inventory slot click animation.
     */
    private static final int TIMER_DELAY = 150;
    /**
     * The experience points required per level.
     */
    private static final int EXP_PER_LEVEL = 100;
    // Font sizes
    private static final int FONT_SIZE_TITLE = 10;
    private static final int FONT_SIZE_NAME = 10;
    private static final int FONT_SIZE_STATS = 9;
    private static final int FONT_SIZE_EXP = 8;
    /**
     * The GUI scaling factor.
     */
    private final int myGuiScale;
    /**
     * The dungeon logic model.
     */
    private final DungeonCrawlerLogic myModel;
    /**
     * The controller for handling user input.
     */
    private final DungeonCrawlerController myController;

    /**
     * Constructs the player information panel.
     *
     * @param theWidth        The logical width of the panel.
     * @param theHeight       The logical height of the panel.
     * @param theDungeonLogic The dungeon game logic.
     * @param theController   The dungeon controller.
     * @param theGuiScale     The GUI scaling factor.
     */
    public PlayerInformationPanel(final int theWidth,
                                  final int theHeight,
                                  final DungeonCrawlerLogic theDungeonLogic,
                                  final DungeonCrawlerController theController,
                                  final int theGuiScale) {
        myModel = theDungeonLogic;
        myController = theController;
        myGuiScale = theGuiScale;

        setPreferredSize(new Dimension(theWidth, theHeight));
        setBackground(Color.ORANGE.darker());
        setLayout(new BorderLayout());

        setupInventoryGrid();
    }

    /**
     * Sets up and configures the inventory grid panel.
     */
    private void setupInventoryGrid() {
        final JPanel myInventoryGrid = new JPanel();
        myInventoryGrid.setLayout(new GridLayout(INVENTORY_ROWS, INVENTORY_COLS, BASE_SLOT_SPACING * myGuiScale, BASE_SLOT_SPACING * myGuiScale));
        myInventoryGrid.setBackground(Color.ORANGE.darker());
        myInventoryGrid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, BORDER_WIDTH_1 * myGuiScale),
                "Inventory",
                0,
                0,
                new Font("SansSerif", Font.BOLD, FONT_SIZE_EXP * myGuiScale),
                Color.WHITE
        ));

        for (int i = 0; i < INVENTORY_ROWS * INVENTORY_COLS; i++) {
            final JPanel slot = createInventorySlot(i);
            myInventoryGrid.add(slot);
        }

        add(myInventoryGrid, BorderLayout.SOUTH);
    }

    /**
     * Creates a single inventory slot panel with click and hover functionality.
     *
     * @param theSlotIndex The index of the slot in the inventory.
     * @return A configured JPanel representing an inventory slot.
     */
    private JPanel createInventorySlot(final int theSlotIndex) {
        final int slotSize = BASE_SLOT_SIZE * myGuiScale;
        final JPanel slot = new JPanel() {
            @Override
            protected void paintComponent(final Graphics theGraphics) {
                super.paintComponent(theGraphics);
                final List<Consumable> items = myModel.getInventory().getItems();
                if (theSlotIndex < items.size()) {
                    final Consumable item = items.get(theSlotIndex);
                    drawItem(theGraphics, item);
                }
            }
        };

        slot.setPreferredSize(new Dimension(slotSize, slotSize));
        slot.setBackground(SLOT_BACKGROUND);
        slot.setBorder(BorderFactory.createLineBorder(SLOT_BORDER, BORDER_WIDTH_1 * myGuiScale));

        // Add mouse listeners for interactive behavior
        slot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent theEvent) {
                if (theSlotIndex < myModel.getInventory().getItems().size()) {
                    slot.setBackground(SLOT_SELECTED);
                    final Timer timer = new Timer(TIMER_DELAY, evt -> slot.setBackground(SLOT_BACKGROUND));
                    timer.setRepeats(false);
                    timer.start();
                    myController.handleInventoryItemClick(theSlotIndex);
                }
            }

            @Override
            public void mouseEntered(final MouseEvent theEvent) {
                final List<Consumable> items = myModel.getInventory().getItems();
                if (theSlotIndex < items.size()) {
                    slot.setBorder(BorderFactory.createLineBorder(SLOT_SELECTED, BORDER_WIDTH_2 * myGuiScale));
                    final Consumable item = items.get(theSlotIndex);
                    final String tooltip = getItemTooltip(item);
                    slot.setToolTipText(tooltip);
                }
            }

            @Override
            public void mouseExited(final MouseEvent theEvent) {
                slot.setBorder(BorderFactory.createLineBorder(SLOT_BORDER, BORDER_WIDTH_1 * myGuiScale));
                slot.setToolTipText(null);
            }
        });

        return slot;
    }

    /**
     * Draws a consumable item's sprite within an inventory slot.
     *
     * @param theGraphics The Graphics object to draw on.
     * @param theItem     The consumable item to draw.
     */
    private void drawItem(final Graphics theGraphics, final Consumable theItem) {
        final Graphics2D g2d = (Graphics2D) theGraphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderConsumableSprite(g2d, 4 * myGuiScale, 4 * myGuiScale, theItem.getName());
        g2d.dispose();
    }


    /**
     * Generates a tooltip text for an inventory item based on its type.
     *
     * @param theItem The consumable item.
     * @return The tooltip string.
     */
    private String getItemTooltip(final Consumable theItem) {
        if (theItem instanceof HealthPotion) {
            if (myModel.isInCombat()) {
                return "Health Potion - Click to heal during your turn";
            } else {
                return "Health Potion - Click to heal (restores 30 HP)";
            }
        } else if (theItem instanceof Bomb) {
            if (myModel.isInCombat()) {
                return "Bomb - Click to deal 50 damage to enemy";
            } else {
                return "Bomb - Can be used to break some walls";
            }
        }
        return theItem.getClass().getSimpleName() + " - Click to use";
    }

    /**
     * Overrides the paintComponent method to draw player stats and information.
     *
     * @param theGraphics The Graphics object to draw on.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        if (myModel.getPlayer() == null) {
            return;
        }

        int y = Y_OFFSET_1 * myGuiScale;
        theGraphics.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE_TITLE * myGuiScale));
        theGraphics.setColor(Color.WHITE);
        theGraphics.drawString("Player Info", X_PADDING * myGuiScale, y);
        y += Y_OFFSET_2 * myGuiScale;

        final String characterName = myModel.getSelectedCharacterName();
        if (characterName != null && !characterName.trim().isEmpty()) {
            theGraphics.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE_NAME * myGuiScale));
            theGraphics.setColor(Color.CYAN);
            theGraphics.drawString(characterName, X_PADDING * myGuiScale, y);
            y += Y_OFFSET_3 * myGuiScale;
        }

        // Draw health info
        theGraphics.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE_STATS * myGuiScale));
        final int currentHealth = myModel.getPlayer().getHealth();
        final int maxHealth = myModel.getPlayer().getMaxHealth();
        theGraphics.setColor(Color.WHITE);

        theGraphics.drawString("Health: " + currentHealth + "/" + maxHealth, X_PADDING * myGuiScale, y);
        y += Y_OFFSET_4 * myGuiScale; // Move down for health bar
        drawHealthBar(theGraphics, X_PADDING * myGuiScale, y, currentHealth, maxHealth);

        // This line is updated to include the new spacing
        y += (Y_OFFSET_3 + HEALTH_BAR_SPACING_OFFSET) * myGuiScale; // Move down for next line of text

        // Draw level and experience info
        final int playerLevel = myModel.getPlayer().getLevel();
        theGraphics.drawString("Level: " + playerLevel, X_PADDING * myGuiScale, y);
        y += Y_OFFSET_4 * myGuiScale;

        final int currentExp = myModel.getPlayer().getExperience();
        final int expNeededForNextLevel = playerLevel * EXP_PER_LEVEL;
        final int expToNext = expNeededForNextLevel - currentExp;

        if (expToNext > 0) {
            theGraphics.drawString("EXP: " + currentExp + "/" + expNeededForNextLevel, X_PADDING * myGuiScale, y);
            y += Y_OFFSET_4 * myGuiScale;
            theGraphics.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE_EXP * myGuiScale));
            theGraphics.setColor(Color.LIGHT_GRAY);
            theGraphics.drawString("(" + expToNext + " to next level)", X_PADDING * myGuiScale, y);
            y += Y_OFFSET_5 * myGuiScale;
        } else {
            theGraphics.drawString("EXP: " + currentExp + " (Ready to level!)", X_PADDING * myGuiScale, y);
            y += Y_OFFSET_4 * myGuiScale;
        }

        y += Y_OFFSET_6 * myGuiScale;
        theGraphics.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE_STATS * myGuiScale));
        theGraphics.setColor(Color.WHITE);

        theGraphics.drawString("Attack Speed: " + myModel.getPlayer().getAttackSpeed(), X_PADDING * myGuiScale, y);
        y += Y_OFFSET_4 * myGuiScale;
        theGraphics.drawString("Move Speed: " + myModel.getPlayer().getMoveSpeed(), X_PADDING * myGuiScale, y);
        y += Y_OFFSET_4 * myGuiScale;
        theGraphics.drawString("Vision: " + myModel.getPlayer().getVisionRange(), X_PADDING * myGuiScale, y);
        y += Y_OFFSET_4 * myGuiScale;
        y += Y_OFFSET_7 * myGuiScale;

        y += Y_OFFSET_5 * myGuiScale;
        theGraphics.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE_EXP * myGuiScale));
        theGraphics.setColor(Color.WHITE);
        theGraphics.drawString("Items: " + myModel.getInventory().getSize() + "/" + myModel.getInventory().getCapacity(), X_PADDING * myGuiScale, y);
    }

    /**
     * Draws the player's health bar.
     *
     * @param theGraphics      The Graphics object to draw on.
     * @param theX             The x-coordinate to start drawing.
     * @param theY             The y-coordinate to start drawing.
     * @param theCurrentHealth The player's current health.
     * @param theMaxHealth     The player's maximum health.
     */
    private void drawHealthBar(final Graphics theGraphics, final int theX, final int theY, final int theCurrentHealth, final int theMaxHealth) {
        final int barWidth = HEALTH_BAR_WIDTH * myGuiScale;
        final int barHeight = HEALTH_BAR_HEIGHT * myGuiScale;
        theGraphics.setColor(Color.DARK_GRAY);
        theGraphics.fillRect(theX, theY, barWidth, barHeight);

        if (theMaxHealth > 0) {
            final int healthWidth = (int) ((double) theCurrentHealth / theMaxHealth * barWidth);
            final double healthPercent = (double) theCurrentHealth / theMaxHealth;
            if (healthPercent > HEALTH_PERCENT_HIGH) {
                theGraphics.setColor(Color.GREEN);
            } else if (healthPercent > HEALTH_PERCENT_MEDIUM) {
                theGraphics.setColor(Color.YELLOW);
            } else {
                theGraphics.setColor(Color.RED);
            }
            theGraphics.fillRect(theX, theY, healthWidth, barHeight);
        }

        theGraphics.setColor(Color.WHITE);
        theGraphics.drawRect(theX, theY, barWidth, barHeight);
    }

    /**
     * Renders a sprite image from the SpriteManager, scaled to a specified size.
     *
     * @param theGraphics         The Graphics2D object to draw on.
     * @param theRenderX          The x-coordinate for rendering.
     * @param theRenderY          The y-coordinate for rendering.
     * @param theRenderWidth      The width to render the sprite.
     * @param theRenderHeight     The height to render the sprite.
     * @param theSpriteIdentifier The identifier of the sprite to retrieve.
     */
    private void renderMatchingSprite(final Graphics2D theGraphics,
                                      final int theRenderX,
                                      final int theRenderY,
                                      final int theRenderWidth,
                                      final int theRenderHeight,
                                      final String theSpriteIdentifier) {
        if (theSpriteIdentifier != null) {
            final ImageIcon spriteIcon = SpriteManager.getSprite(theSpriteIdentifier);
            if (spriteIcon != null) {
                final Image spriteImage = spriteIcon.getImage();
                theGraphics.drawImage(spriteImage, theRenderX, theRenderY, theRenderWidth, theRenderHeight, this);
            } else {
                theGraphics.setColor(Color.PINK.darker());
                theGraphics.fillRect(theRenderX, theRenderY, theRenderWidth, theRenderHeight);
            }
        } else {
            theGraphics.setColor(Color.MAGENTA.darker());
            theGraphics.fillRect(theRenderX, theRenderY, theRenderWidth, theRenderHeight);
        }
    }

    /**
     * Renders a consumable sprite with a fixed size.
     *
     * @param theGraphics         The Graphics2D object to draw on.
     * @param theRenderX          The x-coordinate for rendering.
     * @param theRenderY          The y-coordinate for rendering.
     * @param theSpriteIdentifier The identifier of the sprite to retrieve.
     */
    private void renderConsumableSprite(final Graphics2D theGraphics, final int theRenderX, final int theRenderY, final String theSpriteIdentifier) {
        renderMatchingSprite(theGraphics, theRenderX, theRenderY, CONSUMABLE_SPRITE_SIZE * myGuiScale, CONSUMABLE_SPRITE_SIZE * myGuiScale, theSpriteIdentifier);
    }
}