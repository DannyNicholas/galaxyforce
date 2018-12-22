package com.danosoftware.galaxyforce.models.play.game_handler;

import android.util.Log;

import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.models.button.ButtonModel;
import com.danosoftware.galaxyforce.models.button.ButtonType;
import com.danosoftware.galaxyforce.models.game.GameModel;
import com.danosoftware.galaxyforce.models.play.PlayModel;
import com.danosoftware.galaxyforce.sprites.game.implementations.FlashingTextImpl;
import com.danosoftware.galaxyforce.sprites.game.interfaces.FlashingText;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GameOverHandler implements PlayModel, ButtonModel {

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* logger tag */
    private static final String TAG = GameOverHandler.class.getSimpleName();

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* reference to controller */
    private final Controller controller;

    /* reference to pause menu buttons */
    private final List<SpriteTextButton> menuButtons;

    /* Stores list of all text to be returned. */
    private final List<Text> allText;

    /* Stores list of all sprites to be returned. */
    private final List<ISprite> allSprites;

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
        this.allText = new ArrayList<>();
        this.allSprites = new ArrayList<>();
        this.modelState = ModelState.GAME_OVER;

        // build menu buttons
        buildButtons();

        // build list of sprites and text objects
        buildSpriteList();
        buildTextList();

        // add flashing game over text
        Text gameOver = Text.newTextRelativePositionX("GAME OVER", TextPositionX.CENTRE, 100 + (4 * 170));
        this.flashingGameOverText = new FlashingTextImpl(gameOver, 0.5f, this);
    }

    private void buildButtons() {

        // remove any existing touch controllers
        controller.clearTouchControllers();

        // create list of menu buttons
        menuButtons.clear();
        addNewMenuButton(3, "PLAY", ButtonType.PLAY);
        addNewMenuButton(2, "OPTIONS", ButtonType.OPTIONS);
        addNewMenuButton(1, "EXIT", ButtonType.MAIN_MENU);
    }

    /*
     * ******************************************************
     * PUBLIC INTERFACE METHODS
     * ******************************************************
     */

    @Override
    public void initialise() {
    }

    @Override
    public List<ISprite> getSprites() {
        return allSprites;
    }

    @Override
    public List<Text> getText() {
        return allText;
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
        // TODO Auto-generated method stub
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
                Log.e(TAG, "Illegal Button Type.");
                throw new IllegalArgumentException("Illegal Button Type.");
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
    public void flashText(Text text, boolean flashState) {
        if (flashState) {
            allText.add(text);
        } else {
            allText.remove(text);
        }
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

    /**
     * Build up a list of all sprites to be returned by the model.
     */
    private void buildSpriteList() {
        allSprites.clear();
        allSprites.addAll(stars);
        for (SpriteTextButton eachButton : menuButtons) {
            allSprites.add(eachButton.getSprite());
        }
    }

    /**
     * Build up a list of all text to be returned by the model.
     */
    private void buildTextList() {
        /*
         * adds text for the buttons. no need to add text for flashing text as
         * this is added and removed to the text list by callbacks.
         */
        allText.clear();
        for (SpriteTextButton eachButton : menuButtons) {
            allText.add(eachButton.getText());
        }
    }
}
