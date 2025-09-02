package drippyspelunk.view.title;

import javax.swing.*;
import java.awt.*;

/**
 * A panel for the base.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.1
 */
public class EmptyPanel extends JPanel {

    /**
     * Constructs a new EmptyPanel.
     * The panel is set to be opaque, its background is black, and it is focusable.
     */
    public EmptyPanel() {
        setOpaque(true);
        setBackground(Color.BLACK);
        setFocusable(true);
    }
}