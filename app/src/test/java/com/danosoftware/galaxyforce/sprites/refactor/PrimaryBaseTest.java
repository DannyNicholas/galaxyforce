package com.danosoftware.galaxyforce.sprites.refactor;

import android.util.Log;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sound.SoundEffectBank;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.BasePrimary;
import com.danosoftware.galaxyforce.sprites.game.bases.IBaseHelper;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.game.powerups.PowerUp;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureDetail;
import com.danosoftware.galaxyforce.textures.Textures;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.EXPLODING;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.LEFT;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.RIGHT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, SoundEffectBankSingleton.class, VibrationSingleton.class, Textures.class})
public class PrimaryBaseTest {

    private static final int INITIAL_X = 100;
    private static final int INITIAL_Y = 100;

    TextureDetail mockTextureDetail = new TextureDetail("mock",0,0,0,0);

    private IBasePrimary primaryBase;
    private IBasePrimary primaryBaseSpy;
    private IBaseHelper leftHelper;
    private IBaseHelper rightHelper;

    private GameHandler model;


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

        model = mock(GameHandler.class);
        primaryBase = new BasePrimary(model);
        primaryBaseSpy = spy(primaryBase);

        leftHelper = mock(IBaseHelper.class);
        rightHelper = mock(IBaseHelper.class);
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
    public void shouldNotMoveAfterBeingDestroyed() {
        primaryBase.destroy();
        primaryBase.moveBase(1,1,5f);

        // confirm base hasn't moved
        assertThat(primaryBase.x(), is(INITIAL_X));
        assertThat(primaryBase.y(), is(INITIAL_Y));
    }

    @Test()
    public void baseShouldExplodeWhenDestroyed() throws NoSuchFieldException, IllegalAccessException {
        primaryBase.destroy();
        assertThat(baseState(primaryBase), is(EXPLODING));

        assertThat(primaryBase.allSprites(), hasItem(primaryBase));
        assertThat(primaryBase.activeBases(), not(hasItem(primaryBase)));
    }

    @Test()
    public void baseShouldBeDestroyedWhenHitByPowerfulMissile() {
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(100);
        primaryBaseSpy.onHitBy(missile);
        verify(primaryBaseSpy, times(1)).destroy();
    }

    @Test()
    public void shieldedBaseShouldNotBeDestroyedWhenHitByMissile() {
        IPowerUp shieldPowerUp = new PowerUp(GameSpriteIdentifier.POWERUP_SHIELD, 0, 0, PowerUpType.SHIELD);
        primaryBaseSpy.collectPowerUp(shieldPowerUp);
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(100);
        primaryBaseSpy.onHitBy(missile);
        verify(primaryBaseSpy, times(0)).destroy();
    }

    @Test()
    public void baseShouldRemainActiveWhenHitByWeakMissile() {
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(1);
        primaryBaseSpy.onHitBy(missile);
        verify(primaryBaseSpy, times(0)).destroy();
    }

    @Test()
    public void baseShouldBeDestroyedAfterEightHits() {
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(1);
        for (int i = 0; i < 7; i++) {
            primaryBaseSpy.onHitBy(missile);
            verify(primaryBaseSpy, times(0)).destroy();
        }
        primaryBaseSpy.onHitBy(missile);
        verify(primaryBaseSpy, times(1)).destroy();
    }

    @Test()
    public void baseShouldBeDestroyedWhenHitByAlien() {
        primaryBaseSpy.onHitBy(mock(IAlien.class));
        verify(primaryBaseSpy, times(1)).destroy();
    }

    @Test()
    public void shieldedBaseShouldNotBeDestroyedWhenHitByAlien() {
        IPowerUp shieldPowerUp = new PowerUp(GameSpriteIdentifier.POWERUP_SHIELD, 0, 0, PowerUpType.SHIELD);
        primaryBaseSpy.collectPowerUp(shieldPowerUp);
        primaryBaseSpy.onHitBy(mock(IAlien.class));
        verify(primaryBaseSpy, times(0)).destroy();
    }

    @Test
    public void baseShouldFireMissile() {
        primaryBase.animate(1f);
        verify(model, atLeastOnce()).fireBaseMissiles(any(BaseMissileBean.class));
    }

    @Test
    public void baseShouldNotFireMissileWhenDestroyed() {
        primaryBase.destroy();
        primaryBase.animate(1f);
        verify(model, times(0)).fireBaseMissiles(any(BaseMissileBean.class));
    }

    @Test
    public void baseShouldCollectPowerUp() throws NoSuchFieldException, IllegalAccessException {
        IPowerUp guidedMissilePowerUp = new PowerUp(GameSpriteIdentifier.POWERUP_MISSILE_GUIDED, 0, 0, PowerUpType.MISSILE_GUIDED);
        primaryBase.collectPowerUp(guidedMissilePowerUp);
        BaseMissileType missileType = missileType(primaryBase);
        assertThat(missileType, is(BaseMissileType.GUIDED));
    }

    // this may not be a valid test since destroyed base will never collide with power-up
    @Test
    public void baseShouldNotCollectPowerUpWhenDestroyed() throws NoSuchFieldException, IllegalAccessException {
        IPowerUp guidedMissilePowerUp = new PowerUp(GameSpriteIdentifier.POWERUP_MISSILE_GUIDED, 0, 0, PowerUpType.MISSILE_GUIDED);
        primaryBase.destroy();
        primaryBase.collectPowerUp(guidedMissilePowerUp);
        BaseMissileType missileType = missileType(primaryBase);
        assertThat(missileType, not(BaseMissileType.GUIDED));
    }









