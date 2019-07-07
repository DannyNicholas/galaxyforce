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
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.config.AlienConfig;

import java.util.List;

import static com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory.createFireBehaviour;

public class PathAlien extends AbstractAlienWithPath {

    private PathAlien(
            final GameModel model,
            final SoundPlayerService sounds,
            final VibrationService vibrator,
            final AlienConfig alienConfig,
            final PowerUpType powerUp,
            final List<Point> alienPath,
            final float delayStart,
            final boolean restartImmediately,
            final Animation animation,
            final Animation hitAnimation,
            final AlienMissileCharacter missileCharacter) {
        super(
                animation,
                createFireBehaviour(
                        model,
                        alienConfig,
                        missileCharacter),
                new PowerUpSingle(model, powerUp),
                new SpawnDisabled(),
                new HitAnimation(sounds, vibrator, hitAnimation),
                new ExplodeSimple(sounds, vibrator),
                alienPath,
                delayStart,
                alienConfig.getEnergy(),
                restartImmediately);
    }

    public static class Builder {
        private GameModel model;
        private SoundPlayerService sounds;
        private VibrationService vibrator;
        private AlienConfig alienConfig;
        private PowerUpType powerUp;
        private List<Point> alienPath;
        private float delayStart;
        private boolean restartImmediately;
        private Animation animation;
        private Animation hitAnimation;
        private AlienMissileCharacter missileCharacter;

        public PathAlien build() {
            return new PathAlien(
                    this.model,
                    this.sounds,
                    this.vibrator,
                    this.alienConfig,
                    this.powerUp,
                    this.alienPath,
                    this.delayStart,
                    this.restartImmediately,
                    this.animation,
                    this.hitAnimation,
                    this.missileCharacter);
        }

        public Builder model(GameModel model) {
            this.model = model;
            return this;
        }

        public Builder sounds(SoundPlayerService sounds) {
            this.sounds = sounds;
            return this;
        }

        public Builder vibrator(VibrationService vibrator) {
            this.vibrator = vibrator;
            return this;
        }

        public Builder alienConfig(AlienConfig alienConfig) {
            this.alienConfig = alienConfig;
            return this;
        }

        public Builder powerUp(PowerUpType powerUp) {
            this.powerUp = powerUp;
            return this;
        }

        public Builder alienPath(List<Point> alienPath) {
            this.alienPath = alienPath;
            return this;
        }

        public Builder delayStartTime(float delayStart) {
            this.delayStart = delayStart;
            return this;
        }

        public Builder restartImmediately(boolean restartImmediately) {
            this.restartImmediately = restartImmediately;
            return this;
        }

        public Builder animation(Animation animation) {
            this.animation = animation;
            return this;
        }

        public Builder hitAnimation(Animation hitAnimation) {
            this.hitAnimation = hitAnimation;
            return this;
        }

        public Builder missileCharacter(AlienMissileCharacter missileCharacter) {
            this.missileCharacter = missileCharacter;
            return this;
        }
    }


}
