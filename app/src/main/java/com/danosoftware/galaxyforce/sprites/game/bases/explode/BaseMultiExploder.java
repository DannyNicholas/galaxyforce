package com.danosoftware.galaxyforce.sprites.game.bases.explode;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrateTime;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lombok.Getter;

public class BaseMultiExploder implements IBaseMultiExploder {

    // explosion animation
    private final Animation animation;

    // reference to sound player
    private final SoundPlayerService sounds;

    // reference to vibrator
    private final VibrationService vibrator;

    private final IBasePrimary base;

    private ISpriteIdentifier spriteToExplode;
    private boolean startedMainExplosion;

    private final static int NUMBER_OF_EXPLOSIONS = 4;
    private final static float MAXIMUM_EXPLOSION_START_TIME = 0.25f;
    private final static float MAIN_EXPLOSION_START_TIME = 0.3f;
    private final static float TWO_PI = (float) (2f * Math.PI);

    // time since explosion started
    private float explosionTime;
    private float mainExplosionTime;

    private final List<TimedExplosion> timedExplosions;

    // exploding bases for multi-base explosions
    private final List<IBaseExplosion> explodingBases;

    public BaseMultiExploder(
            final IBasePrimary base,
            SoundPlayerService sounds,
            VibrationService vibrator,
            Animation animation) {
        this.animation = animation;
        this.sounds = sounds;
        this.vibrator = vibrator;
        this.base = base;
        this.timedExplosions = new ArrayList<>();
        this.explodingBases = new ArrayList<>();
    }


    @Override
    public void startExplosion() {
        explosionTime = 0f;
        mainExplosionTime = 0f;
        startedMainExplosion = false;
        spriteToExplode = base.spriteId();

        /*
         * Create a number of additional explosions around the exploding base.
         * The additional explosions form a circle around the alien.
         * Each extra explosion will have a position and random start time.
         * Each explosion will be triggered when elapsed time has exceeded the start time.
         */
        if (NUMBER_OF_EXPLOSIONS > 0) {
            final int angleDelta = 360 / NUMBER_OF_EXPLOSIONS;
            final Random random = new Random();

            for (int i = 0; i < NUMBER_OF_EXPLOSIONS; i++) {
                final float angle = (angleDelta * i) / TWO_PI;
                final int radius = (int) ((base.halfHeight() < base.halfWidth()
                        ? base.halfHeight() : base.halfWidth()) * 1.5);
                final int x = base.x() + (int) (radius * (float) Math.cos(angle));
                final int y = base.y() - (int) (radius * (float) Math.sin(angle));
                timedExplosions.add(
                        new TimedExplosion(
                                x,
                                y,
                                random.nextFloat() * MAXIMUM_EXPLOSION_START_TIME));
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
    public ISpriteIdentifier getExplosion(float deltaTime) {
        explosionTime += deltaTime;

        // trigger any explosions that are due to start
        Iterator<TimedExplosion> iterator = timedExplosions.iterator();
        while (iterator.hasNext()) {
            TimedExplosion timedExplosion = iterator.next();
            if (explosionTime >= timedExplosion.explodeTime) {
                iterator.remove();

                final IBaseExplosion explosion = new BaseExplosion(
                        timedExplosion.getX(),
                        timedExplosion.getY(),
                        sounds,
                        vibrator);
                explosion.startExplosion();
                explodingBases.add(explosion);
            }
        }

        Iterator<IBaseExplosion> explosionIterator = explodingBases.iterator();
        while (explosionIterator.hasNext()) {
            IBaseExplosion explosion = explosionIterator.next();
            explosion.animate(deltaTime);
            if (explosion.isFinished()) {
                explosionIterator.remove();
            }
        }

        // the main explosion will only start if all extra explosions have already started
        if (timedExplosions.isEmpty() && !startedMainExplosion && explosionTime >= MAIN_EXPLOSION_START_TIME) {
            startedMainExplosion = true;
            sounds.play(SoundEffect.BIG_EXPLOSION);
            vibrator.vibrate(VibrateTime.TINY);
        }

        //if (startedMainExplosion) {
            mainExplosionTime += deltaTime;
            return animation.getKeyFrame(mainExplosionTime, Animation.ANIMATION_NONLOOPING);
        //}

        // if we haven't started the main explosion, show the original frozen alien sprite
        //return spriteToExplode;
    }

    @Override
    public boolean finishedExploding() {
        return animation.isAnimationComplete() && timedExplosions.isEmpty();
    }

    @Override
    public List<IBaseExplosion> getMultiExplosion() {
        return explodingBases;
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