    ///////
    /// BASE AND HELPER
    ////////


    @Test
    public void helperBasesShouldBeDestroyedWithPrimaryBase() {

        // return list containing helper base when allSprites() is called on each one
        when(leftHelper.allSprites()).thenReturn(Arrays.asList((ISprite) leftHelper));
        when(rightHelper.allSprites()).thenReturn(Arrays.asList((ISprite) rightHelper));

        // call primaryBase.helperExploding() when mock helpers are destroyed
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                primaryBase.helperExploding(LEFT);
                return null;
            }
        }).when(leftHelper).destroy();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                primaryBase.helperExploding(RIGHT);
                return null;
            }
        }).when(rightHelper).destroy();

        // add mock helpers to primary base
        primaryBase.helperCreated(LEFT, leftHelper);
        primaryBase.helperCreated(RIGHT, rightHelper);

        // assert helpers are in sprite list and active
        assertThat(primaryBase.allSprites(), hasItem(leftHelper));
        assertThat(primaryBase.activeBases(), hasItem(leftHelper));
        assertThat(primaryBase.allSprites(), hasItem(rightHelper));
        assertThat(primaryBase.activeBases(), hasItem(rightHelper));

        // destroy primary base
        primaryBase.destroy();

        // verify helper bases were also destroyed
        verify(leftHelper, times(1)).destroy();
        verify(rightHelper, times(1)).destroy();

        // assert helpers are in sprite list but no longer active
        assertThat(primaryBase.allSprites(), hasItem(leftHelper));
        assertThat(primaryBase.activeBases(), not(hasItem(leftHelper)));
        assertThat(primaryBase.allSprites(), hasItem(rightHelper));
        assertThat(primaryBase.activeBases(), not(hasItem(rightHelper)));
    }

    @Test
    public void helperBasesShouldBeShieldedWithPrimaryBase() {

        // add mock helpers to primary base
        primaryBase.helperCreated(LEFT, leftHelper);
        primaryBase.helperCreated(RIGHT, rightHelper);

        // add shield to primary base
        IPowerUp shieldPowerUp = new PowerUp(GameSpriteIdentifier.POWERUP_SHIELD, 0, 0, PowerUpType.SHIELD);
        primaryBase.collectPowerUp(shieldPowerUp);

        // verify helper bases were also given shields
        verify(leftHelper, times(1)).addShield(any(float.class));
        verify(rightHelper, times(1)).addShield(any(float.class));

        // count number of primary base shields
        Long shields = primaryBase.allSprites().stream().filter(new Predicate<ISprite>() {
            @Override
            public boolean test(ISprite iSprite) {
                return iSprite instanceof BaseShield;
            }
        }).count();
        assertThat(shields, is(1L));
    }

    @Test
    public void helperBasesShouldRemoveShieldedWithPrimaryBase() {

        // add mock helpers to primary base
        primaryBase.helperCreated(LEFT, leftHelper);
        primaryBase.helperCreated(RIGHT, rightHelper);

        // add shield to primary base
        IPowerUp shieldPowerUp = new PowerUp(GameSpriteIdentifier.POWERUP_SHIELD, 0, 0, PowerUpType.SHIELD);
        primaryBase.collectPowerUp(shieldPowerUp);
        primaryBase.animate(20f);

        // verify helper bases were also given shields
        verify(leftHelper, times(1)).removeShield();
        verify(rightHelper, times(1)).removeShield();

        // count number of primary base shields
        Long shields = primaryBase.allSprites().stream().filter(new Predicate<ISprite>() {
            @Override
            public boolean test(ISprite iSprite) {
                return iSprite instanceof BaseShield;
            }
        }).count();
        assertThat(shields, is(0L));
    }


    @Test
    public void helperBasesShouldFireWithPrimaryBase() {

        // add mock helpers to primary base
        primaryBase.helperCreated(LEFT, leftHelper);
        primaryBase.helperCreated(RIGHT, rightHelper);

        primaryBase.animate(1f);

        // verify helper bases fired
        verify(leftHelper, atLeastOnce()).fire(any(BaseMissileType.class));
        verify(rightHelper, atLeastOnce()).fire(any(BaseMissileType.class));
    }

    // use reflection to get helper internal state
    private List<IBaseHelper> helpers(IBasePrimary base) throws NoSuchFieldException, IllegalAccessException {
        Field f = base.getClass().getDeclaredField("helpers");
        f.setAccessible(true);
        return (List) f.get(base);
    }

    // use reflection to get base internal state
    private BaseState baseState(IBasePrimary helper) throws NoSuchFieldException, IllegalAccessException {
        Field f = helper.getClass().getDeclaredField("state");
        f.setAccessible(true);
        return (BaseState) f.get(helper);
    }

    // use reflection to get base internal state
    private BaseMissileType missileType(IBasePrimary base) throws NoSuchFieldException, IllegalAccessException {
        Field f = base.getClass().getDeclaredField("baseMissileType");
        f.setAccessible(true);
        return (BaseMissileType) f.get(base);
    }

        // use reflection to get helper internal state
        private void verifyHelperState(IBasePrimary base, BaseState expectedState) throws NoSuchFieldException, IllegalAccessException {
        Field f = base.getClass().getDeclaredField("helpers");
        f.setAccessible(true);
            List<IBaseHelper> helpers = (List) f.get(base);
        assertThat(helpers.size(), is(2));
            for (IBaseHelper helper : helpers) {
            Field fh = helper.getClass().getDeclaredField("state");
            fh.setAccessible(true);
            BaseState state = (BaseState) fh.get(helper);
            assertThat(state, is(expectedState));
        }
    }


}
