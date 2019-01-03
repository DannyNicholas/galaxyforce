package com.danosoftware.galaxyforce.services.music;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;

import java.io.IOException;

public class AndroidAudio implements Audio {

    AssetManager assets;
//    SoundPool soundPool;

    public AndroidAudio(Context context) {

        if (context instanceof Activity) {
            Activity activity = (Activity) context;

            // allows audio stream to have volume controlled by hardware buttons
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }

        this.assets = context.getAssets();
//        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Music newMusic(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }
}