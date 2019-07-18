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
import com.danosoftware.galaxyforce.waves.config.aliens.DescendingConfig;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory.createSpawnBehaviour;
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

    @Builder
    public DescendingAlien(
            @NonNull AlienFactory alienFactory,
            @NonNull final GameModel model,
            @NonNull final SoundPlayerService sounds,
            @NonNull final VibrationService vibrator,
            @NonNull final DescendingConfig alienConfig,
            final PowerUpType powerUpType,
            @NonNull final Integer xStart,
            @NonNull final Integer yStart) {
        super(
                alienConfig.getAlienCharacter().getAnimation(),
                xStart,
                yStart,
                alienConfig.getEnergy(),
                createFireBehaviour(
                        model,
                        alienConfig),
                new PowerUpSingle(model, powerUpType),
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
                        vibrator));

        this.distanceYMoved = 0f;
        this.originalYPosition = yStart;
        this.movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        /* if active then alien can move */
        if (isActive()) {
            distanceYMoved += movePixelsPerSecond * deltaTime;

            moveY(originalYPosition - (int) distanceYMoved);
        }

        /*
         * if alien off screen then destroy alien no need to handle explosions
         */
        if (offScreenBottom(this)) {
            destroy();
        }
    }
}
