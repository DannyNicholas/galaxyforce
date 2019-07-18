package com.danosoftware.galaxyforce.enumerations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum AlienSpeed {

    VERY_SLOW(new AlienSpeedGenerator() {
        @Override
        public Speed getSpeed() {
            return Speed.VERY_SLOW;
        }
    }),
    SLOW(new AlienSpeedGenerator() {
        @Override
        public Speed getSpeed() {
            return Speed.SLOW;
        }
    }),
    MEDIUM(new AlienSpeedGenerator() {
        @Override
        public Speed getSpeed() {
            return Speed.MEDIUM;
        }
    }),
    FAST(new AlienSpeedGenerator() {
        @Override
        public Speed getSpeed() {
            return Speed.FAST;
        }
    }),
    VERY_FAST(new AlienSpeedGenerator() {
        @Override
        public Speed getSpeed() {
            return Speed.VERY_FAST;
        }
    }),
    RANDOM(new AlienSpeedGenerator() {
        @Override
        public Speed getSpeed() {
            return Speed.VALUES.get(RANDOMIZER.nextInt(Speed.VALUES.size()));
        }
    });

    private static final Random RANDOMIZER = new Random();
    private final AlienSpeedGenerator generator;

    AlienSpeed(final AlienSpeedGenerator generator) {
        this.generator = generator;
    }

    public int getSpeedInPixelsPerSeconds() {
        final Speed speed = generator.getSpeed();
        return speed.getPixelSpeed();
    }

    interface AlienSpeedGenerator {
        Speed getSpeed();
    }

    // inner enum to hold speed in pixels per second
    private enum Speed {
        VERY_SLOW(60),
        SLOW(2 * 60),
        MEDIUM(3 * 60),
        FAST(4 * 60),
        VERY_FAST(5 * 60);

        private static final List<Speed> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private final int pixelSpeed;

        Speed(final int pixelSpeed) {
            this.pixelSpeed = pixelSpeed;
        }

        public int getPixelSpeed() {
            return pixelSpeed;
        }
    }
}
