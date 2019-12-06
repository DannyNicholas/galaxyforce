package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide;
import com.danosoftware.galaxyforce.sprites.game.bases.explode.BaseExploderSimple;
import com.danosoftware.galaxyforce.sprites.game.bases.explode.IBaseExploder;
import com.danosoftware.galaxyforce.sprites.game.factories.BaseMissileFactory;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
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

    // helper's x position offset (in pixels) from primary base
    private static final int X_OFFSET_FROM_PRIMARY_BASE = 64;

    // helper's y position offset (in pixels) from primary base
    private static final int Y_OFFSET_FROM_PRIMARY_BASE = 18;

    // helper base x offset from primary base's position
    private final int xOffset;

    // does base have shield
    private boolean shielded;

    // helper base's shield
    private IBaseShield shield;

    // reference to primary base
    private final IBasePrimary primaryBase;

    // reference to game model
    private final GameModel model;

    // reference to sound player and sounds
    private final SoundPlayerService sounds;

    // explosion behaviour
    private final IBaseExploder explosion;

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
    static void createHelperBase(
            final IBasePrimary primaryBase,
            final GameModel model,
            final SoundPlayerService sounds,
            final VibrationService vibrator,
            final HelperSide side,
            final boolean shieldUp,
            final float shieldSyncTime) {

        IBaseHelper helper = new BaseHelper(
                primaryBase,
                model,
                sounds,
                vibrator,
                side,
                shieldUp,
                shieldSyncTime
        );
        primaryBase.helperCreated(side, helper);
    }

    /**
     * private BaseHelper constructor.
     * Use static helper creator to ensure helper base is registered with primary base.
     */
    private BaseHelper(
            final IBasePrimary primaryBase,
            final GameModel model,
            final SoundPlayerService sounds,
            final VibrationService vibrator,
            final HelperSide side,
            final boolean shieldUp,
            final float shieldSyncTime) {
        super(
                HELPER,
                primaryBase.x() +
                        (side == LEFT ? -X_OFFSET_FROM_PRIMARY_BASE : +X_OFFSET_FROM_PRIMARY_BASE),
                primaryBase.y() + Y_OFFSET_FROM_PRIMARY_BASE
        );
        this.model = model;
        this.sounds = sounds;
        this.primaryBase = primaryBase;
        this.state = ACTIVE;
        this.side = side;
        this.xOffset = (side == LEFT ? -X_OFFSET_FROM_PRIMARY_BASE : +X_OFFSET_FROM_PRIMARY_BASE);
        this.explosion = new BaseExploderSimple(
                sounds,
                vibrator,
                new Animation(
                        0.075f,
                        GameSpriteIdentifier.EXPLODE_01,
                        GameSpriteIdentifier.EXPLODE_02,
                        GameSpriteIdentifier.EXPLODE_03,
                        GameSpriteIdentifier.EXPLODE_04,
                        GameSpriteIdentifier.EXPLODE_05));

        if (shieldUp) {
            addSynchronisedShield(shieldSyncTime);
        } else {
            removeShield();
        }
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
    public BaseMissilesDto fire(BaseMissileType baseMissileType) {

        // helper bases should not fire parallel missiles.
        // instead, use an equivalent single missile.
        if (baseMissileType == BaseMissileType.PARALLEL) {
            return BaseMissileFactory.createBaseMissile(this, BaseMissileType.NORMAL, model);
        }
        if (baseMissileType == BaseMissileType.SPRAY) {
            return BaseMissileFactory.createBaseMissile(this, BaseMissileType.GUIDED, model);
        }

        return BaseMissileFactory.createBaseMissile(this, baseMissileType, model);
    }

    @Override
    public void addSynchronisedShield(float syncTime) {
        shielded = true;
        shield = new BaseShieldHelper(x(), y(), syncTime);
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

        if (shielded) {
            shield.animate(deltaTime);
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
    public void collectPowerUp(IPowerUp powerUp) {
        primaryBase.collectPowerUp(powerUp);
    }

    /**
     * Shifts helper base by the wanted x offset
     */
    @Override
    public void move(int x, int y) {
        if (state == ACTIVE) {
            super.move(x + xOffset, y + Y_OFFSET_FROM_PRIMARY_BASE);
            // if base has a shield, move it with base
            if (shielded) {
                shield.move(x + xOffset, y + Y_OFFSET_FROM_PRIMARY_BASE);
            }
        }
    }
}
