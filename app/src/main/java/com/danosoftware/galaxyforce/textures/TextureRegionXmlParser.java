package com.danosoftware.galaxyforce.textures;

import android.util.Log;
import android.util.Xml;

import com.danosoftware.galaxyforce.services.file.FileIO;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class TextureRegionXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public List<TextureDetail> readTextures(FileIO fileIO, String fileName) throws XmlPullParserException, IOException {
        TextureRegionXmlParser parser = new TextureRegionXmlParser();
        List<TextureDetail> listOfTextureRegions;

        InputStream in;

        in = fileIO.readAsset(fileName);
        listOfTextureRegions = parser.parse(in);

        if (in != null) {
            in.close();
        }

        return listOfTextureRegions;
    }

    private List<TextureDetail> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readTextureFile(parser);
        } finally {
            in.close();
        }
    }

    private List<TextureDetail> readTextureFile(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<TextureDetail> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "TextureAtlas");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // Starts by looking for the sub texture tag
            if (name.equals("SubTexture")) {
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

    // Parses the contents of SubTexture. Extracts attributes it encounters.
    private TextureDetail readTextureRegion(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "SubTexture");

        String textureName = parser.getAttributeValue(null, "name");
        String textureX = parser.getAttributeValue(null, "x");
        String textureY = parser.getAttributeValue(null, "y");
        String textureWidth = parser.getAttributeValue(null, "width");
        String textureHeight = parser.getAttributeValue(null, "height");

        Log.d("xml Reader", "Texture region found. Name : '" + textureName + "'.");

        // move to next tag
        parser.nextTag();

        parser.require(XmlPullParser.END_TAG, ns, "SubTexture");

        return new TextureDetail(textureName, textureX, textureY, textureWidth, textureHeight);
    }
}
