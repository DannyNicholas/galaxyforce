package com.danosoftware.galaxyforce.sprites.game.aliens.path;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienWithPath;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.config.AlienConfig;

import java.util.List;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;

public class AlienMinion extends AbstractAlienWithPath {

    // alien animation
    private static final Animation ANIMATION = new Animation(
            0.5f,
            GameSpriteIdentifier.ALIEN_MINION_NORMAL,
            GameSpriteIdentifier.ALIEN_MINION_FUZZ1,
            GameSpriteIdentifier.ALIEN_MINION_FUZZ2);
    private static final Animation HIT_ANIMATION = new Animation(
            0.5f,
            GameSpriteIdentifier.ALIEN_MINION_NORMAL,
            GameSpriteIdentifier.ALIEN_MINION_FUZZ1,
            GameSpriteIdentifier.ALIEN_MINION_FUZZ2);

    // alien missile
    private static final AlienMissileCharacter MISSILE_CHARACTER = AlienMissileCharacter.LASER;

    /**
     * Create Alien Minion that has simple directional missiles and only
     * generates guided missile power-ups.
     */
    public AlienMinion(
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
                new HitAnimation(sounds, vibrator, HIT_ANIMATION),
                new ExplodeSimple(sounds, vibrator),
                alienPath,
                delayStart,
                alienConfig.getEnergy(),
                restartImmediately);
    }
}
