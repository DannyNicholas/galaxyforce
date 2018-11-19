package com.danosoftware.galaxyforce.sprites.game.bases;

import android.util.Log;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sound.Sound;
import com.danosoftware.galaxyforce.sound.SoundEffect;
import com.danosoftware.galaxyforce.sound.SoundEffectBank;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sound.SoundPlayer;
import com.danosoftware.galaxyforce.sound.SoundPlayerSingleton;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.factories.BaseMissileFactory;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.refactor.BaseShield;
import com.danosoftware.galaxyforce.sprites.refactor.IBaseShield;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.sprites.refactor.MoveBaseHelper;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.danosoftware.galaxyforce.constants.GameConstants.BASE_MAX_ENERGY_LEVEL;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_BOTTOM;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_X;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.ACTIVE;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.DESTROYED;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.EXPLODING;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.LEFT;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.RIGHT;
import static com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier.BASE;
import static com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier.BASE_FLIP;

public class BasePrimary extends AbstractCollidingSprite implements IBasePrimary {

    // shield animation that pulses every 0.5 seconds
    private static final Animation SHIELD_PULSE = new Animation(0.5f, GameSpriteIdentifier.CONTROL, GameSpriteIdentifier.JOYSTICK);

    private static final ISpriteIdentifier BASE_SPRITE = BASE;

    private static final String TAG = "BasePrimary";

    // base's start y position when ready
    private static final int BASE_START_Y = 192;

    // explosion behaviour
    private final ExplodeBehaviour explosion;

    // hit behaviour
    private final HitBehaviour hit;

    // all sprites
    // cached as an optimisation to improve performance
    private List<ISprite> allSprites;

    // active bases
    // cached as an optimisation to improve performance
    private List<IBase> activeBases;

    // left and right helper sprites stored in an enum map.
    // key is the LEFT/RIGHT enum.
    private final Map<HelperSide, IBaseHelper> helpers;

    // as above but only holds active (i.e. non-exploding) helpers
    private final Map<HelperSide, IBaseHelper> activeHelpers;

    private IBaseShield shield;

    // base state
    private BaseState state;

    // helper class to handle base movement and animation
    private final MoveBaseHelper moveHelper;


    /* delay between firing missiles in seconds */
    private static final float DEFAULT_BASE_MISSILE_DELAY = 0.5f;

    /* variable to store current delay between missile fires */
    private float baseMissileDelay;

    // default base missile sprite
    private static final BaseMissileType DEFAULT_MISSILE_TYPE = BaseMissileType.SIMPLE;

    // energy of this base
    private int energy;

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


    /* does base have shield */
    private boolean shielded = false;

    /* reference to model */
    private final GameHandler model;

    /* reference to sound player and sounds */
    private final SoundPlayer soundPlayer;
    private final Sound explosionSound;

    public BasePrimary(
            final GameHandler model) {

        super(BASE_SPRITE, SCREEN_MID_X, SCREEN_BOTTOM);
        this.state = ACTIVE;
        this.helpers = new EnumMap<>(HelperSide.class);
        this.activeHelpers = new EnumMap<>(HelperSide.class);
        this.allSprites = buildAllSprites();
        this.activeBases = buildActiveBases();
        this.moveHelper = new MoveBaseHelper(this);
        moveHelper.updateTarget(SCREEN_MID_X, BASE_START_Y);

        this.explosion = new ExplodeSimple();
        this.hit = new HitAnimation(new Animation(0.25f, BASE_SPRITE, BASE_FLIP));

        this.model = model;


        // set-up sound effects from sound bank
        this.soundPlayer = SoundPlayerSingleton.getInstance();
        SoundEffectBank soundBank = SoundEffectBankSingleton.getInstance();
        this.explosionSound = soundBank.get(SoundEffect.EXPLOSION);

        // set-up missile behaviours
        this.baseMissileType = DEFAULT_MISSILE_TYPE;
        this.baseMissileDelay = DEFAULT_BASE_MISSILE_DELAY;
        timeUntilDefaultMissile = 0f;
        timeSinceBaseLastFired = 0f;

        // new base starts as shielded to avoid being immediately destroyed
        addShield(2f, 0f);

        // reset energy bar to maximum. new energy level returned.
        this.energy = BASE_MAX_ENERGY_LEVEL;
        model.energyUpdate(energy);
    }

