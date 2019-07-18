package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.PathAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.MissileFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.PathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpawningAlienConfig;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class PathConfigTest {

    private final AlienFactory factory = new AlienFactory(
            mock(GameModel.class),
            mock(SoundPlayerService.class),
            mock(VibrationService.class)
    );

    @Test
    public void shouldCreateBasicConfiguredPathAlien() {
        PathConfig config = PathConfig
                .builder()
                .alienCharacter(AlienCharacter.OCTOPUS)
                .energy(10)
                .build();

        assertThat(config.getAlienCharacter(), equalTo(AlienCharacter.OCTOPUS));
        assertThat(config.getEnergy(), equalTo(10));
        assertThat(config.getAlienType(), equalTo(AlienType.PATH));
        assertThat(config.getSpawnConfig(), nullValue());
        assertThat(config.getMissileConfig(), nullValue());

        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        List<IAlien> aliens = factory.createAlien(
                config,
                PowerUpType.LIFE,
                points,
                2f,
                false);

        assertThat(aliens, not(nullValue()));
        assertThat(aliens.size(), equalTo(1));
        assertThat(aliens.get(0) instanceof PathAlien, is(true));
    }

    @Test
    public void shouldCreateFullyConfiguredPathAlien() {
        PathConfig config = PathConfig
                .builder()
                .alienCharacter(AlienCharacter.OCTOPUS)
                .energy(10)
                .spawnConfig(new SpawningAlienConfig(
                        mock(AlienConfig.class),
                        new ArrayList<PowerUpType>(),
                        0f,
                        0f))
                .missileConfig(new MissileFiringConfig(
                        AlienMissileType.DOWNWARDS,
                        AlienMissileSpeed.MEDIUM,
                        AlienMissileCharacter.LASER,
                        0f))
                .build();

        assertThat(config.getAlienCharacter(), equalTo(AlienCharacter.OCTOPUS));
        assertThat(config.getEnergy(), equalTo(10));
        assertThat(config.getAlienType(), equalTo(AlienType.PATH));
        assertThat(config.getSpawnConfig().getType(), equalTo(SpawnConfig.SpawnType.SPAWN));
        assertThat(config.getSpawnConfig() instanceof SpawningAlienConfig, is(true));
        assertThat(config.getMissileConfig().getType(), equalTo(MissileConfig.MissileConfigType.MISSILE));
        assertThat(config.getMissileConfig() instanceof MissileFiringConfig, is(true));

        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        List<IAlien> aliens = factory.createAlien(
                config,
                PowerUpType.LIFE,
                points,
                2f,
                false);

        assertThat(aliens, not(nullValue()));
        assertThat(aliens.size(), equalTo(1));
        assertThat(aliens.get(0) instanceof PathAlien, is(true));
    }
}