package com.danosoftware.galaxyforce.flightpath.dto;

import static com.danosoftware.galaxyforce.helpers.AssetHelpers.pathAsset;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.annotation.NonNull;
import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Tests that check JSON paths are loaded successfully and path Data Transfer Objects created.
 */
public class PathDTOTest {

  private final static Logger logger = LoggerFactory.getLogger(PathDTOTest.class);

  @Test
  public void shouldLoadBezierPaths() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    PathListDTO pathData = mapper.readValue(getFileFromPath(this, "paths/bezier.json"),
        PathListDTO.class);

    assertThat(pathData.getPathList().size(), is(3));
    for (PathDTO pathDTO : pathData.getPathList()) {
      assertThat(pathDTO.getType(), is(PathType.BEZIER));
      assertThat(pathDTO instanceof BezierPathDTO, is(true));
      BezierPathDTO bezierDTO = (BezierPathDTO) pathDTO;
      checkPoints(bezierDTO.getStart());
      checkPoints(bezierDTO.getFinish());
      checkPoints(bezierDTO.getStartControl());
      checkPoints(bezierDTO.getFinishControl());
      assertThat(bezierDTO.getPathPoints(), is(notNullValue()));
    }
    logger.info("Verified Bezier path DTOs");
  }

  @Test
  public void shouldLoadLinearPaths() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    PathListDTO pathData = mapper.readValue(getFileFromPath(this, "paths/linear.json"),
        PathListDTO.class);

    assertThat(pathData.getPathList().size(), is(3));
    for (PathDTO pathDTO : pathData.getPathList()) {
      assertThat(pathDTO.getType(), is(PathType.LINEAR));
      assertThat(pathDTO instanceof LinearPathDTO, is(true));
      LinearPathDTO linearDTO = (LinearPathDTO) pathDTO;
      checkPoints(linearDTO.getStart());
      checkPoints(linearDTO.getFinish());
      assertThat(linearDTO.getPathPoints(), is(notNullValue()));
    }
    logger.info("Verified Linear path DTOs");
  }

  @Test
  public void shouldLoadPausedPaths() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    PathListDTO pathData = mapper.readValue(getFileFromPath(this, "paths/pause.json"),
        PathListDTO.class);

    assertThat(pathData.getPathList().size(), is(3));
    for (PathDTO pathDTO : pathData.getPathList()) {
      assertThat(pathDTO.getType(), is(PathType.PAUSE));
      assertThat(pathDTO instanceof PausePathDTO, is(true));
      PausePathDTO pauseDTO = (PausePathDTO) pathDTO;
      checkPoints(pauseDTO.getPosition());
      assertThat(pauseDTO.getPauseTime(), is(notNullValue()));
    }
    logger.info("Verified Paused path DTOs");
  }


  @Test
  public void shouldLoadCircularPaths() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    PathListDTO pathData = mapper.readValue(getFileFromPath(this, "paths/circular.json"),
        PathListDTO.class);

    assertThat(pathData.getPathList().size(), is(2));
    for (PathDTO pathDTO : pathData.getPathList()) {
      assertThat(pathDTO.getType(), is(PathType.CIRCULAR));
      assertThat(pathDTO instanceof CircularPathDTO, is(true));
      CircularPathDTO circularDTO = (CircularPathDTO) pathDTO;
      checkPoints(circularDTO.getCentre());
      assertThat(circularDTO.getPiMultiplier(), is(notNullValue()));
    }
    logger.info("Verified Circular path DTOs");
  }

  @Test
  public void shouldLoadAllPathAssets() throws IOException {
    for (Path path : Path.values()) {
      logger.info("Checking path : '{}' for: {}.", path.getPathFile(), path.name());
      File file = pathAsset(path.getPathFile());
      ObjectMapper mapper = new ObjectMapper();
      PathListDTO pathData = mapper.readValue(file, PathListDTO.class);
      assertThat(pathData, is(notNullValue()));
    }
    logger.info("All loaded successfully");
  }

  // verify point DTOs
  private void checkPoints(PointDTO point) {
    assertThat(point, is(notNullValue()));
    assertThat(point.getX(), is(notNullValue()));
    assertThat(point.getY(), is(notNullValue()));
  }

  // load file from test resources
  @NonNull
  private static File getFileFromPath(Object obj, String fileName) {
    ClassLoader classLoader = obj.getClass().getClassLoader();
    URL resource = classLoader.getResource(fileName);
    logger.info("File : {}", resource.toString());
    return new File(resource.getPath());
  }
}
