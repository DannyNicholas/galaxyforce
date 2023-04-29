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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractSpriteTest {

  private static final int HEIGHT = 24;
  private static final int WIDTH = 32;
  private static final int HALF_HEIGHT = HEIGHT / 2;
  private static final int HALF_WIDTH = WIDTH / 2;

  // test implementation of an abstract sprite
  private static class TestSprite extends AbstractSprite {

    private TestSprite(SpriteDetails spriteId, int x, int y, int rotation) {
      super(spriteId, x, y, rotation);
    }
  }

  private SpriteDimensions dimensions;
  private SpriteDetails spriteId;
  private ISprite sprite;

  @BeforeEach
  public void setUp() {
    // pre-populate sprite details - only BASE has dimensions
    TextureRegion mockTextureRegion = mock(TextureRegion.class);
    dimensions = mock(SpriteDimensions.class);
    when(dimensions.getHeight()).thenReturn(HEIGHT);
    when(dimensions.getWidth()).thenReturn(WIDTH);
    setUpSpriteDetailsForTests(
        new SpriteDetails[]{SpriteDetails.BASE},
        mockTextureRegion,
        dimensions);
  }

  @Test
  public void shouldReturnZeroWidthAndHeightWhenSpriteNotLoaded() {

    // choose sprite with no dimensions
    spriteId = SpriteDetails.HELPER;
    sprite = new TestSprite(spriteId, 0, 0, 0);

    int height = sprite.height();
    int width = sprite.width();
    int halfHeight = sprite.halfHeight();
    int halfWidth = sprite.halfWidth();

    assertThat(height, equalTo(0));
    assertThat(width, equalTo(0));
    assertThat(halfHeight, equalTo(0));
    assertThat(halfWidth, equalTo(0));
    verify(dimensions, times(0)).getHeight();
    verify(dimensions, times(0)).getWidth();
  }

  @Test
  public void shouldReturnWidthAndHeightWhenSpriteLoaded() {

    // sprite now has properties
    spriteId = SpriteDetails.BASE;
    sprite = new TestSprite(spriteId, 0, 0, 0);

    int height = sprite.height();
    int width = sprite.width();
    int halfHeight = sprite.halfHeight();
    int halfWidth = sprite.halfWidth();

    assertThat(height, equalTo(HEIGHT));
    assertThat(width, equalTo(WIDTH));
    assertThat(halfHeight, equalTo(HALF_HEIGHT));
    assertThat(halfWidth, equalTo(HALF_WIDTH));
    verify(dimensions, times(1)).getHeight();
    verify(dimensions, times(1)).getWidth();
  }

  @Test
  public void shouldOnlyCallPropsOnceForWidthAndHeight() {

    // sprite now has properties
    spriteId = SpriteDetails.BASE;
    sprite = new TestSprite(spriteId, 0, 0, 0);

    int height = sprite.height();
    int width = sprite.width();
    int halfHeight = sprite.halfHeight();
    int halfWidth = sprite.halfWidth();

    // multiple unnecessary calls - should all be cached
    sprite.height();
    sprite.width();
    sprite.halfHeight();
    sprite.halfWidth();
    sprite.height();
    sprite.width();
    sprite.halfHeight();
    sprite.halfWidth();

    assertThat(height, equalTo(HEIGHT));
    assertThat(width, equalTo(WIDTH));
    assertThat(halfHeight, equalTo(HALF_HEIGHT));
    assertThat(halfWidth, equalTo(HALF_WIDTH));
    verify(dimensions, times(1)).getHeight();
    verify(dimensions, times(1)).getWidth();
  }

  @Test
  public void shouldClearDimensionsCacheAfterSpriteChange() {

    // sprite now has properties
    spriteId = SpriteDetails.BASE;
    sprite = new TestSprite(spriteId, 0, 0, 0);

    // confirm initial behaviour
    assertThat(sprite.height(), equalTo(HEIGHT));
    assertThat(sprite.width(), equalTo(WIDTH));
    assertThat(sprite.halfHeight(), equalTo(HALF_HEIGHT));
    assertThat(sprite.halfWidth(), equalTo(HALF_WIDTH));
    verify(dimensions, times(1)).getHeight();
    verify(dimensions, times(1)).getWidth();

    // change sprite Id
    sprite.changeType(SpriteDetails.HELPER);

    // confirm new sprite results in additional call to props and different dimensions
    assertThat(sprite.height(), equalTo(0));
    assertThat(sprite.width(), equalTo(0));
    assertThat(sprite.halfHeight(), equalTo(0));
    assertThat(sprite.halfWidth(), equalTo(0));
    verify(dimensions, times(1)).getHeight();
    verify(dimensions, times(1)).getWidth();
  }
}
