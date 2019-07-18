package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienWithPath;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.PathConfig;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory.createSpawnBehaviour;

/**
 * Alien that follows a pre-determined path
 */
public class PathAlien extends AbstractAlienWithPath {

    @Builder
    public PathAlien(
            @NonNull final AlienFactory alienFactory,
            @NonNull final GameModel model,
            @NonNull final SoundPlayerService sounds,
            @NonNull final VibrationService vibrator,
            @NonNull final PathConfig alienConfig,
            final PowerUpType powerUpType,
            @NonNull final List<Point> alienPath,
            @NonNull final Float delayStartTime,
            @NonNull final Boolean restartImmediately) {
        super(
                alienConfig.getAlienCharacter().getAnimation(),
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
                alienPath,
                delayStartTime,
                alienConfig.getEnergy(),
                restartImmediately);
    }
}
