package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.FollowerConfig;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory.createSpawnBehaviour;

/**
 * Alien that is one follower in a chain of following aliens.
 * Followers will attempt to follow a followable alien
 * (normally the head of the chain).
 */
public class FollowerAlien extends AbstractAlienFollower implements IAlienFollower {

    /* minimum distance between follower bodies */
    private static final int MIN_DISTANCE = 25;

    @Builder
    public FollowerAlien(
            @NonNull final AlienFactory alienFactory,
            @NonNull final GameModel model,
            @NonNull final SoundPlayerService sounds,
            @NonNull final VibrationService vibrator,
            @NonNull final FollowerConfig alienConfig,
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
                alienConfig
                        .getSpeed()
                        .getSpeedInPixelsPerSeconds(),
                MIN_DISTANCE);
    }
}
