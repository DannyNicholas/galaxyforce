package com.danosoftware.galaxyforce.models.aliens;

import static com.danosoftware.galaxyforce.models.aliens.AlienManager.SubWaveState.DESTROYED;
import static com.danosoftware.galaxyforce.models.aliens.AlienManager.SubWaveState.END_OF_PASS;
import static com.danosoftware.galaxyforce.models.aliens.AlienManager.SubWaveState.IDLE;
import static com.danosoftware.galaxyforce.models.aliens.AlienManager.SubWaveState.PLAYING;
import static com.danosoftware.galaxyforce.models.aliens.AlienManager.SubWaveState.WAVE_COMPLETE;
import static com.danosoftware.galaxyforce.waves.utilities.Randomiser.random;

import android.util.Log;
import com.danosoftware.galaxyforce.services.achievements.AchievementService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IResettableAlien;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.managers.WaveManager;
import java.util.ArrayList;
import java.util.List;

public class AlienManager implements IAlienManager {

  /* logger tag */
  private static final String TAG = AlienManager.class.getSimpleName();
  // provides waves
  private final WaveManager waveManager;
  private final AchievementService achievements;
  // temporary queue of spawned aliens that will be added to the
  // main alien list at start of each animation loop
  private final List<IAlien> spawnedAliens;
  // state of current sub-wave
  private List<IAlien> aliens;
  private List<IAlien> activeAliens;
  private List<IAlien> visibleAliens;
  private SubWaveState subWaveState;
  private boolean repeatedSubWave;

  public AlienManager(
      WaveManager waveManager,
      AchievementService achievements) {
    this.waveManager = waveManager;
    this.achievements = achievements;
    this.aliens = new ArrayList<>();
    this.activeAliens = new ArrayList<>();
    this.visibleAliens = new ArrayList<>();
    this.spawnedAliens = new ArrayList<>();
    this.subWaveState = IDLE;
  }

  @Override
  public List<IAlien> activeAliens() {
    return activeAliens;
  }

  @Override
  public List<IAlien> allAliens() {
    return visibleAliens;
  }

  @Override
  public void animate(float deltaTime) {
    int finishedAliens = 0;
    List<IAlien> nonDestroyedAliens = new ArrayList<>();
    List<IAlien> currentActiveAliens = new ArrayList<>();
    List<IAlien> currentVisibleAliens = new ArrayList<>();

    // add queued spawned aliens.
    // spawned aliens are newly created aliens to the wave.
    // they should appear behind existing sprites so are added to beginning of list
    aliens.addAll(0, spawnedAliens);
    spawnedAliens.clear();

    for (IAlien alien : aliens) {
      alien.animate(deltaTime);

      if (alien.isActive()) {
        currentActiveAliens.add(alien);
      }
      if (alien.isVisible()) {
        currentVisibleAliens.add(alien);
      }
      if (alien instanceof IResettableAlien && ((IResettableAlien) alien).isEndOfPass()) {
        finishedAliens++;
      }
      if (!alien.isDestroyed()) {
        nonDestroyedAliens.add(alien);
      } else {
        achievements.alienDestroyed(alien.character());
      }
    }

    this.aliens = nonDestroyedAliens;
    this.activeAliens = currentActiveAliens;
    this.visibleAliens = currentVisibleAliens;

    // have all aliens finished pass or been destroyed
    if (subWaveState == PLAYING && aliens.size() == 0) {
      subWaveState = DESTROYED;
    } else if (subWaveState == PLAYING && aliens.size() == finishedAliens) {
      subWaveState = END_OF_PASS;
    }

    // handles complex sub-wave scenarios
    updateSubWaveState();
  }

  @Override
  public void spawnAliens(List<IAlien> spawnedAliens) {
    // add spawned aliens to temporary queue.
    // we can't add them directly to the main alien list as this would cause a
    // concurrent modification exception within the animation loop.
    this.spawnedAliens.addAll(spawnedAliens);
  }

  @Override
  public void setUpWave(int wave) {
        /*
          asks wave manager to set-up next level. this is an asynchronous task
          that can be time-consuming so is run in the background.

          This method will return immediately but the wave will not be
          ready until waveManager.isWaveReady() responds with true.
         */
    waveManager.setUpWave(wave);
  }

