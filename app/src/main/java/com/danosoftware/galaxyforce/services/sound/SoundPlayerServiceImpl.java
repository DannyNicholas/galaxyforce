package com.danosoftware.galaxyforce.services.sound;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class SoundPlayerServiceImpl implements SoundPlayerService {

    private final SoundPool soundPool;

    // is sound player enabled?
    private boolean soundEnabled;

    // map of all sound effects enums to sound IDs
    private final Map<SoundEffect, Integer> effectsBank;

    public SoundPlayerServiceImpl(Context context, boolean soundEnabled) {

        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        this.soundEnabled = soundEnabled;
        this.effectsBank = new EnumMap<>(SoundEffect.class);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            // allows audio stream to have volume controlled by hardware buttons
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }

        // load sound effects - loading is asynchronous
        AssetManager assets = context.getAssets();
        for (SoundEffect effect : SoundEffect.values()) {
            loadSound(assets, effect);
        }
    }

    @Override
    public void play(SoundEffect effect) {
        if (soundEnabled && effectsBank.containsKey(effect)) {
            int soundId = effectsBank.get(effect);
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }

    @Override
    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    @Override
    public void dispose() {
        // remove all effects from map and dispose of all sound effects
        for (SoundEffect effect : SoundEffect.values()) {
            if (effectsBank.containsKey(effect)) {
                int soundId = effectsBank.remove(effect);
                soundPool.unload(soundId);
            }
        }
        // release sound pool
        soundPool.release();
    }

    /**
     * Load sound effect from file (loaded asynchronously).
     * Add to effects bank once loaded.
     */
    public void loadSound(final AssetManager assets, final SoundEffect soundEffect) {

        String filename = soundEffect.getFileName();

        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            Log.i(GameConstants.LOG_TAG, "Loading Sound Effect: " + soundEffect.name());
            soundPool.load(assetDescriptor, 0);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    effectsBank.put(soundEffect, sampleId);
                    Log.i(GameConstants.LOG_TAG, "Loaded Sound Effect: " + soundEffect.name() + " with ID: " + sampleId);
                }
            });
        } catch (IOException e) {
            throw new GalaxyForceException("Couldn't load sound '" + filename + "'");
        }
    }
}
