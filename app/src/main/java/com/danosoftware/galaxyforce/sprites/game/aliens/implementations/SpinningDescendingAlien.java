package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.SpinningDescendingConfig;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory.createSpawnBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory.createSpinningBehaviour;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;

/**
 * Alien that descends from starting position down the screen until it reaches
 * the bottom. This alien will spin at a speed relative to it's downward speed.
 */
public class SpinningDescendingAlien extends AbstractAlien {

    /* current for sprite rotation */
    private float angle;

    /* speed of sprite rotation */
    private final int anglularSpeed;

    /* downwards speed */
    private final int speed;

    /* how many seconds to delay before alien starts to follow path */
    private float timeDelayStart;

    /* restart alien as soon as it leaves screen? */
    private final boolean restartImmediately;

    /* variable to store original position for alien */
    private final int originalYPosition;

    /* variable to store how far alien has moved since spawned */
    private float distanceYMoved;

    @Builder
    public SpinningDescendingAlien(
            @NonNull AlienFactory alienFactory,
            @NonNull GameModel model,
            @NonNull final SoundPlayerService sounds,
            @NonNull final VibrationService vibrator,
            @NonNull final SpinningDescendingConfig alienConfig,
            final PowerUpType powerUpType,
            @NonNull final Integer xStart,
            @NonNull final Integer yStart,
            @NonNull final Float timeDelayStart,
            @NonNull final Boolean restartImmediately) {

        super(
                alienConfig.getAlienCharacter().getAnimation(),
                xStart,
                yStart,
                alienConfig.getEnergy(),
                createFireBehaviour(
                        model,
                        alienConfig),
                new PowerUpSingle(
                        model,
                        powerUpType),
                createSpawnBehaviour(
                        model,
                        alienFactory,
                        alienConfig),
                new HitAnimation(
                        sounds,
                        vibrator,
                        alienConfig.getAlienCharacter().getHitAnimation()),
                new ExplodeSimple(
                        sounds,
                        vibrator),
                createSpinningBehaviour(
                        alienConfig,
                        alienConfig.getSpeed()));

        waiting();

        // set positional and movement behaviour
        this.timeDelayStart = timeDelayStart;
        this.originalYPosition = yStart;
        this.distanceYMoved = 0f;
        this.restartImmediately = restartImmediately;

        // set random starting rotation angle
        this.angle = (float) (Math.random() * 360);

        // set angular rotation speed relative to downward speed
        final int pixelSpeed = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
        this.speed = pixelSpeed;
        this.anglularSpeed = (pixelSpeed - 50) * 2;
    }

    @Override
    public void animate(float deltaTime) {

        super.animate(deltaTime);

        /* if active then alien can move */
        if (isActive()) {

            // move until off the screen and then either destroy it or reset it
            distanceYMoved += speed * deltaTime;
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

            // rotate alien
            angle = (angle + (deltaTime * anglularSpeed)) % 360;
            rotate((int) (angle));
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
