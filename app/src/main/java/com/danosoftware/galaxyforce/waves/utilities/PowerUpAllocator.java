package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import static com.danosoftware.galaxyforce.constants.GameConstants.MAX_LIVES;

/**
 * Responsible for allocating a list of power-ups to a wave of aliens.
 *
 * After construction, each alien must call the allocate() method and will
 * either receive a power-up or a null.
 *
 * Allocator guarantees that all power-ups will be allocated by the time
 * the last alien has requested an allocation.
 *
 * Since each alien can only be allocated 1 or 0 power-ups, if an allocator
 * is constructed with more power-ups than aliens, then an exception will
 * be thrown.
 */
public class PowerUpAllocator {

    private final Random rand = new Random();

    // stack holding unallocated power-ups
    private final Deque<PowerUpType> powerUps;

    // how many aliens remaining that will ask for power-up allocation
    private int aliensToAllocate;

    /**
     * Create an allocator responsible for allocating power-ups.
     *
     * @param powerUps - power-ups to allocate
     * @param aliens - number of aliens in wave
     */
    public PowerUpAllocator(
            final List<PowerUpType> powerUps,
            final int aliens,
            final int livesRemaining) {

        // limit extra life power-ups to avoid exceeding max lives
        int additionLivesAllowed = MAX_LIVES - livesRemaining;
        List<PowerUpType> filteredPowerUps = new ArrayList<>();
        for (PowerUpType powerUp : powerUps) {
            if (powerUp != PowerUpType.LIFE || additionLivesAllowed > 0) {
                filteredPowerUps.add(powerUp);
                if (powerUp == PowerUpType.LIFE) {
                    additionLivesAllowed--;
                }
            }
        }

        // randomise list of power-ups
        Collections.shuffle(filteredPowerUps);

        // add power-ups to deque (stack)
        this.powerUps = new ArrayDeque<>();
        for (PowerUpType powerUp : filteredPowerUps) {
            this.powerUps.push(powerUp);
        }

        this.aliensToAllocate = aliens;

        if(filteredPowerUps.size() > aliensToAllocate) {
            throw new GalaxyForceException(
                    "Attempted to allocate " + powerUps.size() + " power-ups to only " + aliensToAllocate + " aliens."
            );
        }
    }

    /**
     * Request a power-up allocation.
     *
     * Will either receive a power-up or a null (no power-up).
     */
    public PowerUpType allocate() {
        PowerUpType allocated = null;

        // if all expected aliens have been allocated, default to retuning nulls
        if (aliensToAllocate == 0) {
            return null;
        }

        // calculate chances of receiving a power-up based on the power-ups remaining
        // and number of aliens remaining.
        //
        // if the number of aliens remaining equals the number of power-ups remaining, then
        // the chances of receiving a power-up are 100%
        float chanceOfAllocation = (float) powerUps.size() / aliensToAllocate;
        if (rand.nextFloat() < chanceOfAllocation) {
            allocated = powerUps.pop();
        }
        aliensToAllocate--;
        return allocated;
    }
}
