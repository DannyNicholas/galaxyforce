package com.danosoftware.galaxyforce.textures;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.util.Log;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.view.GLGraphics;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

/**
 * Represents a single texture map and associated properties/utilities.
 * <p>
 * Loads the texture map PNG image file.
 * Loads the texture map XML properties file.
 * <p>
 * Texture map is bound to Open GL and a texture Id is assigned for future reference.
 * Supports re-loading of the texture map (maybe after application pause).
 */
public class Texture {

    private final GLGraphics glGraphics;
    private final TextureMap textureMap;
    private final TextureLoader textureLoader;

    // properties for each texture region in texture map
    private final Map<String, TextureDetail> textureDetailMap;

    private int textureId;
    private int minFilter;
    private int magFilter;
    private int width;
    private int height;

    private static final String TAG = "Texture";

    public Texture(
            final GLGraphics glGraphics,
            final TextureRegionXmlParser xmlParser,
            final TextureLoader textureLoader,
            final TextureMap textureMap) {
        this.textureMap = textureMap;
        this.glGraphics = glGraphics;
        this.textureLoader = textureLoader;
        this.textureDetailMap = buildTextureRegionMap(
                xmlParser,
                textureMap.getTextureXml());
        load();
    }

    private void load() {
        GL10 gl = glGraphics.getGl();
        int[] textureIds = new int[1];
        gl.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];

        Bitmap bitmap = textureLoader.load(
                textureMap.getTextureImage());
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

        Log.d(TAG, "Loaded texture. Id: " + textureId + ". Filename: " + textureMap.getTextureImage() + ".");
    }

    public void reload() {
        load();
        bind();
        setFilters(minFilter, magFilter);
        glGraphics.getGl().glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    private void setFilters(int minFilter, int magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        GL10 gl = glGraphics.getGl();
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void bind() {
        GL10 gl = glGraphics.getGl();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void dispose() {
        GL10 gl = glGraphics.getGl();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        int[] textureIds =
                {textureId};
        gl.glDeleteTextures(1, textureIds, 0);

        Log.d(TAG, "Disposed texture. Id: " + textureId + ". Filename: " + textureMap.getTextureImage() + ".");
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
            String errorMessage = "Error: No texture details have been returned for region name: '" + textureRegion + "'.";
            Log.e(TAG, errorMessage);
            throw new GalaxyForceException(errorMessage);
        }

        return textureDetailMap.get(textureRegion);
    }
}
