package com.danosoftware.galaxyforce.model.screens;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.game.handlers.GameOverHandler;
import com.danosoftware.galaxyforce.game.handlers.GamePlayHandler;
import com.danosoftware.galaxyforce.game.handlers.PausedHandler;
import com.danosoftware.galaxyforce.interfaces.GameModel;
import com.danosoftware.galaxyforce.interfaces.Model;
import com.danosoftware.galaxyforce.interfaces.Screen;
import com.danosoftware.galaxyforce.screen.ScreenFactory;
import com.danosoftware.galaxyforce.screen.ScreenFactory.ScreenType;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * contains game model. Model includes game objects, fonts, screen size and game
 * state. Model is updated as game objects move or change state.
 */
public class GameModelImpl implements GameModel
{

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

    /*
     * ******************************************************
     * 
     * PUBLIC CONSTRUCTOR
     * 
     * ******************************************************
     */

    public GameModelImpl(Controller controller, int wave, IBillingService billingService)
    {
        this.controller = controller;
        this.billingService = billingService;

        /* set-up initial random position of stars to be passed around handlers */
        stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, GameSpriteIdentifier.STAR_ANIMATIONS);

        // initial handler - goes straight into game
        this.modelHandler = new GamePlayHandler(this, controller, stars, wave, billingService);

        this.modelState = ModelState.RUNNING;
    }

    /*
     * ******************************************************
     * PUBLIC INTERFACE METHODS
     * ******************************************************
     */

    @Override
    public List<ISprite> getSprites()
    {
        return modelHandler.getSprites();
    }

    @Override
    public List<Text> getText()
    {
        return modelHandler.getText();
    }

    @Override
    public void initialise()
    {
        modelHandler.initialise();
    }

    /**
     * update screen game model in playing state. move game objects to next
     * position.
     */
    public void update(float deltaTime)
    {

        switch (getState())
        {
        case RUNNING:

            // normal model state
            break;

        case QUIT:

            // exit game. go to select level screen
            Screen selectScreen = ScreenFactory.newScreen(ScreenType.SELECT_LEVEL);
            Games.getGame().setScreen(selectScreen);
            break;

        case OPTIONS:

            // go to options screen - will return back when done
            Screen optionsScreen = ScreenFactory.newScreen(ScreenType.OPTIONS);
            Games.getGame().setReturningScreen(optionsScreen);

            // set state back to running so doesn't change screens every time
            setState(ModelState.RUNNING);
            break;

        case PAUSED:

            // keep reference of game handler
            pausedGameHandler = modelHandler;

            // get list of game sprites to show on pause screen
            List<ISprite> pausedSprites;
            if (modelHandler instanceof GameHandler)
            {
                GameHandler gameHandler = (GameHandler) modelHandler;
                pausedSprites = gameHandler.getPausedSprites();

            }
            else
            {
                pausedSprites = new ArrayList<>();
            }

            // create new pause handler
            modelHandler = new PausedHandler(this, controller, pausedSprites);
            modelHandler.initialise();

            // set state back to running so doesn't create new handlers every
            // time.
            setState(ModelState.RUNNING);
            break;

        case RESUME:

            // restore previous game handler
            if (pausedGameHandler == null)
            {
                Log.e(TAG, "Unable to resume paused game using handler.");
                throw new IllegalStateException("Unable to resume paused game using handler..");
            }
            modelHandler = pausedGameHandler;
            modelHandler.resume();
            pausedGameHandler = null;

            // set state back to running so doesn't resume every time.
            setState(ModelState.RUNNING);
            break;

        case PLAYING:

            // after game over, restart on last level reached (if valid)
            if (!WaveUtilities.isValidWave(lastWave))
            {
                this.lastWave = 1;
            }

            modelHandler = new GamePlayHandler(this, controller, stars, lastWave, billingService);
            modelHandler.initialise();

            // set state back to running so doesn't create new handlers every
            // time.
            setState(ModelState.RUNNING);
            break;

        case GAME_OVER:

            // create game over handler
            modelHandler = new GameOverHandler(this, controller, stars);
            modelHandler.initialise();

            // set state back to running so doesn't create new handlers every
            // time.
            setState(ModelState.RUNNING);
            break;

        default:

            Log.e(TAG, "Illegal Model State.");
            throw new IllegalArgumentException("Illegal Model State.");

        }

        modelHandler.update(deltaTime);
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause()
    {

        /*
         * It is possible for pause to be called when whole game is suspended by
         * terminal e.g. when user presses home screen. Only set to pause state
         * if game handler is running. Trying to pause from other handlers (e.g.
         * game over or already paused) could cause unexpected behaviour.
         */
        if (modelHandler instanceof GameHandler)
        {
            Log.i(TAG, "State: 'Paused'.");

            // will cause change to pause handler
            setState(ModelState.PAUSED);
        }
    }

    @Override
    public void quit()
    {
        Log.i(TAG, "State: 'Quit'.");

        // change state to quit
        setState(ModelState.QUIT);
    }

    /*
     * Resume an existing new game
     */
    @Override
    public void resumeAfterPause()
    {
        Log.i(TAG, "State: 'Resume'.");

        // will cause change to game handler
        setState(ModelState.RESUME);
    }

    @Override
    public void resume()
    {
        // no action for this model
    }

    /**
     * Start a new game
     */
    @Override
    public void play()
    {
        Log.i(TAG, "State: 'Play'.");

        // will cause change to game handler
        setState(ModelState.PLAYING);
    }

    @Override
    public void gameOver(int wave)
    {
        Log.i(TAG, "State: 'Game Over'.");

        this.lastWave = wave;

        // will cause change to game over handler
        setState(ModelState.GAME_OVER);
    }

    @Override
    public void options()
    {
        Log.i(TAG, "State: 'Options'.");

        // will cause switch to options screen
        setState(ModelState.OPTIONS);
    }

    /**
     * Handle back button presses. If not paused, should trigger a pause. If
     * already paused should exit the game screen.
     */
    @Override
    public void goBack()
    {
        modelHandler.goBack();
    }

    /*
     * ******************************************************
     * PRIVATE HELPER METHODS
     * ******************************************************
     */

    private void setState(ModelState modelState)
    {
        this.modelState = modelState;
    }

    private ModelState getState()
    {
        return modelState;
    }
}
