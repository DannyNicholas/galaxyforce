package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.assets.AlienMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitAnimation;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienMissileFactory;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.config.AlienConfig;

public class AlienBomb extends AbstractAlien {

    /* how many seconds before bomb explodes */
    private static final float TIME_BEFORE_EXPLOSION = 3f;

    // alien animation
    private static final Animation ANIMATION = new Animation(
            0.2f,
            GameSpriteIdentifier.BOMB_01,
            GameSpriteIdentifier.BOMB_02,
            GameSpriteIdentifier.BOMB_03);
    private static final Animation HIT_ANIMATION = new Animation(
            0.2f,
            GameSpriteIdentifier.BOMB_01_HIT,
            GameSpriteIdentifier.BOMB_02_HIT,
            GameSpriteIdentifier.BOMB_03_HIT);

    // alien missile
    private static final AlienMissileCharacter MISSILE_CHARACTER = AlienMissileCharacter.FIREBALL;

    private final GameModel model;

    /* variable to store time passed */
    private float timer;

    private boolean isExploding;

    public AlienBomb(
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
                new ExplodeSimple(sounds, vibrator));

        this.model = model;

        // reset timer
        timer = 0f;
        isExploding = false;
    }

    @Override
    public void animate(float deltaTime) {

        super.animate(deltaTime);

        if (!isExploding) {
            timer += deltaTime;
            if (timer > TIME_BEFORE_EXPLOSION) {
                explode();
                // send missiles to model
                AlienMissilesDto missiles = AlienMissileFactory.createAlienMissile(
                        model.getBase(),
                        this,
                        AlienMissileType.SPRAY,
                        AlienMissileSpeed.MEDIUM,
                        MISSILE_CHARACTER);
                model.fireAlienMissiles(missiles);
                isExploding = true;
            }
        }
    }
}
