package com.danosoftware.galaxyforce.sprites.game.starfield;

import com.danosoftware.galaxyforce.view.Animation;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Star> stars;

    public StarField(
            StarFieldTemplate starFieldTemplate,
            StarAnimationType starAnimationType) {

        this.starFieldTemplate = starFieldTemplate;
        this.stars = new ArrayList<>();
        final Animation[] animations = starAnimationType.getAnimations();
        for (StarTemplate starTemplate : starFieldTemplate.getStarTemplates()) {
            Star star = new Star(
                    starTemplate.getInitialX(),
                    starTemplate.getInitialY(),
                    starFieldTemplate.getHeight(),
                    animations[starTemplate.getAnimationIndex()],
                    starTemplate.getAnimationStateTime(),
                    starTemplate.getSpeed());

            // fast-forward star to latest state
            star.animate(starFieldTemplate.getTimeElapsed());

            stars.add(star);
        }
    }

    public void animate(float deltaTime) {

        // increase total elapsed time since starfield created.
        // needed to allow template to create future stars in the same state
        starFieldTemplate.increaseTimeElapsed(deltaTime);

        // move stars
        for (Star eachStar : stars) {
            eachStar.animate(starFieldTemplate.getTimeElapsed());
        }
    }

    public List<Star> getSprites() {
        return stars;
    }
}
