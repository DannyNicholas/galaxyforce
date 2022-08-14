package com.danosoftware.galaxyforce.flightpath.utilities;

import android.content.res.AssetManager;
import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

/**
 * JSON Path Loader utilities
 */
public class PathLoader {

  private static final String ACTIVITY_TAG = "Path Loader";
  private final EnumMap<Path, PathListDTO> cache;

  private final AssetManager assets;
  private final ObjectMapper mapper;

  public PathLoader(AssetManager assets) {
    this.assets = assets;
    this.cache = new EnumMap<>(Path.class);
    this.mapper = new ObjectMapper();
  }

  /**
   * Retrieve path data from cache or request path load.
   */
  public PathListDTO loadPaths(Path path) {
    if (cache.containsKey(path)) {
      return cache.get(path);
    }
    PathListDTO pathData = loader(path.getPathFile());
    cache.put(path, pathData);
    return pathData;
  }

  /**
   * Load path data JSON file and return as a list of Path DTOs
   */
  private PathListDTO loader(String jsonFile) {
    Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Loading JSON Path: " + jsonFile);
    try (InputStream is = assets.open("paths/" + jsonFile)) {
      final PathListDTO pathData = mapper.readValue(is, PathListDTO.class);
      Log.i(GameConstants.LOG_TAG,
          ACTIVITY_TAG + ": Loaded " + pathData.getPathList().size() + " JSON Paths from: "
              + jsonFile);
      return pathData;
    } catch (IOException e) {
      throw new GalaxyForceException("Error while loading JSON path: " + jsonFile, e);
    }
  }
}
