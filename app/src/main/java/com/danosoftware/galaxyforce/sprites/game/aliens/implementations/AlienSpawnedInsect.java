package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;

public class AlienSpawnedInsect extends AbstractAlien {
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* minimum delay between alien firing missiles in seconds */
    private static final float MIN_MISSILE_DELAY = 1f;

    /* maximum addition random time before firing */
    private static final float MISSILE_DELAY_RANDOM = 0.5f;

    /* energy of this sprite */
    private static final int ENERGY = 1;

    // alien animation
    private static final Animation ANIMATION = new Animation(
            0.5f,
            GameSpriteIdentifier.INSECT_WINGS_UP,
            GameSpriteIdentifier.INSECT_WINGS_DOWN);


    /* distance alien can move in pixels each second */
    public static final int ALIEN_MOVE_PIXELS = 2 * 60;

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* variable to store original position for alien when spawned */
    private final int originalYPosition;

    /* variable to store how far alien has moved since spawned */
    private float distanceYMoved;

    /**
     * Create spawned Alien Insect.
     */
    public AlienSpawnedInsect(
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
                new FireRandomDelay(
                        model,
                        AlienMissileType.SIMPLE,
                        MIN_MISSILE_DELAY,
                        MISSILE_DELAY_RANDOM),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new HitDisabled(),
                new ExplodeSimple(sounds, vibrator));

        /* distance moved since spawned */
        this.distanceYMoved = 0f;

        /* original y position */
        this.originalYPosition = yStart;
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        /* if active then alien can move */
        if (isActive()) {
            distanceYMoved += ALIEN_MOVE_PIXELS * deltaTime;

            move(
                    x(),
                    originalYPosition - (int) distanceYMoved);
        }

        /*
         * if alien off screen then destroy alien no need to handle explosions
         */
        if (offScreenBottom(this)) {
            destroy();
        }
    }
}
