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

        assertThat(height, equalTo(0));
        assertThat(width, equalTo(0));
        verify(spriteId, times(2)).getProperties();
        verify(props, times(0)).getHeight();
        verify(props, times(0)).getWidth();
    }

    @Test
    public void shouldReturnWidthAndHeightWhenSpriteLoaded() {

        // sprite now has properties
        when(spriteId.getProperties()).thenReturn(props);

        int height = sprite.height();
        int width = sprite.width();

        assertThat(height, equalTo(HEIGHT));
        assertThat(width, equalTo(WIDTH));
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

        // multiple unneccessary calls - should all be cached
        sprite.height();
        sprite.width();
        sprite.height();
        sprite.width();

        assertThat(height, equalTo(HEIGHT));
        assertThat(width, equalTo(WIDTH));
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
        verify(spriteId, times(1)).getProperties();
        verify(props, times(1)).getHeight();
        verify(props, times(1)).getWidth();

        // change sprite Id
        sprite.changeType(alternativeSpriteId);

        // confirm new sprite results in additional call to props and different dimensions
        assertThat(sprite.height(), equalTo(100));
        assertThat(sprite.width(), equalTo(200));
        verify(alternativeSpriteId, times(1)).getProperties();
        verify(altProps, times(1)).getHeight();
        verify(altProps, times(1)).getWidth();
    }
}
