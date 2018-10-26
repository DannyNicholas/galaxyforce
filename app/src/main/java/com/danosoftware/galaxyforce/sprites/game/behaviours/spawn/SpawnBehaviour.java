package com.danosoftware.galaxyforce.sprites.game.behaviours.spawn;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

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
    public void spawn(IAlien alien);
}
