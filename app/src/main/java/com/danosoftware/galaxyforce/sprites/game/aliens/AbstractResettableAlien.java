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
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

/**
 * Abstract alien that can be reset and replayed when alien moves off-screen.
 */
public abstract class AbstractResettableAlien extends AbstractAlien implements IResettableAlien {

    private final int startingX;
    private final int startingY;
    private boolean restartImmediately;

    /* how many seconds to delay before alien starts to follow path */
    private float timeDelayStart;

    /* original time delay before alien starts in cases where alien is reset */
    private final float originalTimeDelayStart;

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
        this.originalTimeDelayStart = delayStart;
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        if (isActive()) {
            if (offScreenAnySide(this)) {
                if (restartImmediately) {
                    reset(originalTimeDelayStart);
                } else {
                    endOfPass();
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

    /**
     * Resets alien if the alien has gone off-screen without being destroyed and
     * sub-wave needs to be repeated.
     * <p>
     * Reduce original delay by supplied offset in case alien needs to start
     * earlier.
     */
    @Override
    public void reset(float offset) {
        timeDelayStart = originalTimeDelayStart - offset;
        waiting();

        /*
         * reset back at start position - will be made visible and active before
         * recalculating it's position.
         */
        move(startingX, startingY);
    }

    @Override
    public boolean isEndOfPass() {
        return state == FINISHED_PASS;
    }

    private void endOfPass() {
        state = FINISHED_PASS;
    }

    /**
     * Get the original time delay. Can be used to calculate a corrected time
     * delay offset.
     */
    @Override
    public float getTimeDelay() {
        return originalTimeDelayStart;
    }
}
