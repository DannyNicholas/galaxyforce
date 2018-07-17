package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;

interface IBaseMainSprite extends IBaseSprite {

    void moveBase(float weightingX, float weightingY, float deltaTime);

    void helperDestroyed(HelperSide side);
}
