package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteProperties;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractSpriteTest {

    private static final int HEIGHT = 24;
    private static final int WIDTH = 32;
    private static final int HALF_HEIGHT = HEIGHT / 2;
    private static final int HALF_WIDTH = WIDTH / 2;

    // test implementation of an abstract sprite
    private static class TestSprite extends AbstractSprite {
        private TestSprite(ISpriteIdentifier spriteId, int x, int y, int rotation) {
            super(spriteId, x, y, rotation);
        }
    }

    private ISpriteProperties props;
    private ISpriteIdentifier spriteId;
    private ISprite sprite;

    @Before
    public void setUp() {
        spriteId = mock(ISpriteIdentifier.class);
        sprite = new TestSprite(spriteId, 0, 0, 0);

        props = mock(ISpriteProperties.class);
        when(props.getHeight()).thenReturn(HEIGHT);
        when(props.getWidth()).thenReturn(WIDTH);
    }

    @Test
    public void shouldReturnZeroWidthAndHeightWhenSpriteNotLoaded() {
        int height = sprite.height();
        int width = sprite.width();
        int halfHeight = sprite.halfHeight();
        int halfWidth = sprite.halfWidth();

        assertThat(height, equalTo(0));
        assertThat(width, equalTo(0));
        assertThat(halfHeight, equalTo(0));
        assertThat(halfWidth, equalTo(0));
        verify(spriteId, times(4)).getProperties();
        verify(props, times(0)).getHeight();
        verify(props, times(0)).getWidth();
    }

    @Test
    public void shouldReturnWidthAndHeightWhenSpriteLoaded() {

        // sprite now has properties
        when(spriteId.getProperties()).thenReturn(props);

        int height = sprite.height();
        int width = sprite.width();
        int halfHeight = sprite.halfHeight();
        int halfWidth = sprite.halfWidth();

        assertThat(height, equalTo(HEIGHT));
        assertThat(width, equalTo(WIDTH));
        assertThat(halfHeight, equalTo(HALF_HEIGHT));
        assertThat(halfWidth, equalTo(HALF_WIDTH));
        verify(spriteId, times(1)).getProperties();
        verify(props, times(1)).getHeight();
        verify(props, times(1)).getWidth();
    }

    @Test
    public void shouldOnlyCallPropsOnceForWidthAndHeight() {

        // sprite now has properties
        when(spriteId.getProperties()).thenReturn(props);

        int height = sprite.height();
        int width = sprite.width();
        int halfHeight = sprite.halfHeight();
        int halfWidth = sprite.halfWidth();

        // multiple unneccessary calls - should all be cached
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
        verify(spriteId, times(1)).getProperties();
        verify(props, times(1)).getHeight();
        verify(props, times(1)).getWidth();
    }

    @Test
    public void shouldClearDimensionsCacheAfterSpriteChange() {

        // sprite now has properties
        when(spriteId.getProperties()).thenReturn(props);

        // prepare an alternative sprite Id
        ISpriteIdentifier alternativeSpriteId = mock(ISpriteIdentifier.class);
        ISpriteProperties altProps = mock(ISpriteProperties.class);
        when(altProps.getHeight()).thenReturn(100);
        when(altProps.getWidth()).thenReturn(200);
        when(alternativeSpriteId.getProperties()).thenReturn(altProps);

        // confirm initial behaviour
        assertThat(sprite.height(), equalTo(HEIGHT));
        assertThat(sprite.width(), equalTo(WIDTH));
        assertThat(sprite.halfHeight(), equalTo(HALF_HEIGHT));
        assertThat(sprite.halfWidth(), equalTo(HALF_WIDTH));
        verify(spriteId, times(1)).getProperties();
        verify(props, times(1)).getHeight();
        verify(props, times(1)).getWidth();

        // change sprite Id
        sprite.changeType(alternativeSpriteId);

        // confirm new sprite results in additional call to props and different dimensions
        assertThat(sprite.height(), equalTo(100));
        assertThat(sprite.width(), equalTo(200));
        assertThat(sprite.halfHeight(), equalTo(50));
        assertThat(sprite.halfWidth(), equalTo(100));
        verify(alternativeSpriteId, times(1)).getProperties();
        verify(altProps, times(1)).getHeight();
        verify(altProps, times(1)).getWidth();
    }
}
