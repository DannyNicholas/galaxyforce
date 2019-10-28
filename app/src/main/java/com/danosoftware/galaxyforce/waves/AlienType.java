package com.danosoftware.galaxyforce.waves;

/**
 * Enum representing the different types of alien that can be created
 * (with their associated behaviours).
 * <p>
 * Used by AlienFactory to create instances of the wanted alien.
 */
public enum AlienType {

    // alien that follows a pre-determined path
    PATH,

    // alien that hunts the base
    HUNTER,

    // alien that hunts the base with followers
    HUNTER_FOLLOWABLE,

    // follower of a followable alien
    FOLLOWER,

    // alien that descends to the bottom of the screen
    DESCENDING,

    // alien that spins while executing it's underlying behaviour (e.g. SPINNING while DESCENDING)
    SPINNING,

    // alien that remains static and then explodes missiles
    EXPLODING,

    // alien that remains static
    STATIC,
}
