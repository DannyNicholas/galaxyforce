package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
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
import com.danosoftware.galaxyforce.sprites.game.factories.BaseMissileFactory;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.refactor.BaseShield;
import com.danosoftware.galaxyforce.sprites.refactor.IBaseShield;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.ACTIVE;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.DESTROYED;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.EXPLODING;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.LEFT;
import static com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier.HELPER;

/**
 * Base Helper that moves alongside the Primary Base
 * <p>
 * A Base Helper can
 * - fire missiles
 * - collect power-ups
 * - can be shielded
 * - be destroyed by a missile or alien
 * <p>
 * If the primary base is destroyed, all helper bases will
 * also be destroyed
 * <p>
 * A helper base can have two states:
 * <p>
 * ACTIVE
 * - normal state
 * EXPLODING
 * - base can no longer move, fire, collect power-ups,
 * collide with missiles/aliens or gain shields
 * <p>
 * After finishing exploding, the base will be removed from the game.
 */
public class BaseHelper extends AbstractCollidingSprite implements IBaseHelper {

    // helper's x position (in pixels) from primary base
    private static final int X_OFFSET_FROM_PRIMARY_BASE = 64;

    // helper base x offset from primary base's position
    private final int xOffset;

    // does base have shield
    private boolean shielded;

    // helper base's shield
    private IBaseShield shield;

    // shield animation that pulses every 0.5 seconds
    private static final Animation SHIELD_PULSE = new Animation(0.5f, GameSpriteIdentifier.CONTROL, GameSpriteIdentifier.JOYSTICK);

    // reference to primary base
    private final IBasePrimary primaryBase;

    // reference to game model
    private final GameHandler model;

    // reference to sound player and sounds
    private final SoundPlayer soundPlayer;
    private final Sound explosionSound;

    // explosion behaviour
    private final ExplodeBehaviour explosion;

    // base state
    private BaseState state;

    // help side left or right.
    private final HelperSide side;

    /**
     * Static Helper creator
     * <p>
     * - Creates new helper
     * - Registers helper with primary base
     */
    public final static void createHelperBase(
            final IBasePrimary primaryBase,
            final GameHandler model,
            final HelperSide side,
            final boolean shieldUp,
            final float shieldSyncTime) {

        IBaseHelper helper = new BaseHelper(
                primaryBase,
                model,
                side,
                shieldUp,
                shieldSyncTime
        );
        primaryBase.helperCreated(side, helper);
    }

    /**
     * private BaseHelper constructor.
     * Use static helper creator to ensure helper base is registered with primary base.
     *
     * @param primaryBase
     * @param model
     * @param side
     * @param shieldUp
     * @param shieldSyncTime
     */
    private BaseHelper(
            final IBasePrimary primaryBase,
            final GameHandler model,
            final HelperSide side,
            final boolean shieldUp,
            final float shieldSyncTime) {
        super(
                HELPER,
                primaryBase.x() +
                        (side == LEFT ? -X_OFFSET_FROM_PRIMARY_BASE : +X_OFFSET_FROM_PRIMARY_BASE),
                primaryBase.y()
        );
        this.model = model;
        this.primaryBase = primaryBase;
        this.state = ACTIVE;
        this.side = side;
        this.xOffset = (side == LEFT ? -X_OFFSET_FROM_PRIMARY_BASE : +X_OFFSET_FROM_PRIMARY_BASE);
        this.explosion = new ExplodeSimple();

        if (shieldUp) {
            addShield(shieldSyncTime);
        } else {
            removeShield();
        }

        // set-up sound effects from sound bank
        this.soundPlayer = SoundPlayerSingleton.getInstance();
        SoundEffectBank soundBank = SoundEffectBankSingleton.getInstance();
        this.explosionSound = soundBank.get(SoundEffect.EXPLOSION);
    }

    @Override
    public List<ISprite> allSprites() {
        final List<ISprite> sprites = new ArrayList<>();
        sprites.add(this);
        if (shielded) {
            sprites.add(shield);
        }

        return sprites;
    }

    @Override
    public BaseMissileBean fire(BaseMissileType baseMissileType) {
        return BaseMissileFactory.createBaseMissile(this, baseMissileType, model);
    }

    @Override
    public void addShield(float syncTime) {
        shielded = true;
        shield = new BaseShield(x(), y(), SHIELD_PULSE, syncTime);
    }

    @Override
    public void removeShield() {
        shielded = false;
        shield = null;
    }

    @Override
    public void animate(float deltaTime) {

        // if exploding then animate or set destroyed once finished
        if (state == EXPLODING) {
            changeType(explosion.getExplosion(deltaTime));
            if (explosion.finishedExploding()) {
                state = DESTROYED;
                primaryBase.helperRemoved(side);
            }
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
            // helper is destroyed by single missile hit
            destroy();
        }
        missile.destroy();
    }

    @Override
    public void destroy() {
        this.state = EXPLODING;
        primaryBase.helperExploding(side);
        explosion.startExplosion();
        soundPlayer.playSound(explosionSound);
    }

    @Override
    public boolean isDestroyed() {
        return state == DESTROYED;
    }

    @Override
    public void collectPowerUp(IPowerUp powerUp) {
        primaryBase.collectPowerUp(powerUp);
    }

    /**
     * Shifts helper base by the wanted x offset
     */
    @Override
    public void move(int x, int y) {
        if (state == ACTIVE) {
            super.move(x + xOffset, y);
            // if base has a shield, move it with base
            if (shielded) {
                shield.move(x + xOffset, y);
            }
        }
    }
}
