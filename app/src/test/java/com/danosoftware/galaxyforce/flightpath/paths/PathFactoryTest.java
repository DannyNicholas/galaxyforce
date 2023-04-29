package com.danosoftware.galaxyforce.flightpath.paths;

import static com.danosoftware.galaxyforce.helpers.AssetHelpers.pathAsset;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;
import com.danosoftware.galaxyforce.flightpath.utilities.PathLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test that checks all the paths create a list of points representing their paths.
 */
@ExtendWith(MockitoExtension.class)
public class PathFactoryTest {

  private final static Logger logger = LoggerFactory.getLogger(PathFactoryTest.class);

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
      PathLoader loader = mock(PathLoader.class);
      when(loader.loadPaths(any(Path.class))).thenReturn(pathListDTO);

      PathFactory pathFactory = new PathFactory(loader);
      List<PathPoint> points = pathFactory.createPath(path, emptyTranslators, PathSpeed.NORMAL);
      checkPoints(points);
    }
    logger.info("All paths created.");
  }

  // verify points
  private void checkPoints(List<PathPoint> points) {

    logger.info("Path size : '{}'.", points.size());

    assertThat(points, is(notNullValue()));
    assertThat(points.size() > 0, is(true));

    for (PathPoint point : points) {
      assertThat(point, is(notNullValue()));
      assertThat(point.getX(), is(notNullValue()));
      assertThat(point.getY(), is(notNullValue()));
      assertThat(point.getAngle(), is(notNullValue()));
    }
  }

  // load path data for the supplied data
  private PathListDTO loadPathDTO(Path path) throws IOException {
    File file = pathAsset(path.getPathFile());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(file, PathListDTO.class);
  }
}
