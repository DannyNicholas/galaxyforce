package com.danosoftware.galaxyforce.services.music;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class MusicPlayerServiceImpl implements MusicPlayerService, MediaPlayer.OnCompletionListener {

    private final Map<Music, AssetFileDescriptor> musicFiles;

    private MediaPlayer mediaPlayer;
    private boolean isPrepared;

    // is music player enabled?
    private boolean musicEnabled;

    public MusicPlayerServiceImpl(Context context, boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
        this.musicFiles = new EnumMap<>(Music.class);

        // load map of music to files descriptors
        AssetManager assets = context.getAssets();
        for (Music music : Music.values()) {
            musicFiles.put(music, assetFileDescriptor(assets, music.getFileName()));
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            // allows audio stream to have volume controlled by hardware buttons
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }

    @Override
    public void load(Music music) {

        // dispose old media player
        if (this.mediaPlayer != null) {
            dispose();
        }
        this.mediaPlayer = new MediaPlayer();

        AssetFileDescriptor assetDescriptor = musicFiles.get(music);
        try {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(), assetDescriptor.getStartOffset(), assetDescriptor.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            throw new GalaxyForceException("Couldn't load music for '" + music.getFileName() + "'");
        }
    }

    @Override
    public void play() {

        if (mediaPlayer.isPlaying() || !musicEnabled) {
            return;
        }

        try {
            synchronized (this) {
                if (!isPrepared) {
                    mediaPlayer.prepare();
                }
                mediaPlayer.start();
            }
        } catch (IllegalStateException | IOException e) {
            throw new GalaxyForceException("Unable to play music", e);
        }

    }

    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
        synchronized (this) {
            isPrepared = false;
        }
    }

    @Override
    public void setMusicEnabled(boolean enableMusic) {
        this.musicEnabled = enableMusic;
    }

    @Override
    public void dispose() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
    }

    private AssetFileDescriptor assetFileDescriptor(AssetManager assets, String filename) {
        try {
            return assets.openFd(filename);
        } catch (IOException e) {
            throw new GalaxyForceException("Couldn't load music '" + filename + "'", e);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        synchronized (this) {
            isPrepared = false;
        }
    }
}
