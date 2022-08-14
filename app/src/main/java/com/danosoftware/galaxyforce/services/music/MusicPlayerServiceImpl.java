package com.danosoftware.galaxyforce.services.music;

import static com.danosoftware.galaxyforce.services.music.MusicPlayerHelper.createModernMediaPlayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import java.io.IOException;

public class MusicPlayerServiceImpl implements
    MusicPlayerService,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener {

  private MediaPlayer mediaPlayer;

  // is player prepared for playing (prepares asynchronously)
  private boolean isPrepared;

  // is music player enabled?
  private boolean musicEnabled;

  // currently playing music
  private Music currentlyLoaded;

  private final AssetManager assets;

  public MusicPlayerServiceImpl(Context context, boolean musicEnabled) {
    this.musicEnabled = musicEnabled;
    this.assets = context.getAssets();
    this.currentlyLoaded = null;

    if (context instanceof Activity) {
      Activity activity = (Activity) context;
      // allows audio stream to have volume controlled by hardware buttons
      activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
  }

  /**
   * Load music (if different from currently loaded music) and prepare asynchronously.
   */
  @Override
  public synchronized void load(Music music) {

    // no action if wanted music is already loaded
    if (currentlyLoaded == music) {
      return;
    }

    // create or reset media player
    if (this.mediaPlayer != null) {
      this.mediaPlayer.reset();
    } else {
      this.mediaPlayer = createMediaPlayer();
    }

    Log.i(GameConstants.LOG_TAG, "Load Music");

    // set file as music source
    try (AssetFileDescriptor assetDescriptor = assets.openFd("music/" + music.getFileName())) {
      mediaPlayer.setDataSource(
          assetDescriptor.getFileDescriptor(),
          assetDescriptor.getStartOffset(),
          assetDescriptor.getLength());
    } catch (IOException | IllegalArgumentException e) {
      throw new GalaxyForceException("Couldn't load music: " + music.getFileName(), e);
    }

    currentlyLoaded = music;
    mediaPlayer.setLooping(true);
    mediaPlayer.setVolume(1f, 1f);

    isPrepared = false;
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.prepareAsync();
  }

  @SuppressWarnings("deprecation")
  public static MediaPlayer createMediaPlayer() {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      return createModernMediaPlayer();
    }

    // return legacy media player
    MediaPlayer mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    return mediaPlayer;
  }

  @Override
  public synchronized void play() {
    // if not prepared then exit
    // will play automatically once prepared
    if (!isPrepared || mediaPlayer == null) {
      return;
    }

    Log.i(GameConstants.LOG_TAG, "Play Music");

    // no action if already playing or disabled
    if (mediaPlayer.isPlaying() || !musicEnabled) {
      return;
    }

    // otherwise start playing
    mediaPlayer.start();
  }

  @Override
  public synchronized void pause() {
    Log.i(GameConstants.LOG_TAG, "Pause Music");
    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
      mediaPlayer.pause();
    }
  }

  @Override
  public void setMusicEnabled(boolean enableMusic) {
    this.musicEnabled = enableMusic;
  }

  @Override
  public synchronized void dispose() {
    Log.i(GameConstants.LOG_TAG, "Dispose Music");
    isPrepared = false;
    currentlyLoaded = null;

    if (mediaPlayer == null) {
      return;
    }

    if (mediaPlayer.isPlaying()) {
      mediaPlayer.stop();
    }
    mediaPlayer.release();
    mediaPlayer = null;
  }

  // music is now prepared - so we can play it.
  @Override
  public void onPrepared(MediaPlayer player) {
    Log.i(GameConstants.LOG_TAG, "Music Prepared");
    isPrepared = true;
    play();
  }

  // called if media player encounters an error
  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    Log.e(GameConstants.LOG_TAG,
        String.format("Music exception. type: %d. code: %d", what, extra));
    return false;
  }

  // ideally this should not be called as our music loops.
  // may occur in exceptions and clear-up is needed.
  @Override
  public void onCompletion(MediaPlayer mp) {
    dispose();
  }
}
