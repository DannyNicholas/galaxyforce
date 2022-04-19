package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.helpers.BoundariesChecker;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.HunterConfig;
import lombok.Builder;
import lombok.NonNull;

/**
 * Alien hunter that will attempt to crash into the base.
 */
public class HunterAlien extends AbstractAlien {

    /* distance alien can move each cycle in pixels each second */
    private final int speedInPixelsPerSecond;

    /* time delay between alien direction changes */
    private static final float ALIEN_DIRECTION_CHANGE_DELAY = 0.1f;

    /* maximum alien change direction in radians */
    private static final float MAX_DIRECTION_CHANGE_ANGLE = 0.3f;

    /* angle from alien to base */
    private float angle;

    /* how many seconds to delay before alien starts to follow path */
    private float timeDelayStart;

    /* variable to store time passed since last alien direction change */
    private float timeSinceLastDirectionChange;

    private final GameModel model;

    // represents the boundaries the alien can fly within
    private final BoundariesChecker boundariesChecker;

    /**
     * Create Alien Hunter.
     */
    @Builder
    public HunterAlien(
        @NonNull final ExplosionBehaviourFactory explosionFactory,
        @NonNull final SpawnBehaviourFactory spawnFactory,
        @NonNull final SpinningBehaviourFactory spinningFactory,
        @NonNull final PowerUpBehaviourFactory powerUpFactory,
        @NonNull final FireBehaviourFactory fireFactory,
        @NonNull final HitBehaviourFactory hitFactory,
        @NonNull final GameModel model,
        @NonNull final HunterConfig alienConfig,
        final PowerUpType powerUpType,
        @NonNull final Float xStart,
        @NonNull final Float yStart,
        @NonNull final Float timeDelayStart) {

      super(
          alienConfig.getAlienCharacter(),
          alienConfig.getAlienCharacter().getAnimation(),
          xStart,
          yStart,
          alienConfig.getEnergy(),
          fireFactory.createFireBehaviour(
              alienConfig.getMissileConfig()),
          powerUpFactory.createPowerUpBehaviour(
                        powerUpType),
                spawnFactory.createSpawnBehaviour(
                        alienConfig.getSpawnConfig()),
                hitFactory.createHitBehaviour(
                        alienConfig.getAlienCharacter().getHitAnimation()),
                explosionFactory.createExplosionBehaviour(
                        alienConfig.getExplosionConfig(),
                        alienConfig.getAlienCharacter()),
                spinningFactory.createSpinningBehaviour(
                        alienConfig.getSpinningConfig(),
                        alienConfig.getSpeed()));

        this.boundariesChecker = new BoundariesChecker(
                this,
                alienConfig.getBoundaries().getMinX(),
                alienConfig.getBoundaries().getMaxX(),
                alienConfig.getBoundaries().getMinY(),
                alienConfig.getBoundaries().getMaxY(),
                alienConfig.getBoundaries().getLanePolicy(),
                alienConfig.getBoundaries().getLanes());

        waiting();

        this.model = model;

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
          float xDelta = speedInPixelsPerSecond * (float) Math.cos(this.angle);
          float yDelta = speedInPixelsPerSecond * (float) Math.sin(this.angle);

          // move alien by calculated deltas
          moveByDelta(
              xDelta * deltaTime,
              yDelta * deltaTime);

        } else if (isWaiting()) {
            // countdown until activation time
            timeDelayStart -= deltaTime;

            // activate alien. can only happen once!
            if (timeDelayStart <= 0) {
                activate();
                animate(0 - timeDelayStart);
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
            float newAngle = (float) Math.atan2(base.y() - y(), base.x() - x());

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
