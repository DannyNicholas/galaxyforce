package com.danosoftware.galaxyforce.sprites.game.factories;

import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.models.assets.SpawnedAliensDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienAsteroid;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienAsteroidSimple;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienBomb;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienBook;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienDragonBody;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienDragonHead;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienHunter;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienSpawnedInsect;
import com.danosoftware.galaxyforce.sprites.game.aliens.path.AlienBomber;
import com.danosoftware.galaxyforce.sprites.game.aliens.path.AlienDroid;
import com.danosoftware.galaxyforce.sprites.game.aliens.path.AlienInsectPath;
import com.danosoftware.galaxyforce.sprites.game.aliens.path.AlienMinion;
import com.danosoftware.galaxyforce.sprites.game.aliens.path.AlienMothership;
import com.danosoftware.galaxyforce.sprites.game.aliens.path.AlienStork;
import com.danosoftware.galaxyforce.sprites.game.aliens.path.PathAlien;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.AlienWithMissileConfig;
import com.danosoftware.galaxyforce.waves.config.AlienWithoutMissileConfig;
import com.danosoftware.galaxyforce.waves.config.MissileConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlienFactory {

    private final static String TAG = "AlienFactory";

    private final GameModel model;
    private final SoundPlayerService sounds;
    private final VibrationService vibrator;

    public AlienFactory(
            GameModel model,
            SoundPlayerService sounds,
            VibrationService vibrator) {
        this.model = model;
        this.sounds = sounds;
        this.vibrator = vibrator;
    }

    /**
     * Creates an alien with a supplied path. Starting position will be based on
     * path.
     */
    public List<IAlien> createAlien(
            final AlienConfig alienConfig,
            final PowerUpType powerUp,
            final List<Point> alienPath,
            final float delay,
            final boolean restartImmediately) {

        final List<IAlien> aliens = new ArrayList<>();
        final AlienType alienType = alienConfig.getAlienType();

        // create instance of the wanted alien
        switch (alienType) {
            case CIRCUIT:
                aliens.add(
                        new PathAlien.Builder()
                                .model(model)
                                .sounds(sounds)
                                .vibrator(vibrator)
                                .alienConfig(alienConfig)
                                .powerUp(powerUp)
                                .alienPath(alienPath)
                                .delayStartTime(delay)
                                .restartImmediately(restartImmediately)
                                .animation(alienType.getAnimation())
                                .hitAnimation(alienType.getHitAnimation())
                                .missileCharacter(AlienMissileCharacter.LIGHTNING)
                                .build());
                break;

            case OCTOPUS:
                aliens.add(
                        new PathAlien.Builder()
                                .model(model)
                                .sounds(sounds)
                                .vibrator(vibrator)
                                .alienConfig(alienConfig)
                                .powerUp(powerUp)
                                .alienPath(alienPath)
                                .delayStartTime(delay)
                                .restartImmediately(restartImmediately)
                                .animation(new Animation(
                                        0.5f,
                                        GameSpriteIdentifier.OCTOPUS_LEFT,
                                        GameSpriteIdentifier.OCTOPUS_RIGHT))
                                .hitAnimation(new Animation(
                                        0.5f,
                                        GameSpriteIdentifier.OCTOPUS_LEFT_HIT,
                                        GameSpriteIdentifier.OCTOPUS_RIGHT_HIT))
                                .missileCharacter(AlienMissileCharacter.LASER)
                                .build());
                break;

            case GOBBY:
                aliens.add(
                        new PathAlien.Builder()
                                .model(model)
                                .sounds(sounds)
                                .vibrator(vibrator)
                                .alienConfig(alienConfig)
                                .powerUp(powerUp)
                                .alienPath(alienPath)
                                .delayStartTime(delay)
                                .restartImmediately(restartImmediately)
                                .animation(new Animation(
                                        0.5f,
                                        GameSpriteIdentifier.ALIEN_GOBBY_LEFT,
                                        GameSpriteIdentifier.ALIEN_GOBBY_RIGHT))
                                .hitAnimation(new Animation(
                                        0.5f,
                                        GameSpriteIdentifier.ALIEN_GOBBY_LEFT,
                                        GameSpriteIdentifier.ALIEN_GOBBY_RIGHT))
                                .missileCharacter(AlienMissileCharacter.LASER)
                                .build());
                break;

            case BOOK:
                aliens.add(
                        new AlienBook(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUp,
                                alienPath,
                                delay,
                                restartImmediately));
                break;

            case MINION:
                aliens.add(
                        new AlienMinion(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUp,
                                alienPath,
                                delay,
                                restartImmediately));
                break;

            case MOTHERSHIP:
                aliens.add(
                        new AlienMothership(
                                this,
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                new AlienWithMissileConfig(
                                        AlienType.SPAWNED_INSECT,
                                        1,
                                        new MissileConfig(
                                                AlienMissileType.SIMPLE,
                                                AlienMissileSpeed.MEDIUM,
                                                1.5f
                                        )
                                ),
                                powerUp,
                                Arrays.asList(
                                        PowerUpType.MISSILE_GUIDED,
                                        PowerUpType.MISSILE_FAST,
                                        PowerUpType.MISSILE_PARALLEL),
                                alienPath,
                                delay,
                                restartImmediately));
                break;

            case STORK:
                aliens.add(
                        new AlienStork(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUp,
                                alienPath,
                                delay,
                                restartImmediately));
                break;

            case DROID:
                aliens.add(
                        new AlienDroid(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUp,
                                alienPath,
                                delay,
                                restartImmediately));
                break;

            case INSECT:
                aliens.add(
                        new AlienInsectPath(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUp,
                                alienPath,
                                delay,
                                restartImmediately));
                break;

            case BOMBER:
                aliens.add(
                        new AlienBomber(
                                this,
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                new AlienWithoutMissileConfig(
                                        AlienType.BOMB,
                                        1),
                                powerUp,
                                Arrays.asList(
                                        PowerUpType.MISSILE_GUIDED,
                                        PowerUpType.MISSILE_FAST,
                                        PowerUpType.MISSILE_PARALLEL),
                                alienPath,
                                delay,
                                restartImmediately));
                break;

            default:
                String errorMessage = "Error: Unrecognised Path AlienType: '" + alienType + "'";
                Log.e(TAG, errorMessage);
                throw new IllegalStateException(errorMessage);
        }

        // return alien;
        return aliens;
    }

    /**
     * Creates an alien with no path in the specified position.
     */
    public List<IAlien> createAlien(
            final AlienConfig alienConfig,
            final PowerUpType powerUp,
            final boolean xRandom,
            final boolean yRandom,
            final int xStart,
            final int yStart,
            final float delay,
            final boolean restartImmediately) {

        List<IAlien> aliens = new ArrayList<>();
        final AlienType alienType = alienConfig.getAlienType();

        // choose a x start position
        int xStartPos;
        if (xRandom) {
            /*
             * set random start position - but don't allow creation right at
             * edge where it may be impossible to hit. ensure min 32 pixels from
             * screen edges
             */
            xStartPos = 32 + (int) (Math.random() * (GameConstants.GAME_WIDTH - 64));
        } else {
            xStartPos = xStart;
        }

        // choose a y start position
        int yStartPos;
        if (yRandom) {
            /*
             * set random start position - but don't allow creation right at
             * edge where it may be impossible to hit. ensure min 32 pixels from
             * screen edges
             */
            yStartPos = 32 + (int) (Math.random() * (GameConstants.GAME_HEIGHT - 64));
        } else {
            yStartPos = yStart;
        }

        // create instance of the wanted alien
        switch (alienType) {
            case ASTEROID:
                aliens.add(
                        new AlienAsteroid(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUp,
                                xStartPos,
                                yStartPos,
                                delay,
                                restartImmediately));
                break;

            case ASTEROID_SIMPLE:
                aliens.add(
                        new AlienAsteroidSimple(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUp,
                                xStartPos,
                                yStartPos,
                                delay,
                                restartImmediately));
                break;

            case HUNTER:
                aliens.add(
                        new AlienHunter(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUp,
                                xStartPos,
                                yStartPos,
                                delay));
                break;

            case DRAGON:

                /*
                 * dragon consists of multiple body parts and a head. The head
                 * contains a reference to the body parts as all body parts will be
                 * destroyed when the head is destroyed.
                 */
                List<IAlienFollower> dragonBodies = new ArrayList<>();
                int dragonBodyCount = 20;
                List<PowerUpType> powerUpTypes = Arrays.asList(
                        PowerUpType.MISSILE_GUIDED,
                        PowerUpType.MISSILE_PARALLEL,
                        PowerUpType.MISSILE_SPRAY);
                PowerUpAllocator powerUpAllocator = new PowerUpAllocator(powerUpTypes, dragonBodyCount, model.getLives());
                for (int i = 0; i < dragonBodyCount; i++) {
                    AlienDragonBody dragonBody = new AlienDragonBody(
                            model,
                            sounds,
                            vibrator,
                            new AlienWithoutMissileConfig(
                                    AlienType.DRAGON,
                                    1),
                            powerUpAllocator.allocate(),
                            xStartPos,
                            yStartPos);
                    dragonBodies.add(dragonBody);
                }

                aliens.add(new AlienDragonHead(
                        model,
                        sounds,
                        vibrator,
                        alienConfig,
                        powerUp,
                        xStartPos,
                        yStartPos,
                        delay,
                        dragonBodies));
                aliens.addAll(dragonBodies);
                break;

            default:
                String errorMessage = "Error: Unrecognised AlienType: '" + alienType + "'";
                Log.e(TAG, errorMessage);
                throw new IllegalStateException(errorMessage);
        }

        return aliens;
    }

    /**
     * Creates a spawned alien with no path in the specified position. Simple
     * implementation that has no random elements or direction.
     * <p>
     * Returns spawned alien bean containing alien and sound effect.
     */
    public SpawnedAliensDto createSpawnedAlien(
            final AlienConfig alienConfig,
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart) {

        List<IAlien> aliens = new ArrayList<>();
        final AlienType alienType = alienConfig.getAlienType();

        switch (alienType) {
            case SPAWNED_INSECT:
                aliens.add(
                        new AlienSpawnedInsect(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUpType,
                                xStart,
                                yStart));
                return new SpawnedAliensDto(aliens, SoundEffect.ALIEN_SPAWN);

            case BOMB:
                aliens.add(
                        new AlienBomb(
                                model,
                                sounds,
                                vibrator,
                                alienConfig,
                                powerUpType,
                                xStart,
                                yStart));
                return new SpawnedAliensDto(aliens, SoundEffect.ALIEN_SPAWN);

            default:
                String errorMessage = "Error: Unrecognised Spawned AlienType: '" + alienType + "'";
                Log.e(TAG, errorMessage);
                throw new GalaxyForceException(errorMessage);
        }
    }
}
