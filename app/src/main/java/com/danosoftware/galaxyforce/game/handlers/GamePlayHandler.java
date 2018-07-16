package com.danosoftware.galaxyforce.game.handlers;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.buttons.impl.FlipButton;
import com.danosoftware.galaxyforce.buttons.impl.MovingBaseButton;
import com.danosoftware.galaxyforce.buttons.impl.PauseButton;
import com.danosoftware.galaxyforce.buttons.interfaces.Button;
import com.danosoftware.galaxyforce.buttons.interfaces.SpriteButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controller.game.BaseControllerFactory;
import com.danosoftware.galaxyforce.controller.interfaces.BaseControllerModel;
import com.danosoftware.galaxyforce.controller.interfaces.ControllerBase;
import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.enumerations.Direction;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.enumerations.TextPositionY;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.game.beans.SpawnedAlienBean;
import com.danosoftware.galaxyforce.interfaces.DirectionListener;
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
import com.danosoftware.galaxyforce.sprites.game.implementations.BaseHelper;
import com.danosoftware.galaxyforce.sprites.game.implementations.BaseMain;
import com.danosoftware.galaxyforce.sprites.game.implementations.FlashingTextImpl;
import com.danosoftware.galaxyforce.sprites.game.implementations.HitAlienGlow;
import com.danosoftware.galaxyforce.sprites.game.implementations.HitBaseGlow;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Energy;
import com.danosoftware.galaxyforce.sprites.game.interfaces.EnergyBar;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Flag;
import com.danosoftware.galaxyforce.sprites.game.interfaces.FlashingText;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Life;
import com.danosoftware.galaxyforce.sprites.game.interfaces.MovingSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Sprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlienWithPath;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteBase;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteBaseMissile;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteHitGlow;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpritePowerUp;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteShield;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.IAlien;
import com.danosoftware.galaxyforce.sprites.refactor.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.refactor.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.refactor.IBaseSprite;
import com.danosoftware.galaxyforce.sprites.refactor.ICollidingSprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.OverlapTester;
import com.danosoftware.galaxyforce.utilities.Rectangle;
import com.danosoftware.galaxyforce.vibration.VibrateTime;
import com.danosoftware.galaxyforce.vibration.Vibration;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;
import com.danosoftware.galaxyforce.view.FPSCounter;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.managers.WaveManager;
import com.danosoftware.galaxyforce.waves.managers.WaveManagerImpl;
import com.danosoftware.galaxyforce.waves.utilities.WaveFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

public class GamePlayHandler implements GameHandler
{

    private enum ModelState
    {
        GET_READY, NEW_BASE, PLAYING;
    }

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* logger tag */
    private static final String TAG = PausedHandler.class.getSimpleName();

    /* number of lives for new game */
    private static final int START_LIVES = 3;

    /* base's start y position */
    private static final int BASE_START_Y = 192;

    /* reference to current state */
    private ModelState modelState;

    /*
     * empty base bounds when base has been destroyed - avoids
     * null pointer exceptions when checking if in bounds
     */
     private static final Rectangle BASE_EMPTY_BOUNDS =
            new Rectangle(0, 0, 0,0);

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* width and height of actual usable screen */
    private int width, height;

    // variables used to handle direction change transitions
    private float directionChangeStateTime;
    private boolean changingDirection;

    private enum AlienState
    {
        PLAYING, END_OF_PASS, DESTROYED, UNKNOWN
    };

    // used to store if aliens are active, all at end of pass or all destroyed
    private AlienState alienState;

    /*
     * reference to a list of direction listeners. They will be notified
     * following any changes of direction.
     */
    private List<DirectionListener> directionListeners;

    /* current wave (level) number */
    private int wave;

    /* number of lives remaining */
    private int lives;

    /* reference to the main base - used for base calculations */
    private SpriteBase primaryBase = null;

    /* references to game objects - bases, aliens, missiles, power-ups, stars */
    private List<SpriteAlien> aliens;
    private List<SpriteBase> bases;
    private List<SpriteAlienMissile> aliensMissiles;
    private List<SpriteBaseMissile> baseMissiles;
    private List<SpritePowerUp> powerUps;
    private List<SpriteHitGlow> hits;
    private List<Star> stars;
    private List<Text> allText;
    private List<Flag> flags;
    private List<Energy> energy;
    private List<Life> lifeSprites;
    private SpriteButton pauseButton = null;
    private SpriteButton flipButton = null;
    private List<SpriteShield> shields = null;

    /* is current sub-wave to be repeated */
    private boolean repeatedSubWave;

    /* reference to sound player */
    private final SoundPlayer soundPlayer;

    /* reference to sound effects */
    private final Sound powerUpCollisionSound;
    private final Sound flipSound;
    private final Sound hitAlienSound;
    private final Sound hitBaseSound;

    /* reference to vibrator */
    private final Vibration vibrator;

    private Button movingBaseButton = null;

    /*
     * Stores list of all sprites to be returned. List will need to be
     * re-created if any sprites are added/deleted
     */
    private List<Sprite> allSprites = null;

    // set to true if list of sprites should be re-built before displaying them
    private boolean reBuildSprites = false;

    // set to true if list of text objects should be re-built before displaying
    // them
    private boolean reBuildText = false;

    BaseControllerModel baseController = null;

    private ControllerBase controller = null;
    private GameModel model = null;

    /* FPS counter - for de-buggining only */
    private FPSCounter fpsCounter = null;
    private int currentFps = 0;

    // reference to base's energy bar
    private EnergyBar energyBar = null;

    /* Wave Factory and Manager used to create lists of aliens */
    private final WaveManager waveManager;

    /*
     * TODO - TEMP references to text elements - may need refining for final
     * game. References must be kept to allow text strings to be changed during
     * the game.
     */
    Text tempFps = null;
    Text tempWave = null;

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
    private List<Text> getReadyTexts = new ArrayList<Text>();

    /*
     * Instance variables required in NEW_BASE state
     */

