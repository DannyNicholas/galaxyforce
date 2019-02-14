package com.danosoftware.galaxyforce.textures;

import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class TextureRegionXmlParser {


    private static final String ACTIVITY_TAG = "Path Loader";

    private static final String TEXTURE_ATLAS_XML_TAG = "TextureAtlas";
    private static final String SPRITE_XML_TAG = "sprite";

    // We don't use namespaces
    private static final String NS = null;

    private final AssetManager assets;

    public TextureRegionXmlParser(AssetManager assets) {
        this.assets = assets;
    }

    /**
     * Load texture XML data file and return as a list of Texture Details
     */
    public List<TextureDetail> loadTextures(String xmlFile) {
        Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Loading texture file: " + xmlFile);
        try {
            InputStream is = assets.open("textures/" + xmlFile);
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            List<TextureDetail> textures = readTextureFile(parser);
            is.close();
            return textures;
        } catch (XmlPullParserException | IOException e) {
            throw new GalaxyForceException("Error while loading texture file: " + xmlFile, e);
        }
    }

    private List<TextureDetail> readTextureFile(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<TextureDetail> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NS, TEXTURE_ATLAS_XML_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // Starts by looking for the sprite tag
            if (name.equals(SPRITE_XML_TAG)) {
                entries.add(readTextureRegion(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    // Parses the contents of sprite. Extracts attributes it encounters.
    private TextureDetail readTextureRegion(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, SPRITE_XML_TAG);

        final String textureName = parser.getAttributeValue(null, "n");
        final String textureX = parser.getAttributeValue(null, "x");
        final String textureY = parser.getAttributeValue(null, "y");
        final String textureWidth = parser.getAttributeValue(null, "w");
        final String textureHeight = parser.getAttributeValue(null, "h");

        Log.d("xml Reader", "Texture region found. Name : '" + textureName + "'.");

        // move to next tag
        parser.nextTag();

        parser.require(XmlPullParser.END_TAG, NS, SPRITE_XML_TAG);

        return new TextureDetail(textureName, textureX, textureY, textureWidth, textureHeight);
    }
}