  @Override
  public boolean isWaveReady() {
    if (waveManager.isWaveReady() && waveManager.hasNext()) {
      createAlienSubWave(waveManager.next());
      return true;
    }
    return false;
  }

  @Override
  public boolean isWaveComplete() {
    return (subWaveState == WAVE_COMPLETE);
  }

  @Override
  public IAlien chooseActiveAlien() {

    // refresh active alien list
    List<IAlien> currentActiveAliens = new ArrayList<>();
    for (IAlien alien : aliens) {
      if (alien.isActive()) {
        currentActiveAliens.add(alien);
      }
    }
    this.activeAliens = currentActiveAliens;

    // if no aliens are active return null
    if (activeAliens.size() == 0) {
      return null;
    }

    // choose a random active alien
    int index = (int) (random() * activeAliens.size());
    return activeAliens.get(index);
  }

  /**
   * Initialises a new sub alien sub-wave
   */
  private void createAlienSubWave(final SubWave subWave) {
    this.aliens = subWave.getAliens();
    this.repeatedSubWave = subWave.isWaveRepeated();
    this.subWaveState = PLAYING;

    List<IAlien> currentActiveAliens = new ArrayList<>();
    List<IAlien> currentVisibleAliens = new ArrayList<>();

    for (IAlien alien : aliens) {
      if (alien.isActive()) {
        currentActiveAliens.add(alien);
      }
      if (alien.isVisible()) {
        currentVisibleAliens.add(alien);
      }
    }

    this.activeAliens = currentActiveAliens;
    this.visibleAliens = currentVisibleAliens;
  }

  /**
   * This method performs the following functionality:
   * <p>
   * 1) Restarts a repeated sub-wave if all aliens are finished but not all were destroyed.
   * <p>
   * 2) Sets-up the next sub-wave if the current one is finished and another sub-wave is available.
   * <p>
   * 3) Sets the state to WAVE_COMPLETE if all sub-waves are completed.
   */
  private void updateSubWaveState() {
        /*
          if no aliens left then decide what action to take.
         */
    if (subWaveState == DESTROYED || subWaveState == END_OF_PASS) {
      /*
       * Have we reached the end of a sub-wave that repeats?
       */
      if (repeatedSubWave && subWaveState == END_OF_PASS) {
        /*
         * if there are finished aliens that were not destroyed and
         * the current sub-wave should be repeated (until all aliens are
         * destroyed) then reset the aliens and repeat the sub-wave.
         */
        List<IResettableAlien> aliensToRepeat = new ArrayList<>();

        Float minDelay = null;
        for (IAlien anAlien : aliens) {
          /*
           * if aliens are resettable then we want to restart
           * them immediately and not have to wait for their initial delay
           * to expire. find the lowest time delay and reduce all
           * aliens by this offset so the first alien starts
           * immediately.
           */
          if (anAlien instanceof IResettableAlien) {
            IResettableAlien alienWithPath = (IResettableAlien) anAlien;
            aliensToRepeat.add(alienWithPath);

            float timeDelay = alienWithPath.getTimeDelay();

            if (minDelay == null || timeDelay < minDelay) {
              minDelay = timeDelay;
            }
          }
        }

        /*
         * reduce offset of all repeated resettable aliens by minimum
         * offset. causes first alien to start immediately.
         */
        for (IResettableAlien anAlienToRepeat : aliensToRepeat) {
          anAlienToRepeat.reset(minDelay);
        }

        this.subWaveState = PLAYING;
        Log.i(TAG, "Wave: Reset SubWave");
      }
      /*
       * if there is another sub-wave, get it and assign new list to
       * aliens.
       */
      else if (waveManager.hasNext()) {
        createAlienSubWave(waveManager.next());
        Log.i(TAG, "Wave: Next SubWave");
      }
      /*
       * otherwise wave is finished. there are no more sub-waves.
       * we can advance to next wave.
       */
      else {
        this.subWaveState = WAVE_COMPLETE;
        Log.i(TAG, "Wave: All SubWaves Complete");
      }
    }
  }

  public enum SubWaveState {
    IDLE, PLAYING, END_OF_PASS, DESTROYED, WAVE_COMPLETE
  }
}
