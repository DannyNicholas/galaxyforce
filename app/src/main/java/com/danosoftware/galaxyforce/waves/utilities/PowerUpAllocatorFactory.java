package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;

import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.MAX_LIVES;

/**
 * Responsible for creating power-up allocators.
 * <p>
 * The power-up factory also tracks the number of life power-ups allocated.
 * This ensures lives allocated can not exceed the maximum number of lives possible.
 */
public class PowerUpAllocatorFactory {

    // total extra life power-ups remaining that can be allocated
    private int totalExtraLivesAvailableToAllocate;

    private final GameModel model;

    public PowerUpAllocatorFactory(final GameModel model) {
        this.model = model;
        this.totalExtraLivesAvailableToAllocate = 0;
    }

    /**
     * Reset the power-up allocation factory for a new wave.
     * All sub-waves for a wave are created at the same time so the factory
     * needs to know at the start of each wave how many life power-ups can
     * be allocated across all sub-waves (based on the current lives remaining).
     */
    void newWave() {
        this.totalExtraLivesAvailableToAllocate = MAX_LIVES - model.getLives() - 1;
    }

    /**
     * Create an allocator to provide power-ups.
     * Allocator will only supply power-ups up to the maximum lives possible.
     */
    public PowerUpAllocator createAllocator(
            final List<PowerUpType> powerUps,
            final int numberOfAliens) {

        PowerUpAllocator powerUpAllocator = new PowerUpAllocator(
                powerUps,
                numberOfAliens,
                totalExtraLivesAvailableToAllocate);

        // deduct actual life power-ups allocated from total available
        totalExtraLivesAvailableToAllocate -= powerUpAllocator.lifePowerUpsAllocated();

        return powerUpAllocator;
    }
}
