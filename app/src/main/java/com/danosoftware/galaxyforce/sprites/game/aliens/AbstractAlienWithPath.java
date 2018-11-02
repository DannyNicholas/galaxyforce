package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.List;

import static com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState.FINISHED_PASS;

/**
 * @author Danny
 */
public abstract class AbstractAlienWithPath extends AbstractAlien implements IAlienWithPath {
    /*
     * reference path this alien will follow in list of x,y co-ordinates (Point
     * objects)
     */
    private final List<Point> alienPath;

    /* how many seconds to delay before alien starts to follow path */
    private float timeDelayStart;

    /* original time delay before alien starts in cases where alien is reset */
    private final float originalTimeDelayStart;

    /* how many seconds have passed since alien started movement */
    private float timeSinceStarted;

    /* max index in path array */
    private final int maximumPathIndex;

    /* should the alien restart it's path immediately when it reaches the end */
    private final boolean restartImmediately;

    public AbstractAlienWithPath(
            Animation animation,
            FireBehaviour fireBehaviour,
            PowerUpBehaviour powerUpBehaviour,
            SpawnBehaviour spawnBehaviour,
            HitBehaviour hitBehaviour,
            ExplodeBehaviour explodeBehaviour,
            List<Point> alienPath,
            float delayStart,
            int energy,
            boolean restartImmediately) {
        // default is that all aliens with paths start invisible at first
        // position
        super(
                animation,
                alienPath.get(0).getX(),
                alienPath.get(0).getY(),
                energy,
                fireBehaviour,
                powerUpBehaviour,
                spawnBehaviour,
                hitBehaviour,
                explodeBehaviour);

        this.alienPath = alienPath;
        this.maximumPathIndex = alienPath.size() - 1;
        this.originalTimeDelayStart = delayStart;
        this.restartImmediately = restartImmediately;

        // reset timers and sets sprite inactive
        reset(0);
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        /* if alien active then alien can move */
        if (isActive()) {
            timeSinceStarted += deltaTime;

            /*
             * calculate path index based on start time. assumption is that
             * aliens path advances 60 elements every second. Rounds to the
             * nearest whole number to keep movement as smooth as possible.
             */
            int index = Math.round(timeSinceStarted * 60f);

            /*
             * if alien at end of path then set finished pass.
             */
            if (index > maximumPathIndex && isActive()) {
                if (restartImmediately) {
                    // alien is reset to beginning of path immediately
                    reset(originalTimeDelayStart);
                } else {
                    // alien set to end of path. will wait until manually reset
                    // or destroyed by model
                    endOfPass();
                }
            } else {
                // set alien new position
                move(
                        alienPath.get(index).getX(),
                        alienPath.get(index).getY()
                );
            }
        } else if (isWaiting()) {
            /* if delayStart still > 0 then count down delay */
            if (timeDelayStart > 0) {
                timeDelayStart -= deltaTime;
            }
            /* otherwise activate alien. */
            else {
                activate();
            }
        }
    }

    /**
     * Resets alien if the alien has finished path without being destroyed and
     * sub-wave needs to be repeated.
     * <p>
     * Reduce original delay by supplied offset in case alien needs to start
     * earlier.
     */
    @Override
    public void reset(float offset) {
        timeDelayStart = originalTimeDelayStart - offset;
        timeSinceStarted = 0f;
        waiting();

        /*
         * reset back at start position - will be made visible and active before
         * recalculating it's position.
         */
        move(
                alienPath.get(0).getX(),
                alienPath.get(0).getY()
        );
    }

    @Override
    public boolean isEndOfPass() {
        return (state == FINISHED_PASS);
    }

    @Override
    public void endOfPass() {
        state = FINISHED_PASS;
    }

    /**
     * Get the original time delay. Can be used to calculate a corrected time
     * delay offset.
     */
    public float getTimeDelay() {
        return originalTimeDelayStart;
    }
}
