package com.danosoftware.galaxyforce.sprites.game.aliens;

/**
 * Implemented by aliens whose behaviour/movement is based
 * on the position of another alien they follow.
 */
public interface IAlienFollower extends IAlien {

    /**
     * Update our alien based on the alien we are following.
     *
     * @param alienFollowed - alien being followed
     * @param deltaTime     - time delta since last update
     */
    void follow(IAlien alienFollowed, float deltaTime);
}
