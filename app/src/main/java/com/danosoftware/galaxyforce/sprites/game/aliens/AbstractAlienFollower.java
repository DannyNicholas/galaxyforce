package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviour;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienCharacter;

import static com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState.EXPLODING;

public class AbstractAlienFollower extends AbstractAlien implements IAlienFollower {
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* distance alien can move each cycle in pixels each second */
    private final int alienMoveInPixels;

    /* minimum distance between alien followers */
    private final int minimumDistanceSquared;

    /*
     * keep local copies of hit behaviour and stateTime to allow
     * showHit() to keep hit animation in sync with parent.
     */
    private final HitBehaviour hitBehaviour;
    private float stateTime;

    private final ExplodeBehaviour explodeBehaviour;
    private final PowerUpBehaviour powerUpBehaviour;

    private boolean started;


    public AbstractAlienFollower(
            AlienCharacter character,
            Animation animation,
            int x,
            int y,
            int energy,
            FireBehaviour fireBehaviour,
            PowerUpBehaviour powerUpBehaviour,
            SpawnBehaviour spawnBehaviour,
            HitBehaviour hitBehaviour,
            ExplodeBehaviour explodeBehaviour,
            SpinningBehaviour spinningBehaviour,
            final int alienMoveInPixels,
            int minimumDistance) {

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

        this.hitBehaviour = hitBehaviour;
        this.explodeBehaviour = explodeBehaviour;
        this.powerUpBehaviour = powerUpBehaviour;
        this.stateTime = 0f;
        this.started = false;
        this.alienMoveInPixels = alienMoveInPixels;
        this.minimumDistanceSquared = minimumDistance * minimumDistance;
    }

    /**
     * Update position of follower based on alien this is following.
     * The follower will attempt to follow the alien in front but will throttle
     * it's speed if it gets too close.
     */
    @Override
    public void follow(IAlien alienFollowed, float deltaTime) {

        if (!started) {
            activate();
            this.started = true;
        }

        // calculate angle from this follower to the alien we are following
        float newAngle = (float) Math.atan2(
                alienFollowed.y() - y(),
                alienFollowed.x() - x());

        // calculate the deltas to be applied each move
        int xDelta = (int) ((alienMoveInPixels) * (float) Math.cos(newAngle));
        int yDelta = (int) ((alienMoveInPixels) * (float) Math.sin(newAngle));

        // calculate new position
        int newX = x() + (int) (xDelta * deltaTime);
        int newY = y() + (int) (yDelta * deltaTime);

        // calculate squared distance from alien we are following
        int distX = (alienFollowed.x() - newX);
        int distY = (alienFollowed.y() - newY);
        int distSquared = (distX * distX) + (distY * distY);

        // if we are too close we need to throttle our speed
        if (distSquared > minimumDistanceSquared) {
            move(newX, newY);
        } else {
            float throttleRatio = (float) distSquared / minimumDistanceSquared;

            // calculate new position based on reduced speed
            int reducedXDelta = (int) (xDelta * throttleRatio);
            int reducedYDelta = (int) (yDelta * throttleRatio);

            // move alien
            moveByDelta(
                    (int) (reducedXDelta * deltaTime),
                    (int) (reducedYDelta * deltaTime));
        }
    }

    /**
     * Update local instance of stateTime to keep in sync with parent
     */
    @Override
    public void animate(float deltaTime) {
        stateTime += deltaTime;
        super.animate(deltaTime);
    }

    /**
     * Show graphical hit in alien follower without impacting it's energy.
     * Called if followable alien was hit and passed onto this follower.
     * We hit silently to allow followable alien to handle sound/vibration.
     */
    @Override
    public void showHit() {
        hitBehaviour.startHitSilently(stateTime);
    }

    /**
     * Show graphical explosion in alien follower.
     * Called if followable alien explodes and passed onto this follower.
     * We hit silently to allow followable alien to handle sound/vibration.
     */
    @Override
    public void showExplode() {
        explodeBehaviour.startExplosionSilently();
        state = EXPLODING;
        powerUpBehaviour.releasePowerUp(this);
    }
}
