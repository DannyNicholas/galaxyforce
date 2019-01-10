package com.danosoftware.galaxyforce.flightpath.paths;

import android.util.Log;

import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;
import com.danosoftware.galaxyforce.flightpath.utilities.PathLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.danosoftware.galaxyforce.helpers.AssetHelpers.pathAsset;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test that checks all the paths create a list of points representing their paths.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, PathLoader.class})
public class PathFactoryTest {

    private final static Logger logger = LoggerFactory.getLogger(PathFactoryTest.class);

    @Before
    public void setup() {
        // mock any static android logging
        mockStatic(Log.class);
    }

    @Test
    public void shouldCreateAllPaths() throws IOException {

        final PointTranslatorChain emptyTranslators = new PointTranslatorChain();

        for (Path path : Path.values()) {
            logger.info("Creating path : '{}'.", path.name());

            // The PathFactory uses a PathLoader to load path data from a JSON file.
            // We can't use the real PathLoader to load JSON files using the context/assets
            // as they are not available from unit tests.
            //
            // Instead we mock the PathLoader static method and give it the path data
            // it would have loaded for the current path.
            PathListDTO pathListDTO = loadPathDTO(path);
            mockStatic(PathLoader.class);
            when(PathLoader.loadPaths(any(String.class))).thenReturn(pathListDTO);

            List<Point> points = PathFactory.createPath(path, emptyTranslators);
            checkPoints(points);
        }
        logger.info("All paths created.");
    }

    // verify points
    private void checkPoints(List<Point> points) {

        logger.info("Path size : '{}'.", points.size());

        assertThat(points, is(notNullValue()));
        assertThat(points.size() > 0, is(true));

        for (Point point : points) {
            assertThat(point, is(notNullValue()));
            assertThat(point.getX(), is(notNullValue()));
            assertThat(point.getY(), is(notNullValue()));
        }
    }

    // load path data for the supplied data
    private PathListDTO loadPathDTO(Path path) throws IOException {
        File file = pathAsset(path.getPathFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, PathListDTO.class);
    }
}
