package com.danosoftware.galaxyforce.textures;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.view.GLShaderHelper;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single texture map and associated properties/utilities.
 * <p>
 * Loads the texture map PNG image file. Loads the texture map XML properties file.
 * <p>
 * Texture map is bound to Open GL and a texture Id is assigned for future reference. Supports
 * re-loading of the texture map (maybe after application pause).
 */
public class Texture {

  private final TextureMap textureMap;
  private final TextureLoader textureLoader;

  // properties for each texture region in texture map
  private final Map<String, TextureDetail> textureDetailMap;

  private int textureId;
  private int width;
  private int height;

  private static final String TAG = "Texture";

  public Texture(
      final TextureRegionXmlParser xmlParser,
      final TextureLoader textureLoader,
      final TextureMap textureMap) {
    this.textureMap = textureMap;
    this.textureLoader = textureLoader;
    this.textureDetailMap = buildTextureRegionMap(
        xmlParser,
        textureMap.getTextureXml());
  }

  public void load() {

    // create unique id for our texture
    int[] textureIds = new int[1];
    GLES20.glGenTextures(1, textureIds, 0);
    textureId = textureIds[0];

    // load bitmap
    Bitmap bitmap = textureLoader.load(textureMap.getTextureImage());
    this.width = bitmap.getWidth();
    this.height = bitmap.getHeight();

    // Bind the texture id to the 2D texture target.
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

    // Configure min/mag filtering, i.e. what scaling method do we use if what we're rendering
    // is smaller or larger than the source image.
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
        GLES20.GL_NEAREST);
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
        GLES20.GL_NEAREST);

    // Load the bitmap into the bound texture.
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

    bitmap.recycle();

    Log.d(TAG,
        "Loaded texture. Id: " + textureId + ". Filename: " + textureMap.getTextureImage()
            + ".");
  }

  public void bind() {
    // Set the active texture unit to texture unit 0.
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

    // Bind our texture to this unit.
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

    // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
    GLES20.glUniform1i(GLShaderHelper.sTextureHandle, 0);
  }

  public void dispose() {
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
    int[] textureIds =
        {textureId};
    GLES20.glDeleteTextures(1, textureIds, 0);

    Log.d(TAG,
        "Disposed texture. Id: " + textureId + ". Filename: " + textureMap.getTextureImage()
            + ".");
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  /**
   * Create map of texture region name to region's texture properties.
   */
  private Map<String, TextureDetail> buildTextureRegionMap(
      final TextureRegionXmlParser xmlParser,
      final String textureXmlFile) {

    final Map<String, TextureDetail> textureDetailMap = new HashMap<>();
    for (TextureDetail texture : xmlParser.loadTextures(textureXmlFile)) {
      textureDetailMap.put(texture.getName(), texture);
    }
    return textureDetailMap;
  }

  /**
   * Return texture detail for supplied texture region name.
   */
  public TextureDetail getTextureDetail(String textureRegion) {

    if (!textureDetailMap.containsKey(textureRegion)) {
      String errorMessage =
          "Error: No texture details have been returned for region name: '" + textureRegion + "'.";
      Log.e(TAG, errorMessage);
      throw new GalaxyForceException(errorMessage);
    }

    return textureDetailMap.get(textureRegion);
  }
}
