package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteProperties;
import com.danosoftware.galaxyforce.utilities.Rectangle;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractCollidingSpriteTest {

    private static final int HEIGHT = 24;
    private static final int WIDTH = 32;
    private static final int HALF_HEIGHT = HEIGHT / 2;
    private static final int HALF_WIDTH = WIDTH / 2;
    private static final int X = 100;
    private static final int Y = 200;

    private static final Rectangle EXPECTED_EMPTY_BOUNDS = new Rectangle(X, Y, 0, 0);
    private static final Rectangle EXPECTED_BOUNDS = new Rectangle(X - HALF_WIDTH, Y - HALF_HEIGHT, WIDTH, HEIGHT);

    // alternative sprite of different size
    private static final int ALT_HEIGHT = 100;
    private static final int ALT_WIDTH = 200;
    private static final Rectangle ALTERNATIVE_BOUNDS = new Rectangle(X - ALT_WIDTH / 2, Y - ALT_HEIGHT / 2, ALT_WIDTH, ALT_HEIGHT);

    // moved sprite
    private static final int MOVED_X = 300;
    private static final int MOVED_Y = 400;
    private static final Rectangle MOVED_BOUNDS = new Rectangle(MOVED_X - HALF_WIDTH, MOVED_Y - HALF_HEIGHT, WIDTH, HEIGHT);


    // test implementation of an abstract sprite
    private static class TestSprite extends AbstractCollidingSprite {
        private TestSprite(ISpriteIdentifier spriteId, int x, int y, int rotation) {
            super(spriteId, x, y, rotation);
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

    private ISpriteProperties props;
    private ISpriteIdentifier spriteId;
    private ICollidingSprite sprite;

    @Before
    public void setUp() {
        spriteId = mock(ISpriteIdentifier.class);
        sprite = new TestSprite(spriteId, X, Y, 0);

        props = mock(ISpriteProperties.class);
        when(props.getHeight()).thenReturn(HEIGHT);
        when(props.getWidth()).thenReturn(WIDTH);
    }

    @Test
    public void shouldReturnZeroDimensionBoundsWhenSpriteNotLoaded() {

        Rectangle bounds = sprite.getBounds();
        verifyRectangle(bounds, EXPECTED_EMPTY_BOUNDS);

        verify(spriteId, times(1)).getProperties();
        verify(props, times(0)).getHeight();
        verify(props, times(0)).getWidth();
    }

    @Test
    public void shouldReturnExpectedBoundsWhenSpriteLoaded() {

        // sprite now has properties
        when(spriteId.getProperties()).thenReturn(props);

        Rectangle bounds = sprite.getBounds();
        verifyRectangle(bounds, EXPECTED_BOUNDS);

        verify(spriteId, times(2)).getProperties();
        verify(props, times(1)).getWidth();
        verify(props, times(1)).getHeight();
    }

    @Test
    public void shouldOnlyCallPropsOnceForBounds() {

        // sprite now has properties
        when(spriteId.getProperties()).thenReturn(props);

        Rectangle bounds = sprite.getBounds();

        // multiple unneccessary calls - should all be cached
        sprite.getBounds();
        sprite.getBounds();
        sprite.getBounds();
        sprite.getBounds();

        verifyRectangle(bounds, EXPECTED_BOUNDS);
        verify(spriteId, times(2)).getProperties();
        verify(props, times(1)).getWidth();
        verify(props, times(1)).getHeight();
    }

    @Test
    public void shouldClearBoundsCacheAfterSpriteChange() {

        // sprite now has properties
        when(spriteId.getProperties()).thenReturn(props);

        // prepare an alternative sprite Id
        ISpriteIdentifier alternativeSpriteId = mock(ISpriteIdentifier.class);
        ISpriteProperties altProps = mock(ISpriteProperties.class);
        when(altProps.getHeight()).thenReturn(100);
        when(altProps.getWidth()).thenReturn(200);
        when(alternativeSpriteId.getProperties()).thenReturn(altProps);

        // confirm initial behaviour
        Rectangle bounds = sprite.getBounds();
        verifyRectangle(bounds, EXPECTED_BOUNDS);
        verify(spriteId, times(2)).getProperties();
        verify(props, times(1)).getWidth();
        verify(props, times(1)).getHeight();

        // change sprite Id
        sprite.changeType(alternativeSpriteId);

        // confirm new sprite results in additional calls to props and different dimensions
        verifyRectangle(sprite.getBounds(), ALTERNATIVE_BOUNDS);
        verify(alternativeSpriteId, times(2)).getProperties();
        verify(altProps, times(1)).getWidth();
        verify(altProps, times(1)).getHeight();
    }

    @Test
    public void shouldMoveBoundsAfterSpriteMove() {

        // sprite now has properties
        when(spriteId.getProperties()).thenReturn(props);

        // confirm initial behaviour
        Rectangle bounds = sprite.getBounds();
        verifyRectangle(bounds, EXPECTED_BOUNDS);

        // move sprite
        sprite.move(MOVED_X, MOVED_Y);

        // confirm new sprite results in additional call to props and different dimensions
        verifyRectangle(sprite.getBounds(), MOVED_BOUNDS);

        // should only make any extra call to check properties
        verify(spriteId, times(3)).getProperties();
        verify(props, times(1)).getWidth();
        verify(props, times(1)).getHeight();
    }


    private void verifyRectangle(Rectangle bounds, Rectangle expected) {
        assertThat(bounds.height, equalTo(expected.height));
        assertThat(bounds.width, equalTo(expected.width));
        assertThat(bounds.lowerLeft.x, equalTo(expected.lowerLeft.x));
        assertThat(bounds.lowerLeft.y, equalTo(expected.lowerLeft.y));
    }
}
