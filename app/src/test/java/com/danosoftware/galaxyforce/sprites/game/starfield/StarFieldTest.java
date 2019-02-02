package com.danosoftware.galaxyforce.sprites.game.starfield;

import com.danosoftware.galaxyforce.view.Animation;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

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

        // create starfield an animate it
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

    @Test
    public void animationShouldMoveStar() {

        Animation animation = mock(Animation.class);
        Star star = new Star(0, 0, 1000, animation, 0, 100);
        assertThat(star.x(), equalTo(0));
        assertThat(star.y(), equalTo(0));

        // after 5 seconds, star should be at 500 (100 speed x 5 seconds)
        star.animate(5f);
        assertThat(star.x(), equalTo(0));
        assertThat(star.y(), equalTo(500));

        // after another 5 seconds, star should be back at 0.
        // height should be 1000 wraps back to 0.
        star.animate(5f);
        assertThat(star.x(), equalTo(0));
        assertThat(star.y(), equalTo(0));

        // after another 5 seconds, star should be at 500 (100 speed x 5 seconds)
        star.animate(5f);
        assertThat(star.x(), equalTo(0));
        assertThat(star.y(), equalTo(500));

        // after another 500 seconds, star should be back at 500
        // at chosen speed of 100 and height of 1000,
        // any deltaTime which is a multiple of 10 should move star
        // by 1000 and hence back to same position
        star.animate(500f);
        assertThat(star.x(), equalTo(0));
        assertThat(star.y(), equalTo(500));
    }
}
