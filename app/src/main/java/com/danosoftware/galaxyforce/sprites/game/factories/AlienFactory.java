package com.danosoftware.galaxyforce.sprites.game.factories;

import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;
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
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.DescendingAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.DirectionalAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.DriftingAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.ExplodingAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.FollowableHunterAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.FollowerAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.HunterAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.PathAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.StaticAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.utilities.Reversed;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DescendingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DriftingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.ExplodingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowableHunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.HunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.PathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.SplitterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.StaticConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocator;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

import java.util.ArrayList;
import java.util.List;

public class AlienFactory {

    private final static String TAG = "AlienFactory";

    private final GameModel model;
    private final ExplosionBehaviourFactory explosionFactory;
    private final SpawnBehaviourFactory spawnFactory;
    private final SpinningBehaviourFactory spinningFactory;
    private final PowerUpAllocatorFactory powerUpAllocatorFactory;
    private final PowerUpBehaviourFactory powerUpFactory;
    private final FireBehaviourFactory fireFactory;
    private final HitBehaviourFactory hitFactory;

    public AlienFactory(
            final GameModel model,
            final PowerUpAllocatorFactory powerUpAllocatorFactory,
            final SoundPlayerService sounds,
            final VibrationService vibrator) {
        this.model = model;
        this.powerUpAllocatorFactory = powerUpAllocatorFactory;
        this.spawnFactory = new SpawnBehaviourFactory(model, this, powerUpAllocatorFactory);
        this.explosionFactory = new ExplosionBehaviourFactory(model, this, spawnFactory, sounds, vibrator);
        this.spinningFactory =  new SpinningBehaviourFactory();
        this.powerUpFactory = new PowerUpBehaviourFactory(model);
        this.fireFactory = new FireBehaviourFactory(model);
        this.hitFactory = new HitBehaviourFactory(sounds, vibrator);
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

            case PATH:
                aliens.add(
                        PathAlien
                                .builder()
                                .explosionFactory(explosionFactory)
                                .spawnFactory(spawnFactory)
                                .spinningFactory(spinningFactory)
                                .powerUpFactory(powerUpFactory)
                                .fireFactory(fireFactory)
                                .hitFactory(hitFactory)
                                .alienConfig((PathConfig) alienConfig)
                                .powerUpType(powerUp)
                                .alienPath(alienPath)
                                .delayStartTime(delay)
                                .restartImmediately(restartImmediately)
                                .build());
                break;

            default:
                String errorMessage = String.format(
                        "Error: Unrecognised Path AlienType: '%s'",
                        alienType.name());
                Log.e(TAG, errorMessage);
                throw new GalaxyForceException(errorMessage);
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
            case HUNTER:
                aliens.add(
                        HunterAlien
                                .builder()
                                .explosionFactory(explosionFactory)
                                .spawnFactory(spawnFactory)
                                .spinningFactory(spinningFactory)
                                .powerUpFactory(powerUpFactory)
                                .fireFactory(fireFactory)
                                .hitFactory(hitFactory)
                                .model(model)
                                .alienConfig((HunterConfig) alienConfig)
                                .powerUpType(powerUp)
                                .xStart(xStartPos)
                                .yStart(yStartPos)
                                .timeDelayStart(delay)
                                .build());
                break;

            case DESCENDING:
                aliens.add(
                        DescendingAlien
                                .builder()
                                .explosionFactory(explosionFactory)
                                .spawnFactory(spawnFactory)
                                .spinningFactory(spinningFactory)
                                .powerUpFactory(powerUpFactory)
                                .fireFactory(fireFactory)
                                .hitFactory(hitFactory)
                                .alienConfig((DescendingConfig) alienConfig)
                                .powerUpType(powerUp)
                                .xStart(xStartPos)
                                .yStart(yStartPos)
                                .timeDelayStart(delay)
                                .restartImmediately(restartImmediately)
                                .build());
                break;

            case EXPLODING:
                aliens.add(
                        ExplodingAlien
                                .builder()
                                .explosionFactory(explosionFactory)
                                .spawnFactory(spawnFactory)
                                .spinningFactory(spinningFactory)
                                .powerUpFactory(powerUpFactory)
                                .fireFactory(fireFactory)
                                .hitFactory(hitFactory)
                                .model(model)
                                .alienConfig((ExplodingConfig) alienConfig)
                                .powerUpType(powerUp)
                                .xStart(xStartPos)
                                .yStart(yStartPos)
                                .build());
                break;

            case HUNTER_FOLLOWABLE:

                /*
                 * Hunter Followable consists of a head and multiple followers (that attempt to
                 * follow the head's movements).
                 * All followers will be destroyed when the head is destroyed.
                 */
                final FollowableHunterConfig followableHunterConfig = (FollowableHunterConfig) alienConfig;
                final int followerCount = followableHunterConfig.getNumberOfFollowers();
                final PowerUpAllocator powerUpAllocator = powerUpAllocatorFactory.createAllocator(
                        followableHunterConfig.getFollowerPowerUps(),
                        followerCount);

                // create followers
                final List<IAlienFollower> followers = new ArrayList<>();
                for (int i = 0; i < followerCount; i++) {
                    followers.add(
                            FollowerAlien
                                    .builder()
                                    .explosionFactory(explosionFactory)
                                    .spawnFactory(spawnFactory)
                                    .spinningFactory(spinningFactory)
                                    .powerUpFactory(powerUpFactory)
                                    .fireFactory(fireFactory)
                                    .hitFactory(hitFactory)
                                    .alienConfig(followableHunterConfig.getFollowerConfig())
                                    .powerUpType(powerUpAllocator.allocate())
                                    .xStart(xStartPos)
                                    .yStart(yStartPos)
                                    .build());
                }

                // create head
                aliens.add(
                        FollowableHunterAlien
                                .builder()
                                .explosionFactory(explosionFactory)
                                .spawnFactory(spawnFactory)
                                .spinningFactory(spinningFactory)
                                .powerUpFactory(powerUpFactory)
                                .fireFactory(fireFactory)
                                .hitFactory(hitFactory)
                                .model(model)
                                .alienConfig(followableHunterConfig)
                                .powerUpType(powerUp)
                                .xStart(xStartPos)
                                .yStart(yStartPos)
                                .timeDelayStart(delay)
                                .followers(followers)
                                .build());
                aliens.addAll(followers);
                break;

            case STATIC:
                aliens.add(
                        StaticAlien
                                .builder()
                                .explosionFactory(explosionFactory)
                                .spawnFactory(spawnFactory)
                                .spinningFactory(spinningFactory)
                                .powerUpFactory(powerUpFactory)
                                .fireFactory(fireFactory)
                                .hitFactory(hitFactory)
                                .alienConfig((StaticConfig) alienConfig)
                                .powerUpType(powerUp)
                                .xStart(xStartPos)
                                .yStart(yStartPos)
                                .build());
                break;

            case SPLITTER:
                SplitterConfig splitterConfig = (SplitterConfig) alienConfig;

                for (AlienConfig splitAlienConfig : splitterConfig.getAlienConfigs()) {
                    aliens.addAll(
                            createAlien(
                                    splitAlienConfig,
                                    powerUp,    // same power-up for all split aliens
                                    false,
                                    false,
                                    xStart,
                                    yStart,
                                    0f,
                                    false));
                }
                break;

            case DRIFTING:
                DriftingConfig driftingConfig = (DriftingConfig) alienConfig;
                aliens.add(
                        DriftingAlien
                                .builder()
                                .explosionFactory(explosionFactory)
                                .spawnFactory(spawnFactory)
                                .spinningFactory(spinningFactory)
                                .powerUpFactory(powerUpFactory)
                                .fireFactory(fireFactory)
                                .hitFactory(hitFactory)
                                .alienConfig(driftingConfig)
                                .powerUpType(powerUp)
                                .xStart(xStartPos)
                                .yStart(yStartPos)
                                .timeDelayStart(delay)
                                .restartImmediately(restartImmediately)
                                .build());
                break;

            case DIRECTIONAL:
                DirectionalConfig directionalConfig = (DirectionalConfig) alienConfig;
                aliens.add(
                        DirectionalAlien
                                .builder()
                                .explosionFactory(explosionFactory)
                                .spawnFactory(spawnFactory)
                                .spinningFactory(spinningFactory)
                                .powerUpFactory(powerUpFactory)
                                .fireFactory(fireFactory)
                                .hitFactory(hitFactory)
                                .alienConfig(directionalConfig)
                                .powerUpType(powerUp)
                                .xStart(xStartPos)
                                .yStart(yStartPos)
                                .timeDelayStart(delay)
                                .restartImmediately(restartImmediately)
                                .build());
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

        final List<IAlien> aliens = createAlien(
                alienConfig,
                powerUpType,
                false,
                false,
                xStart,
                yStart,
                0f,
                false);

        List<IAlien> reversedAlienList = new ArrayList<>();
        for (IAlien eachAlien : Reversed.reversed(aliens)) {
            reversedAlienList.add(eachAlien);
        }

        return new SpawnedAliensDto(reversedAlienList, SoundEffect.ALIEN_SPAWN);
    }
}
