package com.danosoftware.galaxyforce.models.screens.game;

import android.util.Log;

import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.models.screens.flashing.FlashingText;
import com.danosoftware.galaxyforce.models.screens.flashing.FlashingTextImpl;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePausedModelImpl implements Model, ButtonModel {

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* logger tag */
    private static final String TAG = "GamePausedModelImpl";

    private enum PausedState {
        RUNNING, RESUME, OPTIONS, EXIT
    }

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    private final Game game;

    /* reference to pause menu buttons */
    private final List<SpriteTextButton> menuButtons;

    /* Stores list of all sprites to be shown as paused. */
    private final List<ISprite> pausedSprites;

    /* reference to current state */
    private PausedState modelState;

    /* reference to flashing paused text */
    private final FlashingText flashingPausedText;

    /*
     * ******************************************************
     *
     * PUBLIC CONSTRUCTOR
     *
     * ******************************************************
     */

    public GamePausedModelImpl(
            Game game,
            Controller controller,
            List<ISprite> pausedSprites) {
        this.game = game;
        this.pausedSprites = pausedSprites;
        this.menuButtons = new ArrayList<>();
        this.modelState = PausedState.RUNNING;

        // create list of menu buttons
        addNewMenuButton(controller, 3, "RESUME", ButtonType.RESUME);
        addNewMenuButton(controller, 2, "OPTIONS", ButtonType.OPTIONS);
        addNewMenuButton(controller, 1, "EXIT", ButtonType.EXIT);

        // add flashing paused text
        Text pausedText = Text.newTextRelativePositionX(
                "PAUSED",
                TextPositionX.CENTRE,
                100 + (4 * 170));
        this.flashingPausedText = new FlashingTextImpl(
                Collections.singletonList(pausedText),
                0.5f);
    }

    /*
     * ******************************************************
     * PUBLIC INTERFACE METHODS
     * ******************************************************
     */

    @Override
    public List<ISprite> getSprites() {

        List<ISprite> sprites = new ArrayList<>(pausedSprites);
        for (SpriteTextButton eachButton : menuButtons) {
            sprites.add(eachButton.getSprite());
        }
        return sprites;
    }

    @Override
    public List<Text> getText() {

        List<Text> text = new ArrayList<>();
        for (SpriteTextButton eachButton : menuButtons) {
            text.add(eachButton.getText());
        }
        text.addAll(flashingPausedText.text());
        return text;
    }

    @Override
    public void update(float deltaTime) {

        switch (modelState) {

            case RUNNING:
                // normal state if no buttons are pressed
                flashingPausedText.update(deltaTime);
                break;

            case EXIT:
                // on exit, return to select level screen
                game.changeToScreen(ScreenType.SELECT_LEVEL);
                break;

            case OPTIONS:
                // go to options screen - will return back here when done
                game.changeToReturningScreen(ScreenType.OPTIONS);
                this.modelState = PausedState.RUNNING;
                break;

            case RESUME:
                // on resume - return back to game screen
                game.screenReturn();
                break;

            default:
                Log.e(TAG, "Illegal Model State.");
                throw new IllegalArgumentException("Illegal Model State.");

        }
    }

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {
            case EXIT:
                Log.i(TAG, "'Exit' selected.");
                this.modelState = PausedState.EXIT;
                break;
            case OPTIONS:
                Log.i(TAG, "'Options' selected.");
                this.modelState = PausedState.OPTIONS;
                break;
            case RESUME:
                Log.i(TAG, "'Resume' selected.");
                this.modelState = PausedState.RESUME;
                break;
            default:
                Log.e(TAG, "Illegal Button Type: " + buttonType.name());
                throw new GalaxyForceException("Illegal Button Type: " + buttonType.name());
        }
    }

    @Override
    public void goBack() {
        Log.i(TAG, "'Back Button' selected.");
        this.modelState = PausedState.EXIT;
    }

    @Override
    public void resume() {
        // no action for this model

    }

    @Override
    public void pause() {
        // no action for this model
    }

    @Override
    public void dispose() {
        // no action
    }

    /*
     * ******************************************************
     * PRIVATE HELPER METHODS
     * ******************************************************
     */

    private void addNewMenuButton(Controller controller, int row, String label, ButtonType buttonType) {
        MenuButton button = new MenuButton(
                this,
                GameConstants.GAME_WIDTH / 2,
                100 + (row * 170),
                label,
                buttonType,
                GameSpriteIdentifier.MENU_BUTTON_UP,
                GameSpriteIdentifier.MENU_BUTTON_DOWN);

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(button));

        // add new button to list
        menuButtons.add(button);
    }
}
