package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.models.screens.game.handlers.IGameHandler;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienType;

import java.util.List;

public class AlienMothership extends AbstractAlienWithPath {
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* minimum delay between spawning aliens in seconds */
    private static final float MIN_SPAWN_DELAY = 0.5f;

    /*
     * maximum addition random time before spawning aliens
     */
    private static final float SPAWN_DELAY_RANDOM = 0.25f;

    /* energy of this sprite */
    private static final int ENERGY = 10;

    // alien animation
    private static final Animation ANIMATION = new Animation(
            0f,
            GameSpriteIdentifier.EXPLODE_03);

    /**
     * Create Alien Mothership that has rotated missiles and generates random
     * power-ups.
     */
    public AlienMothership(
            final AlienFactory alienFactory,
            final IGameHandler model,
            final SoundPlayerService sounds,
            final VibrationService vibrator,
            final PowerUpType powerUpType,
            final List<PowerUpType> spwanPowerUpTypes,
            final List<Point> alienPath,
            final float delayStart,
            final boolean restartImmediately) {
        super(
                ANIMATION,
                new FireDisabled(),
                new PowerUpSingle(model, powerUpType),
                new SpawnRandomDelay(
                        alienFactory,
                        model,
                        AlienType.SPAWNED_INSECT,
                        spwanPowerUpTypes,
                        MIN_SPAWN_DELAY,
                        SPAWN_DELAY_RANDOM),
                new HitDisabled(),
                new ExplodeSimple(sounds, vibrator),
                alienPath,
                delayStart,
                ENERGY,
                restartImmediately);
    }
}
