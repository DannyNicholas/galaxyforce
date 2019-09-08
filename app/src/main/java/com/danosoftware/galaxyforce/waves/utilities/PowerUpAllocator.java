package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Responsible for allocating a list of power-ups to a wave of aliens.
 * <p>
 * After construction, each alien must call the allocate() method and will
 * either receive a power-up or a null.
 * <p>
 * Allocator guarantees that all power-ups will be allocated by the time
 * the last alien has requested an allocation.
 * <p>
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

    private int lifePowerUpsAllocated;

    /**
     * Create an allocator responsible for allocating power-ups.
     *
     * @param powerUps - power-ups to allocate
     * @param aliens   - number of aliens in wave
     */
    PowerUpAllocator(
            final List<PowerUpType> powerUps,
            final int aliens,
            final int extraLifePowerUpsAllowed) {

        this.lifePowerUpsAllocated = 0;

        // limit extra life power-ups to avoid exceeding max lives
        List<PowerUpType> filteredPowerUps = new ArrayList<>();
        for (PowerUpType powerUp : powerUps) {
            if (powerUp != PowerUpType.LIFE || lifePowerUpsAllocated < extraLifePowerUpsAllowed) {
                filteredPowerUps.add(powerUp);
                if (powerUp == PowerUpType.LIFE) {
                    lifePowerUpsAllocated++;
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

        if (filteredPowerUps.size() > aliensToAllocate) {
            throw new GalaxyForceException(
                    "Attempted to allocate " + powerUps.size() + " power-ups to only " + aliensToAllocate + " aliens."
            );
        }
    }

    /**
     * How many life power-ups have been allocated?
     */
    int lifePowerUpsAllocated() {
        return lifePowerUpsAllocated;
    }

    /**
     * Request a power-up allocation.
     * <p>
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
