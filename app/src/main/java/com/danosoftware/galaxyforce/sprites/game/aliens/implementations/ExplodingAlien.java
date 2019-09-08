package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.assets.AlienMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienMissileFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.ExplodingConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

import lombok.Builder;
import lombok.NonNull;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory.createSpawnBehaviour;
import static com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory.createSpinningBehaviour;

/**
 * Alien that stays in a fixed position once spawned for a defined duration
 * and then explodes by spraying missiles in all directions.
 */
public class ExplodingAlien extends AbstractAlien {

    /* how many seconds before bomb explodes */
    private final float timeBeforeExpolosion;

    // exploding missile
    private final AlienMissileCharacter explodingMissileCharacter;

    private final GameModel model;

    /* variable to store time passed */
    private float timer;

    private boolean isExploding;

    @Builder
    public ExplodingAlien(
            @NonNull final AlienFactory alienFactory,
            @NonNull final PowerUpAllocatorFactory powerUpAllocatorFactory,
            @NonNull final GameModel model,
            @NonNull final SoundPlayerService sounds,
            @NonNull final VibrationService vibrator,
            @NonNull final ExplodingConfig alienConfig,
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
                        powerUpAllocatorFactory,
                        alienConfig),
                new HitAnimation(
                        sounds,
                        vibrator,
                        alienConfig.getAlienCharacter().getHitAnimation()),
                new ExplodeSimple(
                        sounds,
                        vibrator),
                createSpinningBehaviour(
                        alienConfig));

        this.model = model;
        this.timeBeforeExpolosion = alienConfig.getExplosionTime();
        this.explodingMissileCharacter = alienConfig.getExplodingMissileCharacter();

        // reset timer
        timer = 0f;
        isExploding = false;
    }

    @Override
    public void animate(float deltaTime) {

        super.animate(deltaTime);

        if (!isExploding) {
            timer += deltaTime;
            if (timer > timeBeforeExpolosion) {
                explode();
                // send missiles to model
                AlienMissilesDto missiles = AlienMissileFactory.createAlienMissile(
                        model.getBase(),
                        this,
                        AlienMissileType.SPRAY,
                        AlienMissileSpeed.MEDIUM,
                        explodingMissileCharacter);
                model.fireAlienMissiles(missiles);
                isExploding = true;
            }
        }
    }
}
