package com.danosoftware.galaxyforce.sprites.game.factories;

import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.game.beans.SpawnedAlienBean;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienAsteroid;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienAsteroidSimple;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienDragonBody;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienDragonHead;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienDroid;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienGobby;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienHunter;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienInsectPath;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienMinion;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienMothership;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienOctopus;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienSpawnedInsect;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.AlienStork;
import com.danosoftware.galaxyforce.waves.AlienType;
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
            final AlienType alienType,
            final PowerUpType powerUp,
            final List<Point> path,
            final float delay,
            final boolean restartImmediately) {

        List<IAlien> aliens = new ArrayList<>();

        /*
         * Create new path per alien as different aliens may modify paths
         * differently
         */
        List<Point> alienPath = new ArrayList<>();

        /*
         * convert path so alien is positioned at centre of provided point. This
         * is dependent on sprite size.
         */
        for (Point currentPoint : path) {
            int x = currentPoint.getX();
            int y = currentPoint.getY();

            alienPath.add(new Point(x, y));
        }

        // create instance of the wanted alien
        switch (alienType) {
            case OCTOPUS:
                aliens.add(new AlienOctopus(model, sounds, vibrator, powerUp, alienPath, delay, restartImmediately));
                break;

            case GOBBY:
                aliens.add(new AlienGobby(model, sounds, vibrator, powerUp, alienPath, delay, restartImmediately));
                break;

            case MINION:
                aliens.add(new AlienMinion(model, sounds, vibrator, powerUp, alienPath, delay, restartImmediately));
                break;

            case MOTHERSHIP:
                aliens.add(new AlienMothership(
                        this,
                        model,
                        sounds,
                        vibrator,
                        powerUp,
                        Arrays.asList(PowerUpType.ENERGY, PowerUpType.MISSILE_FAST, PowerUpType.MISSILE_PARALLEL),
                        alienPath,
                        delay,
                        restartImmediately));
                break;

            case STORK:
                aliens.add(new AlienStork(model, sounds, vibrator, powerUp, alienPath, delay, restartImmediately));
                break;

            case DROID:
                aliens.add(new AlienDroid(model, sounds, vibrator, powerUp, alienPath, delay, restartImmediately));
                break;

            case INSECT:
                aliens.add(new AlienInsectPath(model, sounds, vibrator, powerUp, alienPath, delay, restartImmediately));
                break;

            default:
                String errorMessage = "Error: Unrecognised AlienType: '" + alienType + "'";
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
            final AlienType alienType,
            final PowerUpType powerUp,
            final boolean xRandom,
            final boolean yRandom,
            final int xStart,
            final int yStart,
            final float delay,
            final boolean restartImmediately) {

        List<IAlien> aliens = new ArrayList<>();

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
                aliens.add(new AlienAsteroid(powerUp, xStartPos, yStartPos, delay, restartImmediately, model, sounds, vibrator));
                break;

            case ASTEROID_SIMPLE:
                aliens.add(new AlienAsteroidSimple(powerUp, xStartPos, yStartPos, delay, restartImmediately, model, sounds, vibrator));
                break;

            case HUNTER:
                aliens.add(new AlienHunter(powerUp, xStartPos, yStartPos, delay, model, sounds, vibrator));
                break;

            case DRAGON:

                /*
                 * dragon consists of multiple body parts and a head. The head
                 * contains a reference to the body parts as all body parts will be
                 * destroyed when the head is destroyed.
                 */
                List<IAlienFollower> dragonBodies = new ArrayList<>();
                int dragonBodyCount = 20;
                List<PowerUpType> powerUpTypes = Arrays.asList(PowerUpType.MISSILE_GUIDED, PowerUpType.MISSILE_PARALLEL, PowerUpType.MISSILE_SPRAY);
                PowerUpAllocator powerUpAllocator = new PowerUpAllocator(powerUpTypes, dragonBodyCount, model.getLives());
                for (int i = 0; i < dragonBodyCount; i++) {
                    AlienDragonBody dragonBody = new AlienDragonBody(powerUpAllocator.allocate(), xStartPos, yStartPos, model, sounds, vibrator);
                    dragonBodies.add(dragonBody);
                }

                aliens.add(new AlienDragonHead(powerUp, xStartPos, yStartPos, delay, model, sounds, vibrator, dragonBodies));
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
    public SpawnedAlienBean createSpawnedAlien(
            final AlienType alienType,
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart) {

        List<IAlien> aliens = new ArrayList<>();

        switch (alienType) {
            case SPAWNED_INSECT:
                aliens.add(new AlienSpawnedInsect(powerUpType, xStart, yStart, model, sounds, vibrator));
                return new SpawnedAlienBean(aliens, SoundEffect.ALIEN_SPAWN);

            default:
                String errorMessage = "Error: Unrecognised AlienType: '" + alienType + "'";
                Log.e(TAG, errorMessage);
                throw new GalaxyForceException(errorMessage);
        }
    }
}
