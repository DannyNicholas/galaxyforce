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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;

public class SoundPlayerServiceImpl implements SoundPlayerService, SoundPool.OnLoadCompleteListener {

    private static final float EFFECTS_VOLUME = 0.2f;
    private static final int MAX_STREAMS = 20;

    private final SoundPool soundPool;

    // is sound player enabled?
    private boolean soundEnabled;

    // map of all sound effects enums to sound IDs
    private final Map<SoundEffect, Integer> effectsBank;

    // queue of last N stream IDs that have been played
    // allows us to stop any playing streams if user disables sound.
    //
    // this makes (the reasonable) assumption that any currently playing
    // sounds started within the last N effects. It is possible that a
    // very long sound effect started earlier and is still playing while
    // other shorter effects started later and finished earlier.
    //
    // we have no way of asking the SoundPool what streams are currently playing.
    private final Deque<Integer> streams;

    public SoundPlayerServiceImpl(Context context, boolean soundEnabled) {

        this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        this.soundEnabled = soundEnabled;
        this.effectsBank = new EnumMap<>(SoundEffect.class);
        this.streams = new ArrayDeque<>(MAX_STREAMS);

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
            int streamId = soundPool.play(soundId, EFFECTS_VOLUME, EFFECTS_VOLUME, 0, 0, 1);
            addStreamToQueue(streamId);
        }
    }

    @Override
    public void pause() {
        soundPool.autoPause();
    }

    @Override
    public void resume() {
        // if we are resuming and sound is now disabled then manually stop all streams.
        // otherwise, any streams that were in the middle of playing when we paused will continue.
        if (!soundEnabled) {
            while (!streams.isEmpty()) {
                int streamId = streams.poll();
                soundPool.stop(streamId);
            }
        }

        soundPool.autoResume();
    }

    @Override
    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    @Override
    public void dispose() {
        // remove all effects from map and dispose of all sound effects
        Log.i(GameConstants.LOG_TAG, "Unloading all Sound Effects");
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
     * Add to effects bank prior to loading.
     *
     * Ideally we should only add effects to bank after loading completes
     * but this would require additional logic to temporarily
     * hold sound IDs and enums until loading completes.
     *
     * Not worth the additional effort. It's likely all effects will be loaded within seconds.
     * Attempting to play an unloaded effect just produces a warning (not an exception).
     */
    private void loadSound(final AssetManager assets, final SoundEffect soundEffect) {

        String filename = soundEffect.getFileName();

        try {
            AssetFileDescriptor assetDescriptor = assets.openFd("sounds/" + filename);
            soundPool.setOnLoadCompleteListener(this);
            int sampleId = soundPool.load(assetDescriptor, 0);
            effectsBank.put(soundEffect, sampleId);
            Log.i(GameConstants.LOG_TAG, "Loading Sound Effect: " + soundEffect.name() + " with ID: " + sampleId);
        } catch (IOException e) {
            throw new GalaxyForceException("Couldn't load sound '" + filename + "'");
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.i(GameConstants.LOG_TAG, "Loaded Sound Effect ID : " + sampleId);
    }

    /**
     * Add streamId to queue. If queue is full remove the oldest stream Id.
     * Queue will hold the last N stream Ids.
     */
    private void addStreamToQueue(int streamId) {
        if (streams.size() == MAX_STREAMS) {
            streams.poll();
        }
        streams.offer(streamId);
    }
}
