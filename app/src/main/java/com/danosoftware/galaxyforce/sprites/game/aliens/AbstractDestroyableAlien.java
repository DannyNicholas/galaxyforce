package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviour;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienCharacter;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

/**
 * Abstract alien that can be destroyed when alien moves off-screen.
 */
public abstract class AbstractDestroyableAlien extends AbstractAlien {

    private final int startingX;
    private final int startingY;
    private boolean restartImmediately;

    // how many seconds to delay before alien activates
    private float timeDelayStart;

    protected AbstractDestroyableAlien(
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
            final SpinningBehaviour spinningBehaviour,
            final float delayStart,
            final boolean restartImmediately) {

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

        waiting();
        this.startingX = x;
        this.startingY = y;
        this.restartImmediately = restartImmediately;
        this.timeDelayStart = delayStart;
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        if (isActive()) {
            if (offScreenAnySide(this)) {
                if (restartImmediately) {
                    move(startingX, startingY);
                } else {
                    destroy();
                }
            }
        } else if (isWaiting()) {
            // countdown until activation time
            if (timeDelayStart > 0) {
                timeDelayStart -= deltaTime;
            }
            // otherwise activate alien. can only happen once!
            else {
                activate();
            }
        }
    }
}
