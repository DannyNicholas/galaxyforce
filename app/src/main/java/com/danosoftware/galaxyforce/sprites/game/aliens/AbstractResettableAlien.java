package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviour;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienCharacter;

import static com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState.FINISHED_PASS;

/**
 * Abstract alien that can be reset and replayed when alien moves off-screen.
 */
public abstract class AbstractResettableAlien extends AbstractAlien implements IResettableAlien {

    protected AbstractResettableAlien(
            final AlienCharacter character,
            final Animation animation,
            final int x,
            final int y,
            final int energy,
            final FireBehaviour fireBehaviour,
            final PowerUpBehaviour powerUpBehaviour,
            final SpawnBehaviour spawnBehaviour,
            final HitBehaviour hitBehaviour,
            final ExplodeBehaviour explodeBehaviour,
            final SpinningBehaviour spinningBehaviour) {

        super(
                character,
                animation,
                x,
                y,
                energy,
                fireBehaviour,
                powerUpBehaviour,
                spawnBehaviour,
                hitBehaviour,
                explodeBehaviour,
                spinningBehaviour);
    }

    @Override
    public boolean isEndOfPass() {
        return state == FINISHED_PASS;
    }

    @Override
    public void endOfPass() {
        state = FINISHED_PASS;
    }
}
