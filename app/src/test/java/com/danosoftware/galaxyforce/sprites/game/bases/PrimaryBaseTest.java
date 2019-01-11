package com.danosoftware.galaxyforce.sprites.game.bases;

import android.util.Log;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.game.powerups.PowerUp;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureDetail;
import com.danosoftware.galaxyforce.textures.Textures;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.Collections;
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
@PrepareForTest({Log.class, Textures.class})
public class PrimaryBaseTest {

    private final TextureDetail mockTextureDetail = new TextureDetail("mock", 0, 0, 0, 0);

    private IBasePrimary primaryBase;
    private IBasePrimary primaryBaseSpy;
    private IBaseHelper leftHelper;
    private IBaseHelper rightHelper;

    private GameModel model;
    private SoundPlayerService sounds;
    private VibrationService vibrator;


    @Before
    public void setUp() {
        // mock any static android logging
        mockStatic(Log.class);

        mockStatic(Textures.class);
        when(Textures.getTextureDetail(any(String.class))).thenReturn(mockTextureDetail);

        Texture mockTexture = mock(Texture.class);
        for (GameSpriteIdentifier spriteId : GameSpriteIdentifier.values()) {
            spriteId.updateProperties(mockTexture);
        }

        model = mock(GameModel.class);
        sounds = mock(SoundPlayerService.class);
        vibrator = mock(VibrationService.class);

        primaryBase = new BasePrimary(model, sounds, vibrator);
        primaryBaseSpy = spy(primaryBase);

        leftHelper = mock(IBaseHelper.class);
        rightHelper = mock(IBaseHelper.class);
    }


    @Test
    public void baseShouldBeActive() {
        assertThat(primaryBase.activeBases(), hasItem(primaryBase));
    }

    @Test()
    public void shouldMoveBaseX() {
        // move x left to right
        primaryBase.move(0, 0);
        primaryBase.moveTarget(300, 0);
        primaryBase.animate(10f);
        assertThat(primaryBase.x(), is(300));
        assertThat(primaryBase.y(), is(0));

        // move x right to left
        primaryBase.move(GAME_WIDTH, 0);
        primaryBase.moveTarget(300, 0);
        primaryBase.animate(10f);
        assertThat(primaryBase.x(), is(300));
        assertThat(primaryBase.y(), is(0));
    }

    @Test()
    public void shouldMoveBaseY() {
        // move y bottom to top
        primaryBase.move(0, 0);
        primaryBase.moveTarget(0, 300);
        primaryBase.animate(10f);
        assertThat(primaryBase.x(), is(0));
        assertThat(primaryBase.y(), is(300));

        // move y top to bottom
        primaryBase.move(0, GAME_HEIGHT);
        primaryBase.moveTarget(0, 300);
        primaryBase.animate(10f);
        assertThat(primaryBase.x(), is(0));
        assertThat(primaryBase.y(), is(300));
    }

    @Test()
    public void shouldNotMoveAfterBeingDestroyed() {
        primaryBase.move(0, 0);
        primaryBase.destroy();
        primaryBase.moveTarget(100, 100);
        primaryBase.animate(10f);

        // confirm base hasn't moved
        assertThat(primaryBase.x(), is(0));
        assertThat(primaryBase.y(), is(0));
    }

    @Test()
    public void baseShouldExplodeWhenDestroyed() throws NoSuchFieldException, IllegalAccessException {
        primaryBase.destroy();
        assertThat(baseState(primaryBase), is(EXPLODING));

        assertThat(primaryBase.allSprites(), hasItem(primaryBase));
        assertThat(primaryBase.activeBases(), not(hasItem(primaryBase)));
    }

