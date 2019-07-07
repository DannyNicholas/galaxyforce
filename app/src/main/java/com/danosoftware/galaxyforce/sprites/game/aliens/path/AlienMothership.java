package com.danosoftware.galaxyforce.sprites.game.aliens.path;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienWithPath;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.config.AlienConfig;

import java.util.List;

public class AlienMothership extends AbstractAlienWithPath {

    /* minimum delay between spawning aliens in seconds */
    private static final float MIN_SPAWN_DELAY = 0.5f;

    /*
     * maximum addition random time before spawning aliens
     */
    private static final float SPAWN_DELAY_RANDOM = 0.25f;


    // alien animation
    private static final Animation ANIMATION = new Animation(
            0.5f,
            GameSpriteIdentifier.MOTHER_BUZZER_WINGS_DOWN,
            GameSpriteIdentifier.MOTHER_BUZZER_WINGS_UP);
    private static final Animation ANIMATION_HIT = new Animation(
            0.5f,
            GameSpriteIdentifier.MOTHER_BUZZER_WINGS_DOWN_HIT,
            GameSpriteIdentifier.MOTHER_BUZZER_WINGS_UP_HIT);

    /**
     * Create Alien Mothership that spawns aliens.
     */
    public AlienMothership(
            final AlienFactory alienFactory,
            final GameModel model,
            final SoundPlayerService sounds,
            final VibrationService vibrator,
            final AlienConfig alienConfig,
            final AlienConfig spawnedAlienConfig,
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
                        spawnedAlienConfig,
                        spwanPowerUpTypes,
                        MIN_SPAWN_DELAY,
                        SPAWN_DELAY_RANDOM),
                new HitAnimation(sounds, vibrator, ANIMATION_HIT),
                new ExplodeSimple(sounds, vibrator),
                alienPath,
                delayStart,
                alienConfig.getEnergy(),
                restartImmediately);
    }
}
