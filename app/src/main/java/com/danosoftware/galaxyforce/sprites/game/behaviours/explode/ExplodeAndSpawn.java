package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;

/**
 * Implementation of explosion behaviour that will trigger aliens to spawn when the explosion
 * happens.
 */
public class ExplodeAndSpawn implements ExplodeBehaviour {

  // explosion behaviour we are wrapping
  private final ExplodeBehaviour exploder;

  // spawner that will trigger on explosion
  private final SpawnBehaviour spawner;

  public ExplodeAndSpawn(
      final ExplodeBehaviour exploder,
      final SpawnBehaviour spawner) {
    this.exploder = exploder;
    this.spawner = spawner;
  }

  @Override
  public void startExplosion(IAlien alien) {
    exploder.startExplosion(alien);
    spawner.spawn(alien);
  }

  @Override
  public void startExplosionFollower(IAlienFollower alien) {
    startExplosion(alien);
  }

  @Override
  public SpriteDetails getExplosion(float deltaTime) {
    return exploder.getExplosion(deltaTime);
  }

  @Override
  public boolean finishedExploding() {
    return exploder.finishedExploding();
  }
}