    @Test()
    public void baseShouldBeDestroyedWhenHitByPowerfulMissile() throws NoSuchFieldException, IllegalAccessException {
        removeShield(primaryBase);
        IBasePrimary baseSpy = spy(primaryBase);
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(100);
        baseSpy.onHitBy(missile);
        verify(baseSpy, times(1)).destroy();
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
    public void baseShouldRemainActiveWhenHitByWeakMissile() throws NoSuchFieldException, IllegalAccessException {
        removeShield(primaryBase);
        IBasePrimary baseSpy = spy(primaryBase);
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(1);
        baseSpy.onHitBy(missile);
        verify(baseSpy, times(0)).destroy();
    }

    @Test()
    public void baseShouldBeDestroyedAfterEightHits() throws NoSuchFieldException, IllegalAccessException {
        removeShield(primaryBase);
        IBasePrimary baseSpy = spy(primaryBase);
        IAlienMissile missile = mock(IAlienMissile.class);
        when(missile.energyDamage()).thenReturn(1);
        for (int i = 0; i < 7; i++) {
            baseSpy.onHitBy(missile);
            verify(baseSpy, times(0)).destroy();
        }
        baseSpy.onHitBy(missile);
        verify(baseSpy, times(1)).destroy();
    }

    @Test()
    public void baseShouldBeDestroyedWhenHitByAlien() throws NoSuchFieldException, IllegalAccessException {
        removeShield(primaryBase);
        IBasePrimary baseSpy = spy(primaryBase);
        baseSpy.onHitBy(mock(IAlien.class));
        verify(baseSpy, times(1)).destroy();
    }

    @Test()
    public void shieldedBaseShouldNotBeDestroyedWhenHitByAlien() {
        IPowerUp shieldPowerUp = mock(IPowerUp.class);
        when(shieldPowerUp.getPowerUpType()).thenReturn(PowerUpType.SHIELD);
        primaryBaseSpy.collectPowerUp(shieldPowerUp);
        primaryBaseSpy.onHitBy(mock(IAlien.class));
        verify(primaryBaseSpy, times(0)).destroy();
    }

    @Test
    public void baseShouldFireMissile() {
        primaryBase.animate(1f);
        verify(model, atLeastOnce()).fireBaseMissiles(any(BaseMissilesDto.class));
    }

    @Test
    public void baseShouldNotFireMissileWhenDestroyed() {
        primaryBase.destroy();
        primaryBase.animate(1f);
        verify(model, times(0)).fireBaseMissiles(any(BaseMissilesDto.class));
    }

    @Test
    public void baseShouldCollectPowerUp() throws NoSuchFieldException, IllegalAccessException {
        IPowerUp guidedMissilePowerUp = mock(IPowerUp.class);
        when(guidedMissilePowerUp.getPowerUpType()).thenReturn(PowerUpType.MISSILE_GUIDED);
        primaryBase.collectPowerUp(guidedMissilePowerUp);

        BaseMissileType missileType = missileType(primaryBase);
        assertThat(missileType, is(BaseMissileType.GUIDED));
    }


    ///////
    /// BASE AND HELPER
    ////////


    @Test
    public void helperBasesShouldBeDestroyedWithPrimaryBase() {

        // return list containing helper base when allSprites() is called on each one
        when(leftHelper.allSprites()).thenReturn(Collections.singletonList((ISprite) leftHelper));
        when(rightHelper.allSprites()).thenReturn(Collections.singletonList((ISprite) rightHelper));

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
        IPowerUp shieldPowerUp = mock(IPowerUp.class);
        when(shieldPowerUp.getPowerUpType()).thenReturn(PowerUpType.SHIELD);
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

    // use reflection to get base internal state
    private BaseState baseState(IBasePrimary helper) throws NoSuchFieldException, IllegalAccessException {
        Field f = helper.getClass().getDeclaredField("state");
        f.setAccessible(true);
        return (BaseState) f.get(helper);
    }

    // use reflection to remove base shield - base shielded on construction
    private void removeShield(IBasePrimary base) throws NoSuchFieldException, IllegalAccessException {
        Field f = base.getClass().getDeclaredField("shielded");
        f.setAccessible(true);
        f.set(base, false);
    }

    // use reflection to get base internal state
    private BaseMissileType missileType(IBasePrimary base) throws NoSuchFieldException, IllegalAccessException {
        Field f = base.getClass().getDeclaredField("baseMissileType");
        f.setAccessible(true);
        return (BaseMissileType) f.get(base);
    }
}
