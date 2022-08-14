package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.isTravellingOffScreen;

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
 * Alien that moves from starting position across the screen in a set direction until it moves
 * off-screen. The alien will then be reset and replayed.
 */
public class DirectionalResettableAlien extends AbstractResettableAlien {

  private final float startingX;
  private final float startingY;
  private final boolean restartImmediately;

  /* how many seconds to delay before alien starts to follow path */
  private float timeDelayStart;

  /* original time delay before alien starts in cases where alien is reset */
  private final float originalTimeDelayStart;

  // offset applied to x and y every move
  private final float xDelta;
  private final float yDelta;

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
      @NonNull final Float xStart,
      @NonNull final Float yStart,
      @NonNull final Float timeDelayStart,
      @NonNull final Boolean restartImmediately) {

    super(
        alienConfig.getAlienCharacter(),
        xStart,
        yStart,
        alienConfig.getEnergy(),
        fireFactory.createFireBehaviour(
            alienConfig.getMissileConfig()),
        powerUpFactory.createPowerUpBehaviour(
            powerUpType),
        spawnFactory.createSpawnBehaviour(
            alienConfig.getSpawnConfig()),
        hitFactory.createHitBehaviour(),
        explosionFactory.createExplosionBehaviour(
            alienConfig.getExplosionConfig(),
            alienConfig.getAlienCharacter()),
        spinningFactory.createSpinningBehaviour(
            alienConfig.getSpinningConfig(),
            alienConfig.getSpeed()));

    waiting();
    this.startingX = x;
    this.startingY = y;
    this.restartImmediately = restartImmediately;
    this.timeDelayStart = timeDelayStart;
    this.originalTimeDelayStart = timeDelayStart;

    // calculate the deltas to be applied each move
    final int movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
    final float angle = alienConfig.getAngle();
    this.xDelta = movePixelsPerSecond * (float) Math.cos(angle);
    this.yDelta = movePixelsPerSecond * (float) Math.sin(angle);
  }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        if (isActive()) {
          if (isTravellingOffScreen(this, xDelta, yDelta)) {
            if (restartImmediately) {
              reset(originalTimeDelayStart);
            } else {
              endOfPass();
            }
          }
          moveByDelta(
              xDelta * deltaTime,
              yDelta * deltaTime);
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

    /**
     * Resets alien if the alien has gone off-screen without being destroyed and
     * sub-wave needs to be repeated.
     * <p>
     * Reduce original delay by supplied offset in case alien needs to start
     * earlier.
     */
    @Override
    public void reset(float offset) {
        timeDelayStart = originalTimeDelayStart - offset;
        waiting();

        /*
         * reset back at start position - will be made visible and active before
         * recalculating it's position.
         */
        move(startingX, startingY);
    }

    /**
     * Get the original time delay. Can be used to calculate a corrected time
     * delay offset.
     */
    @Override
    public float getTimeDelay() {
        return originalTimeDelayStart;
    }
}