    /**
     * Build list of all sprites owned by the base.
     * Should be called whenever sprites need to added or removed from list of visible sprites.
     *
     * @return sprite list
     */
    private List<ISprite> buildAllSprites() {
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

        return sprites;
    }

    /**
     * Rebuild list of active base sprites.
     * Should be called whenever a new base becomes active or
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

            if (shielded) {
                shield.move(x(), y());
            }

            // move helper bases using built in offset from this primary base
            for (IBaseHelper helper : helpers.values()) {
                helper.move(x(), y());
            }
        }

        if (state == ACTIVE) {
            // if shielded then check when shield should be removed
            if (shielded) {
                timeUntilShieldRemoved -= deltaTime;
                if (timeUntilShieldRemoved <= 0) {
                    removeShield();
                }
            }

            // if hit then continue hit animation
            if (hit.isHit()) {
                changeType(hit.getHit(deltaTime));
            }
        }

        // if exploding then animate or set destroyed once finished
        if (state == EXPLODING) {
            if (explosion.finishedExploding()) {
                state = DESTROYED;
                this.allSprites = buildAllSprites();
                this.activeBases = buildActiveBases();
            } else {
                changeType(explosion.getExplosion(deltaTime));
            }
        }

        // check to see if base (and any helpers) should fire their missiles
        fireBaseMissile(deltaTime);
    }

    /**
     * Set movement weightings of base
     */
    @Override
    public void moveTarget(int targetX, int targetY) {
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
            energy -= missile.energyDamage();
            model.energyUpdate(energy);

            if (energy <= 0) {
                destroy();
            } else {
                hit.startHit(0f);
            }
        }
        missile.destroy();
    }

    @Override
    public void destroy() {

        explosion.startExplosion();
        state = EXPLODING;

        // play explosion sound effect
        soundPlayer.playSound(explosionSound);

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
            // add energy to base
            case ENERGY:
                energy += 2;
                model.energyUpdate(energy);
                break;

            // add extra life
            case LIFE:
                model.addLife();
                break;

            // add blast missile for set time
            case MISSILE_BLAST:
                setBaseMissileType(BaseMissileType.BLAST, 0.5f, 10f);
                break;

            // add fast missile for set time
            case MISSILE_FAST:
                setBaseMissileType(BaseMissileType.FAST, 0.2f, 10f);
                break;

            // add guided missile for set time
            case MISSILE_GUIDED:
                setBaseMissileType(BaseMissileType.GUIDED, 0.5f, 10f);
                break;

            // add laser missile for set time
            case MISSILE_LASER:
                setBaseMissileType(BaseMissileType.LASER, 0.5f, 10f);
                break;

            // add parallel missile for set time
            case MISSILE_PARALLEL:
                setBaseMissileType(BaseMissileType.PARALLEL, 0.5f, 10f);
                break;

            // add spray missile for set time
            case MISSILE_SPRAY:
                setBaseMissileType(BaseMissileType.SPRAY, 0.5f, 10f);
                break;

            // add shield for set time
            case SHIELD:
                addShield(10f, 0f);
                break;

            // add helper bases for set time
            case HELPER_BASES:
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
    public List<ISprite> allSprites() {
        return allSprites;
    }

    @Override
    public List<IBase> activeBases() {
        return activeBases;
    }

    /**
     * Created helper base for wanted side.
     * <p>
     * Helper will register itself with the primary base when created.
     *
     * @param side
     */
    private void createHelperBase(HelperSide side) {
        BaseHelper.createHelperBase(
                this,
                model,
                side,
                shielded,
                shielded ? shield.getSynchronisation() : 0
        );
    }

    @Override
    public void helperCreated(HelperSide side, IBaseHelper helper) {
        helpers.put(side, helper);
        activeHelpers.put(side, helper);
        this.allSprites = buildAllSprites();
        this.activeBases = buildActiveBases();
    }

    @Override
    public void helperRemoved(HelperSide side) {
        helpers.remove(side);
        this.allSprites = buildAllSprites();
    }

    @Override
    public void helperExploding(HelperSide side) {
        activeHelpers.remove(side);
        this.activeBases = buildActiveBases();
    }


    /*
      ***********************
      PRIVATE HELPERS *
      ***********************
     */

    /**
     * @param baseMissileType
     * @param baseMissileDelay
     * @param timeActive
     */
    private void setBaseMissileType(BaseMissileType baseMissileType, float baseMissileDelay, float timeActive) {
        this.baseMissileType = baseMissileType;
        this.baseMissileDelay = baseMissileDelay;
        this.timeUntilDefaultMissile = timeActive;

        // change time since last fired so new missile type fires immediately.
        if (baseMissileType != DEFAULT_MISSILE_TYPE) {
            timeSinceBaseLastFired = baseMissileDelay;
        }

        // change the missile type for any helper bases
//        for (IBaseHelperSprite aHelperBase : helpers) {
//            aHelperBase.setBaseMissileType(baseMissileType, baseMissileDelay, timeActive);
//        }

    }


    /**
     * return an instance of base at supplied position
     */
//    public static SpriteBase newBase(int xStart, int yStart, int width, int height, EnergyBar energyBar, Direction direction,
//                                     GameHandler model)
//    {
//        return new com.danosoftware.galaxyforce.sprites.game.implementations.BaseMain(xStart, yStart, com.danosoftware.galaxyforce.sprites.game.implementations.BaseMain.BASE_SPRITE, width, height, energyBar, direction, model);
//    }

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
            List<BaseMissileBean> missiles = new ArrayList<BaseMissileBean>();

            // primary base fires
            missiles.add(fire());

            // any helper bases fire
            for (IBaseHelper aHelperBase : helpers.values()) {
                // create new missile and add to missile list.
                missiles.add(aHelperBase.fire(baseMissileType));
            }

            // send missiles to model
            for (BaseMissileBean aMissile : missiles) {
                model.fireBaseMissiles(aMissile);
            }
        }
    }

    /**
     * Returns true if base is ready to fire. measures total time since base
     * last fired compared to a set delay
     *
     * @param deltaTime
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

                // baseMissileType = DEFAULT_MISSILE_TYPE;
                // baseMissileDelay = DEFAULT_BASE_MISSILE_DELAY;
                // timeUntilDefaultMissile = 0f;
            }
        }

        // increment timer referencing time since base last fired
        timeSinceBaseLastFired = timeSinceBaseLastFired + deltaTime;

        // if missile timer has exceeded delay time and base is active - ready
        // to fire!!
        return (timeSinceBaseLastFired > baseMissileDelay && state == ACTIVE);
    }

    /**
     * Returns the base's current missile type when base fires. Reset time when
     * last fired.
     *
     * @return current base missile
     */
    private BaseMissileBean fire() {
        // reset timer since base last fired
        timeSinceBaseLastFired = 0f;

        // create and return missile
        return BaseMissileFactory.createBaseMissile(this, baseMissileType, model);
    }


    /**
     * ***********************
     * SHIELD HELPERS *
     * ***********************
     */

    private void addShield(float timeActive, float syncTime) {

        // reset the shield timer.
        // will extend shield time if already shielded.
        timeUntilShieldRemoved = timeActive;

        // only create new shields if we are not shielded
        if (!shielded) {
            shielded = true;
            shield = new BaseShield(x(), y(), SHIELD_PULSE, syncTime);
        }

        // add shield for any helper bases
        for (IBaseHelper aHelperBase : helpers.values()) {
            aHelperBase.addShield(syncTime);
        }

        // refresh list of sprites
        this.allSprites = buildAllSprites();
    }

    private void removeShield() {
        timeUntilShieldRemoved = 0f;
        shielded = false;
        shield = null;

        // remove shield for any helper bases
        for (IBaseHelper aHelperBase : helpers.values()) {
            aHelperBase.removeShield();
        }

        // refresh list of sprites
        this.allSprites = buildAllSprites();
    }

    @Override
    public boolean isDestroyed() {
        return state == DESTROYED;
    }
}
