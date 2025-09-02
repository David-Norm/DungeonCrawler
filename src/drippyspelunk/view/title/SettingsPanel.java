package drippyspelunk.view.title;

import drippyspelunk.controller.audio.AudioManager;
import drippyspelunk.model.menu.SettingsData;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 * A panel for the Settings menu options with scroll support.
 * Follows MVC pattern - only handles UI presentation and user input.
 *
 * @author Devin Arroyo
 * @author David Norman
 * @version 1.6
 */
public class SettingsPanel extends JPanel {

    /**
     * The font name used for all text.
     */
    private static final String FONT_NAME = "SansSerif";
    /**
     * The font size for the panel title.
     */
    private static final int TITLE_FONT_SIZE = 16;
    /**
     * The font size for settings labels and slider labels.
     */
    private static final int SETTINGS_FONT_SIZE = 12;
    /**
     * The base width for all sliders.
     */
    private static final int SLIDER_WIDTH = 200;
    /**
     * The base height for all sliders.
     */
    private static final int SLIDER_HEIGHT = 50;
    /**
     * The base width for value labels next to sliders.
     */
    private static final int VALUE_LABEL_WIDTH = 40;
    /**
     * The base height for value labels next to sliders.
     */
    private static final int VALUE_LABEL_HEIGHT = 25;
    /**
     * The base size for insets (padding) around components.
     */
    private static final int INSET_SIZE = 5;

    /**
     * The minimum value for the GUI scale slider.
     */
    private static final int GUI_SCALE_MIN = 1;
    /**
     * The maximum value for the GUI scale slider.
     */
    private static final int GUI_SCALE_MAX = 6;
    /**
     * The minimum value for volume sliders.
     */
    private static final int VOLUME_SLIDER_MIN = 0;
    /**
     * The maximum value for volume sliders.
     */
    private static final float VOLUME_SLIDER_MAX = 100;
    /**
     * The spacing between major ticks on volume sliders.
     */
    private static final int VOLUME_TICK_SPACING = 25;
    /**
     * A constant for 50 percent, used for volume slider labels.
     */
    private static final int PERCENT_50 = 50;
    /**
     * A constant for 100 percent, used for volume slider labels.
     */
    private static final int PERCENT_100 = 100;
    /**
     * The minimum difficulty value.
     */
    private static final int DIFFICULTY_MIN = 1;
    /**
     * The maximum difficulty value.
     */
    private static final int DIFFICULTY_MAX = 3;

    // Scroll pane constants
    /**
     * The scroll unit increment for the scroll pane.
     */
    private static final int SCROLL_UNIT_INCREMENT = 16;
    /**
     * The padding for the content within the scroll pane.
     */
    private static final int CONTENT_PADDING = 20;

    /**
     * Checkbox to enable/disable an undecorated window.
     */
    private final JCheckBox myUndecoratedCheckbox;
    /**
     * Slider to control music volume.
     */
    private final JSlider myMusicSlider;
    /**
     * Label to display the current music volume value.
     */
    private final JLabel myMusicLabelValue;
    /**
     * Slider to control sound effects volume.
     */
    private final JSlider mySfxSlider;
    /**
     * Label to display the current sound effects volume value.
     */
    private final JLabel mySfxLabelValue;
    /**
     * Slider to control UI sound volume.
     */
    private final JSlider myUiSlider;
    /**
     * Label to display the current UI sound volume value.
     */
    private final JLabel myUiLabelValue;
    /**
     * Slider to control the game difficulty.
     */
    private final JSlider myDifficultySlider;

    /**
     * The current GUI scale value.
     */
    private int myCurrentGuiScale;

