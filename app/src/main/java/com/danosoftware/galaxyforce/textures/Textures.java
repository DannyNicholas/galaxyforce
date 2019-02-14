package com.danosoftware.galaxyforce.textures;

import android.util.Log;

import com.danosoftware.galaxyforce.view.GLGraphics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Textures {
    private static final String TAG = "Textures";

    private final TextureRegionXmlParser xmlParser;

    public Textures(TextureRegionXmlParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    // static map to hold list of known Textures
    private final Map<TextureMap, Texture> textureMap = new HashMap<>();

    // static map to hold sprite names and equivalent Texture Details for last
    // requested texture
    private final Map<String, TextureDetail> textureDetailMap = new HashMap<>();

    // return TextureDetail for the supplied sprite name
    public TextureDetail getTextureDetail(String name) {
        return textureDetailMap.get(name);
    }

    // static factory to create instances
    public Texture newTexture(GLGraphics glGraphics, TextureMap textureState) {
        if (textureState == null) {
            throw new IllegalArgumentException("Supplied TextureState object can not be null.");
        }

        // get xml linked to texture state enum
        String textureXml = textureState.getTextureXml();

        // empty texture map
        textureDetailMap.clear();

        // populate texture map
        storeTextureDetails(textureXml);

        Texture newTexture;

        // if texture has been requested before retrieve from map.
        // if texture is new then create and add to map.
        // constructing a new texture triggers texture to be loaded so avoid if
        // not needed.
        if (textureMap.containsKey(textureState)) {

            Log.i(TAG, "Reload existing texture.");
            newTexture = textureMap.get(textureState);
            // if retrieving an existing texture then reload/bind for OpenGL
            newTexture.reload();
        } else {
            Log.i(TAG, "Create new texture.");
            newTexture = new Texture(glGraphics, fileIO, textureState.getTextureImage());
            textureMap.put(textureState, newTexture);
        }

        return newTexture;
    }

    // get textures from texture file
    // store textures into texture details map
    private void storeTextureDetails(String textureXml) {
        List<TextureDetail> listOfTextureRegions = xmlParser.loadTextures(textureXml);
            for (TextureDetail texture : listOfTextureRegions) {
                // store texture details into map
                textureDetailMap.put(texture.name, texture);
            }
    }
}
