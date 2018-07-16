package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;

interface IBaseHelperSprite extends IBaseSprite {

    BaseMissileBean fire(BaseMissileType baseMissileType);

    void addShield(float syncTime);

    void removeShield();
}