    /**
     * Constructs the settings panel.
     *
     * @param theInitialSettings The initial settings data to populate the UI.
     * @param theApplyButton     The button that, when clicked, will apply settings.
     */
    public SettingsPanel(final SettingsData theInitialSettings, final JButton theApplyButton) {
        final int guiScale = theInitialSettings.getGuiScale();
        myCurrentGuiScale = guiScale;
        setBackground(Color.ORANGE.darker());
        setLayout(new BorderLayout());

        // Set up Apply button to handle audio changes
        theApplyButton.addActionListener(e -> applyAudioSettings());

        // Create the main content panel with GridBagLayout
        final JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.ORANGE.darker());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_SIZE * guiScale, CONTENT_PADDING * guiScale, INSET_SIZE * guiScale, CONTENT_PADDING * guiScale);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        final JLabel title = new JLabel("Settings");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(FONT_NAME, Font.BOLD, TITLE_FONT_SIZE * guiScale));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        contentPanel.add(title, gbc);

        // GUI Scale
        gbc.gridy++;
        final JPanel guiScalePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        guiScalePanel.setOpaque(false);
        final JLabel guiScaleText = new JLabel("GUI Scale:");
        guiScaleText.setForeground(Color.WHITE);
        guiScaleText.setFont(new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * guiScale));

        myCurrentGuiScale = Math.max(GUI_SCALE_MIN, Math.min(GUI_SCALE_MAX, guiScale));

        final JSlider guiScaleSlider = new JSlider(GUI_SCALE_MIN, GUI_SCALE_MAX, myCurrentGuiScale);
        guiScaleSlider.setMajorTickSpacing(1);
        guiScaleSlider.setPaintTicks(true);
        guiScaleSlider.setPaintLabels(true);
        guiScaleSlider.setSnapToTicks(true);
        guiScaleSlider.setOpaque(false);
        guiScaleSlider.setForeground(Color.WHITE);
        guiScaleSlider.setPreferredSize(new Dimension(SLIDER_WIDTH * guiScale, SLIDER_HEIGHT * guiScale));

        final Hashtable<Integer, JLabel> guiLabelTable = new Hashtable<>();
        final Font labelFont = new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * guiScale);
        for (int i = GUI_SCALE_MIN; i <= GUI_SCALE_MAX; i++) {
            JLabel lbl = new JLabel(i + "x");
            lbl.setFont(labelFont);
            lbl.setForeground(Color.WHITE);
            guiLabelTable.put(i, lbl);
        }
        guiScaleSlider.setLabelTable(guiLabelTable);

        guiScaleSlider.addChangeListener(e -> myCurrentGuiScale = guiScaleSlider.getValue());

        guiScalePanel.add(guiScaleText);
        guiScalePanel.add(guiScaleSlider);
        contentPanel.add(guiScalePanel, gbc);

        // Undecorated Window
        gbc.gridy++;
        myUndecoratedCheckbox = new JCheckBox("Undecorated Window");
        myUndecoratedCheckbox.setForeground(Color.WHITE);
        myUndecoratedCheckbox.setBackground(new Color(0, 0, 0, 0));
        myUndecoratedCheckbox.setSelected(theInitialSettings.isUndecorated());
        myUndecoratedCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * guiScale));
        myUndecoratedCheckbox.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(myUndecoratedCheckbox, gbc);

        // Music
        gbc.gridy++;
        final JPanel musicPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        musicPanel.setOpaque(false);
        final JLabel musicText = new JLabel("Music:");
        musicText.setForeground(Color.WHITE);
        musicText.setFont(new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * guiScale));
        myMusicSlider = createVolumeSlider(theInitialSettings.getMusicVolume(), guiScale);
        myMusicLabelValue = createValueLabel(myMusicSlider.getValue(), guiScale);
        musicPanel.add(musicText);
        musicPanel.add(myMusicSlider);
        musicPanel.add(myMusicLabelValue);
        myMusicSlider.addChangeListener(e -> myMusicLabelValue.setText(String.valueOf(myMusicSlider.getValue())));
        contentPanel.add(musicPanel, gbc);

        // SFX
        gbc.gridy++;
        final JPanel sfxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sfxPanel.setOpaque(false);
        final JLabel sfxText = new JLabel("SFX:");
        sfxText.setForeground(Color.WHITE);
        sfxText.setFont(new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * guiScale));
        mySfxSlider = createVolumeSlider(theInitialSettings.getSfxVolume(), guiScale);
        mySfxLabelValue = createValueLabel(mySfxSlider.getValue(), guiScale);
        sfxPanel.add(sfxText);
        sfxPanel.add(mySfxSlider);
        sfxPanel.add(mySfxLabelValue);
        mySfxSlider.addChangeListener(e -> mySfxLabelValue.setText(String.valueOf(mySfxSlider.getValue())));
        contentPanel.add(sfxPanel, gbc);

        // UI
        gbc.gridy++;
        final JPanel uiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        uiPanel.setOpaque(false);
        final JLabel uiText = new JLabel("UI:");
        uiText.setForeground(Color.WHITE);
        uiText.setFont(new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * guiScale));
        myUiSlider = createVolumeSlider(theInitialSettings.getUiVolume(), guiScale);
        myUiLabelValue = createValueLabel(myUiSlider.getValue(), guiScale);
        uiPanel.add(uiText);
        uiPanel.add(myUiSlider);
        uiPanel.add(myUiLabelValue);
        myUiSlider.addChangeListener(e -> myUiLabelValue.setText(String.valueOf(myUiSlider.getValue())));
        contentPanel.add(uiPanel, gbc);

        // Difficulty
        gbc.gridy++;
        final JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        difficultyPanel.setOpaque(false);
        final JLabel difficultyText = new JLabel("Difficulty:");
        difficultyText.setForeground(Color.WHITE);
        difficultyText.setFont(new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * guiScale));
        myDifficultySlider = createDifficultySlider(theInitialSettings.getDifficulty(), guiScale);
        difficultyPanel.add(difficultyText);
        difficultyPanel.add(myDifficultySlider);
        contentPanel.add(difficultyPanel, gbc);

        // Apply button
        gbc.gridy++;
        gbc.insets = new Insets(CONTENT_PADDING * guiScale, CONTENT_PADDING * guiScale, CONTENT_PADDING * guiScale, CONTENT_PADDING * guiScale);
        contentPanel.add(theApplyButton, gbc);

        // Extra space
        gbc.gridy++;
        gbc.weighty = 1.0;
        contentPanel.add(Box.createVerticalGlue(), gbc);

        // Scroll pane
        final JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.ORANGE.darker());
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT * guiScale);
        scrollPane.getVerticalScrollBar().setBlockIncrement(SCROLL_UNIT_INCREMENT * 4 * guiScale);

        // Style scroll bar
        scrollPane.getVerticalScrollBar().setBackground(Color.ORANGE.darker().darker());
        scrollPane.getVerticalScrollBar().setForeground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates a JSlider for volume control with specified initial value and GUI scale.
     *
     * @param theInitialValue The initial volume value (0.0 to 1.0).
     * @param theGuiScale     The GUI scaling factor.
     * @return A configured JSlider.
     */
    private JSlider createVolumeSlider(final float theInitialValue, final int theGuiScale) {
        final JSlider slider = new JSlider(VOLUME_SLIDER_MIN, (int) VOLUME_SLIDER_MAX, (int) (theInitialValue * VOLUME_SLIDER_MAX));
        slider.setMajorTickSpacing(VOLUME_TICK_SPACING);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setOpaque(false);
        slider.setForeground(Color.WHITE);
        slider.setPreferredSize(new Dimension(SLIDER_WIDTH * theGuiScale, SLIDER_HEIGHT * theGuiScale));

        final Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        final Font labelFont = new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * theGuiScale);
        labelTable.put(VOLUME_SLIDER_MIN, new JLabel("0%"));
        labelTable.put(PERCENT_50, new JLabel("50%"));
        labelTable.put(PERCENT_100, new JLabel("100%"));

        for (final JLabel label : labelTable.values()) {
            label.setFont(labelFont);
            label.setForeground(Color.WHITE);
        }

        slider.setLabelTable(labelTable);
        return slider;
    }

    /**
     * Creates a JLabel to display the numerical value of a slider.
     *
     * @param theValue    The initial value to display.
     * @param theGuiScale The GUI scaling factor.
     * @return A configured JLabel.
     */
    private JLabel createValueLabel(final int theValue, final int theGuiScale) {
        JLabel label = new JLabel(String.valueOf(theValue));
        label.setForeground(Color.WHITE);
        label.setFont(new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * theGuiScale));
        label.setPreferredSize(new Dimension(VALUE_LABEL_WIDTH, VALUE_LABEL_HEIGHT));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Creates a JSlider for difficulty selection.
     *
     * @param theInitialValue The initial difficulty value.
     * @param theGuiScale     The GUI scaling factor.
     * @return A configured JSlider.
     */
    private JSlider createDifficultySlider(final int theInitialValue, final int theGuiScale) {
        int initialValue = Math.max(DIFFICULTY_MIN, Math.min(DIFFICULTY_MAX, theInitialValue));

        final JSlider slider = new JSlider(DIFFICULTY_MIN, DIFFICULTY_MAX, initialValue);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setOpaque(false);
        slider.setForeground(Color.WHITE);
        slider.setPreferredSize(new Dimension(SLIDER_WIDTH * theGuiScale, SLIDER_HEIGHT * theGuiScale));

        final Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        final Font labelFont = new Font(FONT_NAME, Font.PLAIN, SETTINGS_FONT_SIZE * theGuiScale);
        labelTable.put(DIFFICULTY_MIN, new JLabel("Easy"));
        labelTable.put(2, new JLabel("Medium"));
        labelTable.put(DIFFICULTY_MAX, new JLabel("Hard"));

        for (final JLabel label : labelTable.values()) {
            label.setFont(labelFont);
            label.setForeground(Color.WHITE);
        }

        slider.setLabelTable(labelTable);
        return slider;
    }

    /**
     * Applies the current audio settings to the AudioManager.
     * Called when the Apply button is clicked.
     */
    private void applyAudioSettings() {
        AudioManager.setMusicVolume(getMusicSliderValue());
        AudioManager.setSfxVolume(getSfxSliderValue());
        AudioManager.setUiVolume(getUiSliderValue());
        System.out.println("Audio settings applied!");
    }

    /**
     * Gets the current value of the GUI scale slider.
     *
     * @return The GUI scale value.
     */
    public int getGuiScaleValue() {
        return myCurrentGuiScale;
    }

    /**
     * Gets the selected state of the undecorated window checkbox.
     *
     * @return true if the checkbox is selected, false otherwise.
     */
    public boolean getUndecoratedValue() {
        return myUndecoratedCheckbox.isSelected();
    }

    /**
     * Gets the current value of the music volume slider.
     *
     * @return The music volume as a float between 0.0 and 1.0.
     */
    public float getMusicSliderValue() {
        return myMusicSlider.getValue() / VOLUME_SLIDER_MAX;
    }

    /**
     * Gets the current value of the sound effects volume slider.
     *
     * @return The SFX volume as a float between 0.0 and 1.0.
     */
    public float getSfxSliderValue() {
        return mySfxSlider.getValue() / VOLUME_SLIDER_MAX;
    }

    /**
     * Gets the current value of the UI sound volume slider.
     *
     * @return The UI volume as a float between 0.0 and 1.0.
     */
    public float getUiSliderValue() {
        return myUiSlider.getValue() / VOLUME_SLIDER_MAX;
    }

    /**
     * Gets the current value of the difficulty slider.
     *
     * @return The difficulty value.
     */
    public int getDifficultySliderValue() {
        return myDifficultySlider.getValue();
    }
}