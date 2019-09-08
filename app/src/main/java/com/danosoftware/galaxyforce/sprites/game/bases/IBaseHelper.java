package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;

interface IBaseHelper extends IBase {

    BaseMissilesDto fire(BaseMissileType baseMissileType);

    void addSynchronisedShield(float syncTime);

    void removeShield();
}
