package com.danosoftware.galaxyforce.services.achievements;

import com.danosoftware.galaxyforce.R;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.services.googleplay.GooglePlayServices;
import com.danosoftware.galaxyforce.waves.AlienCharacter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Typical achievements:
 *
 * completing specific waves
 * completing specific waves with no lives lost
 * completing a set number of waves without losing a life
 * total waves completed in a single game
 * failing to complete a single wave
 *
 * number or types of power ups collected
 * number or types of aliens destroyed
 */
public class AchievementService {

    // map of waves completed to unlocked achievements
    private static final Map<Integer, Integer> completedWaveAchievementsMap = new HashMap<>();
    static {
        completedWaveAchievementsMap.put(
                1,
                R.string.achievement_smashing_start);
        completedWaveAchievementsMap.put(
                12,
                R.string.achievement_exit_the_dragon);
        completedWaveAchievementsMap.put(
                24,
                R.string.achievement_book_basher);
    }

    // map of waves completed in one life to unlocked achievements
    private static final Map<Integer, Integer> oneLifeCompletedWaveAchievementsMap = new HashMap<>();
    static {
        oneLifeCompletedWaveAchievementsMap.put(
                12,
                R.string.achievement_dragon_buster);
        oneLifeCompletedWaveAchievementsMap.put(
                24,
                R.string.achievement_book_basher_one_life);
    }


    // map of power-up types to incremental achievements
    private static final Map<PowerUpType, Integer> powerUpAchievementsMap = new EnumMap<>(PowerUpType.class);
    static {
        powerUpAchievementsMap.put(
                PowerUpType.LIFE,
                R.string.achievement_collect_10_life_powerups);
        powerUpAchievementsMap.put(
                PowerUpType.MISSILE_FAST,
                R.string.achievement_collect_10_fast_missile_powerups);
        powerUpAchievementsMap.put(
                PowerUpType.MISSILE_BLAST,
                R.string.achievement_collect_10_blast_powerups);
        powerUpAchievementsMap.put(
                PowerUpType.MISSILE_GUIDED,
                R.string.achievement_collect_10_guided_missile_powerups);
        powerUpAchievementsMap.put(
                PowerUpType.MISSILE_PARALLEL,
                R.string.achievement_collect_10_parallel_missile_powerups);
        powerUpAchievementsMap.put(
                PowerUpType.MISSILE_SPRAY,
                R.string.achievement_collect_10_spray_missile_powerups);
        powerUpAchievementsMap.put(
                PowerUpType.MISSILE_LASER,
                R.string.achievement_collect_10_laser_powerups);
        powerUpAchievementsMap.put(
                PowerUpType.SHIELD,
                R.string.achievement_collect_10_shield_powerups);
        powerUpAchievementsMap.put(
                PowerUpType.HELPER_BASES,
                R.string.achievement_collect_10_helper_base_powerups);
    }

    // map of aliens to incremental achievements
    private static final Map<AlienCharacter, Integer> aliensAchievementsMap = new EnumMap<>(AlienCharacter.class);
    static {
        aliensAchievementsMap.put(
                AlienCharacter.OCTOPUS,
                R.string.achievement_destroy_25_octopus);
    }

    // achievements that have been earned but not yet unlocked
    // this is a list to maintain order of achievements earned
    private final List<Integer> unclaimedAchievements;

    private final GooglePlayServices playService;

    private final Map<PowerUpType, Integer> powerUpsCollected;
    private final Map<AlienCharacter, Integer> aliensDestroyed;
    private int sequentialWavesCompletedWithoutLosingALife;
    private int totalWavesCompletedInCurrentGame;

    public AchievementService(final GooglePlayServices playService) {
        this.playService = playService;
        this.powerUpsCollected = new EnumMap<>(PowerUpType.class);
        this.aliensDestroyed = new EnumMap<>(AlienCharacter.class);
        this.sequentialWavesCompletedWithoutLosingALife = 0;
        this.totalWavesCompletedInCurrentGame = 0;
        this.unclaimedAchievements = new ArrayList<>();
    }

