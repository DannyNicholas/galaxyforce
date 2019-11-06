package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.helpers.BoundariesChecker;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowableHunterConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory.createExplosionBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory.createSpawnBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory.createSpinningBehaviour;

/**
 * Alien that is the head of a chain of following aliens.
 * This head decides how to move and instructs followers
 * to follow it.
 * <p>
 * This alien is a hunter that will attempt to crash into the base.
 */
public class FollowableHunterAlien extends AbstractAlien {

    /* distance alien can move each cycle in pixels each second */
    private final int speedInPixelsPerSecond;

    /* time delay between alien direction changes */
    private static final float ALIEN_DIRECTION_CHANGE_DELAY = 0.1f;

    /* maximum alien change direction in radians */
    private static final float MAX_DIRECTION_CHANGE_ANGLE = 0.3f;

    /* follower body parts - these will be destroyed when the head is destroyed */
    private final List<IAlienFollower> followers;

    /* current for sprite rotation */
    private float angle;

    /* how many seconds to delay before alien starts */
    private float timeDelayStart;

    /* variable to store time passed since last alien direction change */
    private float timeSinceLastDirectionChange;

    private final GameModel model;

    // represents the boundaries the alien can fly within
    private final BoundariesChecker boundariesChecker;

    @Builder
    public FollowableHunterAlien(
            @NonNull final AlienFactory alienFactory,
            @NonNull final PowerUpAllocatorFactory powerUpAllocatorFactory,
            @NonNull GameModel model,
            @NonNull final SoundPlayerService sounds,
            @NonNull final VibrationService vibrator,
            @NonNull final FollowableHunterConfig alienConfig,
            final PowerUpType powerUpType,
            @NonNull final Integer xStart,
            @NonNull final Integer yStart,
            @NonNull final Float timeDelayStart,
            @NonNull final List<IAlienFollower> followers) {

        super(
                alienConfig.getAlienCharacter(),
                alienConfig.getAlienCharacter().getAnimation(),
                xStart,
                yStart,
                alienConfig.getEnergy(),
                createFireBehaviour(
                        model,
                        alienConfig.getMissileConfig()),
                new PowerUpSingle(
                        model,
                        powerUpType),
                createSpawnBehaviour(
                        model,
                        alienFactory,
                        powerUpAllocatorFactory,
                        alienConfig.getSpawnConfig()),
                new HitAnimation(
                        sounds,
                        vibrator,
                        alienConfig.getAlienCharacter().getHitAnimation()),
                createExplosionBehaviour(
                        model,
                        alienConfig.getExplosionConfig(),
                        alienConfig.getAlienCharacter().getExplosionAnimation(),
                        alienFactory,
                        powerUpAllocatorFactory,
                        sounds,
                        vibrator),
                createSpinningBehaviour(
                        alienConfig.getSpinningConfig(),
                        alienConfig.getSpeed()));

        this.boundariesChecker = new BoundariesChecker(
                this,
                alienConfig.getBoundaries().getMinX(),
                alienConfig.getBoundaries().getMaxX(),
                alienConfig.getBoundaries().getMinY(),
                alienConfig.getBoundaries().getMaxY());

        this.model = model;
        this.followers = followers;

        // set positional and movement behaviour
        this.timeDelayStart = timeDelayStart;

        // reset timer since last alien direction change
        timeSinceLastDirectionChange = 0f;

        // set starting direction angle
        this.angle = recalculateAngle(0f);

        this.speedInPixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
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
            int xDelta = (int) (speedInPixelsPerSecond * (float) Math.cos(this.angle));
            int yDelta = (int) (speedInPixelsPerSecond * (float) Math.sin(this.angle));

            // move alien by calculated deltas
            moveByDelta(
                    (int) (xDelta * deltaTime),
                    (int) (yDelta * deltaTime)
            );

            // update position of the following bodies so each are following the one before
            IAlien followableAlien = this;
            for (IAlienFollower follower : followers) {
                if (follower.isActive()) {
                    follower.follow(followableAlien, deltaTime);
                    followableAlien = follower;
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
     * If head explodes then all body parts should also explode.
     * If not, show hit across all body parts.
     */
    @Override
    public void onHitBy(IBaseMissile baseMissile) {
        super.onHitBy(baseMissile);

        if (isExploding()) {
            for (IAlienFollower follower : followers) {
                if (follower.isActive()) {
                    follower.showExplode();
                }
            }
        } else {
            for (IAlienFollower follower : followers) {
                if (follower.isActive()) {
                    follower.showHit();
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

            // if alien is outside wanted boundaries (e.g. off-screen), return it back immediately (can get lost!).
            // calculates new angle to take it to the centre of it's boundaries
            if (boundariesChecker.isOutsideBoundaries()) {
                return (float) Math.atan2(boundariesChecker.centreY() - y(), boundariesChecker.centreX() - x());
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
