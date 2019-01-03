package com.danosoftware.galaxyforce.waves;

import android.util.Log;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.flightpath.utilities.PathLoader;
import com.danosoftware.galaxyforce.models.screens.game.handlers.IGameHandler;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.utilities.WaveFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.MAX_WAVES;
import static com.danosoftware.galaxyforce.helpers.AssetHelpers.pathAsset;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test to confirm behaviour of wave factory to create all waves.
 * <p>
 * Also tries to create waves that are not yet supported.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, PathLoader.class, AlienFactory.class})
public class WaveFactoryTest {

    final static Logger logger = LoggerFactory.getLogger(WaveFactoryTest.class);

    // static answer that returns a PathListDTO dependent on the path name argument
    private static Answer<PathListDTO> pathListDTO() {
        return new Answer<PathListDTO>() {
            public PathListDTO answer(InvocationOnMock invocation) throws IOException {
                String pathName = (String) invocation.getArguments()[0];
                logger.info("Loading path: " + pathName);
                return loadPathDTO(pathName);
            }
        };
    }

    @Before
    public void setup() {
        // mock any static android logging
        mockStatic(Log.class);

        // The PathFactory uses a PathLoader to load path data from an assets JSON file.
        //
        // Instead we mock the PathLoader static method and return the path data
        // it would have loaded for the supplied path argument.
        mockStatic(PathLoader.class);
        when(PathLoader.loadPaths(any(String.class))).thenAnswer(pathListDTO());

        // creating aliens is complex so mock it for this test
        List<IAlien> mockedAliens = new ArrayList<>();
        mockStatic(AlienFactory.class);
        when(AlienFactory.createAlien(
                any(AlienType.class),
                any(PowerUpType.class),
                any(Boolean.class),
                any(Boolean.class),
                any(Integer.class),
                any(Integer.class),
                any(Float.class),
                any(IGameHandler.class),
                any(Boolean.class))).thenReturn(mockedAliens);
    }

    @Test
    public void shouldCreateAllWaves() {
        for (int wave = 1; wave <= MAX_WAVES; wave++) {

            logger.info("Creating wave: " + wave);

            IGameHandler handler = mock(IGameHandler.class);
            WaveFactory waveFactory = new WaveFactory(handler);
            List<SubWave> subWave = waveFactory.createWave(wave);

            logger.info("Sub-waves: " + subWave.size());

            assertThat(subWave.size() > 0, is(true));

        }
    }

    // load path data for the supplied data
    private static PathListDTO loadPathDTO(String pathFile) throws IOException {
        File file = pathAsset(pathFile);
        ObjectMapper mapper = new ObjectMapper();
        PathListDTO pathData = mapper.readValue(file, PathListDTO.class);
        return pathData;
    }
}
