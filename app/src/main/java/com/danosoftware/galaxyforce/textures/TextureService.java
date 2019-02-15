package com.danosoftware.galaxyforce.textures;

import android.util.Log;

import com.danosoftware.galaxyforce.view.GLGraphics;

import java.util.EnumMap;

/**
 * Service to create or retrieve textures.
 */
public class TextureService {

    private static final String TAG = "Textures";

    // XML file loader
    private final TextureRegionXmlParser xmlParser;

    // PNG texture image file loader
    private final TextureLoader textureLoader;

    // Open GL graphics
    private final GLGraphics glGraphics;

    // cache of texture maps and their texture utilities
    private final EnumMap<TextureMap, Texture> textureMaps;

    public TextureService(
            final GLGraphics glGraphics,
            final TextureRegionXmlParser xmlParser,
            final TextureLoader textureLoader) {
        this.glGraphics = glGraphics;
        this.xmlParser = xmlParser;
        this.textureLoader = textureLoader;
        this.textureMaps = new EnumMap<>(TextureMap.class);
    }

    /**
     * Retrieve a texture from cache if available.
     * Otherwise create a new one.
     */
    public Texture getOrCreateTexture(TextureMap textureMap) {

        final Texture texture;

        if (textureMaps.containsKey(textureMap)) {
            Log.i(TAG, "Reload existing texture for: " + textureMap.name());
            texture = textureMaps.get(textureMap);
            // if retrieving an existing texture then reload/bind for OpenGL
            texture.reload();
        } else {
            Log.i(TAG, "Create new texture for: " + textureMap.name());
            texture = new Texture(glGraphics, xmlParser, textureLoader, textureMap);
            textureMaps.put(textureMap, texture);
        }

        return texture;
    }
}
