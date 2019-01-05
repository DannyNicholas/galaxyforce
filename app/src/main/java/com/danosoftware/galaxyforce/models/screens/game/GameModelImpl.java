package com.danosoftware.galaxyforce.models.screens.game;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.models.screens.game.handlers.GameHandlerFrameRateDecorator;
import com.danosoftware.galaxyforce.models.screens.game.handlers.GameOverHandler;
import com.danosoftware.galaxyforce.models.screens.game.handlers.GamePlayHandler;
import com.danosoftware.galaxyforce.models.screens.game.handlers.IGameHandler;
import com.danosoftware.galaxyforce.models.screens.game.handlers.PausedHandler;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.SHOW_FPS;

/**
 * contains game model. Model includes game objects, fonts, screen size and game
 * state. Model is updated as game objects move or change state.
 */
public class GameModelImpl implements GameModel {

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* logger tag */
    private static final String TAG = GameModelImpl.class.getSimpleName();

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    private final Game game;

    /* reference to current game state */
    private ModelState modelState;

    private final Controller controller;

    /* current handler looking after the current state of game model */
    private Model modelHandler;

    /*
     * temporary reference to a game handler when paused. used to reinstate
     * paused game.
     */
    private Model pausedGameHandler;

    /* stars sprites to be passed around handlers */
    private final List<Star> stars;

    /* the wave we were playing before last game over */
    private int lastWave;

    // reference to the billing service
    private final IBillingService billingService;

    // sound player service
    private final SoundPlayerService sounds;

    // vibration service
    private final VibrationService vibrator;

    // saved game service
    private final SavedGame savedGame;


    /*
     * ******************************************************
     *
     * PUBLIC CONSTRUCTOR
     *
     * ******************************************************
     */

    public GameModelImpl(
            Game game,
            Controller controller,
            int wave,
            IBillingService billingService,
            SoundPlayerService sounds,
            VibrationService vibrator,
            SavedGame savedGame) {
        this.game = game;
        this.controller = controller;
        this.billingService = billingService;
        this.sounds = sounds;
        this.vibrator = vibrator;
        this.savedGame = savedGame;
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, GameSpriteIdentifier.STAR_ANIMATIONS);
        this.modelHandler = createGameModel(wave);
        this.modelState = ModelState.RUNNING;
    }

    /*
     * ******************************************************
     * PUBLIC INTERFACE METHODS
     * ******************************************************
     */

    @Override
    public List<ISprite> getSprites() {
        return modelHandler.getSprites();
    }

    @Override
    public List<Text> getText() {
        return modelHandler.getText();
    }

    /**
     * update screen game model in playing state. move game objects to next
     * position.
     */
    public void update(float deltaTime) {

        switch (modelState) {
            case RUNNING:

                // normal model state
                break;

            case QUIT:

                // exit game. go to select level screen
                game.changeToScreen(ScreenType.SELECT_LEVEL);
                break;

            case OPTIONS:

                // go to options screen - will return back when done
                game.changeToReturningScreen(ScreenType.OPTIONS);
                this.modelState = ModelState.RUNNING;
                break;

            case PAUSED:

                // keep reference of game handler
                pausedGameHandler = modelHandler;

                // get list of game sprites to show on pause screen
                List<ISprite> pausedSprites;
                if (modelHandler instanceof IGameHandler) {
                    IGameHandler gameHandler = (IGameHandler) modelHandler;
                    pausedSprites = gameHandler.getPausedSprites();

                } else {
                    pausedSprites = new ArrayList<>();
                }

                // create new pause handler
                modelHandler = new PausedHandler(this, controller, pausedSprites);
                this.modelState = ModelState.RUNNING;
                break;

            case RESUME:

                // restore previous game handler
                if (pausedGameHandler == null) {
                    Log.e(TAG, "Unable to resume paused game using handler.");
                    throw new IllegalStateException("Unable to resume paused game using handler..");
                }
                modelHandler = pausedGameHandler;
                modelHandler.resume();
                pausedGameHandler = null;
                this.modelState = ModelState.RUNNING;
                break;

            case PLAYING:

                // after game over, restart on last level reached (if valid)
                if (!WaveUtilities.isValidWave(lastWave)) {
                    this.lastWave = 1;
                }
                modelHandler = createGameModel(lastWave);
                this.modelState = ModelState.RUNNING;
                break;

            case GAME_OVER:

                // create game over handler
                modelHandler = new GameOverHandler(this, controller, stars);
                this.modelState = ModelState.RUNNING;
                break;

            default:

                Log.e(TAG, "Illegal Model State.");
                throw new IllegalArgumentException("Illegal Model State.");

        }

        modelHandler.update(deltaTime);
    }

    @Override
    public void dispose() {
        // no action
    }

    @Override
    public void pause() {

        /*
         * It is possible for pause to be called when whole game is suspended by
         * terminal e.g. when user presses home screen. Only set to pause state
         * if game handler is running. Trying to pause from other handlers (e.g.
         * game over or already paused) could cause unexpected behaviour.
         */
        if (modelHandler instanceof IGameHandler) {
            Log.i(TAG, "State: 'Paused'.");

            // will cause change to pause handler
            this.modelState = ModelState.PAUSED;
        }
    }

    @Override
    public void quit() {
        Log.i(TAG, "State: 'Quit'.");

        // change state to quit
        this.modelState = ModelState.QUIT;
    }

    /*
     * Resume an existing new game
     */
    @Override
    public void resumeAfterPause() {
        Log.i(TAG, "State: 'Resume'.");

        // will cause change to game handler
        this.modelState = ModelState.RESUME;
    }

    @Override
    public void resume() {
        // no action for this model
    }

    /**
     * Start a new game
     */
    @Override
    public void play() {
        Log.i(TAG, "State: 'Play'.");

        // will cause change to game handler
        this.modelState = ModelState.PLAYING;
    }

    @Override
    public void gameOver(int wave) {
        Log.i(TAG, "State: 'Game Over'.");

        this.lastWave = wave;

        // will cause change to game over handler
        this.modelState = ModelState.GAME_OVER;
    }

    @Override
    public void options() {
        Log.i(TAG, "State: 'Options'.");

        // will cause switch to options screen
        this.modelState = ModelState.OPTIONS;
    }

    /**
     * Handle back button presses. If not paused, should trigger a pause. If
     * already paused should exit the game screen.
     */
    @Override
    public void goBack() {
        modelHandler.goBack();
    }

    /**
     * Returns a game play handler model.
     * If we want to show the FPS counter, then return a decorated model to provide this.
     */
    private Model createGameModel(int wave) {
        Model gameHandler = new GamePlayHandler(
                game,
                this,
                controller,
                stars,
                wave,
                billingService,
                sounds,
                vibrator,
                savedGame);

        if (SHOW_FPS) {
            return new GameHandlerFrameRateDecorator((GamePlayHandler) gameHandler);
        }

        return gameHandler;
    }
}
