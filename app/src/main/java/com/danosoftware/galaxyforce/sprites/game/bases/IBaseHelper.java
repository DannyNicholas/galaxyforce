package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;

public interface IBaseHelper extends IBase {

    BaseMissileBean fire(BaseMissileType baseMissileType);

    void addShield(float syncTime);

    void removeShield();
}
