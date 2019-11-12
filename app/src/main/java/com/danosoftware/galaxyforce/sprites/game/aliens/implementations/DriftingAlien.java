package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DriftingConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory.createExplosionBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory.createSpawnBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory.createSpinningBehaviour;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenLeft;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenRight;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenTop;

/**
 * Alien that descends from starting position down the screen until it reaches
 * the bottom.
 */
public class DriftingAlien extends AbstractAlien {

    // offset applied to x and y every move
    private final int xDelta;
    private final int yDelta;

    /* original position */
    private final int originalXPosition;
    private final int originalYPosition;

    /* how many seconds to delay before alien starts */
    private float timeDelayStart;

    /* restart alien as soon as it leaves screen? */
    private final boolean restartImmediately;

    @Builder
    public DriftingAlien(
            @NonNull AlienFactory alienFactory,
            @NonNull final PowerUpAllocatorFactory powerUpAllocatorFactory,
            @NonNull final GameModel model,
            @NonNull final SoundPlayerService sounds,
            @NonNull final VibrationService vibrator,
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

        waiting();

        // set positional and movement behaviour
        this.timeDelayStart = timeDelayStart;
        this.originalXPosition = xStart;
        this.originalYPosition = yStart;
        this.restartImmediately = restartImmediately;

        // calculate the deltas to be applied each move
        final int movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
        final float angle = alienConfig.getAngle();

        // calculate angle from missile position to base
//        final double angle = Math.atan2(
//                SCREEN_MID_Y - yStart,
//                SCREEN_MID_X - xStart);

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
