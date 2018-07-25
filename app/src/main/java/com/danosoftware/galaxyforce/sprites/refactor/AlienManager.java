package com.danosoftware.galaxyforce.sprites.refactor;

import android.util.Log;

import com.danosoftware.galaxyforce.game.handlers.GamePlayHandlerRefactor;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlienWithPath;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.managers.WaveManager;

import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.sprites.refactor.AlienManager.AlienState.DESTROYED;
import static com.danosoftware.galaxyforce.sprites.refactor.AlienManager.AlienState.END_OF_PASS;
import static com.danosoftware.galaxyforce.sprites.refactor.AlienManager.AlienState.IDLE;
import static com.danosoftware.galaxyforce.sprites.refactor.AlienManager.AlienState.PLAYING;
import static com.danosoftware.galaxyforce.sprites.refactor.AlienManager.AlienState.WAVE_COMPLETE;

public class AlienManager implements IAlienManager {

    /* logger tag */
    private static final String TAG = AlienManager.class.getSimpleName();

    private enum AlienState {
        IDLE, PLAYING, END_OF_PASS, DESTROYED, WAVE_COMPLETE
    }

    ;

    // provides waves
    private final WaveManager waveManager;

    // state of current sub-wave
    private List<IAlien> aliens;
    private AlienState alienState;
    private boolean repeatedSubWave;


    public AlienManager(WaveManager waveManager) {
        this.waveManager = waveManager;
        this.aliens = new ArrayList<>();
        this.alienState = IDLE;
    }

    @Override
    public List<IAlien> activeAliens() {
        return null;
    }

    @Override
    public List<IAlien> allAliens() {
        return null;
    }

    @Override
    public void animate(float deltaTime) {
        int finishedAliens = 0;
        int destroyedAliens = 0;

        for (IAlien alien : aliens) {
            alien.animate(deltaTime);

            if (alien.isEndOfPass()) {
                finishedAliens++;
            }
            if (alien.isDestroyed()) {
                destroyedAliens++;
            }
        }

        // have all aliens finished pass or been destoyed
        if (aliens.size() == destroyedAliens) {
            alienState = DESTROYED;
        } else if (aliens.size() == finishedAliens) {
            alienState = END_OF_PASS;
        }

        // handles complex sub-wave scenarios
        updateSubWaveState();
    }

    @Override
    public void setUpWave(int wave) {
        /**
         * asks wave manager to set-up next level. this is an asynchronous task
         * that can be time-consuming so is run in the background.
         *
         * This method will return immediately but the wave will not be
         * ready until waveManager.isWaveReady() responds with true.
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
        boolean waveFinished = false;
    }

    /**
     * Initialises a new sub alien sub-wave
     */
    private void createAlienSubWave(final SubWave subWave) {
        this.aliens = subWave.getAliens();
        this.repeatedSubWave = subWave.isWaveRepeated();
        this.alienState = PLAYING;
    }


    /**
     * This method performs the following functionality:
     * <p>
     * 1) Restarts a repeated sub-wave if all aliens are finished but not all
     * were destroyed.
     * <p>
     * 2) Sets-up the next sub-wave if the current one is finished and
     * another sub-wave is available.
     * <p>
     * 3) Sets the state to WAVE_COMPLETE if all sub-waves are completed.
     */
    private void updateSubWaveState() {
        /**
         * if no aliens left then decide what action to take.
         */
        if (alienState == DESTROYED || alienState == END_OF_PASS) {
            /**
             * Have we reached the end of a sub-wave that repeats?
             */
            if (repeatedSubWave && alienState == END_OF_PASS) {
                /**
                 * if there are finished aliens that were not destroyed and
                 * the current sub-wave should be repeated (until all aliens are
                 * destroyed) then reset the aliens and repeat the sub-wave.
                 */
                List<SpriteAlienWithPath> aliensToRepeat = new ArrayList<>();

                Float minDelay = null;
                for (IAlien anAlien : aliens) {
                    /**
                     * if aliens have a path then we want to restart
                     * them immediately and not have to wait for their initial delay
                     * to expire. find the lowest time delay and reduce all
                     * aliens by this offset so the first alien starts
                     * immediately.
                     */
                    if (anAlien instanceof SpriteAlienWithPath) {
                        SpriteAlienWithPath alienWithPath = (SpriteAlienWithPath) anAlien;
                        aliensToRepeat.add(alienWithPath);

                        float timeDelay = alienWithPath.getTimeDelay();

                        if (minDelay == null || timeDelay < minDelay) {
                            minDelay = timeDelay;
                        }
                    }
                }

                /**
                 * reduce offset of all repeated aliens with path by minimum
                 * offset. causes first alien to start immediately.
                 */
                for (SpriteAlienWithPath anAlienToRepeat : aliensToRepeat) {
                    anAlienToRepeat.reset(minDelay);
                }

                this.alienState = PLAYING;
                Log.i(TAG, "Wave: Reset SubWave");
            }
            /**
             * if there is another sub-wave, get it and assign new list to
             * aliens.
             */
            else if (waveManager.hasNext()) {
                createAlienSubWave(waveManager.next());
//                this.subWave = waveManager.next();
//                aliens = subWave.getAliens();
//                repeatedSubWave = nextSubWave.isWaveRepeated();

                /**
                 * The aliens can jump positions when the wave is started if
                 * there has been a time delay between construction and starting
                 * moves. To avoid this jump, reset each alien so it re-starts
                 * it's path.
                 */
                //TODO is this reset really neccessary???
//                for (SpriteAlien anAlien : aliens) {
//                    if (anAlien.isActive())
//                    {
//                        anAlien.setVisible(true);
//                    }

//                    if (anAlien instanceof SpriteAlienWithPath)
//                    {
//                        SpriteAlienWithPath alienWithPath = (SpriteAlienWithPath) anAlien;
//                        alienWithPath.reset(0);
//                    }
//                }

                Log.i(TAG, "Wave: Next SubWave");
            }
            /**
             * otherwise wave is finished so we should advance to next wave
             */
            else {
                this.alienState = WAVE_COMPLETE;
                Log.i(TAG, "Wave: All SubWaves Complete");
            }
        }
    }
