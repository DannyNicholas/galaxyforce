package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.HunterAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.HunterBoundariesConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.HunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.MissileFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpawningAlienConfig;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class HunterConfigTest {

    private final AlienFactory factory = new AlienFactory(
            mock(GameModel.class),
            mock(SoundPlayerService.class),
            mock(VibrationService.class)
    );

    @Test
    public void shouldCreateBasicConfiguredHunter() {
        HunterConfig config = HunterConfig
                .builder()
                .alienCharacter(AlienCharacter.OCTOPUS)
                .energy(10)
                .speed(AlienSpeed.SLOW)
                .boundaries(HunterBoundariesConfig.builder().build())
                .build();

        assertThat(config.getAlienCharacter(), equalTo(AlienCharacter.OCTOPUS));
        assertThat(config.getEnergy(), equalTo(10));
        assertThat(config.getAlienType(), equalTo(AlienType.HUNTER));
        assertThat(config.getSpeed(), equalTo(AlienSpeed.SLOW));
        assertThat(config.getSpawnConfig(), nullValue());
        assertThat(config.getMissileConfig(), nullValue());
        assertThat(config.getBoundaries().getMinX(), equalTo(0));
        assertThat(config.getBoundaries().getMaxX(), equalTo(GAME_WIDTH));
        assertThat(config.getBoundaries().getMinY(), equalTo(0));
        assertThat(config.getBoundaries().getMaxY(), equalTo(GAME_HEIGHT));


        List<IAlien> aliens = factory.createAlien(
                config,
                PowerUpType.LIFE,
                false,
                false,
                0,
                0,
                0f,
                false);

        assertThat(aliens, not(nullValue()));
        assertThat(aliens.size(), equalTo(1));
        assertThat(aliens.get(0) instanceof HunterAlien, is(true));
    }

    @Test
    public void shouldCreateFullyConfiguredHunter() {
        HunterConfig config = HunterConfig
                .builder()
                .alienCharacter(AlienCharacter.OCTOPUS)
                .energy(10)
                .speed(AlienSpeed.SLOW)
                .boundaries(
                        HunterBoundariesConfig
                                .builder()
                                .minX(100)
                                .maxX(400)
                                .minY(50)
                                .maxY(250)
                                .build())
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
        assertThat(config.getAlienType(), equalTo(AlienType.HUNTER));
        assertThat(config.getSpeed(), equalTo(AlienSpeed.SLOW));
        assertThat(config.getSpawnConfig().getType(), equalTo(SpawnConfig.SpawnType.SPAWN));
        assertThat(config.getSpawnConfig() instanceof SpawningAlienConfig, is(true));
        assertThat(config.getMissileConfig().getType(), equalTo(MissileConfig.MissileConfigType.MISSILE));
        assertThat(config.getMissileConfig() instanceof MissileFiringConfig, is(true));
        assertThat(config.getBoundaries().getMinX(), equalTo(100));
        assertThat(config.getBoundaries().getMaxX(), equalTo(400));
        assertThat(config.getBoundaries().getMinY(), equalTo(50));
        assertThat(config.getBoundaries().getMaxY(), equalTo(250));


        List<IAlien> aliens = factory.createAlien(
                config,
                PowerUpType.LIFE,
                false,
                false,
                0,
                0,
                0f,
                false);

        assertThat(aliens, not(nullValue()));
        assertThat(aliens.size(), equalTo(1));
        assertThat(aliens.get(0) instanceof HunterAlien, is(true));
    }
}
