package com.danosoftware.galaxyforce.dto;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * Tests that check JSON paths are loaded successfully and path Data Transfer Objects created.
 */
public class PathDTOTest {

    final static Logger logger = LoggerFactory.getLogger(PathDTOTest.class);

    @Test
    public void shouldLoadBezierPaths() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        PathListDTO pathData = mapper.readValue(getFileFromPath(this, "paths/bezier.json"), PathListDTO.class);

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
        PathListDTO pathData = mapper.readValue(getFileFromPath(this, "paths/linear.json"), PathListDTO.class);

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