    // previous model state before adding a new base
    private ModelState previousModelState;

    // reference to the billing service
    private final IBillingService billingService;

    /*
     * ******************************************************
     * 
     * PUBLIC CONSTRUCTOR
     * 
     * ******************************************************
     */

    public GamePlayHandler(GameModel model, ControllerBase controller, List<Star> stars, int wave, IBillingService billingService)
    {

        this.model = model;
        this.controller = controller;
        this.stars = stars;
        this.wave = wave;

        this.billingService = billingService;

        /* width and height of screen size. */
        width = GameConstants.GAME_WIDTH;
        height = GameConstants.GAME_HEIGHT;

        /*
         * create wave factory and manager to create lists of aliens on each
         * wave
         */
        WaveFactory waveFactory = new WaveFactory(this);
        waveManager = new WaveManagerImpl(waveFactory);

        /* counter used to report frames per second */
        fpsCounter = new FPSCounter();

        /* initialise with empty list of listeners */
        directionListeners = new ArrayList<DirectionListener>();

        /* set-up empty lists for game objects */
        aliens = new ArrayList<SpriteAlien>();
        bases = new ArrayList<SpriteBase>();
        shields = new ArrayList<SpriteShield>();
        aliensMissiles = new ArrayList<SpriteAlienMissile>();
        baseMissiles = new ArrayList<SpriteBaseMissile>();
        powerUps = new ArrayList<SpritePowerUp>();
        hits = new ArrayList<SpriteHitGlow>();
        flags = new ArrayList<Flag>();
        energy = new ArrayList<Energy>();
        lifeSprites = new ArrayList<Life>();
        allSprites = new ArrayList<Sprite>();

        // initial state until checks completed
        alienState = AlienState.UNKNOWN;

        /*
         * current wave should not be repeated - default behaviour that can be
         * over-ridden by sub-wave
         */
        repeatedSubWave = false;

        /* create sound player */
        this.soundPlayer = SoundPlayerSingleton.getInstance();

        /* create reference to sound effects */
        SoundEffectBank soundBank = SoundEffectBankSingleton.getInstance();
        powerUpCollisionSound = soundBank.get(SoundEffect.POWER_UP_COLLIDE);
        flipSound = soundBank.get(SoundEffect.BASE_FLIP);
        hitAlienSound = soundBank.get(SoundEffect.ALIEN_HIT);
        hitBaseSound = soundBank.get(SoundEffect.BASE_HIT);

        /* create reference to vibrator */
        this.vibrator = VibrationSingleton.getInstance();

        this.modelState = ModelState.PLAYING;
    }

    /*
     * ******************************************************
     * PUBLIC INTERFACE METHODS
     * ******************************************************
     */

    @Override
    public void initialise()
    {

        /* initialise base controllers and buttons */
        initialiseControllers();

        movingBaseButton = new MovingBaseButton(this, controller);

        /* creates base's energy bar */
        energyBar = new EnergyBar();

        /* reset lives */
        lives = START_LIVES;

        /* clear game objects */
        aliens.clear();
        bases.clear();
        shields.clear();
        aliensMissiles.clear();
        baseMissiles.clear();
        powerUps.clear();
        hits.clear();
        flags.clear();
        energy.clear();
        lifeSprites.clear();

        /*
         * current wave should not be repeated - default behaviour that can be
         * over-ridden by sub-wave
         */
        repeatedSubWave = false;

        // clear primary base
        primaryBase = null;

        /* create new base at default position */
        addNewBase();

        /* create text elements */
        tempFps = Text.newTextRelativePositionBoth("FPS 0", TextPositionX.CENTRE, TextPositionY.TOP);
        // tempWave = Text.newTextRelativePositionX("LIVES " + lives + ". WAVE "
        // + wave + ".", TextPositionX.LEFT, 750);
        allText = new ArrayList<Text>();
        allText.add(tempFps);
        // allText.add(tempWave);

        /* reset stars back to normal */
        Star.reset();

        // reset direction change transitions
        directionChangeStateTime = 0f;
        changingDirection = false;

        // state = State.PLAYING;

        // setupLevel();
    }

    @Override
    public List<Sprite> getSprites()
    {
        // only re-build sprites when something has changed
        if (reBuildSprites)
        {
            buildSpriteList();
        }

        return allSprites;
    }

    @Override
    public List<Sprite> getPausedSprites()
    {
        List<Sprite> pausedSprites = new ArrayList<Sprite>();
        pausedSprites.addAll(stars);
        pausedSprites.addAll(aliens);
        pausedSprites.addAll(bases);
        pausedSprites.addAll(shields);
        pausedSprites.addAll(aliensMissiles);
        pausedSprites.addAll(baseMissiles);
        pausedSprites.addAll(powerUps);
        pausedSprites.addAll(hits);

        pausedSprites.addAll(flags);
        pausedSprites.addAll(lifeSprites);
        pausedSprites.addAll(energy);

        return pausedSprites;
    }

    @Override
    public List<Text> getText()
    {
        // only re-build text items when something has changed
        if (reBuildText)
        {
            buildTextList();
        }

        return allText;
    }

