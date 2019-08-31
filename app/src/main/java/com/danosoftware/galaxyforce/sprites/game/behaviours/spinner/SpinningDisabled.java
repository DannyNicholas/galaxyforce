package com.danosoftware.galaxyforce.sprites.game.behaviours.spinner;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

public class SpinningDisabled implements SpinningBehaviour {

    @Override
    public boolean isSpinning() {
        return false;
    }

    @Override
    public void spin(IAlien alien, float deltaTime) {
        // no spinning action
    }
}
