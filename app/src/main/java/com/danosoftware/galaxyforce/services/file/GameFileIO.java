package com.danosoftware.galaxyforce.services.file;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class GameFileIO implements FileIO {

    private final AssetManager assets;

    public GameFileIO(Context context) {
        this.assets = context.getAssets();
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return assets.open(fileName);
    }
}
