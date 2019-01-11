package com.danosoftware.galaxyforce.models.screens.game;

import android.content.res.AssetManager;
import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.buttons.sprite_button.PauseButton;
import com.danosoftware.galaxyforce.buttons.sprite_button.SpriteButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.models.base_touch.BaseDragModel;
import com.danosoftware.galaxyforce.controllers.models.base_touch.TouchBaseControllerModel;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.controllers.touch_base.BaseTouchController;
import com.danosoftware.galaxyforce.controllers.touch_base.ControllerDrag;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.paths.PathFactory;
import com.danosoftware.galaxyforce.flightpath.utilities.PathLoader;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.aliens.AlienManager;
import com.danosoftware.galaxyforce.models.aliens.IAlienManager;
import com.danosoftware.galaxyforce.models.assets.AlienMissilesDto;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;
import com.danosoftware.galaxyforce.models.assets.GamePlayAssetsManager;
import com.danosoftware.galaxyforce.models.assets.IGamePlayAssetsManager;
import com.danosoftware.galaxyforce.models.assets.PowerUpsDto;
import com.danosoftware.galaxyforce.models.assets.SpawnedAliensDto;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.models.screens.flashing.FlashingText;
import com.danosoftware.galaxyforce.models.screens.flashing.FlashingTextImpl;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.ICollidingSprite;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.BasePrimary;
import com.danosoftware.galaxyforce.sprites.game.bases.IBase;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.game.starfield.Star;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;
import com.danosoftware.galaxyforce.utilities.OverlapTester;
import com.danosoftware.galaxyforce.waves.managers.WaveManager;
import com.danosoftware.galaxyforce.waves.managers.WaveManagerImpl;
import com.danosoftware.galaxyforce.waves.utilities.WaveCreationUtils;
import com.danosoftware.galaxyforce.waves.utilities.WaveFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePlayModelImpl implements Model, GameModel {

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    private enum ModelState {
        GET_READY, PLAYING, PAUSE, GAME_OVER
    }

    private static final String TAG = "GamePlayModelImpl";

    // number of lives for new game
    private static final int START_LIVES = 3;

    // how long get ready message appears in seconds
    private static final float GET_READY_DELAY = 3f;


    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    private final Game game;

    // model states
    private ModelState modelState;

    // previous model state - only used to return to previous state after pausing
    private ModelState previousModelState = null;

    // handles aliens and waves
    private final IAlienManager alienManager;

    // current wave number
    private int wave;

    // number of lives remaining
    private int lives;

    // main base
    private IBasePrimary primaryBase;

    private final SpriteButton pauseButton;

    // sound player that provide sound effects
    private final SoundPlayerService sounds;

    // vibration service
    private final VibrationService vibrator;

    // specific controller to move current base
    private final BaseTouchController baseTouchController;

    // manages other game assets such as missiles and power-ups
    private final IGamePlayAssetsManager assets;

    // reference to the billing service
    private final IBillingService billingService;

    // saved game service
    private final SavedGame savedGame;

    /*
     * Instance variables required in GET_READY state
     */

    // get ready text instances
    private Text waveText;
    private FlashingText getReadyFlashingText;

    // time since get ready message first appeared
    private float timeSinceGetReady;

    /*
     * ******************************************************
     *
     * PUBLIC CONSTRUCTOR
     *
     * ******************************************************
     */

    public GamePlayModelImpl(
            Game game,
            Controller controller,
            List<Star> stars,
            int wave,
            IBillingService billingService,
            SoundPlayerService sounds,
            VibrationService vibrator,
            SavedGame savedGame,
            AssetManager assets) {
        this.game = game;
        this.wave = wave;
        this.billingService = billingService;
        this.sounds = sounds;
        this.vibrator = vibrator;
        this.savedGame = savedGame;

        // no text initially
        this.waveText = null;
        this.getReadyFlashingText = null;

        /*
         * create alien manager used to co-ordinate aliens waves.
         */
        this.alienManager = createAlienManager(sounds, vibrator, assets, this);

        /*
         * create asset manager to co-ordinate in-game assets
         */
        this.assets = new GamePlayAssetsManager(stars);

        // reset lives
        this.lives = START_LIVES;

        /*
         * add pause button
         */
        this.pauseButton = new PauseButton(this);
        controller.addTouchController(new DetectButtonTouch(pauseButton));

        /*
         * add base controller
         */
        this.baseTouchController = new ControllerDrag();
        controller.addTouchController(baseTouchController);

        // create new base at default position
        addNewBase();

        // initialise first wave
        setupLevel();
    }

    /*
     * ******************************************************
     * PUBLIC INTERFACE METHODS
     * ******************************************************
     */

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

        return gameSprites;
    }

    private List<ISprite> getPausedSprites() {
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

        List<Text> text = new ArrayList<>();
        if (modelState == ModelState.GET_READY) {
            text.add(waveText);
            text.addAll(getReadyFlashingText.text());
        }

        return text;
    }

    @Override
    public void update(float deltaTime) {
        switch (modelState) {

            case PLAYING:
                primaryBase.animate(deltaTime);
                updatePlayingState();
                break;

            case GET_READY:
                primaryBase.animate(deltaTime);
                updateGetReady(deltaTime);
                break;

            case PAUSE:
                game.changeToGamePausedScreen(getPausedSprites());
                this.modelState = previousModelState;
                break;

            case GAME_OVER:
                game.changeToGameOverScreen(wave);
                break;

            default:
                String errorMsg = "Illegal Model State : " + modelState.name();
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

    @Override
    public void dispose() {
        // no action
    }

    /**
     * pause game.
     */
    @Override
    public void pause() {
        Log.i(TAG, "Pause Game.");

        // set previous model state so we can return to this state after pausing.
        // make sure we're not already paused as this will keep us eternally paused.
        // pause() can also be called by the screen service on screen changes.
        if (modelState != ModelState.PAUSE) {
            this.previousModelState = this.modelState;
        }
        this.modelState = ModelState.PAUSE;
    }

    /**
     * triggered by resuming game after a pause.
     */
    @Override
    public void resume() {
        Log.i(TAG, "Resume Game.");
    }

    /**
     * pause game when back button pressed.
     */
    @Override
    public void goBack() {
        Log.i(TAG, "'Back Button' selected.");
        pause();
    }

    @Override
    public IAlien chooseActiveAlien() {
        return alienManager.chooseActiveAlien();
    }

    @Override
    public void spawnAliens(SpawnedAliensDto spawnedAliens) {
        alienManager.spawnAliens(spawnedAliens.getAliens());
        sounds.play(spawnedAliens.getSoundEffect());
    }

    @Override
    public void addPowerUp(PowerUpsDto powerUp) {
        assets.addPowerUp(powerUp);
        sounds.play(powerUp.getSoundEffect());
    }

    @Override
    public void fireBaseMissiles(BaseMissilesDto missiles) {
        assets.fireBaseMissiles(missiles);
        sounds.play(missiles.getSoundEffect());
    }

    @Override
    public void fireAlienMissiles(AlienMissilesDto missiles) {
        assets.fireAlienMissiles(missiles);
        sounds.play(missiles.getSoundEffect());
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

    /*
     * ******************************************************
     * PRIVATE HELPER METHODS
     * ******************************************************
     */

    /**
     * Checks if the level has finished. if so set-up next level.
     * <p>
     * if base was destroyed then add a new base. if the base and last alien
     * were destroyed, the new base method will manage setting up the next
     * level.
     */
    private void updatePlayingState() {
        if (alienManager.isWaveComplete()) {

            // check user is allowed to play next wave
            if (wave >= GameConstants.MAX_FREE_ZONE && billingService.isNotPurchased(GameConstants.FULL_GAME_PRODUCT_ID)) {
                Log.i(TAG, "Exceeded maximum free zone. Must upgrade.");
                game.changeToReturningScreen(ScreenType.UPGRADE_FULL_VERSION);

                /*
                 * the user may not upgrade but we still want to store the
                 * highest level they have reached. normally this occurs
                 * when the next wave is set-up but if the user does not
                 * upgrade, this set-up will never be called.
                 */
                final int unlockedWave = wave + 1;
                int maxLevelUnlocked = savedGame.getGameLevel();
                if (unlockedWave > maxLevelUnlocked) {
                    savedGame.saveGameLevel(unlockedWave);
                }

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
                game.changeToScreen(ScreenType.GAME_COMPLETE);
                return;
            }
            // advance to next level
            else {
                Log.i(TAG, "Wave: New Wave");
                wave++;
                int maxLevelUnlocked = savedGame.getGameLevel();
                if (wave > maxLevelUnlocked) {
                    Log.i(TAG, "New wave unlocked: " + wave);
                    savedGame.saveGameLevel(wave);
                }
            }
        }

        /*
         * determine whether to set-up next base or next level. must not do both
         * as set-up level will bypass the new base state.
         */
        if (primaryBase.isDestroyed()) {
            /*
             * if destroyed object primary base then add new base and change
             * model state.
             */
            setupNewBase();
        } else if (alienManager.isWaveComplete()) {
            /*
             * if base not destroyed and wave finished then start next level.
             * special case: if the base and last alien was destroyed at the
             * same time, then the new base state will handle the setting up of
             * the next level after it leaves the new base state.
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
                    sounds.play(SoundEffect.POWER_UP_COLLIDE);
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
    private void setupLevel() {
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
    private void setupGetReady() {
        modelState = ModelState.GET_READY;

        int yPosition = 100 + (3 * 170);

        // add text
        this.waveText = Text.newTextRelativePositionX("WAVE " + wave, TextPositionX.CENTRE, yPosition + 64);

        // create flashing text - gets added to get ready text by callbacks
        Text getReadyText = Text.newTextRelativePositionX(
                "GET READY",
                TextPositionX.CENTRE,
                yPosition);
        this.getReadyFlashingText = new FlashingTextImpl(
                Collections.singletonList(getReadyText),
                0.5f);

        timeSinceGetReady = 0f;
    }

    /**
     * add new base - normally called at start of game or after losing a life.
     */
    private void addNewBase() {
        primaryBase = new BasePrimary(this, sounds, vibrator);

        // bind base controller to the new base
        TouchBaseControllerModel baseController = new BaseDragModel(primaryBase);
        baseTouchController.setBaseController(baseController);

        // reduce remaining lives by 1
        lives--;

        // update displayed number of lives
        assets.setLives(lives);

        modelState = ModelState.PLAYING;
    }

    /**
     * Update "Get Ready" flashing text until wave is ready.
     * Used at start of a new wave.
     */
    private void updateGetReady(float deltaTime) {
        getReadyFlashingText.update(deltaTime);

        timeSinceGetReady += deltaTime;

        /*
         * Game should stay in "get ready" state until minimum delay has passed
         * and next wave is ready.
         */
        if (timeSinceGetReady > GET_READY_DELAY && alienManager.isWaveReady()) {
            getReadyFlashingText = null;
            waveText = null;
            modelState = ModelState.PLAYING;
        }
    }

    /**
     * Set-up a new a base.
     */
    private void setupNewBase() {

        /* if lives left then create new base */
        if (lives > 0) {
            // create new base at default position and change state
            addNewBase();
        }
        /* if no lives left then game over */
        else {
            this.modelState = ModelState.GAME_OVER;
        }
    }

    /*
     * create alien manager used to co-ordinate aliens waves.
     */
    private IAlienManager createAlienManager(
            SoundPlayerService sounds,
            VibrationService vibrator,
            AssetManager assets,
            GameModel model) {
        PathLoader pathLoader = new PathLoader(assets);
        PathFactory pathFactory = new PathFactory(pathLoader);
        AlienFactory alienFactory = new AlienFactory(model, sounds, vibrator);
        WaveCreationUtils creationUtils = new WaveCreationUtils(model, alienFactory, pathFactory);
        WaveFactory waveFactory = new WaveFactory(creationUtils);
        WaveManager waveManager = new WaveManagerImpl(waveFactory);

        return new AlienManager(waveManager);
    }
}