package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.refactor.ICollidingSprite;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;

import java.util.List;

public interface IBase extends ICollidingSprite {

    void onHitBy(IAlien alien);

    void onHitBy(IAlienMissile missile);

    void collectPowerUp(IPowerUp powerUp);

    List<ISprite> allSprites();
}
