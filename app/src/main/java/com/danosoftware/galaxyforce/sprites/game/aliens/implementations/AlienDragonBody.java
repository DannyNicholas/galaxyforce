package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

public class AlienDragonBody extends AbstractAlien implements IAlienFollower {
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* minimum delay between alien firing missiles in seconds */
    private static final float MIN_MISSILE_DELAY = 4.5f;

    /* maximum addition random time before firing */
    private static final float MISSILE_DELAY_RANDOM = 2f;

    /* distance alien can move each cycle in pixels each second */
    private static final int ALIEN_MOVE_PIXELS = 5 * 60;

    /* minimum distance between dragon alien bodies */
    private static final int MIN_DISTANCE_SQUARED = 25 * 25;

    /* energy of this sprite */
    private static final int ENERGY = 1;

    // alien animation
    private static final Animation ANIMATION = new Animation(0f, GameSpriteIdentifier.DRAGON_BODY);

    private boolean started;

    /**
     * Create Alien Dragon's Body that has rotated missiles and generates random
     * power-ups.
     */
    public AlienDragonBody(
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart,
            final GameModel model,
            final SoundPlayerService sounds,
            final VibrationService vibrator) {

        super(
                ANIMATION,
                xStart,
                yStart,
                ENERGY,
                new FireRandomDelay(model, AlienMissileType.ROTATED, MIN_MISSILE_DELAY, MISSILE_DELAY_RANDOM),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new HitDisabled(),
                new ExplodeSimple(sounds, vibrator));

        this.started = false;
    }

    /**
     * Update position of body based on alien this is following.
     * The body will attempt to follow the body in front but will throttle
     * it's speed if it gets too close.
     */
    @Override
    public void follow(IAlien alienFollowed, float deltaTime) {

        if (!started) {
            activate();
            this.started = true;
        }

        // calculate angle from this dragon body to the one we are following
        float newAngle = (float) Math.atan2(
                alienFollowed.y() - y(),
                alienFollowed.x() - x());

        // calculate the deltas to be applied each move
        int xDelta = (int) ((ALIEN_MOVE_PIXELS) * (float) Math.cos(newAngle));
        int yDelta = (int) ((ALIEN_MOVE_PIXELS) * (float) Math.sin(newAngle));

        // calculate new position
        int newX = x() + (int) (xDelta * deltaTime);
        int newY = y() + (int) (yDelta * deltaTime);

        // calculate squared distance from alien we are following
        int distX = (alienFollowed.x() - newX);
        int distY = (alienFollowed.y() - newY);
        int distSquared = (distX * distX) + (distY * distY);

        // if we are too close we need to throttle our speed
        if (distSquared > MIN_DISTANCE_SQUARED) {
            move(newX, newY);
        } else {
            float throttleRatio = (float) distSquared / MIN_DISTANCE_SQUARED;

            // calculate new position based on reduced speed
            int reducedXDelta = (int) (xDelta * throttleRatio);
            int reducedYDelta = (int) (yDelta * throttleRatio);
            int recalculatedX = x() + (int) (reducedXDelta * deltaTime);
            int recalculatedY = y() + (int) (reducedYDelta * deltaTime);

            // move alien
            move(recalculatedX, recalculatedY);
        }
    }
}
