package com.danosoftware.galaxyforce.sprites.game.starfield;

import com.danosoftware.galaxyforce.view.Animation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Star field of multiple animated stars.
 * <p>
 * Stars are created from a supplied template.
 * <p>
 * The created stars can be fast-forwarded to the current state. This allows
 * a new starfield to be created using different sprite IDs that should be
 * the identical state to the previous starfield.
 * <p>
 * This ensures seamless animation of the starfield when switching screens.
 */
public class StarField {

    private final StarFieldTemplate starFieldTemplate;

    // map of star-tuples grouped per speed
    // allows more efficient updating stars with the same speed
    private final Map<StarSpeed, List<StarTuple>> starSpeedMap;

    // separate list of all stars to simplify returning all sprites
    private final List<Star> stars;

    public StarField(
            StarFieldTemplate starFieldTemplate,
            StarAnimationType starAnimationType) {

        this.starFieldTemplate = starFieldTemplate;
        this.stars = new ArrayList<>();

        // initialise map
        this.starSpeedMap = new EnumMap<>(StarSpeed.class);
        for (StarSpeed speed : StarSpeed.values()) {
            starSpeedMap.put(speed, new ArrayList<StarTuple>());
        }

        final Animation[] animations = starAnimationType.getAnimations();
        for (StarTemplate starTemplate : starFieldTemplate.getStarTemplates()) {
            Star star = new Star(
                    starTemplate.getInitialX(),
                    starTemplate.getInitialY(),
                    animations[starTemplate.getAnimationIndex()],
                    starTemplate.getAnimationStateTime());

            stars.add(star);

            List<StarTuple> starsWithSameSpeed = starSpeedMap.get(starTemplate.getSpeed());
            StarTuple starTuple = new StarTuple(
                    star,
                    starTemplate.getInitialY());
            starsWithSameSpeed.add(starTuple);
        }
    }


    public void animate(float deltaTime) {

        // increase total elapsed time since starfield created.
        // used to compute current star positions/animation.
        // also needed to allow template to create future stars in the same state
        starFieldTemplate.increaseTimeElapsed(deltaTime);

        // each star with the same speed, will move the same computed amount.
        // more efficient if we update stars in batches based on their speed.
        for (StarSpeed speed : StarSpeed.values()) {

            // for consistent movement, we compute the distance from the initial
            // position using the total time elapsed since initial construction.
            int distanceDelta = (int) (
                    (speed.getPixelsPerSecond() * starFieldTemplate.getTimeElapsed())
                            % starFieldTemplate.getHeight());

            for (StarTuple starTuple : starSpeedMap.get(speed)) {

                int starY = starTuple.getInitialY() - distanceDelta;

                // if star has reached the bottom of screen then re-position at the top.
                if (starY < 0) {
                    starY = starFieldTemplate.getHeight() + starY;
                }

                Star star = starTuple.getStar();
                star.move(star.x(), starY);

                // update animation frame
                star.animate(starFieldTemplate.getTimeElapsed());
            }
        }
    }

    public List<Star> getSprites() {
        return stars;
    }
}
