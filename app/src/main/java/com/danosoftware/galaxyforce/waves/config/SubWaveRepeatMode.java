package com.danosoftware.galaxyforce.waves.config;

/**
 * Describes the sub-wave repeated mode.
 */
public enum SubWaveRepeatMode {

    // repeat the subwave until all aliens are destroyed
    REPEAT_UNTIL_DESTROYED,

    // do not repeat the subwave once path is completed.
    SINGLE_PASS
}