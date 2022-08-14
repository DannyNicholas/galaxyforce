package com.danosoftware.galaxyforce.sprites.game.starfield;

import static java.lang.Math.round;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StarFieldTest {

  /**
   * Test position of star after different delta-times.
   */
  @Test
  public void animationShouldMoveStar() {

    // create starfield
    int height = 1200;
    StarField starField = new StarField(250, height);

    // extract 1st star to use as test sample
    Star star = starField.getStarField()[0];
    float initialX = star.x;
    float initialY = star.y;

    // confirm star's initial position
    assertThat(round(star.x), equalTo(round(initialX)));
    assertThat(round(star.y), equalTo(round(initialY)));

    // after 5 seconds, star should have moved by 600 (120 speed x 5 seconds)
    starField.animate(5f);
    assertThat(round(star.x), equalTo(round(initialX)));
    assertThat(round(star.y), equalTo(round((initialY + 600f) % height)));

    // after another 5 seconds, star should have moved by another 600 (120 speed x 5 seconds)
    // star Y will wrap-back to its initial position as game height is 1200.
    starField.animate(5f);
    assertThat(round(star.x), equalTo(round(initialX)));
    assertThat(round(star.y), equalTo(round((initialY + 1200f) % height)));
    assertThat(round(star.y), equalTo(round(initialY)));

    // let's try a long delay involving many wrap-backs
    // we know star completes a 600 pixel journey in a 5 second delay (120 speed x 5 seconds = 600 pixels).
    // after another 125 seconds, we expect star to 600 from starting position.
    starField.animate(125f);
    assertThat(round(star.x), equalTo(round(initialX)));
    assertThat(round(star.y), equalTo(round((initialY + 600f) % height)));

    // we know star completes a full-height journey in a 10 second delay (120 speed x 10 sec = 1200 pixels).
    // after another 1000 seconds, we expect star to be back at the same position
    starField.animate(1000f);
    assertThat(round(star.x), equalTo(round(initialX)));
    assertThat(round(star.y), equalTo(round((initialY + 600f) % height)));
  }
}
