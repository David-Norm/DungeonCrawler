package drippyspelunk.view.title;

import drippyspelunk.view.GUIConstants;

import javax.swing.*;
import java.awt.*;

/**
 * A panel for the credits.
 * This panel displays a list of contributors to the project with their roles.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
class CreditsPanel extends JPanel {

    /**
     * The font size for the panel's title.
     */
    private static final int TITLE_FONT_SIZE = 16;
    /**
     * The size of the insets (padding) around components.
     */
    private static final int INSET_SIZE = 5;

    /**
     * Constructs the CreditsPanel.
     *
     * @param theGuiScale The GUI scaling factor.
     */
    CreditsPanel(final int theGuiScale) {
        setBackground(Color.MAGENTA.darker());
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_SIZE * theGuiScale, 0, INSET_SIZE * theGuiScale, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        // Title label
        final JLabel titleLabel = new JLabel("Credits");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(GUIConstants.FONT_NAME, Font.BOLD, TITLE_FONT_SIZE * theGuiScale));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Credits content
        gbc.gridy++;
        add(createCreditsContent(theGuiScale), gbc);
    }

    /**
     * Creates a JComponent containing the credits list.
     *
     * @param theGuiScale The GUI scaling factor.
     * @return A JPanel with the formatted credits.
     */
    private JComponent createCreditsContent(final int theGuiScale) {
        final JPanel panel = new JPanel(new GridLayout(0, 1, 0, 5 * theGuiScale));
        panel.setBackground(Color.MAGENTA.darker());

        final String[] credits = {
                "Led by:",
                "Devin Arroyo",
                "",
                "Programmers:",
                "David Norman",
                "Devin Arroyo",
                "Mark Malyshev",
                "",
                "Art by:",
                "Kevin Michalson",
                "Mark Malyshev"
        };

        for (final String credit : credits) {
            final JLabel label = new JLabel(credit, SwingConstants.CENTER);
            label.setFont(new Font(GUIConstants.FONT_NAME, Font.PLAIN, 12 * theGuiScale));
            label.setForeground(Color.WHITE);
            panel.add(label);
        }

        return panel;
    }
}