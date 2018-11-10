package com.danosoftware.galaxyforce.game.handlers;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.buttons.impl.PauseButton;
import com.danosoftware.galaxyforce.buttons.interfaces.SpriteButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controller.game.BaseDragModel;
import com.danosoftware.galaxyforce.controller.game.ControllerDrag;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.interfaces.TouchBaseControllerModel;
import com.danosoftware.galaxyforce.controller.utilities.DetectButtonTouch;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.game.beans.SpawnedAlienBean;
import com.danosoftware.galaxyforce.interfaces.GameModel;
import com.danosoftware.galaxyforce.interfaces.Screen;
import com.danosoftware.galaxyforce.screen.ScreenFactory;
import com.danosoftware.galaxyforce.screen.ScreenFactory.ScreenType;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.services.SavedGame;
import com.danosoftware.galaxyforce.sound.Sound;
import com.danosoftware.galaxyforce.sound.SoundEffect;
import com.danosoftware.galaxyforce.sound.SoundEffectBank;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sound.SoundPlayer;
import com.danosoftware.galaxyforce.sound.SoundPlayerSingleton;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.BasePrimary;
import com.danosoftware.galaxyforce.sprites.game.bases.IBase;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.implementations.FlashingTextImpl;
import com.danosoftware.galaxyforce.sprites.game.interfaces.FlashingText;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.refactor.AlienManager;
import com.danosoftware.galaxyforce.sprites.refactor.IAlienManager;
import com.danosoftware.galaxyforce.sprites.refactor.ICollidingSprite;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.OverlapTester;
import com.danosoftware.galaxyforce.vibration.Vibration;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;
import com.danosoftware.galaxyforce.waves.managers.WaveManager;
import com.danosoftware.galaxyforce.waves.managers.WaveManagerImpl;
import com.danosoftware.galaxyforce.waves.utilities.WaveFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class GamePlayHandlerRefactor implements GameHandler {

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    private enum ModelState {
        GET_READY, NEW_BASE, PLAYING
    }

    private static final String TAG = GamePlayHandlerRefactor.class.getSimpleName();

    // number of lives for new game
    private static final int START_LIVES = 3;


    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    // current model state
    private ModelState modelState;

    // handles aliens and waves
    private final IAlienManager alienManager;

    // current wave number
    private int wave;

    // number of lives remaining
    private int lives;

    // main base
    private IBasePrimary primaryBase;

    //TODO make immutable in future?
    private SpriteButton pauseButton;

    // sound player that provide sound effects
    private final SoundPlayer soundPlayer;

    // reference to sound effects
    private final Sound powerUpCollisionSound;

    // provides vibration within the game
    private final Vibration vibrator;

    //TODO make immutable in future
    // provides updates on the base movement
//    private BaseControllerModel baseController;

    // allows the current base controller method (e.g. drag) to be changed
    private Controller controller;

    // used to change the current model state
    private GameModel model;

    // manages other game assets such as missiles and power-ups
    private final IGamePlayAssetsManager assets;

    // reference to the billing service
    private final IBillingService billingService;

    /*
     * Instance variables required in GET_READY state
     */

    // flashing text instance
    private FlashingText flashingText;

    // how long get ready message to appear in seconds
    private final float GET_READY_DELAY = 3f;

    // time since get ready message first appeared
    private float timeSinceGetReady;

    // list of text instances required for get ready message
    private List<Text> getReadyTexts = new ArrayList<>();

    /*
     * Instance variables required in NEW_BASE state
     */

    /*
     * ******************************************************
     *
     * PUBLIC CONSTRUCTOR
     *
     * ******************************************************
     */

    public GamePlayHandlerRefactor(
            GameModel model,
            Controller controller,
            List<Star> stars,
            int wave,
            IBillingService billingService) {

        this.model = model;
        this.controller = controller;
        this.wave = wave;
        this.billingService = billingService;

        /*
         * create wave factory and manager to create lists of aliens on each
         * wave
         */
        WaveFactory waveFactory = new WaveFactory(this);
        WaveManager waveManager = new WaveManagerImpl(waveFactory);
        this.alienManager = new AlienManager(waveManager);



        // set-up sound effects
        this.soundPlayer = SoundPlayerSingleton.getInstance();
        SoundEffectBank soundBank = SoundEffectBankSingleton.getInstance();
        powerUpCollisionSound = soundBank.get(SoundEffect.POWER_UP_COLLIDE);

        this.assets = new GamePlayAssetsManager(stars, soundPlayer);

        // set-up vibration
        this.vibrator = VibrationSingleton.getInstance();

//        this.modelState = ModelState.PLAYING;

        /* reset lives */
        lives = START_LIVES;

        /* create new base at default position */
        addNewBase();

    }

    /*
     * ******************************************************
     * PUBLIC INTERFACE METHODS
     * ******************************************************
     */

    // TODO - can we remove this method?
    @Override
    public void initialise() {

        /* initialise base controllers and buttons */
//        initialiseControllers();

        /* reset lives */
//        lives = START_LIVES;
//
//        /* create new base at default position */
//        addNewBase();
    }

    @Override
    public List<ISprite> getSprites() {

        List<ISprite> gameSprites = new ArrayList<>();
        gameSprites.addAll(assets.getStars());
        gameSprites.addAll(alienManager.allAliens());
        gameSprites.addAll(primaryBase.allSprites());
        gameSprites.addAll(assets.getAliensMissiles());
        gameSprites.addAll(assets.getBaseMissiles());
        gameSprites.addAll(assets.getPowerUps());
        gameSprites.add(pauseButton.getSprite());
        gameSprites.addAll(assets.getFlags());
        gameSprites.addAll(assets.getLives());
        gameSprites.addAll(assets.getEnergyBar());
//        gameSprites.addAll(baseController.getSprites());

        return gameSprites;
    }

    @Override
    public List<ISprite> getPausedSprites() {
        List<ISprite> pausedSprites = new ArrayList<>();
        pausedSprites.addAll(assets.getStars());
        pausedSprites.addAll(alienManager.allAliens());
        pausedSprites.addAll(primaryBase.allSprites());
        pausedSprites.addAll(assets.getAliensMissiles());
        pausedSprites.addAll(assets.getBaseMissiles());
        pausedSprites.addAll(assets.getPowerUps());
        pausedSprites.addAll(assets.getFlags());
        pausedSprites.addAll(assets.getLives());
        pausedSprites.addAll(assets.getEnergyBar());

        return pausedSprites;
    }

    @Override
    public List<Text> getText() {
        return getReadyTexts;
    }

    @Override
    public void update(float deltaTime) {
        switch (modelState) {

            case PLAYING:

                // use the base controller to move the base
//                baseController.update(deltaTime);
                primaryBase.animate(deltaTime);

                // check if level is finished and set-up next level.
                checkLevelFinished();

                break;

            case GET_READY:

                // use the base controller to move the base
//                baseController.update(deltaTime);
                primaryBase.animate(deltaTime);

                // flash get ready message
                updateGetReady(deltaTime);

                break;

            case NEW_BASE:

                // Move new base to starting position.
                // Base will not be controlled by the base controller until in position.
                primaryBase.animate(deltaTime);

                break;

            default:
                String errorMsg = "Illegal Model State : " +  modelState.name();
                Log.e(TAG, errorMsg);
                throw new GalaxyForceException(errorMsg);
        }

        // move game sprites
        alienManager.animate(deltaTime);
        assets.animate(deltaTime);

        // check for game object collision
        collisionDetection();
    }

    @Override
    public IBasePrimary getBase() {
        return primaryBase;
    }

    // called by the base when it is in position and ready to start
    @Override
    public void baseReady() {

        // reset base controller to current base position

//        TODO - is this a problem is we don't reset controller?
// once base in starting position - only touch events will update the controller
        //baseController.reset();

        // if we were previously playing, return to playing state.
//        if (previousModelState == ModelState.PLAYING)
//        {
            modelState = ModelState.PLAYING;
//        }
//        // otherwise set-up next level
//        else
//        {
//            setupLevel();
//        }
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }

    /**
     * triggered by pressing pause button. pass pause request back to model.
      */
    @Override
    public void pause() {
        Log.i(TAG, "'Pause Button' selected. Pause.");
        model.pause();
    }

    /**
     * triggered by resuming game after a pause.
     */
    @Override
    public void resume() {
        Log.i(TAG, "Resume Game.");

        /*
         * re-initialise controllers after game pause as touch controllers will
         * have been lost and chosen controller type may have changed.
         */
        initialiseControllers();
    }

    /**
     * triggered by pressing back button. pass pause request back to model.
     */
    @Override
    public void goBack() {
        Log.i(TAG, "'Back Button' selected. Pause.");
        model.pause();
    }

    @Override
    public IAlien chooseActiveAlien() {
        return alienManager.chooseActiveAlien();
    }

    @Override
    public void spawnAliens(SpawnedAlienBean spawnedAliens) {
        alienManager.spawnAliens(spawnedAliens.getAliens());
        soundPlayer.playSound(spawnedAliens.getSoundEffect());
    }

    @Override
    public void addPowerUp(PowerUpBean powerUp) {
        assets.addPowerUp(powerUp);
        soundPlayer.playSound(powerUp.getSoundEffect());
    }

    @Override
    public void fireBaseMissiles(BaseMissileBean missiles) {
        assets.fireBaseMissiles(missiles);
        soundPlayer.playSound(missiles.getSoundEffect());
    }

    @Override
    public void fireAlienMissiles(AlienMissileBean missiles) {
        assets.fireAlienMissiles(missiles);
        soundPlayer.playSound(missiles.getSoundEffect());
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void energyUpdate(int energy) {
        assets.updateEnergyBar(energy);
    }

    @Override
    public void addLife() {
        lives++;
        assets.setLives(lives);
    }

    @Override
    public void flashText(Text text, boolean flashState) {
        if (flashState) {
            getReadyTexts.add(text);
        } else {
            getReadyTexts.remove(text);
        }
    }

    /*
     * ******************************************************
     * PRIVATE HELPER METHODS
     * ******************************************************
     */

    /**
     * Initialise base controllers and buttons on initialisation or after a game
     * resume when all touch controllers will have been lost.
     */
    private void initialiseControllers() {
        // remove any existing touch controllers
        controller.clearTouchControllers();

        /*
         * get the base controller using currently selected option. if option
         * has not changed then the same base controller will be returned.
         */
        TouchBaseControllerModel baseController = new BaseDragModel(primaryBase);
//        TouchController baseTouchController = new ControllerDrag(baseController);



//       TouchController baseTouchController = BaseControllerFactory.getBaseController(primaryBase);
        // set controller's base controller to use drag
        controller.addTouchController(new ControllerDrag(baseController));

        /*
         * reset controller. important for drag controller as sets target to
         * current base location.
         */
//        baseController.reset();

        /*
         * initialise pause and flip buttons
         */
        pauseButton = new PauseButton(this);

        // add a new button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(pauseButton));
    }

    /**
     * Checks if the level has finished. if so set-up next level.
     * <p>
     * if base was destroyed then add a new base. if the base and last alien
     * were destroyed, the new base method will manage setting up the next
     * level.
     */
    private void checkLevelFinished() {
        if (alienManager.isWaveComplete()) {

            // check user is allowed to play next wave
            if (wave >= GameConstants.MAX_FREE_ZONE && billingService.isNotPurchased(GameConstants.FULL_GAME_PRODUCT_ID)) {
                Log.i(TAG, "Exceeded maximum free zone. Must upgrade.");
                pause();
                Screen unlockFullVersionScreen = ScreenFactory.newScreen(ScreenType.UPGRADE_FULL_VERSION);
                Games.getGame().setReturningScreen(unlockFullVersionScreen);

                /*
                 * the user may not upgrade but we still want to store the
                 * highest level they have reached. normally this occurs
                 * when the next wave is set-up but if the user does not
                 * upgrade, this set-up will never be called.
                 *
                 * since this could involve I/O operation it is best to run
                 * this within this separate thread.
                 */
                final int unlockedWave = wave + 1;
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        SavedGame savedGame = SavedGame.getInstance();
                        int maxLevelUnlocked = savedGame.getGameLevel();
                        if (unlockedWave > maxLevelUnlocked) {
                            savedGame.setGameLevel(unlockedWave);
                            savedGame.persistSavedGame();
                        }
                    }
                });

                /*
                 * must return at this point to prevent next wave being
                 * set-up before upgrade occurs. failure to do so may allow
                 * user to continue to next level even if they don't
                 * upgrade.
                 */
                return;
            }
            // check if all waves have been completed
            else if (wave >= GameConstants.MAX_WAVES) {
                Log.i(TAG, "Game completed.");
                pause();
                Screen gameComplete = ScreenFactory.newScreen(ScreenType.GAME_COMPLETE);
                Games.getGame().setScreen(gameComplete);
                return;
            }
            // advance to next level
            else {
                wave++;
                Log.i(TAG, "Wave: New Wave");
            }
        }

        /*
         * determine whether to set-up next base or next level. must not do both
         * as set-up level will bypass the new base state.
         */
        if (primaryBase.isDestroyed())
        {
            /*
             * if destroyed object primary base then add new base and change
             * model state.
             */
            setupNewBase();
        }
        else if (alienManager.isWaveComplete()) {
            /*
             * if base not destroyed and wave finished then start next level.
             * special case: if the base and last alien was destroyed at the
             * same time, then the new base state will handle the setting up of
             * the next level after it leaves the new base state.
             */
            setupLevel();
        } else if (alienManager.isWaveIdle()) {
            /*
             * only occurs after wave manager construction before any waves have been set-up.
             * used to set-up the first wave after adding the base without triggering all
             * the unwanted wave complete events.
             */
            setupLevel();
        }
    }

    /**
     * checks for sprite collisions.
     */
    private void collisionDetection() {

        for (IAlien eachAlien : alienManager.activeAliens()) {
            // check for base and alien collisions
            for (IBase eachBase : primaryBase.activeBases()) {
                if (checkCollision(eachAlien, eachBase)) {
                    eachBase.onHitBy(eachAlien);
                }
            }

            /*
             * check for base missiles and alien collisions.
             * we also check if base missile and alien have collided before.
             * base missiles can only damage the same alien once.
             * used for missile implementations that do not destroy
             * themselves on initial collision (e.g. laser).
             */
            for (IBaseMissile eachBaseMissile : assets.getBaseMissiles()) {
                if (checkCollision(eachAlien, eachBaseMissile) && !eachBaseMissile.hitBefore(eachAlien)) {
                    eachAlien.onHitBy(eachBaseMissile);
                }
            }
        }

        for (IBase eachBase : primaryBase.activeBases()) {

            // collision detection for base and alien missiles
            for (IAlienMissile eachAlienMissile : assets.getAliensMissiles()) {
                if (checkCollision(eachAlienMissile, eachBase)) {
                    eachBase.onHitBy(eachAlienMissile);
                }
            }

            // collision detection for base and power ups
            for (IPowerUp eachPowerUp : assets.getPowerUps()) {
                if (checkCollision(eachPowerUp, eachBase)) {
                    eachBase.collectPowerUp(eachPowerUp);
                    soundPlayer.playSound(powerUpCollisionSound);
                }
            }
        }
    }

    /**
     * helper method to check if two sprites have collided. returns true if
     * sprites have collided.
     */
    private boolean checkCollision(ICollidingSprite sprite1, ICollidingSprite sprite2) {
        return OverlapTester.overlapRectangles(sprite1.getBounds(), sprite2.getBounds());
    }

    /**
     * initialise next wave.
     */
    private void setupLevel()
    {
        // set-up level flags
        assets.setLevelFlags(wave);

        /*
         * asks alien manager to set-up next level. this is an asynchronous task
         * that can be time-consuming. This current thread can continue until
         * wave is ready.
         */
        alienManager.setUpWave(wave);

        // set up get ready text and put model in get ready state.
        setupGetReady();
    }

    /**
     * Set-up "Get Ready" text for start of a new level
     */
    private void setupGetReady()
    {
        modelState = ModelState.GET_READY;

        int yPosition = 100 + (3 * 170);

        // add text
        Text waveText = Text.newTextRelativePositionX("WAVE " + wave, TextPositionX.CENTRE, yPosition + 64);
        getReadyTexts.add(waveText);

        // create flashing text - gets added to get ready text by callbacks
        Text getReadyText = Text.newTextRelativePositionX("GET READY", TextPositionX.CENTRE, yPosition);
        flashingText = new FlashingTextImpl(getReadyText, 0.5f, this);

        timeSinceGetReady = 0f;
    }

    /**
     * add new base - normally called at start of game or after losing a life.
     */
    private void addNewBase()
    {
        primaryBase = new BasePrimary(this);

        // bind controller to the new base
        initialiseControllers();

        // reduce remaining lives by 1
        lives--;

        // update number of lives
        assets.setLives(lives);

        // reset base controller to current base position
//        baseController.reset();

        // change model state to handle movement of base to starting position
        modelState = ModelState.NEW_BASE;
    }

    /**
     * Update "Get Ready" at start of a new level
     */
    private void updateGetReady(float deltaTime)
    {
        flashingText.update(deltaTime);

        timeSinceGetReady += deltaTime;

        /*
         * Game should stay in "get ready" state until minimum delay has passed
         * and next wave is ready.
         */
        if (timeSinceGetReady > GET_READY_DELAY && alienManager.isWaveReady()) {
            getReadyTexts.clear();
//            if (primaryBase == null) {
//                addNewBase();
//            }
//            else {
                modelState = ModelState.PLAYING;
//            }
        }
    }

    /**
     * Set-up a new a base.
     */
    private void setupNewBase()
    {
        // reset stars direction as last base could have been flipped
//        Star.reset();

        /* if lives left then create new base */
        if (lives > 0)
        {
            /*
             * store previous model state before adding new base and changing
             * state.
             */
//            previousModelState = modelState;

            // create new base at default position and change state
            addNewBase();

            /* if we are playing, add a shield to the base for 2 seconds */
            // TODO we don't need to do this as new base is always shielded
//            if (previousModelState == ModelState.PLAYING)
//            {
//                primaryBase.addShield(2f, 0f);
//            }
        }
        /* if no lives left then game over */
        else
        {
            model.gameOver(wave);
        }
    }

}
