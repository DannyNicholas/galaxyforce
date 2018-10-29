package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide;

import java.util.List;

public interface IBasePrimary extends IBase {

    void moveBase(float weightingX, float weightingY, float deltaTime);

    void helperExploding(HelperSide side);

    void helperRemoved(HelperSide side);

    void helperCreated(HelperSide side, IBaseHelper helper);

    List<IBase> activeBases();
}
