package com.danosoftware.galaxyforce.waves.utilities;

import android.util.Log;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Test that power-up allocator behaves as expected when allocating a list of power-ups
 * across a wave of aliens.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class PowerUpAllocatorTest {

    final static Logger logger = LoggerFactory.getLogger(PowerUpAllocatorTest.class);

    private PowerUpAllocator powerUpAllocator;

    @Before
    public void setUp() {
        // mock any static android logging
        mockStatic(Log.class);
    }

    /**
     * Allocate power-ups across the same number of aliens.
     * Each alien shoud have a power-up.
     */
    @Test
    public void shouldAllocateOnePowerUpPerAlien() {

        // create one alien per power-up
        List<PowerUpType> powerUpTypes = Arrays.asList(PowerUpType.ENERGY, PowerUpType.ENERGY, PowerUpType.LIFE, PowerUpType.LIFE, PowerUpType.MISSILE_FAST);
        int numberOfAliens = powerUpTypes.size();
        powerUpAllocator = new PowerUpAllocator(powerUpTypes, numberOfAliens, 3);

        shouldAllocateExpectedPowerUps(numberOfAliens, powerUpTypes);
    }

    /**
     * Allocate power-ups across twice as many aliens.
     */
    @Test
    public void shouldAllocateAllPowerUpsAcrossManyAliens() {

        // create twice as many aliens as power-ups
        List<PowerUpType> powerUpTypes = Arrays.asList(PowerUpType.ENERGY, PowerUpType.ENERGY, PowerUpType.LIFE, PowerUpType.LIFE, PowerUpType.MISSILE_FAST);
        int numberOfAliens = powerUpTypes.size() * 2;
        powerUpAllocator = new PowerUpAllocator(powerUpTypes, numberOfAliens, 3);

        shouldAllocateExpectedPowerUps(numberOfAliens, powerUpTypes);
    }

    /**
     * Attempt to allocate an empty list of power-ups.
     * Should not allocate any power-ups.
     */
    @Test
    public void shouldNotAllocateAnyPowerUps() {

        // create empty list of power-ups
        List<PowerUpType> powerUpTypes = new ArrayList<>();
        int numberOfAliens = 10;
        powerUpAllocator = new PowerUpAllocator(powerUpTypes, numberOfAliens, 3);

        shouldAllocateExpectedPowerUps(numberOfAliens, powerUpTypes);
    }

    /**
     * Throw exception if attempting to add more power-ups than aliens.
     */
    @Test(expected = GalaxyForceException.class)
    public void shouldThrowExceptionWhenMorePowerUpsThanAliens() {

        // should fail when trying to allocate 5 power-ups across 4 aliens
        List<PowerUpType> powerUpTypes = Arrays.asList(PowerUpType.ENERGY, PowerUpType.ENERGY, PowerUpType.LIFE, PowerUpType.LIFE, PowerUpType.MISSILE_FAST);
        new PowerUpAllocator(powerUpTypes, 4, 3);
    }

    /**
     * Always return null if attempting to call allocate() even after all aliens have been allocated.
     */
    @Test
    public void shouldReturnNullWhenAllocateCalledTooManyTimes() {

        // create one alien per power-up
        List<PowerUpType> powerUpTypes = Arrays.asList(PowerUpType.ENERGY, PowerUpType.ENERGY, PowerUpType.LIFE, PowerUpType.LIFE, PowerUpType.MISSILE_FAST);
        int numberOfAliens = powerUpTypes.size();
        powerUpAllocator = new PowerUpAllocator(powerUpTypes, numberOfAliens, 3);

        shouldAllocateExpectedPowerUps(numberOfAliens, powerUpTypes);

        // attempt to allocate more power-ups - should return null
        for (int i = 0; i < 20; i++) {
            PowerUpType powerUp = powerUpAllocator.allocate();
            assertThat(powerUp, nullValue());
        }
    }

    /**
     * Should only allocate lifes up to a maximum
     */
    @Test
    public void shouldOnlyAllocateUpToMaximumLives() {

        // create one alien per power-up
        List<PowerUpType> powerUpTypes = Arrays.asList(PowerUpType.LIFE, PowerUpType.LIFE, PowerUpType.LIFE, PowerUpType.LIFE, PowerUpType.LIFE);
        int numberOfAliens = powerUpTypes.size();
        powerUpAllocator = new PowerUpAllocator(powerUpTypes, numberOfAliens, 3);

        // allocate power-ups to aliens
        List<PowerUpType> allAllocatedPowerUps = new ArrayList<>();
        for (int i = 0; i < numberOfAliens; i++) {
            PowerUpType powerUp = powerUpAllocator.allocate();
            if (powerUp != null) {
                allAllocatedPowerUps.add(powerUp);
                assertThat(powerUp, equalTo(PowerUpType.LIFE));
            }
        }
        assertThat(allAllocatedPowerUps.size(), equalTo(2));
    }

    /**
     * Check that all supplied power-ups were allocated to the aliens in the wave.
     *
     * @param numberOfAliens - number of aliens in wave
     * @param powerUpTypes   - list of power-ups to be allocated across aliens
     */
    private void shouldAllocateExpectedPowerUps(
            final int numberOfAliens,
            final List<PowerUpType> powerUpTypes) {

        // count of each different power-up supplied
        final Map<PowerUpType, Integer> powerUpCount = createPowerUpMap(powerUpTypes);

        // allocate power-ups to aliens
        List<PowerUpType> allAllocatedPowerUps = new ArrayList<>();
        for (int i = 0; i < numberOfAliens; i++) {
            PowerUpType powerUp = powerUpAllocator.allocate();
            if (powerUp != null) {
                allAllocatedPowerUps.add(powerUp);
            }
        }

        // create a count of each power-up allocated
        Map<PowerUpType, Integer> foundPowerUpCount = createPowerUpMap(allAllocatedPowerUps);

        logger.info(allAllocatedPowerUps.toString());
        assertThat(allAllocatedPowerUps.size(), equalTo(powerUpTypes.size()));
        assertThat(foundPowerUpCount, equalTo(powerUpCount));
    }

    /**
     * Create a map of power-ups and instance counts.
     */
    private Map<PowerUpType, Integer> createPowerUpMap(List<PowerUpType> powerUpTypes) {
        Map<PowerUpType, Integer> powerUpCount = new HashMap<>();
        for (PowerUpType powerUp : powerUpTypes) {
            int count = powerUpCount.getOrDefault(powerUp, 0);
            powerUpCount.put(powerUp, count + 1);
        }

        return powerUpCount;
    }
}
