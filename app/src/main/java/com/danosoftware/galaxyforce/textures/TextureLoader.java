package com.danosoftware.galaxyforce.textures;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import java.io.IOException;
import java.io.InputStream;

public class TextureLoader {

  private final AssetManager assets;

  public TextureLoader(AssetManager assets) {
    this.assets = assets;
  }

  public Bitmap load(String fileName) {
    try (InputStream in = assets.open("textures/" + fileName)) {
      return BitmapFactory.decodeStream(in);
    } catch (IOException e) {
      throw new GalaxyForceException("Couldn't load texture '" + fileName + "'", e);
    }
  }
}
