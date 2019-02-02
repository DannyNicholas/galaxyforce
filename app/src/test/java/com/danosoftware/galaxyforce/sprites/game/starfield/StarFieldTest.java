package com.danosoftware.galaxyforce.sprites.game.starfield;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class StarFieldTest {

    @Test
    public void shouldIncreaseElapsedTime() {

        StarFieldTemplate starFieldTemplate = new StarFieldTemplate(100, 100);
        float initialElapsedTime = starFieldTemplate.getTimeElapsed();
        assertThat(initialElapsedTime, equalTo(0f));

        StarField starField = new StarField(starFieldTemplate, StarAnimationType.GAME);
        starField.animate(100f);
        assertThat(starFieldTemplate.getTimeElapsed(), equalTo(100f));

        starField.animate(100f);
        assertThat(starFieldTemplate.getTimeElapsed(), equalTo(200f));
    }

    @Test
    public void newStarFieldShouldMatchOriginal() {

        // create starfield and animate it
        StarFieldTemplate starFieldTemplate = new StarFieldTemplate(100, 100);
        StarField starField = new StarField(starFieldTemplate, StarAnimationType.GAME);
        starField.animate(10f);

        // get position of first star
        List<Star> initalStars = starField.getSprites();
        Star firstStar = initalStars.get(0);

        // create new starfield from same template
        // confirm first star starts from previous position
        StarField starField1 = new StarField(starFieldTemplate, StarAnimationType.GAME);
        List<Star> stars1 = starField1.getSprites();
        Star firstStar1 = stars1.get(0);
        assertThat(firstStar1.x(), equalTo(firstStar.x()));
        assertThat(firstStar1.y(), equalTo(firstStar.y()));

        // animate starfield for a longer period
        // create new starfield from same template
        // confirm first star still starts from previous position
        starField1.animate(60000f);
        StarField starField2 = new StarField(starFieldTemplate, StarAnimationType.GAME);
        List<Star> stars2 = starField2.getSprites();
        Star firstStar2 = stars2.get(0);
        assertThat(firstStar2.x(), equalTo(firstStar.x()));
        assertThat(firstStar2.y(), equalTo(firstStar.y()));
    }

    /**
     * Test position of star after different delta-times.
     */
    @Test
    public void animationShouldMoveStar() {

        // create starfield using a predictable template
        int height = 1200;
        StarFieldTemplate starFieldTemplate = new StarFieldTemplate(height, 250, 1200, 0, 0, StarSpeed.SLOW);
        StarField starField = new StarField(starFieldTemplate, StarAnimationType.GAME);

        // confirm star's initial position
        Star star = starField.getSprites().get(0);
        assertThat(star.x(), equalTo(250));
        assertThat(star.y(), equalTo(1200));

        // after 5 seconds, star should have moved by 600 (120 speed x 5 seconds)
        // star Y will now be at 600 (1200 - 600)
        starField.animate(5f);
        assertThat(star.x(), equalTo(250));
        assertThat(star.y(), equalTo(600));

        // after another 5 seconds, star should have moved by another 600 (120 speed x 5 seconds)
        // star Y will compute position as 0
        // star Y will wrap back to 1200 when it hits 0.
        starField.animate(5f);
        assertThat(star.x(), equalTo(250));
        assertThat(star.y(), equalTo(1200));

        // let's try a long delay involving many wrap-backs
        // we know star completes a 600 pixel journey in a 5 second delay (120 speed x 5 seconds = 600 pixels).
        // after another 125 seconds, we expect star to be back at 600 (half-way up screen)
        starField.animate(125f);
        assertThat(star.x(), equalTo(250));
        assertThat(star.y(), equalTo(600));

        // we know star completes a full-height journey in a 10 second delay (120 speed x 10 sec = 1200 pixels).
        // after another 1000 seconds, we expect star to be back at it's current position
        starField.animate(1000f);
        assertThat(star.x(), equalTo(250));
        assertThat(star.y(), equalTo(600));
    }
}
