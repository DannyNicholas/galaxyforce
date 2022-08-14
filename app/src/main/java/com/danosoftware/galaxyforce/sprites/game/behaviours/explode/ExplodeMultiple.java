package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.models.assets.SpawnedAliensDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrateTime;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.StaticExplosionConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import lombok.Getter;

/**
 * Implementation of explosion behaviour that will trigger multiple explosions to coincide with the
 * alien's explosion.
 */
public class ExplodeMultiple implements ExplodeBehaviour {

  // alien config to be used for spawning exploding aliens
  private final StaticExplosionConfig explodingConfig;

  // explosion mainAnimation
  private final Animation mainAnimation;

  // reference to sound player
  private final SoundPlayerService sounds;

  // reference to vibrator
  private final VibrationService vibrator;

  private final GameModel model;
  private final AlienFactory alienFactory;

  private SpriteDetails spriteToExplode;
  private boolean startedMainExplosion;

  private final int numberOfExplosions;
  private final float maximumExplosionStartTime;

  // time since explosion started
  private float explosionTime;
  private float mainExplosionTime;

  private final List<TimedExplosion> timedExplosions;

  public ExplodeMultiple(
      final AlienFactory alienFactory,
      final GameModel model,
      final SoundPlayerService sounds,
      final VibrationService vibrator,
      final AlienCharacter character,
      final int numberOfExplosions,
      final float maximumExplosionStartTime,
      final ExplosionConfig explosionConfig) {
    this.mainAnimation = character.getExplosionAnimation();
    this.sounds = sounds;
    this.vibrator = vibrator;
    this.model = model;
    this.alienFactory = alienFactory;

    this.numberOfExplosions = numberOfExplosions;
    this.maximumExplosionStartTime = maximumExplosionStartTime;
    this.explodingConfig = StaticExplosionConfig
        .builder()
        .alienCharacter(character)
        .explosionConfig(explosionConfig)
        .build();

    this.timedExplosions = new ArrayList<>();
  }

  @Override
  public void startExplosion(IAlien alien) {
    explosionTime = 0f;
    mainExplosionTime = 0f;
    startedMainExplosion = false;
    spriteToExplode = alien.spriteDetails();

    /*
     * Create a number of additional explosions around the exploding alien.
     * The additional explosions form a circle around the alien.
     * Each extra explosion will have a position and random start time.
     * Each explosion will be triggered when elapsed time has exceeded the start time.
     */
    if (numberOfExplosions > 0) {
      final int angleDelta = 360 / numberOfExplosions;
      final Random random = new Random();

      for (int i = 0; i < numberOfExplosions; i++) {
        final float angle = angleDelta * i;
        final int radius = Math.max(alien.halfHeight(), alien.halfWidth());
        final float x = alien.x() + (radius * (float) Math.cos(angle));
        final float y = alien.y() - (radius * (float) Math.sin(angle));
        timedExplosions.add(
            new TimedExplosion(
                x,
                y,
                random.nextFloat() * maximumExplosionStartTime));
      }

      // pick a random timed-explosion and reset start time to 0.
      // ensures at least one explosion starts immediately
      int idx = random.nextInt(timedExplosions.size());
      TimedExplosion timedExplosion = timedExplosions.get(idx);
      timedExplosions.set(idx, new TimedExplosion(
          timedExplosion.getX(),
          timedExplosion.getY(),
          0f));
    }
  }

  @Override
  public void startExplosionFollower(IAlienFollower alien) {
    startExplosion(alien);
  }

  @Override
  public SpriteDetails getExplosion(float deltaTime) {
    explosionTime += deltaTime;

    // trigger any explosions that are due to start
    Iterator<TimedExplosion> iterator = timedExplosions.iterator();
    while (iterator.hasNext()) {
      TimedExplosion timedExplosion = iterator.next();
      if (explosionTime >= timedExplosion.explodeTime) {
        iterator.remove();

        // create alien, immediately explode it and then spawn to model
        SpawnedAliensDto aliens = alienFactory.createStaticExplosion(
            explodingConfig,
            timedExplosion.getX(),
            timedExplosion.getY()
        );

        for (IAlien aAlien : aliens.getAliens()) {
          aAlien.explode();
        }
        model.spawnAliens(aliens);
      }
    }

    // the main explosion will only start if all extra explosions have already started
    if (timedExplosions.isEmpty() && !startedMainExplosion
        && explosionTime >= maximumExplosionStartTime) {
      startedMainExplosion = true;
      sounds.play(SoundEffect.EXPLOSION);
      vibrator.vibrate(VibrateTime.TINY);
    }

    if (startedMainExplosion) {
      mainExplosionTime += deltaTime;
      return mainAnimation.getKeyFrame(mainExplosionTime, Animation.ANIMATION_NONLOOPING);
    }

    // if we haven't started the main explosion, show the original frozen alien sprite
    return spriteToExplode;
  }

  @Override
  public boolean finishedExploding() {
    return mainAnimation.isAnimationComplete() && timedExplosions.isEmpty();
  }

  @Getter
  private static class TimedExplosion {

    private final float x;
    private final float y;
    private final float explodeTime;

    private TimedExplosion(
        final float x,
        final float y,
        final float explodeTime) {
      this.x = x;
      this.y = y;
      this.explodeTime = explodeTime;
    }
  }
}
