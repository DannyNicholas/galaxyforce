package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;

import java.util.List;

public interface IBaseSprite extends ICollidingSprite {

    void onHitBy(IAlien alien);

    void onHitBy(IAlienMissile missile);

    void collectPowerUp(IPowerUp powerUp);

    List<ISprite> allSprites();
}
