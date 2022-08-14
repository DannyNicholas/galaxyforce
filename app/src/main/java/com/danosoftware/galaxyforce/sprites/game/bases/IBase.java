package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.sprites.common.ICollidingSprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;

public interface IBase extends ICollidingSprite {

  void onHitBy(IAlien alien);

  void onHitBy(IAlienMissile missile);

  void collectPowerUp(IPowerUp powerUp);

  boolean isActive();
}
