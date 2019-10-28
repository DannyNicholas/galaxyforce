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
import com.danosoftware.galaxyforce.waves.config.aliens.StaticConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory.createSpawnBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory.createSpinningBehaviour;

/**
 * Alien that stays in a fixed position once spawned.
 */
public class StaticAlien extends AbstractAlien {

    @Builder
    public StaticAlien(
            @NonNull final AlienFactory alienFactory,
            @NonNull final PowerUpAllocatorFactory powerUpAllocatorFactory,
            @NonNull final GameModel model,
            @NonNull final SoundPlayerService sounds,
            @NonNull final VibrationService vibrator,
            @NonNull final StaticConfig alienConfig,
            final PowerUpType powerUpType,
            @NonNull final Integer xStart,
            @NonNull final Integer yStart) {

        super(
                alienConfig.getAlienCharacter(),
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
                        powerUpAllocatorFactory,
                        alienConfig),
                new HitAnimation(
                        sounds,
                        vibrator,
                        alienConfig.getAlienCharacter().getHitAnimation()),
                new ExplodeSimple(
                        sounds,
                        vibrator,
                        alienConfig.getAlienCharacter().getExplosionAnimation()),
                createSpinningBehaviour(
                        alienConfig));
    }
}
