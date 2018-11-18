package com.danosoftware.galaxyforce.textures;

import android.util.Log;

import com.danosoftware.galaxyforce.interfaces.FileIO;
import com.danosoftware.galaxyforce.interfaces.Game;
import com.danosoftware.galaxyforce.services.Games;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Textures {
    private static final String TAG = "Textures";

    // private constructor
    private Textures() {

    }

    // static map to hold list of known Textures
    private static Map<TextureMap, Texture> textureMap = new HashMap<TextureMap, Texture>();

    // static map to hold sprite names and equivalent Texture Details for last
    // requested texture
    private static Map<String, TextureDetail> textureDetailMap = new HashMap<String, TextureDetail>();

    // return TextureDetail for the supplied sprite name
    public static TextureDetail getTextureDetail(String name) {
        return textureDetailMap.get(name);
    }

    // static factory to create instances
    public static Texture newTexture(TextureMap textureState) {
        if (textureState == null) {
            throw new IllegalArgumentException("Supplied TextureState object can not be null.");
        }

        // get xml linked to texture state enum
        String textureXml = textureState.getTextureXml();

        // empty texture map
        textureDetailMap.clear();

        // populate texture map
        Game game = Games.getGame();
        storeTextureDetails(game.getFileIO(), textureXml);

        Texture newTexture = null;

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
            newTexture = new Texture(game, textureState.getTextureImage());
            textureMap.put(textureState, newTexture);
        }

        return newTexture;
    }

    // get textures from texture file
    // store textures into texture details map
    private static void storeTextureDetails(FileIO fileIO, String textureXml) {
        TextureRegionXmlParser textureParser = new TextureRegionXmlParser();

        List<TextureDetail> listOfTextureRegions = null;
        try {
            listOfTextureRegions = textureParser.readTextures(fileIO, textureXml);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing texture region xml file.");
        } catch (IOException e) {
            Log.e(TAG, "Error reading texture region xml file.");
        }

        for (TextureDetail texture : listOfTextureRegions) {
            // store texture details into map
            textureDetailMap.put(texture.name, texture);
        }
    }
}
