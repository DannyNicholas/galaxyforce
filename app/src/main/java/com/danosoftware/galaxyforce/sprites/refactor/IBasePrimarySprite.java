package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;

import java.util.List;

public interface IBasePrimarySprite extends IBaseSprite {

    void moveBase(float weightingX, float weightingY, float deltaTime);

    void helperExploding(HelperSide side);

    void helperRemoved(HelperSide side);

    void helperCreated(HelperSide side, IBaseHelperSprite helper);

    List<IBaseSprite> activeBases();
}
