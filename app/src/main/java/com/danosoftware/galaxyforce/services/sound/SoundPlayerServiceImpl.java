package com.danosoftware.galaxyforce.services.sound;

import static com.danosoftware.galaxyforce.services.sound.SoundPlayerHelper.createModernSoundPool;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;

public class SoundPlayerServiceImpl implements SoundPlayerService,
    SoundPool.OnLoadCompleteListener {

  private static final float EFFECTS_VOLUME = 0.2f;
  private static final int MAX_STREAMS = 5;
  private static final String TAG = "SoundPlayerService";

  // the most common sampling rate expected by devices is 44.1 kHz and 48.0 kHz
  // the folders below hold sound effects for each sample rate
  private static final String SAMPLES_FOLDER_44_1_KHZ = "44100";
  private static final String SAMPLES_FOLDER_48_0_KHZ = "48000";

  private static final int SAMPLE_RATE_44_1_KHZ = 44100;
  private static final int SAMPLE_RATE_48_0_KHZ = 48000;
  private static final int DEFAULT_SAMPLING_RATE = SAMPLE_RATE_44_1_KHZ;

  private final SoundPool soundPool;

  // is sound player enabled?
  private boolean soundEnabled;

  // map of all sound effects enums to sound IDs
  private final EnumMap<SoundEffect, Integer> effectsBank;

  private final String samplesFolder;

  // queue of last N stream IDs that have been played
  // allows us to stop any playing streams if user disables sound.
  //
  // this makes the (reasonable) assumption that any currently playing
  // sounds started within the last N effects. It is possible that a
  // very long sound effect started earlier and is still playing while
  // other shorter effects started later and finished earlier.
  //
  // we have no way of asking the SoundPool what streams are currently playing.
  private final Deque<Integer> streams;

  public SoundPlayerServiceImpl(Context context, boolean soundEnabled) {
    this.soundPool = createSoundPool();
    this.soundEnabled = soundEnabled;
    this.effectsBank = new EnumMap<>(SoundEffect.class);
    this.streams = new ArrayDeque<>(MAX_STREAMS);
    this.samplesFolder = chooseSamplesFolder(nativeSampleRate(context));

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

  @SuppressWarnings("deprecation")
  private SoundPool createSoundPool() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return createModernSoundPool(MAX_STREAMS);
    } else {
      // legacy sound pool
      return new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
    }
  }

  @Override
  public void play(SoundEffect effect) {
    if (soundEnabled) {
      Integer soundId = effectsBank.get(effect);
      if (soundId != null) {
        int streamId = soundPool.play(soundId, EFFECTS_VOLUME, EFFECTS_VOLUME, 0, 0, 1f);
        addStreamToQueue(streamId);
      } else {
        Log.w(TAG,
            "Sound Effect: " + effect.name() + " could not be found.");
      }
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
        Integer streamId = streams.poll();
        if (streamId != null) {
          soundPool.stop(streamId);
        }
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
    Log.i(TAG, "Unloading all Sound Effects");
    for (SoundEffect effect : SoundEffect.values()) {
      if (effectsBank.containsKey(effect)) {
        Integer soundId = effectsBank.remove(effect);
        if (soundId != null) {
          soundPool.unload(soundId);
        }
      }
    }
    // release sound pool
    soundPool.release();
  }

  /**
   * Load sound effect from file (loaded asynchronously). Add to effects bank prior to loading.
   * <p>
   * Ideally we should only add effects to bank after loading completes but this would require
   * additional logic to temporarily hold sound IDs and enums until loading completes.
   * <p>
   * Not worth the additional effort. It's likely all effects will be loaded within seconds.
   * Attempting to play an unloaded effect just produces a warning (not an exception).
   */
  private void loadSound(final AssetManager assets, final SoundEffect soundEffect) {

    String filename = soundEffect.getFileName();

    try (AssetFileDescriptor assetDescriptor = assets
        .openFd("sounds/" + samplesFolder + "/" + filename)) {
      soundPool.setOnLoadCompleteListener(this);
      int sampleId = soundPool.load(assetDescriptor, 0);
      effectsBank.put(soundEffect, sampleId);
      Log.i(TAG,
          "Loading Sound Effect: " + soundEffect.name() + " with ID: " + sampleId);
    } catch (IOException e) {
      throw new GalaxyForceException("Couldn't load sound '" + filename + "'");
    }
  }

  @Override
  public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
    Log.i(TAG, "Loaded Sound Effect ID : " + sampleId);
  }

  /**
   * Add streamId to queue. If queue is full remove the oldest stream Id. Queue will hold the last N
   * stream Ids.
   */
  private void addStreamToQueue(int streamId) {
    if (streams.size() == MAX_STREAMS) {
      streams.poll();
    }
    streams.offer(streamId);
  }

  // return the native sampling rate of the current device
  private int nativeSampleRate(Context context) {
    AudioManager audioManager = (AudioManager) context
        .getSystemService(Context.AUDIO_SERVICE);
    String sampleRateString = audioManager
        .getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);

    int nativeOutputSampleRate = DEFAULT_SAMPLING_RATE;

    if (sampleRateString != null) {
      try {
        nativeOutputSampleRate = Integer.parseInt(sampleRateString);
        Log.i(TAG, "Device native sample rate: " + sampleRateString);
      } catch (NumberFormatException ex) {
        Log.w(TAG, "Unable to parse device native sample rate: " + sampleRateString);
      }
    }

    return nativeOutputSampleRate;
  }

  // select the samples folder appropriate for the native sample rate
  private String chooseSamplesFolder(int nativeOutputSampleRate) {
    final String samplesFolder;
    switch (nativeOutputSampleRate) {
      case SAMPLE_RATE_48_0_KHZ:
        samplesFolder = SAMPLES_FOLDER_48_0_KHZ;
        break;
      case SAMPLE_RATE_44_1_KHZ:
      default:
        samplesFolder = SAMPLES_FOLDER_44_1_KHZ;
        break;
    }
    Log.i(TAG, "Selected effects folder: " + samplesFolder);
    return samplesFolder;
  }
}
