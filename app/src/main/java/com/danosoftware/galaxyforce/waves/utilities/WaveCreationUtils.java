package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.PathFactory;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWavePathConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRule;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRuleProperties;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRuleProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Wave creation utilities
 */
public class WaveCreationUtils {

    private final AlienFactory alienFactory;
    private final PathFactory pathFactory;
    private final GameModel model;

    public WaveCreationUtils(
            GameModel model,
            AlienFactory alienFactory,
            PathFactory pathFactory) {
        this.model = model;
        this.alienFactory = alienFactory;
        this.pathFactory = pathFactory;
    }

    /**
     * Create a list of aliens that follow a path from
     * the supplied config and model.
     *
     * @param config - contains sub-wave configuration
     * @return list of aliens
     */
    public List<IAlien> createPathAlienSubWave(
            final SubWavePathConfig config) {

        List<IAlien> aliens = new ArrayList<>();

        final AlienType alienType = config.getAlien();
        final List<PowerUpType> powerUps = config.getPowerUps();
        final SubWavePathRule rules = config.getSubWaveRule();

        // initialise power-up allocator
        int numberOfAliens = 0;
        for (SubWavePathRuleProperties props : rules.subWaveProps()) {
            numberOfAliens += props.getNumberOfAliens();
        }
        final PowerUpAllocator powerUpAllocator = new PowerUpAllocator(
                config.getPowerUps(),
                numberOfAliens,
                model.getLives());


        for (SubWavePathRuleProperties props : rules.subWaveProps()) {

            // create path points (that alien will follow) for sub-wave
            List<Point> path = pathFactory.createPath(
                    props.getPath(),
                    props.getTranslators()
            );

            // create and add a sub-wave of aliens according to provided properties
            aliens.addAll(
                    createAliens(alienType, powerUpAllocator, path, props)
            );
        }

        return aliens;
    }

    /**
     * Create a list of aliens from the supplied config and model. These
     * aliens do not follow a normal pre-defined path.
     *
     * @param config - contains sub-wave configuration
     * @return list of aliens
     */
    public List<IAlien> createNoPathAlienSubWave(
            final SubWaveNoPathConfig config) {

        List<IAlien> aliens = new ArrayList<>();

        final AlienType alienType = config.getAlien();
        final SubWaveRule rules = config.getSubWaveRule();

        // initialise power-up allocator
        int numberOfAliens = 0;
        for (SubWaveRuleProperties props : rules.subWaveProps()) {
            numberOfAliens += props.getNumberOfAliens();
        }
        final PowerUpAllocator powerUpAllocator = new PowerUpAllocator(
                config.getPowerUps(),
                numberOfAliens,
                model.getLives());

        for (SubWaveRuleProperties props : rules.subWaveProps()) {

            for (int i = 0; i < props.getNumberOfAliens(); i++) {
                aliens.addAll(alienFactory.createAlien(
                        alienType,
                        powerUpAllocator.allocate(),
                        props.isxRandom(),
                        props.isyRandom(),
                        props.getxStart(),
                        props.getyStart(),
                        (i * props.getDelayBetweenAliens()) + props.getDelayOffet(),
                        props.isRestartImmediately()));
            }
        }

        return aliens;
    }

    /**
     * adds a wanted number of aliens with a path. each alien is spaced by
     * the delay seconds specified.
     */
    private List<IAlien> createAliens(
            final AlienType alienType,
            final PowerUpAllocator powerUpAllocator,
            final List<Point> path,
            final SubWavePathRuleProperties props) {

        List<IAlien> aliensOnPath = new ArrayList<>();

        for (int i = 0; i < props.getNumberOfAliens(); i++) {
            aliensOnPath.addAll(
                    alienFactory.createAlien(
                            alienType,
                            powerUpAllocator.allocate(),
                            path,
                            (i * props.getDelayBetweenAliens()) + props.getDelayOffet(),
                            props.isRestartImmediately()
                    ));
        }

        return aliensOnPath;
    }
}
