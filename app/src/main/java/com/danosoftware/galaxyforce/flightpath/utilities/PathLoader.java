package com.danosoftware.galaxyforce.flightpath.utilities;

import android.content.res.AssetManager;
import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * JSON Path Loader utilities
 */
public class PathLoader {

    private static final String ACTIVITY_TAG = "Path Loader";

    private final AssetManager assets;

    public PathLoader(AssetManager assets) {
        this.assets = assets;
    }

    /**
     * Load path data JSON file and return as a list of Path DTOs
     */
    public PathListDTO loadPaths(String jsonFile) {
        Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Loading JSON Path");
        try {
            InputStream is = assets.open("paths/" + jsonFile);
            ObjectMapper mapper = new ObjectMapper();
            final PathListDTO pathData = mapper.readValue(is, PathListDTO.class);
            is.close();
            Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Loaded " + pathData.getPathList().size() + " JSON Paths");
            return pathData;
        } catch (IOException e) {
            throw new GalaxyForceException("Error while loading JSON path: " + jsonFile, e);
        }
    }
}
