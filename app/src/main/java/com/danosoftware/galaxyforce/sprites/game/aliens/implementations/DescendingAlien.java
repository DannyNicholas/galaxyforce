package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DescendingConfig;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;

/**
 * Alien that descends from starting position down the screen until it reaches
 * the bottom.
 */
public class DescendingAlien extends AbstractAlien {

    /* distance alien can move in pixels each second */
    private final int movePixelsPerSecond;

    /* original y position */
    private final int originalYPosition;

    /* distance moved since spawned */
    private float distanceYMoved;

    /* how many seconds to delay before alien starts */
    private float timeDelayStart;

    /* restart alien as soon as it leaves screen? */
    private final boolean restartImmediately;

    @Builder
    public DescendingAlien(
            @NonNull final ExplosionBehaviourFactory explosionFactory,
            @NonNull final SpawnBehaviourFactory spawnFactory,
            @NonNull final SpinningBehaviourFactory spinningFactory,
            @NonNull final PowerUpBehaviourFactory powerUpFactory,
            @NonNull final FireBehaviourFactory fireFactory,
            @NonNull final HitBehaviourFactory hitFactory,
            @NonNull final DescendingConfig alienConfig,
            final PowerUpType powerUpType,
            @NonNull final Integer xStart,
            @NonNull final Integer yStart,
            @NonNull final Float timeDelayStart,
            @NonNull final Boolean restartImmediately) {

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
                        alienConfig.getAlienCharacter().getExplosionAnimation()),
                spinningFactory.createSpinningBehaviour(
                        alienConfig.getSpinningConfig(),
                        alienConfig.getSpeed()));

        waiting();

        // set positional and movement behaviour
        this.timeDelayStart = timeDelayStart;
        this.originalYPosition = yStart;
        this.distanceYMoved = 0f;
        this.restartImmediately = restartImmediately;
        this.movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        /* if active then alien can move */
        if (isActive()) {

            // move until off the screen and then either destroy it or reset it
            distanceYMoved += movePixelsPerSecond * deltaTime;
            moveY(originalYPosition - (int) distanceYMoved);

            // if alien is now off screen then decide whether to destory
            // it or reset
            if (offScreenBottom(this)) {
                if (restartImmediately) {
                    moveY(originalYPosition);
                    distanceYMoved = 0f;
                } else {
                    destroy();
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
}
