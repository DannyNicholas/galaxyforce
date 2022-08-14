package com.danosoftware.galaxyforce.waves.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningFixedAngularConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowerConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;
import java.util.ArrayList;
import org.junit.Test;

public class FollowerConfigTest {

  private final AlienFactory factory = new AlienFactory(
      mock(GameModel.class),
      mock(PowerUpAllocatorFactory.class),
      mock(SoundPlayerService.class),
      mock(VibrationService.class)
  );

  @Test
  public void shouldCreateBasicConfiguredFollower() {
    FollowerConfig config = FollowerConfig
        .builder()
        .alienCharacter(AlienCharacter.OCTOPUS)
        .energy(10)
        .speed(AlienSpeed.SLOW)
        .build();

    assertThat(config.getAlienCharacter(), equalTo(AlienCharacter.OCTOPUS));
    assertThat(config.getEnergy(), equalTo(10));
    assertThat(config.getAlienType(), equalTo(AlienType.FOLLOWER));
    assertThat(config.getSpeed(), equalTo(AlienSpeed.SLOW));
    assertThat(config.getSpawnConfig(), nullValue());
    assertThat(config.getMissileConfig(), nullValue());
  }

  @Test
  public void shouldCreateFullyConfiguredFollower() {
    FollowerConfig config = FollowerConfig
        .builder()
        .alienCharacter(AlienCharacter.OCTOPUS)
        .energy(10)
        .speed(AlienSpeed.SLOW)
        .spawnConfig(new SpawningAlienConfig(
            mock(AlienConfig.class),
            new ArrayList<>(),
            0f,
            0f))
        .missileConfig(new MissileFiringConfig(
            AlienMissileType.DOWNWARDS,
            AlienMissileSpeed.MEDIUM,
            AlienMissileCharacter.LASER,
            0f))
        .spinningConfig(new SpinningFixedAngularConfig(
            10))
        .build();

    assertThat(config.getAlienCharacter(), equalTo(AlienCharacter.OCTOPUS));
    assertThat(config.getEnergy(), equalTo(10));
    assertThat(config.getAlienType(), equalTo(AlienType.FOLLOWER));
    assertThat(config.getSpawnConfig().getType(), equalTo(SpawnConfig.SpawnType.SPAWN));
    assertThat(config.getSpawnConfig() instanceof SpawningAlienConfig, is(true));
    assertThat(config.getMissileConfig().getType(),
        equalTo(MissileConfig.MissileConfigType.MISSILE));
    assertThat(config.getMissileConfig() instanceof MissileFiringConfig, is(true));
    assertThat(config.getSpinningConfig().getType(),
        equalTo(SpinningConfig.SpinningConfigType.FIXED_ANGULAR_ROTATION));
    assertThat(config.getSpinningConfig() instanceof SpinningFixedAngularConfig, is(true));
  }
}
