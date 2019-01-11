package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.List;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

public class AlienDragonHead extends AbstractAlien {
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* minimum delay between alien firing missiles in seconds */
    private static final float MIN_MISSILE_DELAY = 4.5f;

    /* maximum addition random time before firing */
    private static final float MISSILE_DELAY_RANDOM = 2f;

    /* energy of this sprite */
    private static final int ENERGY = 20;

    /* distance alien can move each cycle in pixels each second */
    private static final int ALIEN_MOVE_PIXELS = 5 * 60;

    /* time delay between alien direction changes */
    private static final float ALIEN_DIRECTION_CHANGE_DELAY = 0.1f;

    /* maximum alien change direction in radians */
    private static final float MAX_DIRECTION_CHANGE_ANGLE = 0.3f;

    // alien animation
    private static final Animation ANIMATION = new Animation(0f, GameSpriteIdentifier.DRAGON_HEAD);

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* dragon body parts - these will be destroyed when the head is destroyed */
    private final List<IAlienFollower> dragonBodies;

    /* current for sprite rotation */
    private float angle;

    /* how many seconds to delay before alien starts to follow path */
    private float timeDelayStart;

    /* variable to store time passed since last alien direction change */
    private float timeSinceLastDirectionChange;

    private final GameModel model;

    /**
     * Create Alien Dragon's Head that has rotated missiles and generates random
     * power-ups.
     */
    public AlienDragonHead(
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart,
            final float timeDelayStart,
            final GameModel model,
            final SoundPlayerService sounds,
            final VibrationService vibrator,
            final List<IAlienFollower> dragonBodies
    ) {

        super(
                ANIMATION,
                xStart,
                yStart,
                ENERGY,
                new FireRandomDelay(model, AlienMissileType.ROTATED, MIN_MISSILE_DELAY, MISSILE_DELAY_RANDOM),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new HitDisabled(),
                new ExplodeSimple(sounds, vibrator));

        this.model = model;
        this.dragonBodies = dragonBodies;

        // set positional and movement behaviour
        this.timeDelayStart = timeDelayStart;

        // reset timer since last alien direction change
        timeSinceLastDirectionChange = 0f;

        // set starting direction angle
        this.angle = recalculateAngle(0f);
    }

    @Override
    public void animate(float deltaTime) {

        super.animate(deltaTime);

        /* if active then alien can move */
        if (isActive()) {
            timeSinceLastDirectionChange += deltaTime;

            /*
             * Guide alien every x seconds so the alien changes direction to
             * follow any changes in the base's position.
             */
            if (timeSinceLastDirectionChange > ALIEN_DIRECTION_CHANGE_DELAY) {

                // recalculate direction angle
                this.angle = recalculateAngle(this.angle);

                // reset timer since last missile direction change
                timeSinceLastDirectionChange = 0f;
            }

            // calculate the deltas to be applied each move
            int xDelta = (int) (ALIEN_MOVE_PIXELS * (float) Math.cos(this.angle));
            int yDelta = (int) (ALIEN_MOVE_PIXELS * (float) Math.sin(this.angle));

            // move alien by calculated deltas
            move(
                    x() + (int) (xDelta * deltaTime),
                    y() + (int) (yDelta * deltaTime)
            );

            // update position of the dragon bodies so each are following the one before
            IAlien dragonToFollow = this;
            for (IAlienFollower dragonBody : dragonBodies) {
                if (dragonBody.isActive()) {
                    dragonBody.follow(dragonToFollow, deltaTime);
                    dragonToFollow = dragonBody;
                }
            }

        } else if (isWaiting()) {

            /* if delayStart still > 0 then count down delay */
            if (timeDelayStart > 0) {
                timeDelayStart -= deltaTime;
            }
            /* otherwise activate alien. can only happen once! */
            else {
                activate();
            }
        }
    }

    /**
     * If dragon head explodes then all body parts should also explode
     */
    @Override
    public void onHitBy(IBaseMissile baseMissile) {
        super.onHitBy(baseMissile);

        if (isExploding()) {
            for (IAlien dragonBody : dragonBodies) {
                if (dragonBody.isActive()) {
                    dragonBody.explode();
                }
            }
        }
    }

    /**
     * Recalculate the angle direction of the alien so it heads towards base.
     */
    private float recalculateAngle(float angle) {

        IBasePrimary base = model.getBase();
        if (base != null) {

            // calculate angle from alien position to base
            float newAngle = (float) Math.atan2(
                    base.y() - y(),
                    base.x() - x());

            // if alien is off screen, return it back immediately (can get lost!).
            if (offScreenAnySide(this)) {
                return newAngle;
            }

            // don't allow sudden changes of direction. limit to MAX radians.
            if ((newAngle - angle) > MAX_DIRECTION_CHANGE_ANGLE) {
                return angle + MAX_DIRECTION_CHANGE_ANGLE;
            }
            if ((newAngle - angle) < MAX_DIRECTION_CHANGE_ANGLE) {
                return angle - MAX_DIRECTION_CHANGE_ANGLE;
            }

            // otherwise return calculated angle.
            return newAngle;
        }

        return angle;
    }
}