    @Override
    public void update(float deltaTime)
    {
        switch (modelState)
        {

        case PLAYING:

            // update model if changing direction
            if (changingDirection)
            {
                updateDirectionChange(deltaTime);
            }

            // remove any destroyed objects
            removeDestroyedObjects();

            /*
             * check level is finished and set-up next level. should only be
             * called in playing state.
             */
            checkLevelFinished();

            // check for game object collision
            collisionDetection();

            // move all game sprites
            moveSprites(deltaTime);

            // check if base should fire new missile
            // fireBaseMissile(deltaTime);

            // check if aliens should fire new missile
            // fireMissile(deltaTime);

            // check if aliens should spawn
            // spawnAlien(deltaTime);

            // update the base controller model
            baseController.update(deltaTime);

            // move base by current weighting
            moveBase(baseController.getWeightingX(), baseController.getWeightingY(), deltaTime);

            // move stars
            moveStars(deltaTime);

            break;

        case GET_READY:

            // update model if changing direction
            if (changingDirection)
            {
                updateDirectionChange(deltaTime);
            }

            // remove any destroyed objects - needed to remove
            // power-ups/replaced helper bases etc.
            removeDestroyedObjects();

            // check for game object collision
            collisionDetection();

            // flash get ready message
            updateGetReady(deltaTime);

            // move all game sprites
            moveSprites(deltaTime);

            // update the base controller model
            baseController.update(deltaTime);

            // move base by current weighting
            moveBase(baseController.getWeightingX(), baseController.getWeightingY(), deltaTime);

            // move stars
            moveStars(deltaTime);

            break;

        case NEW_BASE:

            // move new base to starting position
            updateNewBase(deltaTime);

            // remove any destroyed objects - needed to remove
            // power-ups/replaced helper bases etc.
            removeDestroyedObjects();

            // check for game object collision
            collisionDetection();

            /*
             * move all game sprites - if base was destroyed during a level
             * aliens will still need to move
             */
            moveSprites(deltaTime);

            // move stars
            moveStars(deltaTime);

            break;

        default:
            Log.e(TAG, "Illegal Model State.");
            throw new IllegalArgumentException("Illegal Model State.");

        }

        // if (resetControllers)
        // {
        //
        // OptionController optionController =
        // Configurations.getControllerType();
        // controller.clearTouchControllers();
        //
        // switch (optionController)
        // {
        // case ACCELEROMETER:
        // baseController = new BaseTiltModel(controller);
        // break;
        // case DRAG:
        // baseController = new BaseDragModel(this, controller);
        // break;
        // case JOYSTICK:
        // baseController = new BaseJoystickModel(controller);
        // break;
        // default:
        // throw new
        // IllegalArgumentException("Unrecognised OptionController found: '" +
        // optionController + "'.");
        // }
        //
        // pauseButton = new PauseButton(this, controller,
        // SpriteProperty.PAUSE_BUTTON_UP, SpriteProperty.PAUSE_BUTTON_DOWN);
        // flipButton = new FlipButton(this, controller,
        // SpriteProperty.FLIP_BUTTON_UP, SpriteProperty.FLIP_BUTTON_DOWN);
        // movingBaseButton = new MovingBaseButton(this, controller);
        //
        // break;
        //
        // buildSpriteList();
        // buildTextList();
        //
        // resetControllers = false;
        // }

        // // update model if changing direction
        // if (changingDirection)
        // {
        // updateDirectionChange(deltaTime);
        // }
        //
        // // remove any destroyed objects
        // removeDestroyedObjects();
        //
        // // if all aliens destroyed and still playing then start next level
        // if (aliens.size() == 0 && powerUps.size() == 0)
        // {
        // setupNextLevel();
        // }
        //
        // // check for game object collision
        // collisionDetection();
        //
        // // move all game sprites
        // moveSprites(deltaTime);
        //
        // // check if base should fire new missile
        // // fireBaseMissile(deltaTime);
        //
        // // check if aliens should fire new missile
        // // fireMissile(deltaTime);
        //
        // // check if aliens should spawn
        // // spawnAlien(deltaTime);
        //
        // // move base by current weighting
        // moveBase(baseController.getWeightingX(),
        // baseController.getWeightingY(), deltaTime);
        //
        // // move stars
        // moveStars(deltaTime);

        /* update fps for development. only update if fps has changed */
        int fps = fpsCounter.getValue();
        if (fps != currentFps)
        {
            currentFps = fps;
            tempFps = Text.newTextRelativePositionBoth("FPS " + fps, TextPositionX.CENTRE, TextPositionY.TOP);
            reBuildText = true;
        }
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

    @Override
    // triggered by pressing pause button. pass pause request back to model.
    public void pause()
    {
        Log.i(TAG, "'Pause Button' selected. Pause.");
        model.pause();
    }

    @Override
    // triggered by resuming game after a pause.
    public void resume()
    {
        Log.i(TAG, "Resume Game.");

        /*
         * re-initialise controllers after game pause as touch controllers will
         * have been lost and chosen controller type may have changed.
         */
        initialiseControllers();
    }

    @Override
    // triggered by pressing back button. pass pause request back to model.
    public void goBack()
    {
        Log.i(TAG, "'Back Button' selected. Pause.");
        model.pause();
    }

    @Override
    public float getBaseX()
    {
        return (primaryBase == null ? 0 : primaryBase.getX());
    }

    @Override
    public float getBaseY()
    {
        return (primaryBase == null ? 0 : primaryBase.getY());
    }

    @Override
    public SpriteBase getBase()
    {
        return (primaryBase == null ? null : primaryBase);
    }

    @Override
    public void flipBase()
    {
        /*
         * only change direction if not already changing direction and primary
         * base is active.
         */
        if (!changingDirection && primaryBase.isActive())
        {
            // notify any listeners of direction change
            notifyStartDirectionChangeListeners();

            changingDirection = true;
            directionChangeStateTime = 0f;

            /*
             * flip primary base - primary base will also flip any active helper
             * bases
             */
            primaryBase.flip();
            soundPlayer.playSound(flipSound);
        }
    }

     @Override
     public Rectangle getBaseTouchBounds()
     {
         // if base has been destroyed return an empty bounds
         return (primaryBase == null ? BASE_EMPTY_BOUNDS : primaryBase.getTouchBounds());
     }

    /*
     * add a new direction listener to be notified following any direction
     * changes
     */
    @Override
    public void addDirectionListener(DirectionListener listener)
    {
        directionListeners.add(listener);
    }

    @Override
    public void addPowerUp(PowerUpBean powerUp)
    {
        powerUps.add(powerUp.getPowerUp());
        soundPlayer.playSound(powerUp.getSoundEffect());

        // refresh displayed sprites
        reBuildSprites = true;
    }

    @Override
    public void addShield(SpriteShield shield)
    {
        // add new shield sprite
        shields.add(shield);

        // refresh displayed sprites
        reBuildSprites = true;
    }

    @Override
    public void removeShield(SpriteShield shield)
    {
        // remove shield sprite
        shields.remove(shield);

        // refresh displayed sprites
        reBuildSprites = true;
    }

    @Override
    public SpriteAlien chooseActiveAlien()
    {
        // make a list of active aliens
        List<SpriteAlien> activeAliens = new ArrayList<SpriteAlien>();

        for (SpriteAlien alien : aliens)
        {
            if (alien.isActive())
            {
                activeAliens.add(alien);
            }
        }

        // if no aliens are active return null
        if (activeAliens.size() == 0)
        {
            return null;
        }

        // choose a random active alien
        int index = (int) (Math.random() * activeAliens.size());

        return activeAliens.get(index);
    }

    @Override
    public void fireBaseMissiles(BaseMissileBean missiles)
    {
        // create new missile and add to missile list.
        baseMissiles.addAll(missiles.getMissiles());

        // play sound effect
        soundPlayer.playSound(missiles.getSoundEffect());

        // refresh displayed sprites
        reBuildSprites = true;
    }

    @Override
    public void fireAlienMissiles(AlienMissileBean missiles)
    {
        // add new missiles to list.
        aliensMissiles.addAll(missiles.getMissiles());

        // play sound effect
        soundPlayer.playSound(missiles.getSoundEffect());

        // refresh displayed sprites
        reBuildSprites = true;
    }

    @Override
    public void spawnAliens(SpawnedAlienBean spawnedAliens)
    {
        if (aliens.size() > 0)
        {
            // spawned aliens should appear behind existing sprites so are added
            // to beginning of list
            this.aliens.addAll(0, spawnedAliens.getAliens());
        }
        else
        {
            this.aliens.addAll(spawnedAliens.getAliens());
        }

        // play sound effect
        soundPlayer.playSound(spawnedAliens.getSoundEffect());

        // refresh displayed sprites
        reBuildSprites = true;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void flashText(Text text, boolean flashState)
    {
        if (flashState)
        {
            getReadyTexts.add(text);
        }
        else
        {
            getReadyTexts.remove(text);
        }

        reBuildText = true;
    }

    /*
     * ******************************************************
     * PRIVATE HELPER METHODS
     * ******************************************************
     */

    private void moveStars(float deltaTime)
    {
        for (Star eachStar : stars)
        {
            eachStar.move(deltaTime);
        }
    }

    /**
     * Initialise base controllers and buttons on initialisation or after a game
     * resume when all touch controllers will have been lost.
     */
    private void initialiseControllers()
    {
        // remove any existing touch controllers
        controller.clearTouchControllers();

        /*
         * get the base controller using currently selected option. if option
         * has not changed then the same base controller will be returned.
         */
        baseController = BaseControllerFactory.getBaseController(controller, this);

        /*
         * reset controller. important for drag controller as sets target to
         * current base location.
         */
        baseController.reset();

        /*
         * initialise pause and flip buttons
         */
        pauseButton = new PauseButton(this, controller, GameSpriteIdentifier.PAUSE_BUTTON_UP, GameSpriteIdentifier.PAUSE_BUTTON_DOWN);
        flipButton = new FlipButton(this, controller, GameSpriteIdentifier.FLIP_BUTTON_UP, GameSpriteIdentifier.FLIP_BUTTON_DOWN);
    }

    /**
     * Build up a list of all sprites to be returned by the game model.
     */
    private void buildSpriteList()
    {
        allSprites.clear();

        allSprites.addAll(stars);
        allSprites.addAll(aliens);
        allSprites.addAll(bases);
        allSprites.addAll(shields);
        allSprites.addAll(aliensMissiles);
        allSprites.addAll(baseMissiles);
        allSprites.addAll(powerUps);
        allSprites.addAll(hits);
        allSprites.add(pauseButton.getSprite());
        allSprites.add(flipButton.getSprite());
        allSprites.addAll(flags);
        allSprites.addAll(lifeSprites);
        allSprites.addAll(energy);
        allSprites.addAll(baseController.getSprites());

        reBuildSprites = false;
    }

    private void buildTextList()
    {
        allText.clear();

        allText.add(tempFps);
        allText.addAll(getReadyTexts);

        reBuildText = false;
    }

    /**
     * handles updates if currently changing direction - model handles direction
     * timings and animations for bases and stars.
     * 
     * @param deltaTime
     */
    private void updateDirectionChange(float deltaTime)
    {
        // increase direction change state time by delta
        directionChangeStateTime += deltaTime;

        // calculate transistion number
        int directionChangeTransitionNumber = (int) (directionChangeStateTime / GameConstants.DIRECTION_CHANGE_TRANSITION_TIME);

        /* update stars speed and direction */
        Star.updateDirectionChange(directionChangeStateTime);

        /*
         * update base direction change animations - primary base will handle
         * direction changes for any helpers
         */
        if (primaryBase.isActive())
        {
            primaryBase.updateDirectionChange(directionChangeStateTime);
        }

        /*
         * once transition number has reached the max number of transitions
         * expected, reset changeDirection and tell bases to update their
         * direction.
         */
        if (directionChangeTransitionNumber >= GameConstants.DIRECTION_CHANGE_TRANSITIONS - 1)
        {
            changingDirection = false;

            /*
             * updates any direction change listeners - normally this the drag
             * controller to update where the drag point is positioned
             */
            notifyDirectionListeners();

            /* complete base flip - primary base will handle any helpers */
            primaryBase.flipComplete();

            /* complete stars direction change */
            Star.completeDirectionChange();
        }
    }

    /* notify all direction listeners that direction has changed */
    private void notifyDirectionListeners()
    {
        for (DirectionListener aListener : directionListeners)
        {
            aListener.completeDirectionChange();
        }

        // rebuild sprites to update any controller sprites
        reBuildSprites = true;
    }

    /* notify all direction listeners that direction is starting */
    private void notifyStartDirectionChangeListeners()
    {
        for (DirectionListener aListener : directionListeners)
        {
            aListener.startDirectionChange();
        }

        // rebuild sprites to update any controller sprites
        reBuildSprites = true;
    }

    /**
     * Routine removes all destroyed game objects Routine also counts and
     * returns the number of active aliens objects
     */
    public void removeDestroyedObjects()
    {
        // keep a count of how many aliens have finished their pass
        int finishedAliens = 0;

        // remove destroyed aliens
        Iterator<SpriteAlien> alienList = aliens.iterator();
        while (alienList.hasNext())
        {
            SpriteAlien alien = alienList.next();
            if (alien.isEndOfPass())
            {
                // count number of aliens at end of pass
                finishedAliens++;
            }
            else if (alien.isDestroyed())
            {
                // remove alien from list
                alienList.remove();

                // refresh displayed sprites
                reBuildSprites = true;
            }
        }

        // have all aliens finished pass or been destoyed
        if (aliens.size() == 0)
        {
            alienState = AlienState.DESTROYED;
        }
        else if (aliens.size() == finishedAliens)
        {
            alienState = AlienState.END_OF_PASS;
        }
        else
        {
            alienState = AlienState.PLAYING;
        }

        // remove destroyed base missiles
        Iterator<SpriteBaseMissile> baseMissileList = baseMissiles.iterator();
        while (baseMissileList.hasNext())
        {
            SpriteBaseMissile baseMissile = baseMissileList.next();
            if (baseMissile.isDestroyed())
            {
                // remove missile from list
                baseMissileList.remove();

                // refresh displayed sprites
                reBuildSprites = true;
            }
        }

        // remove destroyed alien missiles
        Iterator<SpriteAlienMissile> alienMissileList = aliensMissiles.iterator();
        while (alienMissileList.hasNext())
        {
            SpriteAlienMissile alienMissile = alienMissileList.next();
            if (alienMissile.isDestroyed())
            {
                // remove missile from list
                alienMissileList.remove();

                // refresh displayed sprites
                reBuildSprites = true;
            }
        }

        // remove destroyed power-ups
        Iterator<SpritePowerUp> powerUpList = powerUps.iterator();
        while (powerUpList.hasNext())
        {
            SpritePowerUp powerUp = powerUpList.next();
            if (powerUp.isDestroyed())
            {
                // remove missile from list
                powerUpList.remove();

                // refresh displayed sprites
                reBuildSprites = true;
            }
        }

        // remove destroyed hits
        Iterator<SpriteHitGlow> hitsList = hits.iterator();
        while (hitsList.hasNext())
        {
            SpriteHitGlow hit = hitsList.next();
            if (hit.isDestroyed())
            {
                // remove hit from list
                hitsList.remove();

                // refresh displayed sprites
                reBuildSprites = true;
            }
        }

        // remove destroyed base
        Iterator<SpriteBase> baseList = bases.iterator();
        while (baseList.hasNext())
        {
            SpriteBase aBase = baseList.next();
            if (aBase.isDestroyed())
            {
                // remove base from list
                baseList.remove();

                // refresh displayed sprites
                reBuildSprites = true;
            }
        }
    }

    /**
     * Checks if the level has finished. if so set-up next level.
     * 
     * if base was destroyed then add a new base. if the base and last alien
     * were destroyed, the new base method will manage setting up the next
     * level.
     */
    private void checkLevelFinished()
    {
        boolean waveFinished = false;

        /*
         * if no aliens left then decide what action to take.
         */
        if (alienState == AlienState.DESTROYED || alienState == AlienState.END_OF_PASS)
        {
            if (repeatedSubWave && alienState == AlienState.END_OF_PASS)
            {
                /*
                 * if there are finished aliens that were not destroyed and
                 * current sub-wave should be repeated until all aliens are
                 * destroyed then reset aliens and repeat sub-wave.
                 */
                List<SpriteAlienWithPath> aliensToRepeat = new ArrayList<SpriteAlienWithPath>();

                Float minDelay = null;
                for (SpriteAlien anAlien : aliens)
                {
                    /*
                     * if aliens with sprite path then we want restarted aliens
                     * to start immediately and not have to wait for their delay
                     * to expire. find the lowest time delay and reduce all
                     * aliens by this offset so the first alien starts
                     * immediately.
                     */
                    if (anAlien instanceof SpriteAlienWithPath)
                    {
                        SpriteAlienWithPath alienWithPath = (SpriteAlienWithPath) anAlien;
                        aliensToRepeat.add(alienWithPath);

                        float timeDelay = alienWithPath.getTimeDelay();

                        if (minDelay == null || timeDelay < minDelay)
                        {
                            minDelay = timeDelay;
                        }
                    }
                }

                /*
                 * reduce offset of all repeated aliens with path by minimum
                 * offset. causes first alien to start immediately.
                 */
                for (SpriteAlienWithPath anAlienToRepeat : aliensToRepeat)
                {
                    anAlienToRepeat.reset(minDelay);
                }

                Log.i(TAG, "Wave: Reset SubWave");
            }
            else if (waveManager.hasNext())
            {
                /*
                 * if there is another sub-wave, get it and assign new list to
                 * aliens.
                 */
                SubWave nextSubWave = waveManager.next();
                aliens = nextSubWave.getAliens();
                repeatedSubWave = nextSubWave.isWaveRepeated();

                /*
                 * The aliens can jump positions when the wave is started if
                 * there has been a time delay between construction and starting
                 * moves. To avoid this jump, reset each alien so it re-starts
                 * it's path.
                 */
                for (SpriteAlien anAlien : aliens)
                {
                    if (anAlien.isActive())
                    {
                        anAlien.setVisible(true);
                    }

                    if (anAlien instanceof SpriteAlienWithPath)
                    {
                        SpriteAlienWithPath alienWithPath = (SpriteAlienWithPath) anAlien;
                        alienWithPath.reset(0);
                    }
                }

                Log.i(TAG, "Wave: Next SubWave");
            }
            else
            {
                /*
                 * otherwise wave is finished so we should advance to next wave
                 */
                waveFinished = true;

                // check user is allowed to play next wave
                if (wave >= GameConstants.MAX_FREE_ZONE && billingService.isNotPurchased(GameConstants.FULL_GAME_PRODUCT_ID))
                {
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
                    Executors.newSingleThreadExecutor().execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            SavedGame savedGame = SavedGame.getInstance();
                            int maxLevelUnlocked = savedGame.getGameLevel();
                            if (unlockedWave > maxLevelUnlocked)
                            {
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
                else if (wave >= GameConstants.MAX_WAVES)
                {
                    Log.i(TAG, "Game completed.");
                    pause();
                    Screen gameComplete = ScreenFactory.newScreen(ScreenType.GAME_COMPLETE);
                    Games.getGame().setScreen(gameComplete);
                    return;
                }
                // advance to next level
                else
                {
                    wave++;
                    Log.i(TAG, "Wave: New Wave");
                }
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
        else if (waveFinished)
        {
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
     * checks for sprite collisions and changes state of objects. aliens
     * colliding with bases. base missiles colliding with aliens.
     */
    private void collisionDetection()
    {

        List<IAlien> activeAliens = null;
        List<IBaseSprite > activebases = null;
        List<IBaseMissile> activeBaseMissiles = null;
        List<IAlienMissile> activeAlienMissiles = null;


        for (IAlien eachAlien : activeAliens)
        {
                for (IBaseSprite eachBase : activebases)
                {

                    // base and alien collide
                    if (checkCollision(eachAlien, eachBase))
                    {
                        eachBase.onHitBy(eachAlien);


                        // base and alien lose energy
//                        eachBase.setExploding();
//                        eachAlien.loseEnergy(eachBase.hitEnergy());

                        // base has lost energy. get energy sprites representing
                        // new energy bar level.
//                        energy = energyBar.getEnergyBar();
//                        reBuildSprites = true;

                        // find the collision point
//                        Point collisionPoint = collisionPoint(eachAlien, eachBase);
//
//                        // hit on base - show hit if base is still active
//                        baseHit(collisionPoint.getX(), collisionPoint.getY(), eachBase.isActive());
//
//                        // hit on alien - show hit if alien is still active
//                        alienHit(collisionPoint.getX(), collisionPoint.getY(), eachAlien.isActive());
                    }
                }

                for (IBaseMissile eachBaseMissile : activeBaseMissiles)
                {
                    // base missile and alien collide
                    if (checkCollision(eachAlien, eachBaseMissile))
                    {
                        eachAlien.onHitBy(eachBaseMissile);

                        /*
                         * base missile and alien have collided but we need to
                         * check if they have hit before if so, we do not
                         * register the hit twice for the same missile. used for
                         * missile implementations that do not destroy
                         * themselves on initial impact.
                         */
                        if (!eachBaseMissile.hitBefore(eachAlien))
                        {
                            // base missile and alien lose energy
                            eachAlien.onHitBy(eachBaseMissile);

//                            eachAlien.loseEnergy(eachBaseMissile.hitEnergy());
//                            eachBaseMissile.loseEnergy(eachAlien.hitEnergy());

//                            // find the collision point
//                            Point collisionPoint = collisionPointBaseMissile(eachBaseMissile, eachAlien);
//
//                            // hit on alien - show hit if alien is still active
//                            alienHit(collisionPoint.getX(), collisionPoint.getY(), eachAlien.isActive());
                        }

                    }
                }
        }

        // list to contain list of power-ups to activate
        List<PowerUpType> powerUpActivation = new ArrayList<PowerUpType>();

        for (IBaseSprite eachBase : activebases)
        {
                // collision detection for base and alien missiles
                for (IAlienMissile eachAlienMissile : activeAlienMissiles)
                {
                    // only check collisions for active alien missiles
                    if (checkCollision(eachAlienMissile, eachBase))
                    {

                        eachBase.onHitBy(eachAlienMissile);

                        // base and alien missile energy
//                        eachBase.loseEnergy(eachAlienMissile.hitEnergy());
//                        eachAlienMissile.loseEnergy(eachBase.hitEnergy());

                        // find the collision point
//                        Point collisionPoint = collisionPointAlienMissile(eachAlienMissile, eachBase);
//
//                        // hit on base - show hit if base is still active
//                        baseHit(collisionPoint.getX(), collisionPoint.getY(), eachBase.isActive());

                        // base has lost energy. get energy sprites
                        // representing
                        // new energy bar level.
                        energy = energyBar.getEnergyBar();
                        reBuildSprites = true;
                    }
                }

                // collision detection for base and power ups
                for (SpritePowerUp eachPowerUp : powerUps)
                {
                    // only check collisions for active power ups
                    //if (checkCollision(eachPowerUp, eachBase))
                    // FIXME
                        if (false)
                    {
                        eachBase.collectPowerUp(eachPowerUp.getPowerUpType());

                        // add power-up to activate
                        powerUpActivation.add(eachPowerUp.getPowerUpType());

                        // remove power-up
                        eachPowerUp.setDestroyed();
                        reBuildSprites = true;
                    }
                }
        }

        /*
         * activate all required power ups. this must be done outside the above
         * loop to avoid ConcurrentModificationException. this can be caused by
         * adding new helper bases while iterating through existing bases.
         */
        for (PowerUpType powerUp : powerUpActivation)
        {
            soundPlayer.playSound(powerUpCollisionSound);
            activatePowerUp(powerUp);
        }
    }

    private void alienHit(int x, int y, boolean showHit)
    {
        if (showHit)
        {
            // create flash to indicate alien has been hit
            hits.add(new HitAlienGlow(x, y));
            reBuildSprites = true;

            soundPlayer.playSound(hitAlienSound);
            vibrator.vibrate(VibrateTime.TINY);
        }

        // TODO create sound and vibrate to indicate hit
    }

    private void baseHit(int x, int y, boolean showHit)
    {
        if (showHit)
        {
            // create flash to indicate alien has been hit
            hits.add(new HitBaseGlow(x, y));
            reBuildSprites = true;

            soundPlayer.playSound(hitBaseSound);
            vibrator.vibrate(VibrateTime.TINY);
        }
    }

    /**
     * helper method to check if two sprites have collided. returns true if
     * sprites have collided.
     */
    private boolean checkCollision(MovingSprite sprite1, MovingSprite sprite2)
    {
        return OverlapTester.overlapRectangles(sprite1.getBounds(), sprite2.getBounds());
    }

    private boolean checkCollision(ICollidingSprite sprite1, ICollidingSprite sprite2)
    {
        return false;
    }

    /**
     * helper method to check find the point at which two sprites collide. This
     * method has been optimised to assume that the two sprites are definitely
     * colliding.
     * 
     * call checkCollision() first to check if these two sprites are colliding.
     */
    private Point collisionPoint(MovingSprite sprite1, MovingSprite sprite2)
    {
        return OverlapTester.collisionPoint(sprite1.getBounds(), sprite2.getBounds());
    }

    /**
     * helper method to check find the point at which two sprites collide. This
     * method has been optimised to assume that the two sprites are definitely
     * colliding.
     * 
     * This method has been further optimised for missiles so that only y
     * collision position is calculated. As missile in thin, the centre of the
     * missile's x position can be used for the x collision point.
     * 
     * call checkCollision() first to check if these two sprites are colliding.
     */
    private Point collisionPointBaseMissile(SpriteBaseMissile baseMissile, MovingSprite sprite)
    {
        int yPosition = OverlapTester.collisionYPosition(baseMissile.getBounds(), sprite.getBounds());

        return new Point(baseMissile.getX(), yPosition);
    }

    /**
     * helper method to check find the point at which two sprites collide. This
     * method has been optimised to assume that the two sprites are definitely
     * colliding.
     * 
     * This method has been further optimised for missiles so that only y
     * collision position is calculated. As missile in thin, the centre of the
     * missile's x position can be used for the x collision point.
     * 
     * call checkCollision() first to check if these two sprites are colliding.
     */
    private Point collisionPointAlienMissile(SpriteAlienMissile alienMissile, MovingSprite sprite)
    {
        int yPosition = OverlapTester.collisionYPosition(alienMissile.getBounds(), sprite.getBounds());

        return new Point(alienMissile.getX(), yPosition);
    }

    /**
     * activate the supplied power up type by calling the appropriate method.
     * 
     * @param powerUpType
     */
    private void activatePowerUp(PowerUpType powerUpType)
    {
        Log.i(TAG, "Power-Up: '" + powerUpType.name() + "'.");

        switch (powerUpType)
        {
        case ENERGY:

            // add energy to base
            if (primaryBase.isActive())
            {
                primaryBase.addEnergy(2);
            }

            // update energy bar
            energy = energyBar.getEnergyBar();
            reBuildSprites = true;

            break;

        case LIFE:

            // add extra life
            lives++;

            // update lives
            lifeSprites = Life.getLives(lives);
            reBuildSprites = true;

            break;

        case MISSILE_BLAST:

            // add blast missile for set time
            if (primaryBase.isActive())
            {
                primaryBase.setBaseMissileType(BaseMissileType.BLAST, 0.5f, 10f);
            }

            break;

        case MISSILE_FAST:

            // add fast missile for set time
            if (primaryBase.isActive())
            {
                primaryBase.setBaseMissileType(BaseMissileType.FAST, 0.2f, 10f);
            }

            break;

        case MISSILE_GUIDED:

            // add guided missile for set time
            if (primaryBase.isActive())
            {
                primaryBase.setBaseMissileType(BaseMissileType.GUIDED, 0.5f, 10f);
            }

            break;

        case MISSILE_LASER:

            // add laser missile for set time
            if (primaryBase.isActive())
            {
                primaryBase.setBaseMissileType(BaseMissileType.LASER, 0.5f, 10f);
            }

            break;

        case MISSILE_PARALLEL:

            // add parallel missile for set time
            if (primaryBase.isActive())
            {
                primaryBase.setBaseMissileType(BaseMissileType.PARALLEL, 0.5f, 10f);
            }

            break;

        case MISSILE_SPRAY:

            // add spray missile for set time
            if (primaryBase.isActive())
            {
                primaryBase.setBaseMissileType(BaseMissileType.SPRAY, 0.5f, 10f);
            }

            break;

        case SHIELD:

            // add shield to primary base for set time

            if (primaryBase.isActive())
            {
                // tell base it has a shield for set time. base will call back
                // with shield sprites and also when the timer expires.
                primaryBase.addShield(10f, 0f);
            }

            break;

        case HELPER_BASES:

            // if only one base then add helper bases
            if (primaryBase.isActive())
            {
                /*
                 * clear any previous helper bases - new bases will be added
                 * with full energy. also replaces any previously destroyed
                 * helper bases.
                 */
                for (SpriteBase eachBase : bases)
                {
                    // SpriteBase eachBase = baseList.next();
                    if (eachBase != primaryBase)
                    {
                        // remove current helper base
                        eachBase.setDestroyed();
                    }
                }

                /*
                 * add helper base offset to left/right of primary base
                 */
                SpriteBase helperBaseLeft = BaseHelper.newBase(primaryBase, this, -64);
                bases.add(helperBaseLeft);

                SpriteBase helperBaseRight = BaseHelper.newBase(primaryBase, this, +64);
                bases.add(helperBaseRight);

                reBuildSprites = true;
            }

            break;

        default:
            throw new IllegalArgumentException("Unsupported Power Up Type: '" + powerUpType.name() + "'.");
        }
    }

    // move all game sprites
    private void moveSprites(float deltaTime)
    {

        /*
         * it is possible that new aliens will be spawned while iterating over
         * this list of aliens. to avoid a concurrent modification exception we
         * must iterate over a copy of the list.
         */
        if (aliens.size() > 0 && modelState != ModelState.GET_READY)
        {
            List<SpriteAlien> aliensCopy = new ArrayList<SpriteAlien>(aliens);

            for (SpriteAlien eachAlien : aliensCopy)
            {
                eachAlien.move(deltaTime);
            }
        }

        for (SpriteBase eachBase : bases)
        {
            eachBase.move(deltaTime);
        }

        for (SpriteShield eachShield : shields)
        {
            eachShield.move(deltaTime);
        }

        for (SpriteBaseMissile eachBaseMissile : baseMissiles)
        {
            eachBaseMissile.move(deltaTime);
        }

        for (SpriteAlienMissile eachAlienMissile : aliensMissiles)
        {
            eachAlienMissile.move(deltaTime);
        }

        for (SpritePowerUp eachPowerUp : powerUps)
        {
            eachPowerUp.move(deltaTime);
        }

        for (SpriteHitGlow eachHit : hits)
        {
            eachHit.move(deltaTime);
        }
    }

    /**
     * initialise next wave.
     */
    private void setupLevel()
    {
        // set-up level flags
        setLevelFlags();

        // set-up aliens for next level
        setupAliens();

        // set up get ready text and put model in get ready state.
        setupGetReady();

        // refresh displayed sprites and text
        reBuildSprites = true;
        reBuildText = true;
    }

    /**
     * add new base - normally called at start of game or after losing a life.
     */
    private void addNewBase()
    {
        /* base's initial off screen y position */
        int baseInitialY = -(BaseMain.BASE_SPRITE.getProperties().getHeight() / 2);

        // start primary base off bottom of screen
        primaryBase = BaseMain.newBase(width / 2, baseInitialY, width, height, energyBar, Direction.UP, this);

        // add primary base to list of base sprites
        bases.add(primaryBase);

        // get list of energy sprites representing current energy bar
        energy = energyBar.getEnergyBar();

        // reduce remaining lives by 1
        lives--;

        // update number of lives
        lifeSprites = Life.getLives(lives);

        // reset base controller to current base position
        baseController.reset();

        // refresh displayed sprites next update
        reBuildSprites = true;

        // change model state to handle movement of base to starting position
        modelState = ModelState.NEW_BASE;
    }

    /**
     * initialise alien objects and paths. Use a factory method to create aliens
     */
    private void setupAliens()
    {
        // List<SpriteAlien> alienList = waveFactory.createWave(wave);
        //
        // /*
        // * Add aliens retrieved from wave factory to sprite list in reverse
        // * order.
        // *
        // * Collision detection routines are required to iterate through aliens
        // * in reverse so aliens on top are hit first.
        // *
        // * Any subsequent explosions on these aliens must also display on top
        // so
        // * reversed order is important for how aliens sprites are displayed.
        // */
        // for (SpriteAlien eachAlien : Reversed.reversed(alienList))
        // {
        // aliens.add(eachAlien);
        // }

        /*
         * asks wave manager to set-up next level. this is an asynchronous task
         * that can be time-consuming. This current thread can continue until
         * wave is ready.
         */
        waveManager.setUpWave(wave);

    }

    /**
     * move base by current weighting.
     * 
     * @param weightingX
     * @param weightingY
     * @param deltaTime
     */
    private void moveBase(float weightingX, float weightingY, float deltaTime)
    {
        /*
         * move primary base by weighting - helper bases will be moved by
         * primary base.
         */
        primaryBase.moveBase(weightingX, weightingY, deltaTime);
    }

    /**
     * Creates a map of used to illustrate the level using a set of flags. The
     * map key is the flag number and the map value contains the number of flags
     * needed of this type.
     * 
     * Flags used are 100, 50, 10, 5 and 1.
     * 
     * Example: Level 276 would be represented by 2 x 100 flags, 1 x 50 flag, 2
     * x 10 flags, 1 x 5 flag and 1 x 1 flag.
     */
    private void setLevelFlags()
    {
        // create a new list of flags
        flags = Flag.getFlagList(wave);
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
        reBuildText = true;
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
        if (timeSinceGetReady > GET_READY_DELAY && waveManager.isWaveReady())
        {
            getReadyTexts.clear();
            reBuildText = true;
            if (primaryBase == null)
            {
                addNewBase();
            }
            else
            {
                modelState = ModelState.PLAYING;
            }
        }
    }

    /**
     * Set-up a new a base.
     */
    private void setupNewBase()
    {
        // reset stars direction as last base could have been flipped
        Star.reset();

        /* if lives left then create new base */
        if (lives > 0)
        {
            /*
             * store previous model state before adding new base and changing
             * state.
             */
            previousModelState = modelState;

            // create new base at default position and change state
            addNewBase();

            /* if we are playing, add a shield to the base for 2 seconds */
            if (previousModelState == ModelState.PLAYING)
            {
                primaryBase.addShield(2f, 0f);
            }
        }
        /* if no lives left then game over */
        else
        {
            model.gameOver(wave);
        }
    }

    /**
     * actions required for a new base
     */
    private void updateNewBase(float deltaTime)
    {
        // move base using Y weighting only
        moveBase(0, 0.7f, deltaTime);

        // is base now at it's starting position?
        if (primaryBase.getY() >= BASE_START_Y)
        {
            /*
             * set at starting position in case timing delay has caused base to
             * go beyond it.
             */
            // primaryBase.setY(BASE_START_Y);

            // reset base controller to current base position
            baseController.reset();

            // if we were previously playing, return in playing state.
            if (previousModelState == ModelState.PLAYING)
            {
                modelState = ModelState.PLAYING;
            }
            // otherwise set-up next level
            else
            {
                setupLevel();
            }
        }
    }
}
