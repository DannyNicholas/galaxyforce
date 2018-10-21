package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.List;

/**
 * 
 * @author Danny
 */
public abstract class SpriteAlienWithPath extends SpriteAlien
{
    /*
     * reference path this alien will follow in list of x,y co-ordinates (Point
     * objects)
     */
    private List<Point> alienPath;

    /* how many seconds to delay before alien starts to follow path */
    private float timeDelayStart;

    /* original time delay before alien starts in cases where alien is reset */
    private float originalTimeDelayStart;

    /* how many seconds have passed since alien started movement */
    private float timeSinceStarted;

    /* max index in path array */
    private int maximumPathIndex;

    /* should the alien restart it's path immediately when it reaches the end */
    private boolean restartImmediately;

    public SpriteAlienWithPath(FireBehaviour fireBehaviour, PowerUpBehaviour powerUpBehaviour, SpawnBehaviour spawnBehaviour,
            ExplodeBehaviour explodeBehaviour, Animation animation, List<Point> alienPath, float delayStart, int energy, int hitEnergy,
            boolean restartImmediately)
    {
        // default is that all aliens with paths start invisible at first
        // position
        super(fireBehaviour, powerUpBehaviour, spawnBehaviour, explodeBehaviour, animation, alienPath.get(0).getX(), alienPath.get(0)
                .getY(), energy, hitEnergy, false);

        super.updateBounds();
        this.alienPath = alienPath;
        this.maximumPathIndex = alienPath.size() - 1;
        this.originalTimeDelayStart = delayStart;
        this.restartImmediately = restartImmediately;

        // reset timers and sets sprite inactive
        reset(0);
    }

    @Override
    public void move(float deltaTime)
    {
        /* if alien active then alien can move */
        if (isActive())
        {
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
            if (index > maximumPathIndex && isActive())
            {
                if (restartImmediately)
                {
                    // alien is reset to beginning of path immediately
                    reset(originalTimeDelayStart);
                    setVisible(true);
                }
                else
                {
                    // alien set to end of path. will wait until manually reset
                    // or destroyed by model
                    setState(SpriteState.FINISHED_PASS);
                    setVisible(false);
                }
            }
            else
            {
                // set alien new position
                setX(alienPath.get(index).getX());
                setY(alienPath.get(index).getY());

                // move sprite bounds
                updateBounds();
            }
        }
        else if (isInactive())
        {
            /* if delayStart still > 0 then count down delay */
            if (timeDelayStart > 0)
            {
                timeDelayStart -= deltaTime;
            }
            /* otherwise activate alien. can only happen once! */
            else
            {
                setState(SpriteState.ACTIVE);
                setVisible(true);
            }
        }

        /* use superclass for any explosions */
        super.move(deltaTime);
    }

    /**
     * Resets alien if the alien has finished path without being destroyed and
     * sub-wave needs to be repeated.
     * 
     * Reduce original delay by supplied offset in case alien needs to start
     * earlier.
     */
    public void reset(float offset)
    {
        timeDelayStart = originalTimeDelayStart - offset;
        timeSinceStarted = 0f;
        setState(SpriteState.INACTIVE);

        /*
         * reset back at start position - will be made visible and active before
         * recalculating it's position.
         */
        setX(alienPath.get(0).getX());
        setY(alienPath.get(0).getY());
    }

    /**
     * Get the original time delay. Can be used to calculate a corrected time
     * delay offset.
     */
    public float getTimeDelay()
    {
        return originalTimeDelayStart;
    }
}
