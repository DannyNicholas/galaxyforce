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
        try {
            InputStream in = assets.open("textures/" + fileName);
            final Bitmap bitmap = BitmapFactory.decodeStream(in);
            in.close();
            return bitmap;
        } catch (IOException e) {
            throw new GalaxyForceException("Couldn't load texture '" + fileName + "'", e);
        }
    }
}
