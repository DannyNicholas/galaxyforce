package com.danosoftware.galaxyforce.sprites.game.behaviours.spawn;

import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;

public interface SpawnBehaviour
{
    /**
     * Returns true if alien is ready to spawn.
     * 
     * @param deltaTime
     */
    public boolean readyToSpawn(float deltaTime);

    /**
     * Spawn a new alien.
     * 
     * @param alien
     */
    public void spawn(SpriteAlien alien);
}