    /**
     * Request any achievements for a completed wave.
     *
     * Achievements may be generated for completing specific
     * waves and for completing waves with no lives lost.
     */
    public void waveCompleted(CompletedWaveAchievements completedWaveAchievements) {
        // claim any unclaimed achievements
        unlockPendingAchievements();

        totalWavesCompletedInCurrentGame++;
        if (completedWaveAchievements.isNolivesLostInWave()) {
            sequentialWavesCompletedWithoutLosingALife++;
        } else {
            sequentialWavesCompletedWithoutLosingALife = 0;
        }

        // trigger unlocks based on total waves completed in one game
        switch(totalWavesCompletedInCurrentGame) {
            case 3:
                unlockAchievement(R.string.achievement_complete_3_waves_in_one_game);
                break;
            case 5:
                unlockAchievement(R.string.achievement_complete_5_waves_in_one_game);
                break;
        }

        // trigger unlocks based on sequential waves completed without losing a life
        switch(sequentialWavesCompletedWithoutLosingALife) {
            case 1:
                unlockAchievement(R.string.achievement_complete_1_wave_without_losing_a_life);
                break;
            case 3:
                unlockAchievement(R.string.achievement_complete_3_waves_without_losing_a_life);
                break;
            case 5:
                unlockAchievement(R.string.achievement_complete_5_waves_without_losing_a_life);
                break;
        }

        // trigger unlocks based on waves completed
        final int completedWave = completedWaveAchievements.getWave();
        if (completedWaveAchievementsMap.containsKey(completedWave)) {
            unlockAchievement(completedWaveAchievementsMap.get(completedWave));
        }

        // trigger unlocks based on waves completed in one life
        final boolean completedWaveInOneLife = completedWaveAchievements.isNolivesLostInWave();
        if (completedWaveInOneLife && oneLifeCompletedWaveAchievementsMap.containsKey(completedWave)) {
            unlockAchievement(oneLifeCompletedWaveAchievementsMap.get(completedWave));
        }

        // trigger incremental achievements due to power-ups collected
        // and aliens destroyed
        submitPendingPowerUpsCollected();
        submitPendingAliensDestroyed();
    }

    /**
     * Request any pending achievements when game is over.
     */
    public void gameOver() {
        if (totalWavesCompletedInCurrentGame == 0) {
            // no waves completed in a single game
            unlockAchievement(R.string.achievement_that_was_tricky);
        }

        submitPendingPowerUpsCollected();
        submitPendingAliensDestroyed();
    }

    /**
     * Register a collected power-up.
     *
     * Power-up collected won't trigger any achievements until either
     * the wave is completed or game over.
     */
    public void powerUpCollected(PowerUpType powerUp) {
        final int collected = powerUpsCollected.containsKey(powerUp)
                ? powerUpsCollected.get(powerUp) + 1
                : 1;
        powerUpsCollected.put(powerUp, collected);
    }

    /**
     * Register a destroyed alien.
     *
     * Aliens destroyed won't trigger any achievements until either
     * the wave is completed or game over.
     */
    public void alienDestroyed(AlienCharacter alien) {
        final int destroyed = aliensDestroyed.containsKey(alien)
                ? aliensDestroyed.get(alien) + 1
                : 1;
        aliensDestroyed.put(alien, destroyed);
    }

    /**
     * Increment achievements for each power-up collected
     */
    private void submitPendingPowerUpsCollected() {
        int totalPowerUpsCollected = 0;
        for (Iterator<PowerUpType> powerUps = powerUpsCollected.keySet().iterator(); powerUps.hasNext();) {
            final PowerUpType powerUp = powerUps.next();
            final int collected = powerUpsCollected.get(powerUp);

            // increment achievement of specific power-up type
            if (powerUpAchievementsMap.containsKey(powerUp) &&
                    playService.incrementAchievement(
                        powerUpAchievementsMap.get(powerUp),
                        collected)) {
                powerUps.remove();
                totalPowerUpsCollected += collected;
            }
        }

        // increment generic power-up achievements
        if (totalPowerUpsCollected > 0) {
            playService.incrementAchievement(
                    R.string.achievement_hoarder,
                    totalPowerUpsCollected);
        }
    }

    /**
     * Increment achievements for each alien destroyed
     */
    private void submitPendingAliensDestroyed() {
        int totalAliensDestroyed = 0;
        for (Iterator<AlienCharacter> aliens = aliensDestroyed.keySet().iterator(); aliens.hasNext();) {
            final AlienCharacter alien = aliens.next();
            final int destroyed = aliensDestroyed.get(alien);

            // increment achievement of specific alien destroyed.
            // remove from map if successful
            if (aliensAchievementsMap.containsKey(alien)
                    && playService.incrementAchievement(
                            aliensAchievementsMap.get(alien),
                            destroyed)) {
                aliens.remove();
                totalAliensDestroyed += destroyed;
            }
        }

        // increment generic aliens destroyed achievements
        if (totalAliensDestroyed > 0) {
            playService.incrementAchievement(
                    R.string.achievement_destroy_25_aliens,
                    totalAliensDestroyed);
        }
    }

    /**
     * Unlock achievement.
     * If unlock fails, store in a list to allow re-try later.
     */
    private void unlockAchievement(final int achievementId) {
        if (!playService.unlockAchievement(achievementId)
                && !unclaimedAchievements.contains(achievementId)) {
            unclaimedAchievements.add(achievementId);
        }
    }

    /**
     * Unlock all pending achievements from list.
     * Remove from list if successful.
     */
    private void unlockPendingAchievements() {
        for (Iterator<Integer> achievements = unclaimedAchievements.iterator(); achievements.hasNext();) {
            if(playService.unlockAchievement(achievements.next())) {
                achievements.remove();
            }
        }
    }
}
