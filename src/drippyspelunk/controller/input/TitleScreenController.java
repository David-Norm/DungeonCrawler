package drippyspelunk.controller.input;

import drippyspelunk.controller.audio.AudioEvent;
import drippyspelunk.controller.audio.AudioManager;
import drippyspelunk.controller.core.StateManager;
import drippyspelunk.model.dungeon.entity.dungeoncharacter.Character;
import drippyspelunk.model.menu.SettingsData;
import drippyspelunk.model.menu.TitleScreenLogic;
import drippyspelunk.view.title.TitleExpandedMenu;
import drippyspelunk.view.title.TitleScreenView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The controller for the title screen. This class handles user input,
 * updates the model, and reacts to model changes.
 *
 * @author Devin Arroyo
 * @version 2.7
 */
public class TitleScreenController implements IController, ActionListener, PropertyChangeListener {

    /**
     * The state manager for handling game state transitions.
     */
    private final StateManager myStateManager;

    /**
     * The model for the title screen logic.
     */
    private final TitleScreenLogic myLogic;

    /**
     * The view for the title screen.
     */
    private final TitleScreenView myView;

    /**
     * The current panel type displayed in the expanded menu.
     */
    private TitleExpandedMenu.PanelType myCurrentPanelType;

    /**
     * Constructs a TitleScreenController.
     *
     * @param theStateManager The state manager for game state transitions.
     * @param theLogic        The title screen logic model.
     * @param theView         The title screen view.
     */
    public TitleScreenController(final StateManager theStateManager, final TitleScreenLogic theLogic, final TitleScreenView theView) {
        myStateManager = theStateManager;
        myLogic = theLogic;
        myView = theView;
        myCurrentPanelType = TitleExpandedMenu.PanelType.EMPTY;

        myView.setActionListener(this);
    }

    /**
     * Handles action events from the view's components.
     *
     * @param theEvent The action event.
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        final String actionCommand = theEvent.getActionCommand();

        AudioManager.dispatch(AudioEvent.MENU_SELECT);

        switch (actionCommand) {
            case "New Game" -> myCurrentPanelType = TitleExpandedMenu.PanelType.NEW_GAME;
            case "Load Game" -> myCurrentPanelType = TitleExpandedMenu.PanelType.LOAD_GAME;
            case "Settings" -> myCurrentPanelType = TitleExpandedMenu.PanelType.SETTINGS;
            case "Credits" -> myCurrentPanelType = TitleExpandedMenu.PanelType.CREDITS;
            case "Close Application" -> myCurrentPanelType = TitleExpandedMenu.PanelType.CLOSE_APPLICATION;
            case "Back", "No" -> myCurrentPanelType = TitleExpandedMenu.PanelType.EMPTY;
            case "Apply" -> {
                applySettings();
                myCurrentPanelType = TitleExpandedMenu.PanelType.EMPTY;
            }
            case "Yes" -> System.exit(0);
            case "Start New Game" -> {
                final Character selectedCharacter = myLogic.getSelectedCharacter();
                final String playerName = myView.getNewGamePanel().getPlayerName();

                if (selectedCharacter != null && !playerName.isEmpty()) {
                    myStateManager.goToDungeonCrawler(selectedCharacter, playerName);
                }
            }
            default -> {
                Character selectedCharacter = findCharacterForAction(actionCommand);
                if (selectedCharacter != null) {
                    myLogic.setSelectedCharacter(selectedCharacter);
                }
            }
        }

        myView.showPanel(myCurrentPanelType);
    }

    /**
     * Finds a character based on the action command string.
     *
     * @param theActionCommand The action command string.
     * @return The character object if found, otherwise null.
     */
    private Character findCharacterForAction(final String theActionCommand) {
        if (theActionCommand.startsWith("Select ")) {
            String characterName = theActionCommand.substring("Select ".length());
            List<Character> availableCharacters = myLogic.getAvailableCharacters();
            for (Character character : availableCharacters) {
                if (character.getMyName().equals(characterName)) {
                    return character;
                }
            }
        }
        return null;
    }

    /**
     * Applies the settings from the view to the model and saves them.
     */
    private void applySettings() {
        final SettingsData newSettings = new SettingsData(
                myView.getExpandedMenu().getSettingsPanel().getGuiScaleValue(),
                myView.getExpandedMenu().getSettingsPanel().getUndecoratedValue(),
                myView.getExpandedMenu().getSettingsPanel().getMusicSliderValue(),
                myView.getExpandedMenu().getSettingsPanel().getSfxSliderValue(),
                myView.getExpandedMenu().getSettingsPanel().getUiSliderValue(),
                myView.getExpandedMenu().getSettingsPanel().getDifficultySliderValue()
        );
        myLogic.applyAndSaveSettings(newSettings);
        myStateManager.reinitializeTitleScreen();
    }

    /**
     * Cleans up listeners from this controller instance.
     */
    public void cleanup() {
        myView.removeActionListener(this);
    }

    @Override
    public void keyTyped(final KeyEvent theEvent) {
        // Not implemented for the title screen
    }

    @Override
    public void keyPressed(final KeyEvent theEvent) {
        if (theEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            myCurrentPanelType = TitleExpandedMenu.PanelType.CLOSE_APPLICATION;
            myView.showPanel(myCurrentPanelType);
        }
    }

    @Override
    public void keyReleased(final KeyEvent theEvent) {
        // Not implemented for the title screen
    }

    @Override
    public void propertyChange(PropertyChangeEvent theEvent) {
        String propertyName = theEvent.getPropertyName();
    }
}