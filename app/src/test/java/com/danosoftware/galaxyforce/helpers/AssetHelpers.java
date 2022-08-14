package com.danosoftware.galaxyforce.helpers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility methods for application assets.
 */
public class AssetHelpers {

  private final static String ASSET_PATH = "src/main/assets";

  /**
   * Return a paths assets file with the supplied filename.
   */
  public static File pathAsset(final String filename) {
    Path path = Paths.get(ASSET_PATH, "paths", filename);
    return path.toFile();
  }
}
