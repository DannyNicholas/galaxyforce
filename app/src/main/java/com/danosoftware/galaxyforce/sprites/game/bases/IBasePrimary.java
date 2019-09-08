package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseLean;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide;

import java.util.List;

public interface IBasePrimary extends IBase {

    /**
     * Set the wanted target position of the base.
     * Base will gradually move to this position.
     */
    void moveTarget(int targetX, int targetY);

    void helperExploding(HelperSide side);

    void helperRemoved(HelperSide side);

    void helperCreated(HelperSide side, IBaseHelper helper);

    List<IBase> activeBases();

    BaseLean getLean();

    void setLean(BaseLean lean);

    void addShield(float timeActive);
}
