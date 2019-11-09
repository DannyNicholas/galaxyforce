package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.models.assets.SpawnedAliensDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrateTime;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.StaticConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lombok.Getter;

/**
 * Implementation of explosion behaviour that will trigger multiple explosions
 * to coincide with the alien's explosion.
 */
public class ExplodeMultiple implements ExplodeBehaviour {

    // alien config to be used for spawning exploding aliens
    private final AlienConfig explodingConfig;

    // explosion animation
    private final Animation animation;

    // reference to sound player
    private final SoundPlayerService sounds;

    // reference to vibrator
    private final VibrationService vibrator;

    private final GameModel model;
    private final AlienFactory alienFactory;
    private final PowerUpAllocatorFactory powerUpAllocatorFactory;

    private ISpriteIdentifier spriteToExplode;
    private boolean startedMainExplosion;

    private final int numberOfExplosions;
    private final float maximumExplosionStartTime;

    // time since explosion started
    private float explosionTime;
    private float mainExplosionTime;

    private final List<TimedExplosion> timedExplosions;

    public ExplodeMultiple(
            final AlienFactory alienFactory,
            final PowerUpAllocatorFactory powerUpAllocatorFactory,
            final GameModel model,
            SoundPlayerService sounds,
            VibrationService vibrator,
            Animation animation,
            final int numberOfExplosions,
            final float maximumExplosionStartTime,
            final ExplosionConfig explosionConfig) {
        this.animation = animation;
        this.sounds = sounds;
        this.vibrator = vibrator;
        this.model = model;
        this.alienFactory = alienFactory;
        this.powerUpAllocatorFactory = powerUpAllocatorFactory;

        this.numberOfExplosions = numberOfExplosions;
        this.maximumExplosionStartTime = maximumExplosionStartTime;
        this.explodingConfig = StaticConfig
                .builder()
                .alienCharacter(AlienCharacter.NULL)
                .energy(0)
                .explosionConfig(explosionConfig)
                .build();

        this.timedExplosions = new ArrayList<>();
    }

    @Override
    public void startExplosion(IAlien alien) {
        explosionTime = 0f;
        mainExplosionTime = 0f;
        startedMainExplosion = false;
        spriteToExplode = alien.spriteId();

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
                final int radius = alien.halfHeight() < alien.halfWidth()
                        ? alien.halfHeight() : alien.halfWidth();
                final int x = alien.x() + (int) (radius * (float) Math.cos(angle));
                final int y = alien.y() - (int) (radius * (float) Math.sin(angle));
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
    public void startExplosionSilently() {
        explosionTime = 0f;
    }

    @Override
    public ISpriteIdentifier getExplosion(float deltaTime) {
        explosionTime += deltaTime;

        // trigger any explosions that are due to start
        Iterator<TimedExplosion> iterator = timedExplosions.iterator();
        while (iterator.hasNext()) {
            TimedExplosion timedExplosion = iterator.next();
            if (explosionTime >= timedExplosion.explodeTime) {
                iterator.remove();

                // create alien, immediately explode it and then spawn to model
                SpawnedAliensDto aliens = alienFactory.createSpawnedAlien(
                        explodingConfig,
                        powerUpAllocatorFactory,
                        null,
                        timedExplosion.getX(),
                        timedExplosion.getY());
                for (IAlien aAlien : aliens.getAliens()) {
                    aAlien.explode();
                }
                model.spawnAliens(aliens);
            }
        }

        // the main explosion will only start if all extra explosions have already started
        if (timedExplosions.isEmpty() && !startedMainExplosion && explosionTime >= maximumExplosionStartTime) {
            startedMainExplosion = true;
            sounds.play(SoundEffect.EXPLOSION);
            vibrator.vibrate(VibrateTime.TINY);
        }

        if (startedMainExplosion) {
            mainExplosionTime += deltaTime;
            return animation.getKeyFrame(mainExplosionTime, Animation.ANIMATION_NONLOOPING);
        }

        // if we haven't started the main explosion, show the original frozen alien sprite
        return spriteToExplode;
    }

    @Override
    public boolean finishedExploding() {
        return animation.isAnimationComplete() && timedExplosions.isEmpty();
    }

    @Getter
    private class TimedExplosion {
        private final int x;
        private final int y;
        private final float explodeTime;

        private TimedExplosion(
                final int x,
                final int y,
                final float explodeTime) {
            this.x = x;
            this.y = y;
            this.explodeTime = explodeTime;
        }
    }
}
