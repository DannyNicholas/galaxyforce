package com.danosoftware.galaxyforce.sprites.game.behaviours.spawn;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

public interface SpawnBehaviour {

  /**
   * Returns true if alien is ready to spawn.
   */
  boolean readyToSpawn(float deltaTime);

  /**
   * Spawn a new alien.
   */
  void spawn(IAlien alien);
}
