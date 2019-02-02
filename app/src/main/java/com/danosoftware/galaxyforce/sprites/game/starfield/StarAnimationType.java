package com.danosoftware.galaxyforce.sprites.game.starfield;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Create consistent star-field animations using different sprite identifiers.
 * Should ensure smooth star-field animation transitions when switching
 * between screens.
 */
public enum StarAnimationType {

    GAME(
            GameSpriteIdentifier.STAR,
            GameSpriteIdentifier.STAR_SPARKLE,
            GameSpriteIdentifier.STAR_RED,
            GameSpriteIdentifier.STAR_BLACK,
            GameSpriteIdentifier.STAR_BLUE),
    MENU(
            MenuSpriteIdentifier.STAR,
            MenuSpriteIdentifier.STAR_SPARKLE,
            MenuSpriteIdentifier.STAR_RED,
            MenuSpriteIdentifier.STAR_BLACK,
            MenuSpriteIdentifier.STAR_BLUE);

    // used by star-field template to help randomly select an animation.
    // needed before an actual array of animations is received.
    public static final int ANIMATION_TYPES = 3;

    private final ISpriteIdentifier whiteStar;
    private final ISpriteIdentifier whiteStarSparkle;
    private final ISpriteIdentifier redStar;
    private final ISpriteIdentifier blackStar;
    private final ISpriteIdentifier blueStar;

    StarAnimationType(
            ISpriteIdentifier whiteStar,
            ISpriteIdentifier whiteStarSparkle,
            ISpriteIdentifier redStar,
            ISpriteIdentifier blackStar,
            ISpriteIdentifier blueStar) {
        this.whiteStar = whiteStar;
        this.whiteStarSparkle = whiteStarSparkle;
        this.redStar = redStar;
        this.blackStar = blackStar;
        this.blueStar = blueStar;
    }

    private Animation createAnimationOne() {
        return new Animation(
                0.5f,
                whiteStar,
                whiteStarSparkle,
                whiteStar);
    }

    private Animation createAnimationTwo() {
        return new Animation(
                0.5f,
                redStar,
                blackStar,
                redStar);
    }

    private Animation createAnimationThree() {
        return new Animation(
                0.5f,
                blueStar,
                blackStar,
                blueStar);
    }

    public Animation[] getAnimations() {
        // ensures array is correctly sized for templates
        Animation[] animations = new Animation[ANIMATION_TYPES];

        animations[0] = createAnimationOne();
        animations[1] = createAnimationTwo();
        animations[2] = createAnimationThree();
        return animations;
    }
}
