package drippyspelunk.view.title;

import drippyspelunk.model.dungeon.entity.dungeoncharacter.Character;
import drippyspelunk.model.menu.TitleScreenLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * A panel for the new game menu, allowing the player to enter a name and select a character.
 *
 * @author David Norman
 * @version 1.1
 */
public class NewGamePanel extends JPanel {

    /**
     * The font size for the panel's title.
     */
    private static final int TITLE_FONT_SIZE = 16;
    /**
     * The font size for input fields and other text.
     */
    private static final int INPUT_FONT_SIZE = 12;

    /**
     * The GUI scaling factor.
     */
    private final int myGuiScale;
    /**
     * The logic component for the title screen, used to interact with game data.
     */
    private final TitleScreenLogic myLogic;
    /**
     * The text field where the player enters their name.
     */
    private final JTextField myNameField;
    /**
     * The panel that displays the available characters for selection.
     */
    private final JPanel myCharacterPanel;

    /**
     * The currently selected character.
     */
    private Character mySelectedCharacter;

    /**
     * Constructs a new game panel.
     *
     * @param theGuiScale        The GUI scaling factor.
     * @param theLogic           The title screen logic component.
     * @param theStartGameButton The button to start the game, provided by the parent panel.
     */
    NewGamePanel(final int theGuiScale, final TitleScreenLogic theLogic, final JButton theStartGameButton) {
        myGuiScale = theGuiScale;
        myLogic = theLogic;

        setBackground(new Color(60, 60, 60));
        setLayout(new BorderLayout(0, 10));

        // Create the top panel for the title and player name input
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(45, 45, 45));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title Label
        final JLabel titleLabel = new JLabel("CHOOSE YOUR CHARACTER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE * myGuiScale));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Name Input Panel
        final JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        final JLabel nameLabel = new JLabel("Player Name: ");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, INPUT_FONT_SIZE * myGuiScale));
        myNameField = new JTextField(15);
        myNameField.setFont(new Font("SansSerif", Font.PLAIN, INPUT_FONT_SIZE * myGuiScale));
        myNameField.setText("Nyoron"); // Default name
        namePanel.add(nameLabel);
        namePanel.add(myNameField);

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(namePanel);

        add(topPanel, BorderLayout.NORTH);

        // Character Selection Panel
        myCharacterPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        myCharacterPanel.setBackground(new Color(60, 60, 60));
        myCharacterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        final List<Character> availableCharacters = myLogic.getAvailableCharacters();

        for (final Character character : availableCharacters) {
            final JPanel charCard = createCharacterCard(character);
            myCharacterPanel.add(charCard);
        }

        add(myCharacterPanel, BorderLayout.CENTER);

        // Start Game Button Panel
        final JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(45, 45, 45));
        theStartGameButton.setActionCommand("Start New Game");
        theStartGameButton.setFont(new Font("SansSerif", Font.BOLD, INPUT_FONT_SIZE * myGuiScale));
        buttonPanel.add(theStartGameButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a single character card (JPanel) for character selection.
     *
     * @param character The character object to display on the card.
     * @return A JPanel representing the character card.
     */
    private JPanel createCharacterCard(final Character character) {
        final JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(80, 80, 80));
        card.setBorder(BorderFactory.createRaisedBevelBorder());
        card.setPreferredSize(new Dimension(200 * myGuiScale, 80 * myGuiScale));

        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(80, 80, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10 * myGuiScale, 0, 10 * myGuiScale, 0));

        final JLabel nameLabel = new JLabel(character.getMyName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18 * myGuiScale));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(nameLabel);
        card.add(headerPanel, BorderLayout.CENTER);

        final JButton selectButton = new JButton("SELECT");
        selectButton.setFont(new Font("SansSerif", Font.BOLD, 12 * myGuiScale));
        selectButton.setBackground(new Color(100, 150, 100));
        selectButton.setForeground(Color.WHITE);
        selectButton.setActionCommand("Select " + character.getMyName());

        selectButton.addActionListener(e -> {
            mySelectedCharacter = character;
            myLogic.setSelectedCharacter(character);
            updateColors();
        });

        card.add(selectButton, BorderLayout.SOUTH);

        card.putClientProperty("character", character);
        card.putClientProperty("selectButton", selectButton);

        // Add mouse hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent evt) {
                card.setBackground(new Color(100, 100, 100));
            }

            @Override
            public void mouseExited(final MouseEvent evt) {
                if (card.getClientProperty("character") == mySelectedCharacter) {
                    card.setBackground(new Color(120, 120, 120)); // Keep selected card highlighted
                } else {
                    card.setBackground(new Color(80, 80, 80));
                }
            }
        });

        return card;
    }

    /**
     * Updates the visual state of all character cards, highlighting the selected one.
     */
    private void updateColors() {
        for (final Component comp : myCharacterPanel.getComponents()) {
            if (comp instanceof JPanel card) {
                final Character character = (Character) card.getClientProperty("character");
                final JButton button = (JButton) card.getClientProperty("selectButton");

                if (character != null && button != null) {
                    if (character.equals(mySelectedCharacter)) {
                        button.setBackground(new Color(150, 200, 150));
                        card.setBackground(new Color(120, 120, 120));
                    } else {
                        button.setBackground(new Color(100, 150, 100));
                        card.setBackground(new Color(80, 80, 80));
                    }
                }
            }
        }
        myCharacterPanel.revalidate();
    }

    /**
     * Gets the name entered by the player in the text field.
     *
     * @return The trimmed player name string.
     */
    public String getPlayerName() {
        return myNameField.getText().trim();
    }
}