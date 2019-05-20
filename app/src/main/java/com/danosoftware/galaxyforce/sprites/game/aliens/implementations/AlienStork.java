package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienWithPath;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.config.AlienConfig;

import java.util.List;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;

public class AlienStork extends AbstractAlienWithPath {

    // alien animation
    private static final Animation ANIMATION = new Animation(
            0.4f,
            GameSpriteIdentifier.STORK_1,
            GameSpriteIdentifier.STORK_2);
    private static final Animation ANIMATION_HIT = new Animation(
            0.4f,
            GameSpriteIdentifier.STORK_1,
            GameSpriteIdentifier.STORK_2);

    // alien missile
    private static final AlienMissileCharacter MISSILE_CHARACTER = AlienMissileCharacter.LASER;

    /**
     * Create Alien Stork that has rotated missiles and generates random
     * power-ups.
     */
    public AlienStork(
            final GameModel model,
            final SoundPlayerService sounds,
            final VibrationService vibrator,
            final AlienConfig alienConfig,
            final PowerUpType powerUpType,
            final List<Point> alienPath,
            final float delayStart,
            final boolean restartImmediately) {
        super(
                ANIMATION,
                createFireBehaviour(
                        model,
                        alienConfig,
                        MISSILE_CHARACTER),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new HitDisabled(),
                new ExplodeSimple(sounds, vibrator),
                alienPath,
                delayStart,
                alienConfig.getEnergy(),
                restartImmediately);
    }
}
