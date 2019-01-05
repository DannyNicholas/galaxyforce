package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.handlers.IGameHandler;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;

public class AlienAsteroid extends AbstractAlien {
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* energy of this alien */
    private static final int ENERGY = 5;

    // normal animation
    private static final Animation ANIMATION = new Animation(
            0f, GameSpriteIdentifier.ASTEROID);

    // hit animation
    private static final Animation HIT_ANIMATION = new Animation(
            0f, GameSpriteIdentifier.ALIEN_GOBBY_LEFT);

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* current for sprite rotation */
    private float angle;

    /* speed of sprite rotation */
    private final int anglularSpeed;

    /* speed of asteroid */
    private final int speed;

    /* how many seconds to delay before alien starts to follow path */
    private float timeDelayStart;

    /* restart asteroid as soon as it leaves screen? */
    private final boolean restartImmediately;

    /* variable to store original position for asteroid */
    private final int originalYPosition;

    /* variable to store how far alien has moved since spawned */
    private float distanceYMoved;

    /**
     * Create Alien Asteroid.
     */
    public AlienAsteroid(
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart,
            final float timeDelayStart,
            final boolean restartImmediately,
            final IGameHandler model,
            final SoundPlayerService sounds,
            final VibrationService vibrator) {
        // default is that asteroids are initially invisible
        super(
                ANIMATION,
                xStart,
                yStart,
                ENERGY,
                new FireDisabled(),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new HitAnimation(HIT_ANIMATION),
                new ExplodeSimple(sounds, vibrator));

        waiting();

        // set positional and movement behaviour
        this.timeDelayStart = timeDelayStart;
        this.originalYPosition = yStart;
        this.distanceYMoved = 0f;
        this.restartImmediately = restartImmediately;

        // set random starting rotation angle
        this.angle = (float) (Math.random() * 360);

        // set random rotation speed between 50 and 400
        this.anglularSpeed = 50 + (int) (Math.random() * 350);

        // set asteroid speed between 75 and 250 (related to angular speed)
        this.speed = 50 + (anglularSpeed / 2);
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        /* if active then asteroid can move */
        if (isActive()) {

            // move until off the screen and then either destroy it or reset it

            // move
            distanceYMoved += speed * deltaTime;
            move(
                    x(),
                    (originalYPosition - (int) distanceYMoved)
            );

            // if asteroid is now off screen then decide whether to destory
            // it or reset
            if (offScreenBottom(this)) {
                if (restartImmediately) {
                    move(x(), originalYPosition);
                    distanceYMoved = 0f;
                } else {
                    destroy();
                }
            }

            // rotate asteroid
            angle = (angle + (deltaTime * anglularSpeed)) % 360;
            rotate((int) (angle));
        } else if (isWaiting()) {
            /* if delayStart still > 0 then count down delay */
            if (timeDelayStart > 0) {
                timeDelayStart -= deltaTime;
            }
            /* otherwise activate alien. can only happen once! */
            else {
                activate();
            }
        }
    }
}
