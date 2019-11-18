package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractResettableAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalResettableConfig;

import lombok.Builder;
import lombok.NonNull;

/**
 * Alien that moves from starting position across the screen in a
 * set direction until it moves off-screen. The alien will then be reset and replayed.
 */
public class DirectionalResettableAlien extends AbstractResettableAlien {

    // offset applied to x and y every move
    private final int xDelta;
    private final int yDelta;

    @Builder
    public DirectionalResettableAlien(
            @NonNull final ExplosionBehaviourFactory explosionFactory,
            @NonNull final SpawnBehaviourFactory spawnFactory,
            @NonNull final SpinningBehaviourFactory spinningFactory,
            @NonNull final PowerUpBehaviourFactory powerUpFactory,
            @NonNull final FireBehaviourFactory fireFactory,
            @NonNull final HitBehaviourFactory hitFactory,
            @NonNull final DirectionalResettableConfig alienConfig,
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
                        alienConfig.getSpeed()),
                timeDelayStart,
                restartImmediately);

        // calculate the deltas to be applied each move
        final int movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
        final float angle = alienConfig.getAngle();
        this.xDelta = (int) (movePixelsPerSecond * (float) Math.cos(angle));
        this.yDelta = (int) (movePixelsPerSecond * (float) Math.sin(angle));
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        if (isActive()) {
            moveByDelta(
                    (int) (xDelta * deltaTime),
                    (int) (yDelta * deltaTime));
        }
    }
}
