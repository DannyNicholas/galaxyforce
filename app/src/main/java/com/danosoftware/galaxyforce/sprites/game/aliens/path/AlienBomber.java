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

public class AlienBomber extends AbstractAlienWithPath {

    /* minimum delay between spawning aliens in seconds */
    private static final float MIN_SPAWN_DELAY = 1f;

    /*
     * maximum addition random time before spawning aliens
     */
    private static final float SPAWN_DELAY_RANDOM = 0.5f;


    // alien animation
    private static final Animation ANIMATION = new Animation(
            0.5f,
            GameSpriteIdentifier.ZOGG_UP,
            GameSpriteIdentifier.ZOGG_DOWN);
    private static final Animation ANIMATION_HIT = new Animation(
            0.5f,
            GameSpriteIdentifier.ZOGG_UP_HIT,
            GameSpriteIdentifier.ZOGG_DOWN_HIT);

    /**
     * Create Alien Bomber that spawns bombs.
     */
    public AlienBomber(
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
