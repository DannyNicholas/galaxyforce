package com.danosoftware.galaxyforce.sprites.game.behaviours.spawn;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

/**
 * Spawn behaviour implementation for aliens that do NOT spawn aliens.
 * 
 * Used to switch off alien spawning.
 */
public class SpawnDisabled implements SpawnBehaviour
{
    @Override
    public boolean readyToSpawn(float deltaTime)
    {
        // alien is never ready to spawn
        return false;
    }

    @Override
    public void spawn(IAlien alien)
    {
        // no implementation
    }
}
