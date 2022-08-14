package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import java.util.List;

interface IBaseHelper extends IBase {

  BaseMissilesDto fire(BaseMissileType baseMissileType);

  void addSynchronisedShield(float syncTime);

  void removeShield();

  List<ISprite> allSprites();
}
