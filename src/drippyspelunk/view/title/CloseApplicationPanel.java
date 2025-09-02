package drippyspelunk.view.title;

import javax.swing.*;
import java.awt.*;

/**
 * A panel for exiting the application.
 * This panel displays a confirmation message and provides "Yes" and "No" buttons
 * to confirm or cancel the application's closure.
 *
 * @author Devin Arroyo
 * @version 1.0
 */
class CloseApplicationPanel extends JPanel {

    /**
     * The font name used for text.
     */
    private static final String FONT_NAME = "SansSerif";
    /**
     * The font size for the confirmation title.
     */
    private static final int TITLE_FONT_SIZE = 16;
    /**
     * The size of the insets (padding) around components.
     */
    private static final int INSET_SIZE = 5;

    /**
     * Constructs the CloseApplicationPanel.
     *
     * @param theGuiScale  The GUI scaling factor.
     * @param theYesButton The "Yes" button for confirmation.
     * @param theNoButton  The "No" button to cancel.
     */
    CloseApplicationPanel(final int theGuiScale, final JButton theYesButton, final JButton theNoButton) {
        setBackground(Color.RED.darker());
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_SIZE * theGuiScale, 0, INSET_SIZE * theGuiScale, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        // Confirmation message label
        final JLabel label = new JLabel("Are you sure you want to exit?");
        label.setForeground(Color.WHITE);
        label.setFont(new Font(FONT_NAME, Font.BOLD, TITLE_FONT_SIZE * theGuiScale));
        gbc.gridy = 0;
        add(label, gbc);

        // "Yes" button
        gbc.gridy++;
        add(theYesButton, gbc);

        // "No" button
        gbc.gridy++;
        add(theNoButton, gbc);
    }
}