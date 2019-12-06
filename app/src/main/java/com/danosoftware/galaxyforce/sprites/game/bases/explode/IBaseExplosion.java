package com.danosoftware.galaxyforce.sprites.game.bases.explode;

import com.danosoftware.galaxyforce.sprites.common.ISprite;

public interface IBaseExplosion extends ISprite {

    void startExplosion();

    void animate(float deltaTime);

    boolean isFinished();
}
