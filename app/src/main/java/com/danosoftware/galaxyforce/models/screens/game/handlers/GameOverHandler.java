package com.danosoftware.galaxyforce.models.screens.game.handlers;

import android.util.Log;

import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.implementations.FlashingTextImpl;
import com.danosoftware.galaxyforce.sprites.game.interfaces.FlashingText;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameOverHandler implements Model, ButtonModel {

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* logger tag */
    private static final String TAG = "GameOverHandler";

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* reference to controller */
    private final Controller controller;

    /* reference to pause menu buttons */
    private final List<SpriteTextButton> menuButtons;

    /* reference to current state */
    private ModelState modelState;

    /* Reference to the game model */
    private final GameModel gameModel;

    /* stars sprites */
    private final List<Star> stars;

    /* reference to flashing game over text */
    private final FlashingText flashingGameOverText;

    /*
     * ******************************************************
     *
     * PUBLIC CONSTRUCTOR
     *
     * ******************************************************
     */

    public GameOverHandler(GameModel gameModel, Controller controller, List<Star> stars) {
        this.controller = controller;
        this.gameModel = gameModel;
        this.stars = stars;
        this.menuButtons = new ArrayList<>();
        this.modelState = ModelState.GAME_OVER;

        // build menu buttons
        controller.clearTouchControllers();
        addNewMenuButton(3, "PLAY", ButtonType.PLAY);
        addNewMenuButton(2, "OPTIONS", ButtonType.OPTIONS);
        addNewMenuButton(1, "EXIT", ButtonType.MAIN_MENU);

        // add flashing game over text
        Text gameOver = Text.newTextRelativePositionX(
                "GAME OVER",
                TextPositionX.CENTRE,
                100 + (4 * 170));
        this.flashingGameOverText = new FlashingTextImpl(
                Collections.singletonList(gameOver),
                0.5f);
    }

    /*
     * ******************************************************
     * PUBLIC INTERFACE METHODS
     * ******************************************************
     */

    @Override
    public List<ISprite> getSprites() {

        List<ISprite> sprites = new ArrayList<>();
        sprites.addAll(stars);
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
        text.addAll(flashingGameOverText.text());
        return text;
    }

    @Override
    public void update(float deltaTime) {
        switch (modelState) {

            case GAME_OVER:
                // normal state before any buttons are pressed
                for (Star eachStar : stars) {
                    eachStar.animate(deltaTime);
                }
                break;

            case GO_BACK:
                // if back button pressed then quit
                gameModel.quit();
                break;

            case PLAYING:
                gameModel.play();
                break;

            case OPTIONS:
                // set back to game over state so model will be in
                // game over state when returning from options.
                // otherwise will keep calling options() method.
                this.modelState = ModelState.GAME_OVER;

                gameModel.options();
                break;

            default:
                Log.e(TAG, "Illegal Model State.");
                throw new IllegalArgumentException("Illegal Model State.");
        }

        flashingGameOverText.update(deltaTime);
    }

    @Override
    public void dispose() {
        // no action
    }

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {

            case MAIN_MENU:
                Log.i(TAG, "'Main Menu' selected.");
                this.modelState = ModelState.GO_BACK;
                break;
            case PLAY:
                Log.i(TAG, "'Play' selected.");
                this.modelState = ModelState.PLAYING;
                break;
            case OPTIONS:
                Log.i(TAG, "'Options' selected.");
                this.modelState = ModelState.OPTIONS;
                break;

            default:
                Log.e(TAG, "Illegal Button Type: " + buttonType.name());
                throw new GalaxyForceException("Illegal Button Type: " + buttonType.name());
        }
    }

    @Override
    public void goBack() {
        Log.i(TAG, "'Back Button' selected.");
        this.modelState = ModelState.GO_BACK;
    }

    @Override
    public void resume() {
        // no action for this model
    }

    @Override
    public void pause() {
        // no action for this model
    }

    /*
     * ******************************************************
     * PRIVATE HELPER METHODS
     * ******************************************************
     */

    private void addNewMenuButton(int row, String label, ButtonType buttonType) {
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
