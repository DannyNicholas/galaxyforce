package com.danosoftware.galaxyforce.sprites.game.interfaces;

/**
 * Enum that represents the state of moving sprites
 */
public enum SpriteState
{

    /**
     * Sprite is not yet active and so should not be manipulated. This could be
     * an alien that has been set-up but not yet started it's move.
     */
    INACTIVE,

    /**
     * Sprite is active and can be manipulated. This could be an alien that has
     * now started it's move.
     */
    ACTIVE,

    /**
     * Sprite has finished it's current pass (e.g. alien reached end of path)
     * but has not been destroyed. An alien that has finished it's pass may be
     * re-set and start again in cases where a wave is repeated until all aliens
     * are destroyed.
     */
    FINISHED_PASS,

    /**
     * Sprite has been hit and is now in the process of exploding. During this
     * state the sprite is still on screen but is unlikely to interact with
     * other sprites. Missiles won't explode so will by-pass this state.
     */
    EXPLODING,

    /**
     * Sprite is now completely destroyed. Sprite should not be displayed on
     * screen and must not interact with other sprites.
     */
    DESTROYED

}
