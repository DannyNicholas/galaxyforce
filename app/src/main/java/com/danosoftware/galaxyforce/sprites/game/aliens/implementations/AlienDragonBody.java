package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.config.AlienConfig;

public class AlienDragonBody extends AbstractAlienFollower implements IAlienFollower {

    /* distance alien can move each cycle in pixels each second */
    private static final int ALIEN_MOVE_PIXELS = 5 * 60;

    /* minimum distance between dragon alien bodies */
    private static final int MIN_DISTANCE = 25;

    // alien animation
    private static final Animation ANIMATION = new Animation(0f, GameSpriteIdentifier.DRAGON_BODY);
    private static final Animation HIT_ANIMATION = new Animation(0f, GameSpriteIdentifier.DRAGON_BODY_HIT);

    /**
     * Create Alien Dragon's Body.
     */
    public AlienDragonBody(
            final GameModel model,
            final SoundPlayerService sounds,
            final VibrationService vibrator,
            final AlienConfig alienConfig,
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart) {

        super(
                ANIMATION,
                xStart,
                yStart,
                alienConfig.getEnergy(),
                new FireDisabled(),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new HitAnimation(sounds, vibrator, HIT_ANIMATION),
                new ExplodeSimple(sounds, vibrator),
                ALIEN_MOVE_PIXELS,
                MIN_DISTANCE);
    }
}
