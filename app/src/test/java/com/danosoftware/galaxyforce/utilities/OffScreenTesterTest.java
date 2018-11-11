package com.danosoftware.galaxyforce.utilities;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteProperties;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractSprite;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;

import org.junit.Before;
import org.junit.Test;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenTop;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Confirms the the off-screen tester correctly identifies when
 * sprites are off the edge of the screen.
 * <p>
 * Test moves sprites to position just off and just on screen
 * and checks the off-screen tester returns the expected responses.
 * <p>
 * Test uses a mocked 10 x 10 sprite.
 * <p>
 * A 10x10 sprite is off-screen when x or y is 5 pixels from edge
 * since we measure position from centre of sprite.
 */
public class OffScreenTesterTest {

    private static final int HEIGHT = 10;
    private static final int WIDTH = 10;

    private static final int MID_X = GAME_WIDTH / 2;
    private static final int MID_Y = GAME_HEIGHT / 2;

    // test implementation of an abstract sprite
    private static class TestSprite extends AbstractSprite {
        private TestSprite(ISpriteIdentifier spriteId, int x, int y) {
            super(spriteId, x, y);
        }
    }

    private ISprite sprite;
    private ISpriteIdentifier spriteId;

    @Before
    public void setUp() {
        ISpriteProperties props = mock(ISpriteProperties.class);
        when(props.getHeight()).thenReturn(HEIGHT);
        when(props.getWidth()).thenReturn(WIDTH);

        spriteId = mock(ISpriteIdentifier.class);
        when(spriteId.getProperties()).thenReturn(props);
    }

    // tests sprite just off bottom on screen
    @Test
    public void shouldBeOffScreenAtBottom() {
        sprite = new TestSprite(spriteId, MID_X, -5);
        assertThat(offScreenAnySide(sprite), is(true));
        assertThat(offScreenBottom(sprite), is(true));
        assertThat(offScreenTop(sprite), is(false));
    }

    // tests sprite just on bottom on screen
    @Test
    public void shouldBeOnScreenAtBottom() {
        sprite = new TestSprite(spriteId, MID_X, -4);
        assertThat(offScreenAnySide(sprite), is(false));
        assertThat(offScreenBottom(sprite), is(false));
        assertThat(offScreenTop(sprite), is(false));
    }

    // tests sprite just off top on screen
    @Test
    public void shouldBeOffScreenAtTop() {
        sprite = new TestSprite(spriteId, MID_X, GAME_HEIGHT + 5);
        assertThat(offScreenAnySide(sprite), is(true));
        assertThat(offScreenBottom(sprite), is(false));
        assertThat(offScreenTop(sprite), is(true));
    }

    // tests sprite just on top on screen
    @Test
    public void shouldBeOnScreenAtTop() {
        sprite = new TestSprite(spriteId, MID_X, GAME_HEIGHT + 4);
        assertThat(offScreenAnySide(sprite), is(false));
        assertThat(offScreenBottom(sprite), is(false));
        assertThat(offScreenTop(sprite), is(false));
    }

    // tests sprite just off left on screen
    @Test
    public void shouldBeOffScreenAtLeft() {
        sprite = new TestSprite(spriteId, -5, MID_Y);
        assertThat(offScreenAnySide(sprite), is(true));
        assertThat(offScreenBottom(sprite), is(false));
        assertThat(offScreenTop(sprite), is(false));
    }

    // tests sprite just on left on screen
    @Test
    public void shouldBeOnScreenAtLeft() {
        sprite = new TestSprite(spriteId, -4, MID_Y);
        assertThat(offScreenAnySide(sprite), is(false));
        assertThat(offScreenBottom(sprite), is(false));
        assertThat(offScreenTop(sprite), is(false));
    }

    // tests sprite just off right on screen
    @Test
    public void shouldBeOffScreenAtRight() {
        sprite = new TestSprite(spriteId, GAME_WIDTH + 5, MID_Y);
        assertThat(offScreenAnySide(sprite), is(true));
        assertThat(offScreenBottom(sprite), is(false));
        assertThat(offScreenTop(sprite), is(false));
    }

    // tests sprite just on right on screen
    @Test
    public void shouldBeOnScreenAtRight() {
        sprite = new TestSprite(spriteId, GAME_WIDTH + 4, MID_Y);
        assertThat(offScreenAnySide(sprite), is(false));
        assertThat(offScreenBottom(sprite), is(false));
        assertThat(offScreenTop(sprite), is(false));
    }
}
