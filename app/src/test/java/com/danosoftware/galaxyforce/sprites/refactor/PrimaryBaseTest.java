package com.danosoftware.galaxyforce.sprites.refactor;

import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.sound.SoundEffectBank;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteProperties;
import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureDetail;
import com.danosoftware.galaxyforce.textures.TextureRegion;
import com.danosoftware.galaxyforce.textures.Textures;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.sprites.refactor.BaseState.ACTIVE;
import static com.danosoftware.galaxyforce.sprites.refactor.BaseState.EXPLODING;
import static com.danosoftware.galaxyforce.sprites.refactor.HelperSide.LEFT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, SoundEffectBankSingleton.class, VibrationSingleton.class, Textures.class})
public class PrimaryBaseTest {

    private static final int INITIAL_X = 100;
    private static final int INITIAL_Y = 100;

    TextureDetail mockTextureDetail = new TextureDetail("mock",0,0,0,0);

    private IBaseMainSprite primaryBase;

    @Before
    public void setUp() {
        // mock any static android logging
        mockStatic(Log.class);

        SoundEffectBank soundEffectBank = mock(SoundEffectBank.class);
        mockStatic(SoundEffectBankSingleton.class);
        when(SoundEffectBankSingleton.getInstance()).thenReturn(soundEffectBank);

        VibrationSingleton vibration = mock(VibrationSingleton.class);
        mockStatic(VibrationSingleton.class);
        when(VibrationSingleton.getInstance()).thenReturn(vibration);

        Textures mockTextures = mock(Textures.class);
        mockStatic(Textures.class);
        when(Textures.getTextureDetail(any(String.class))).thenReturn(mockTextureDetail);

        Texture mockTexture = mock(Texture.class);
        for (GameSpriteIdentifier spriteId : GameSpriteIdentifier.values()) {
            spriteId.updateProperties(mockTexture);
        }

        primaryBase = new BaseMain(INITIAL_X, INITIAL_Y);
    }


    @Test()
    public void shouldMoveBaseX() {
        primaryBase.moveBase(1,0,0.5f);
        assertThat(primaryBase.x(), is(INITIAL_X + 300));
        assertThat(primaryBase.y(), is(INITIAL_Y));
    }

    @Test()
    public void shouldMoveBaseY() {
        primaryBase.moveBase(0,1,0.5f);
        assertThat(primaryBase.x(), is(INITIAL_X));
        assertThat(primaryBase.y(), is(INITIAL_Y + 300));
    }

    // this test will move test sprite to max position
    // since mocked width and height are 0.
    // normal sprite will take its size into account.
    @Test()
    public void shouldNotMoveBeyondMaximumPosition() {
        primaryBase.moveBase(1,1,10f);
        assertThat(primaryBase.x(), is(GAME_WIDTH));
        assertThat(primaryBase.y(), is(GAME_HEIGHT));
    }

    // this test will move test sprite to origin position
    // since mocked width and height are 0.
    // normal sprite will take its size into account.
    @Test()
    public void shouldNotMoveBeyondOrigin() {
        primaryBase.moveBase(-1,-1,10f);
        assertThat(primaryBase.x(), is(0));
        assertThat(primaryBase.y(), is(0));
    }

    @Test()
    public void baseShouldExplodeWhenDestroyed() throws NoSuchFieldException, IllegalAccessException {
        primaryBase.destroy();
        assertThat(helperState(primaryBase), is(EXPLODING));
    }

    @Test()
    public void baseShouldExplodeWhenHitByPowerfulMissile() throws NoSuchFieldException, IllegalAccessException {
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(100);
        primaryBase.onHitBy(missile);
        assertThat(helperState(primaryBase), is(EXPLODING));
    }

    @Test()
    public void baseShouldRemainActiveWhenHitByWeakMissile() throws NoSuchFieldException, IllegalAccessException {
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(1);
        primaryBase.onHitBy(missile);
        assertThat(helperState(primaryBase), is(ACTIVE));
    }

    @Test()
    public void baseShouldExplodeWhenHitByAlien() throws NoSuchFieldException, IllegalAccessException {
        primaryBase.onHitBy(mock(IAlien.class));
        assertThat(helperState(primaryBase), is(EXPLODING));
    }


    @Test
    public void helperBaseShouldExplodeWithPrimaryBase() {

    }

    // use reflection to get helper internal state
    private BaseState helperState(IBaseMainSprite helper) throws NoSuchFieldException, IllegalAccessException {
        Field f = helper.getClass().getDeclaredField("state");
        f.setAccessible(true);
        return (BaseState) f.get(helper);
    }


}
