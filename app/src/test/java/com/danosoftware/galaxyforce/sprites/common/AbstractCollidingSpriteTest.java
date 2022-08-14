package com.danosoftware.galaxyforce.sprites.common;

import static com.danosoftware.galaxyforce.common.SpriteDetailsCommon.setUpSpriteDetailsForTests;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDimensions;
import com.danosoftware.galaxyforce.textures.TextureRegion;
import com.danosoftware.galaxyforce.utilities.Rectangle;
import org.junit.Before;
import org.junit.Test;

public class AbstractCollidingSpriteTest {

  private static final int HEIGHT = 24;
  private static final int WIDTH = 32;
  private static final int HALF_HEIGHT = HEIGHT / 2;
  private static final int HALF_WIDTH = WIDTH / 2;
  private static final int X = 100;
  private static final int Y = 200;

  private static final Rectangle EXPECTED_EMPTY_BOUNDS = new Rectangle(X, Y, 0, 0);
  private static final Rectangle EXPECTED_BOUNDS = new Rectangle(X, Y,
      HALF_WIDTH, HALF_HEIGHT);

  // moved sprite
  private static final int MOVED_X = 300;
  private static final int MOVED_Y = 400;
  private static final Rectangle MOVED_BOUNDS = new Rectangle(MOVED_X,
      MOVED_Y, HALF_WIDTH, HALF_HEIGHT);


  // test implementation of an abstract sprite
  private static class TestSprite extends AbstractCollidingSprite {

    private TestSprite(SpriteDetails spriteId) {
      super(spriteId, X, Y, 0);
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean isDestroyed() {
      return false;
    }

    @Override
    public void animate(float deltaTime) {
    }
  }

  private SpriteDimensions dimensions;
  private SpriteDetails spriteId;
  private ICollidingSprite sprite;

  @Before
  public void setUp() {
    // pre-populate sprite details - only BASE has dimensions
    final TextureRegion mockTextureRegion = mock(TextureRegion.class);
    dimensions = mock(SpriteDimensions.class);
    when(dimensions.getHeight()).thenReturn(HEIGHT);
    when(dimensions.getWidth()).thenReturn(WIDTH);
    setUpSpriteDetailsForTests(
        new SpriteDetails[]{SpriteDetails.BASE},
        mockTextureRegion,
        dimensions);
  }

  @Test
  public void shouldReturnZeroDimensionBoundsWhenSpriteNotLoaded() {

    // choose sprite with no dimensions
    spriteId = SpriteDetails.HELPER;
    sprite = new TestSprite(spriteId);

    Rectangle bounds = sprite.getBounds();
    verifyRectangle(bounds, EXPECTED_EMPTY_BOUNDS);

    verify(dimensions, times(0)).getHeight();
    verify(dimensions, times(0)).getWidth();
  }

  @Test
  public void shouldReturnExpectedBoundsWhenSpriteLoaded() {

    // choose sprite with dimensions
    spriteId = SpriteDetails.BASE;
    sprite = new TestSprite(spriteId);

    Rectangle bounds = sprite.getBounds();
    verifyRectangle(bounds, EXPECTED_BOUNDS);

    verify(dimensions, times(1)).getWidth();
    verify(dimensions, times(1)).getHeight();
  }

  @Test
  public void shouldOnlyCallPropsOnceForBounds() {

    // choose sprite with dimensions
    spriteId = SpriteDetails.BASE;
    sprite = new TestSprite(spriteId);

    Rectangle bounds = sprite.getBounds();

    // multiple unnecessary calls - should all be cached
    sprite.getBounds();
    sprite.getBounds();
    sprite.getBounds();
    sprite.getBounds();

    verifyRectangle(bounds, EXPECTED_BOUNDS);
    verify(dimensions, times(1)).getWidth();
    verify(dimensions, times(1)).getHeight();
  }

  @Test
  public void shouldClearBoundsCacheAfterSpriteChange() {

    // choose sprite with dimensions
    spriteId = SpriteDetails.BASE;
    sprite = new TestSprite(spriteId);

    // confirm initial behaviour
    Rectangle bounds = sprite.getBounds();
    verifyRectangle(bounds, EXPECTED_BOUNDS);
    verify(dimensions, times(1)).getWidth();
    verify(dimensions, times(1)).getHeight();

    // change sprite to one without dimensions
    sprite.changeType(SpriteDetails.HELPER);

    // confirm new sprite results in additional calls to props and different dimensions
    verifyRectangle(sprite.getBounds(), EXPECTED_EMPTY_BOUNDS);
    verify(dimensions, times(1)).getWidth();
    verify(dimensions, times(1)).getHeight();
  }

  @Test
  public void shouldMoveBoundsAfterSpriteMove() {

    // choose sprite with dimensions
    spriteId = SpriteDetails.BASE;
    sprite = new TestSprite(spriteId);

    // confirm initial behaviour
    Rectangle bounds = sprite.getBounds();
    verifyRectangle(bounds, EXPECTED_BOUNDS);
    verify(dimensions, times(1)).getWidth();
    verify(dimensions, times(1)).getHeight();

    // move sprite
    sprite.move(MOVED_X, MOVED_Y);

    // confirm new sprite results in additional call to props and different dimensions
    verifyRectangle(sprite.getBounds(), MOVED_BOUNDS);

    // should not any make extra call to check dimensions
    verify(dimensions, times(1)).getWidth();
    verify(dimensions, times(1)).getHeight();
  }


  private void verifyRectangle(Rectangle bounds, Rectangle expected) {
    assertThat(bounds.top, equalTo(expected.top));
    assertThat(bounds.right, equalTo(expected.right));
    assertThat(bounds.left, equalTo(expected.left));
    assertThat(bounds.bottom, equalTo(expected.bottom));
  }
}
