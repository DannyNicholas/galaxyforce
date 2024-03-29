package com.danosoftware.galaxyforce.sprites.game.bases;

import static com.danosoftware.galaxyforce.constants.GameConstants.DEFAULT_BACKGROUND_COLOUR;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_BOTTOM;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_X;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.ACTIVE;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.DESTROYED;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.EXPLODING;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.LEFT;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.RIGHT;
import static com.danosoftware.galaxyforce.sprites.properties.SpriteDetails.BASE;
import static com.danosoftware.galaxyforce.sprites.properties.SpriteDetails.BASE_LEFT;
import static com.danosoftware.galaxyforce.sprites.properties.SpriteDetails.BASE_RIGHT;

import android.util.Log;
import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;
import com.danosoftware.galaxyforce.models.screens.background.BackgroundFlash;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseLean;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide;
import com.danosoftware.galaxyforce.sprites.game.bases.explode.BaseMultiExploder;
import com.danosoftware.galaxyforce.sprites.game.bases.explode.IBaseMultiExploder;
import com.danosoftware.galaxyforce.sprites.game.factories.BaseMissileFactory;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.providers.GamePlaySpriteProvider;
import com.danosoftware.galaxyforce.utilities.MoveBaseHelper;
import com.danosoftware.galaxyforce.view.Animation;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BasePrimary extends AbstractCollidingSprite implements IBasePrimary {

  private static final String TAG = "BasePrimary";
  /* delay between firing missiles in seconds */
  private static final float DEFAULT_BASE_MISSILE_DELAY = 0.5f;
  // default base missile sprite
  private static final BaseMissileType DEFAULT_MISSILE_TYPE = BaseMissileType.NORMAL;
  // explosion behaviour
  private final IBaseMultiExploder explosion;
  private final GamePlaySpriteProvider spriteProvider;
  // left and right helper sprites stored in an enum map.
  // key is the LEFT/RIGHT enum.
  private final Map<HelperSide, IBaseHelper> helpers;

  // as above but only holds active (i.e. non-exploding) helpers
  private final Map<HelperSide, IBaseHelper> activeHelpers;
  // helper class to handle base movement and animation
  private final MoveBaseHelper moveHelper;
  /* reference to model */
  private final GameModel model;
  // reference to sound player
  private final SoundPlayerService sounds;
  // reference to vibrator
  private final VibrationService vibrator;
  // provides the background flash colour - used when base is exploding
  private final BackgroundFlash backgroundFlash;
  // active bases
  // cached as an optimisation to improve performance
  private List<IBase> activeBases;
  private IBaseShield shield;
  // base state
  private BaseState state;
  /* variable to store current delay between missile fires */
  private float baseMissileDelay;
  /* variable to store time passed since base last fired */
  private float timeSinceBaseLastFired;
  /* variable to store current base missile type */
  private BaseMissileType baseMissileType;
  /*
   * variable to store time until missile should be reverted to default. Value
   * of zero indicates no change is needed.
   */
  private float timeUntilDefaultMissile;
  /*
   * variable to store time until shield can be removed. Value of zero
   * indicates no change is needed.
   */
  private float timeUntilShieldRemoved = 0f;
  /*
   * variable to store time until helper bases should be destroyed. Value
   * of zero indicates no change is needed.
   */
  private float timeUntilHelpersExplode;
  /* does base have shield */
  private boolean shielded = false;
  /* current lean of the base (Left, Right, None) */
  private BaseLean lean;

  public BasePrimary(
      final GameModel model,
      final SoundPlayerService sounds,
      final VibrationService vibrator,
      final GamePlaySpriteProvider spriteProvider) {

    super(BASE, SCREEN_MID_X, SCREEN_BOTTOM);
    this.spriteProvider = spriteProvider;
    this.state = ACTIVE;
    this.helpers = new EnumMap<>(HelperSide.class);
    this.activeHelpers = new EnumMap<>(HelperSide.class);
    updateVisibleSprites();
    this.activeBases = buildActiveBases();
    this.lean = BaseLean.NONE;
    this.moveHelper = new MoveBaseHelper(this);
    this.backgroundFlash = new BackgroundFlash();

    this.explosion = new BaseMultiExploder(
        this,
        sounds,
        vibrator,
        new Animation(
            0.05f,
            SpriteDetails.BASE_EXPLODE_01,
            SpriteDetails.BASE_EXPLODE_02,
            SpriteDetails.BASE_EXPLODE_03,
            SpriteDetails.BASE_EXPLODE_04,
            SpriteDetails.BASE_EXPLODE_05,
            SpriteDetails.BASE_EXPLODE_06,
            SpriteDetails.BASE_EXPLODE_07,
            SpriteDetails.BASE_EXPLODE_08,
            SpriteDetails.BASE_EXPLODE_09,
            SpriteDetails.BASE_EXPLODE_10,
            SpriteDetails.BASE_EXPLODE_11));
    this.model = model;
    this.sounds = sounds;
    this.vibrator = vibrator;

    // set-up missile behaviours
    this.baseMissileType = DEFAULT_MISSILE_TYPE;
    this.baseMissileDelay = DEFAULT_BASE_MISSILE_DELAY;
    timeUntilDefaultMissile = 0f;
    timeSinceBaseLastFired = 0f;
  }

  /**
   * Build list of all sprites owned by the base. Should be called whenever sprites need to added or
   * removed from list of visible sprites.
   */
  private void updateVisibleSprites() {
    final List<ISprite> sprites = new ArrayList<>();

    if (!isDestroyed()) {
      sprites.add(this);
      if (shielded) {
        sprites.add(shield);
      }
    }

    for (IBaseHelper helper : helpers.values()) {
      sprites.addAll(helper.allSprites());
    }

    if (isExploding()) {
      sprites.addAll(explosion.getMultiExplosion());
    }

    spriteProvider.setBases(sprites);
  }

  /**
   * Rebuild list of active base sprites. Should be called whenever a new base becomes active or
   * existing base becomes inactive (i.e. explodes).
   *
   * @return sprite list
   */
  private List<IBase> buildActiveBases() {
    final List<IBase> activeBases = new ArrayList<>();

    if (state == ACTIVE) {
      activeBases.add(this);
      activeBases.addAll(activeHelpers.values());
    }

    return activeBases;
  }

  /**
   * Main animation loop
   */
  @Override
  public void animate(float deltaTime) {

    // move and animate base based on current weightings
    if (state == ACTIVE) {
      // move and animate base
      moveHelper.moveBase(deltaTime);

      // move helper bases using built in offset from this primary base
      for (IBaseHelper helper : helpers.values()) {
        helper.move(x(), y());
      }

      if (shielded) {
        // check when shield should be removed
        timeUntilShieldRemoved -= deltaTime;
        if (timeUntilShieldRemoved <= 0) {
          removeShield();
        } else {
          shield.move(x(), y());
          shield.animate(deltaTime);
        }
      }

      // helpers should be destroyed when timer expires
      if (!activeHelpers.isEmpty()) {
        timeUntilHelpersExplode -= deltaTime;
        if (timeUntilHelpersExplode <= 0) {
          for (IBaseHelper aHelperBase : activeHelpers.values()) {
            aHelperBase.destroy();
          }
        }
      }
    }

    // if exploding then animate or set destroyed once finished
    if (state == EXPLODING) {
      backgroundFlash.update(deltaTime);
      if (explosion.finishedExploding()) {
        state = DESTROYED;
        this.activeBases = buildActiveBases();
      } else {
        changeType(explosion.getExplosion(deltaTime));
      }
      updateVisibleSprites();
    }

    // animate helper bases
    for (IBaseHelper helper : helpers.values()) {
      helper.animate(deltaTime);
    }

    // check to see if base (and any helpers) should fire their missiles
    fireBaseMissile(deltaTime);
  }

  /**
   * Set movement weightings of base
   */
  @Override
  public void moveTarget(float targetX, float targetY) {
        /*
         only allow target changes when ACTIVE.
         when in MOVING_TO_START_POSITION state, base must move to a set
         position before becoming ACTIVE
          */
    if (state == ACTIVE) {
      moveHelper.updateTarget(targetX, targetY);
    }
  }

  @Override
  public void onHitBy(IAlien alien) {

    // can only be hit if not shielded
    if (!shielded) {
      destroy();
    }
  }

  @Override
  public void onHitBy(IAlienMissile missile) {

    // can only be hit if not shielded
    if (!shielded) {
      destroy();
    }
    missile.destroy();
  }

  @Override
  public void destroy() {

    explosion.startExplosion();
    state = EXPLODING;

    // if primary base explodes - all helper bases must also explode.
    for (IBaseHelper aHelperBase : helpers.values()) {
      aHelperBase.destroy();
    }

    // re-build active bases
    this.activeBases = buildActiveBases();
  }

  @Override
  public void collectPowerUp(IPowerUp powerUp) {

    // destroy collected power-up
    powerUp.destroy();

    PowerUpType powerUpType = powerUp.getPowerUpType();

    Log.i(TAG, "Power-Up: '" + powerUpType.name() + "'.");

    switch (powerUpType) {
      // add extra life
      case LIFE:
        model.addLife();
        break;

      // add blast missile for set time
      case MISSILE_BLAST:
        setBaseMissileType(BaseMissileType.BLAST, 0.5f, 2f);
        break;

      // add fast missile for set time
      case MISSILE_FAST:
        setBaseMissileType(BaseMissileType.FAST, 0.2f, 10f);
        break;

      // add guided missile for set time
      case MISSILE_GUIDED:
        setBaseMissileType(BaseMissileType.GUIDED, 0.5f, 7.5f);
        break;

      // add laser missile for set time
      case MISSILE_LASER:
        setBaseMissileType(BaseMissileType.LASER, 0.5f, 10f);
        break;

      // add parallel missile for set time
      case MISSILE_PARALLEL:
        setBaseMissileType(BaseMissileType.PARALLEL, 0.5f, 7.5f);
        break;

      // add spray missile for set time
      case MISSILE_SPRAY:
        setBaseMissileType(BaseMissileType.SPRAY, 0.5f, 5f);
        break;

      // add shield for set time
      case SHIELD:
        addShield(7.5f);
        break;

      // add helper bases for set time
      case HELPER_BASES:
        timeUntilHelpersExplode = 10f;
        if (!helpers.containsKey(LEFT)) {
          createHelperBase(LEFT);
        }
        if (!helpers.containsKey(RIGHT)) {
          createHelperBase(RIGHT);
        }
        break;

      default:
        throw new GalaxyForceException("Unsupported Power Up Type: '" + powerUpType.name() + "'.");
    }
  }

  @Override
  public List<IBase> activeBases() {
    return activeBases;
  }

  @Override
  public BaseLean getLean() {
    return lean;
  }

  @Override
  public void setLean(BaseLean lean) {
    this.lean = lean;

    switch (lean) {
      case LEFT:
        changeType(BASE_LEFT);
        break;
      case RIGHT:
        changeType(BASE_RIGHT);
        break;
      case NONE:
        changeType(BASE);
        break;
    }
  }

  /**
   * Created helper base for wanted side.
   * <p>
   * Helper will register itself with the primary base when created.
   */
  private void createHelperBase(HelperSide side) {
    BaseHelper.createHelperBase(
        this,
        model,
        sounds,
        vibrator,
        side,
        shielded,
        shielded ? shield.getSynchronisation() : 0
    );
  }

  @Override
  public void helperCreated(HelperSide side, IBaseHelper helper) {
    helpers.put(side, helper);
    activeHelpers.put(side, helper);
    updateVisibleSprites();
    this.activeBases = buildActiveBases();
  }

  @Override
  public void helperRemoved(HelperSide side) {
    helpers.remove(side);
    updateVisibleSprites();
  }

  @Override
  public void helperExploding(HelperSide side) {
    activeHelpers.remove(side);
    this.activeBases = buildActiveBases();
  }

  @Override
  public void addShield(float timeActive) {
    final float syncTime = 0f;

    // reset the shield timer.
    // will extend shield time if already shielded.
    timeUntilShieldRemoved = timeActive;

    // only create new shields if we are not shielded
    if (!shielded) {
      shielded = true;
      shield = new BaseShieldPrimary(this, sounds, vibrator, x(), y(), syncTime);
    }

    // add shield for any helper bases
    for (IBaseHelper aHelperBase : helpers.values()) {
      aHelperBase.addSynchronisedShield(syncTime);
    }

    // refresh list of sprites
    updateVisibleSprites();
  }

    /*
      ***********************
      PRIVATE HELPERS *
      ***********************
     */

  private void setBaseMissileType(BaseMissileType baseMissileType, float baseMissileDelay,
      float timeActive) {
    this.baseMissileType = baseMissileType;
    this.baseMissileDelay = baseMissileDelay;
    this.timeUntilDefaultMissile = timeActive;

    // change time since last fired so new missile type fires immediately.
    if (baseMissileType != DEFAULT_MISSILE_TYPE) {
      timeSinceBaseLastFired = baseMissileDelay;
    }
  }

    /*
      ***********************
      MISSILE HELPERS *
      ***********************
     */

  /**
   * fire base missile
   */
  private void fireBaseMissile(float deltaTime) {
    // if base is ready to fire - fire!!
    if (readyToFire(deltaTime)) {
      List<BaseMissilesDto> missiles = new ArrayList<>();

      // primary base fires
      missiles.add(fire());

      // any helper bases fire
      for (IBaseHelper aHelperBase : helpers.values()) {
        // create new missile and add to missile list.
        missiles.add(aHelperBase.fire(baseMissileType));
      }

      // send missiles to model
      for (BaseMissilesDto aMissile : missiles) {
        model.fireBaseMissiles(aMissile);
      }
    }
  }

  /**
   * Returns true if base is ready to fire. measures total time since base last fired compared to a
   * set delay
   */
  private boolean readyToFire(float deltaTime) {
    /*
     * check to see if current missile type should be changed back to
     * default.
     */
    if (timeUntilDefaultMissile > 0) {
      timeUntilDefaultMissile = timeUntilDefaultMissile - deltaTime;

      // if base missile time now expired, change back to default missile
      if (timeUntilDefaultMissile <= 0) {
        setBaseMissileType(DEFAULT_MISSILE_TYPE, DEFAULT_BASE_MISSILE_DELAY, 0f);
      }
    }

    // increment timer referencing time since base last fired
    timeSinceBaseLastFired = timeSinceBaseLastFired + deltaTime;

    // if missile timer has exceeded delay time and base is active - ready to fire!!
    return (timeSinceBaseLastFired > baseMissileDelay
        && state == ACTIVE);
  }

  /**
   * Returns the base's current missile type when base fires. Reset time when last fired.
   *
   * @return current base missile
   */
  private BaseMissilesDto fire() {
    // reset timer since base last fired
    timeSinceBaseLastFired = 0f;

    // create and return missile
    return BaseMissileFactory.createBaseMissile(this, baseMissileType, model);
  }


  /**
   * *********************** SHIELD HELPERS * ***********************
   */

  private void removeShield() {
    timeUntilShieldRemoved = 0f;
    shielded = false;
    shield = null;

    // remove shield for any helper bases
    for (IBaseHelper aHelperBase : helpers.values()) {
      aHelperBase.removeShield();
    }

    // refresh list of sprites
    updateVisibleSprites();
  }

  @Override
  public boolean isDestroyed() {
    return state == DESTROYED;
  }

  @Override
  public boolean isActive() {
    return state == ACTIVE;
  }

  @Override
  public boolean isExploding() {
    return state == EXPLODING;
  }

  @Override
  public RgbColour background() {
    if (isExploding()) {
      return backgroundFlash.background();
    }
    return DEFAULT_BACKGROUND_COLOUR;
  }
}
