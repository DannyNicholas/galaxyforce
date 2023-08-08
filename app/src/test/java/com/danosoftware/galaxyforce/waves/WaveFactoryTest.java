package com.danosoftware.galaxyforce.waves;

import static com.danosoftware.galaxyforce.constants.GameConstants.MAX_WAVES;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWavePathConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;
import com.danosoftware.galaxyforce.waves.utilities.WaveCreationUtils;
import com.danosoftware.galaxyforce.waves.utilities.WaveFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test to confirm behaviour of wave factory to create all waves.
 * <p>
 * Also tries to create waves that are not yet supported.
 */
@ExtendWith(MockitoExtension.class)
public class WaveFactoryTest {

  private final static Logger logger = LoggerFactory.getLogger(WaveFactoryTest.class);

  private WaveCreationUtils creationUtils;

  @BeforeEach
  public void setup() {
    IAlien alien = mock(IAlien.class);
    List<IAlien> createdAliens = Collections.singletonList(alien);
    creationUtils = mock(WaveCreationUtils.class);
    when(creationUtils.createNoPathAlienSubWave(any(SubWaveNoPathConfig.class)))
        .thenReturn(createdAliens);
    when(creationUtils.createPathAlienSubWave(any(SubWavePathConfig.class)))
        .thenReturn(createdAliens);
  }

  @Test
  public void shouldCreateAllWaves() {
    for (int wave = 1; wave <= MAX_WAVES; wave++) {

      logger.info("Creating wave: " + wave);

      PowerUpAllocatorFactory powerUpAllocatorFactory = mock(PowerUpAllocatorFactory.class);
      WaveFactory waveFactory = new WaveFactory(creationUtils, powerUpAllocatorFactory);
      List<SubWave> subWaves = waveFactory.createWave(wave);

      logger.info("Sub-waves: " + subWaves.size());

      assertThat(subWaves.size() > 0, is(true));

      for (SubWave subWave : subWaves) {
        logger.info("Aliens: " + subWave.getAliens().size());
        assertThat(subWave.getAliens(), hasSize(greaterThan(0)));
      }
    }
  }
}
