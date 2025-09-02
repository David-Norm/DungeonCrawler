package drippyspelunk.view.title;

import drippyspelunk.view.GUIConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for load game.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
class LoadGamePanel extends JPanel {

    /**
     * The font size for the panel's title.
     */
    private static final int TITLE_FONT_SIZE = 16;
    /**
     * The size of the insets (padding) around components.
     */
    private static final int INSET_SIZE = 5;

    /**
     * Constructs the LoadGamePanel.
     *
     * @param theGuiScale The GUI scaling factor.
     */
    LoadGamePanel(final int theGuiScale) {
        setBackground(Color.GREEN.darker());
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_SIZE * theGuiScale, 0, INSET_SIZE * theGuiScale, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        // "Load Game" title label
        final JLabel titleLabel = new JLabel("Load Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(GUIConstants.FONT_NAME, Font.BOLD, TITLE_FONT_SIZE * theGuiScale));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // "Coming Soon!" message label
        gbc.gridy++;
        final JLabel comingSoonLabel = new JLabel("Coming Soon!");
        comingSoonLabel.setForeground(Color.WHITE);
        comingSoonLabel.setFont(new Font(GUIConstants.FONT_NAME, Font.PLAIN, 12 * theGuiScale));
        add(comingSoonLabel, gbc);
    }
}