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
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.game.implementations.FlashingTextImpl;
import com.danosoftware.galaxyforce.sprites.game.interfaces.FlashingText;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameOverModelImpl implements Model, ButtonModel {

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* logger tag */
    private static final String TAG = "GameOverModelImpl";

    private enum GameOverState {
        RUNNING, NEW_GAME, OPTIONS, EXIT
    }

    ;

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    private final Game game;

    /* reference to pause menu buttons */
    private final List<SpriteTextButton> menuButtons;

    /* reference to current state */
    private GameOverState modelState;

    /* stars sprites */
    private final List<Star> stars;

    /* reference to flashing game over text */
    private final FlashingText flashingGameOverText;

    private final int lastWave;

    /*
     * ******************************************************
     *
     * PUBLIC CONSTRUCTOR
     *
     * ******************************************************
     */

    public GameOverModelImpl(Game game, Controller controller, List<Star> stars, int lastWave) {
        this.game = game;
        this.stars = stars;
        this.lastWave = lastWave;
        this.menuButtons = new ArrayList<>();
        this.modelState = GameOverState.RUNNING;

        // build menu buttons
        addNewMenuButton(controller, 3, "PLAY", ButtonType.PLAY);
        addNewMenuButton(controller, 2, "OPTIONS", ButtonType.OPTIONS);
        addNewMenuButton(controller, 1, "EXIT", ButtonType.MAIN_MENU);

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

            case RUNNING:
                // normal state before any buttons are pressed
                for (Star eachStar : stars) {
                    eachStar.animate(deltaTime);
                }
                break;

            case EXIT:
                // exit game. go to select level screen
                game.changeToScreen(ScreenType.SELECT_LEVEL);
                break;

            case NEW_GAME:
                // after game over, restart on last level reached (if valid)
                final int nextWave;
                if (!WaveUtilities.isValidWave(lastWave)) {
                    nextWave = 1;
                } else {
                    nextWave = lastWave;
                }
                game.changeToGameScreen(nextWave);
                break;

            case OPTIONS:
                // go to options screen - will return back when done
                game.changeToReturningScreen(ScreenType.OPTIONS);
                this.modelState = GameOverState.RUNNING;
                break;

            default:
                Log.e(TAG, "Illegal Model State: " + modelState.name());
                throw new GalaxyForceException("Illegal Model State: " + modelState.name());
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
                this.modelState = GameOverState.EXIT;
                break;
            case PLAY:
                Log.i(TAG, "'Play' selected.");
                this.modelState = GameOverState.NEW_GAME;
                break;
            case OPTIONS:
                Log.i(TAG, "'Options' selected.");
                this.modelState = GameOverState.OPTIONS;
                break;

            default:
                Log.e(TAG, "Illegal Button Type: " + buttonType.name());
                throw new GalaxyForceException("Illegal Button Type: " + buttonType.name());
        }
    }

    @Override
    public void goBack() {
        Log.i(TAG, "'Back Button' selected.");
        this.modelState = GameOverState.EXIT;
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
