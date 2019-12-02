package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DriftingConfig;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenLeft;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenRight;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenTop;

/**
 * Alien that drifts from starting position across the screen.
 * When it reaches the edge of the screen, it reappears on the other side.
 */
public class DriftingAlien extends AbstractAlien {

    // offset applied to x and y every move
    private final int xDelta;
    private final int yDelta;

    /* how many seconds to delay before alien starts */
    private float timeDelayStart;

    @Builder
    public DriftingAlien(
            @NonNull final ExplosionBehaviourFactory explosionFactory,
            @NonNull final SpawnBehaviourFactory spawnFactory,
            @NonNull final SpinningBehaviourFactory spinningFactory,
            @NonNull final PowerUpBehaviourFactory powerUpFactory,
            @NonNull final FireBehaviourFactory fireFactory,
            @NonNull final HitBehaviourFactory hitFactory,
            @NonNull final DriftingConfig alienConfig,
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

        // calculate the deltas to be applied each move
        final int movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
        final float angle = alienConfig.getAngle();

        this.xDelta = (int) (movePixelsPerSecond * Math.cos(angle));
        this.yDelta = (int) (movePixelsPerSecond * Math.sin(angle));
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        /* if active then alien can move */
        if (isActive()) {

            // move alien by calculated deltas
            moveByDelta(
                    (int) (xDelta * deltaTime),
                    (int) (yDelta * deltaTime));

            // test if alien is off the screen and is continuing to move
            // away. In that case, shift aline to the opposite side of screen
            // so it will re-appear on the opposite side.
            if (offScreenLeft(this) && xDelta < 0) {
                moveXByDelta(GAME_WIDTH - x() + halfWidth());
            }
            if (offScreenRight(this) && xDelta > 0) {
                moveXByDelta(-GAME_WIDTH - ((x() + halfWidth()) % GAME_WIDTH));
            }
            if (offScreenBottom(this) && yDelta < 0) {
                moveYByDelta(GAME_HEIGHT - y() + halfHeight());
            }
            if (offScreenTop(this) && yDelta > 0) {
                moveYByDelta(-GAME_HEIGHT - ((y() + halfHeight()) % GAME_HEIGHT));
            }
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
}
