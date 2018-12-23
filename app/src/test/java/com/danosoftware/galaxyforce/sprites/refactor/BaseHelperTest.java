package com.danosoftware.galaxyforce.sprites.refactor;

import android.util.Log;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.models.screens.game.handlers.IGameHandler;
import com.danosoftware.galaxyforce.sound.SoundEffectBank;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.BaseHelper;
import com.danosoftware.galaxyforce.sprites.game.bases.IBaseHelper;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileSimple;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.danosoftware.galaxyforce.enumerations.BaseMissileType.SIMPLE;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.ACTIVE;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState.EXPLODING;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.LEFT;
import static com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide.RIGHT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, SoundEffectBankSingleton.class, VibrationSingleton.class, Textures.class})
public class BaseHelperTest {

    private static final int INITIAL_X = 100;
    private static final int INITIAL_Y = 200;
    private static final int EXPECTED_OFFSET = 64;
    private static final boolean SHIELD_UP = true;
    private static final boolean SHIELD_DOWN = false;
    private static final float SHIELD_SYNC_OFFSET = 0.5f;

    private final IBasePrimary primaryBase = mock(IBasePrimary.class);
    private final IGameHandler model = mock(IGameHandler.class);

    private IBaseHelper baseHelper;

    @Before
    public void setup() {
        // mock any static android logging
        mockStatic(Log.class);

        SoundEffectBank soundEffectBank = mock(SoundEffectBank.class);
        mockStatic(SoundEffectBankSingleton.class);
        when(SoundEffectBankSingleton.getInstance()).thenReturn(soundEffectBank);

        VibrationSingleton vibration = mock(VibrationSingleton.class);
        mockStatic(VibrationSingleton.class);
        when(VibrationSingleton.getInstance()).thenReturn(vibration);

        final TextureDetail mockTextureDetail = new TextureDetail("mock", 0, 0, 0, 0);
        mockStatic(Textures.class);
        when(Textures.getTextureDetail(any(String.class))).thenReturn(mockTextureDetail);

        Texture mockTexture = mock(Texture.class);
        for (GameSpriteIdentifier spriteId : GameSpriteIdentifier.values()) {
            spriteId.updateProperties(mockTexture);
        }

        when(primaryBase.x()).thenReturn(INITIAL_X);
        when(primaryBase.y()).thenReturn(INITIAL_Y);
    }


    private IBaseHelper shieldedHelper(HelperSide side) {
        return createHelper(side, SHIELD_UP, SHIELD_SYNC_OFFSET);
    }

    private IBaseHelper unShieldedHelper(HelperSide side) {
        return createHelper(side, SHIELD_DOWN, 0f);
    }

    @Test()
    public void shouldConstructLeftBaseInExpectedPosition() {
        baseHelper = unShieldedHelper(LEFT);
        assertThat(baseHelper.x(), is(INITIAL_X - EXPECTED_OFFSET));
        assertThat(baseHelper.y(), is(INITIAL_Y));
    }

    @Test()
    public void shouldConstructRightBaseInExpectedPosition() {
        baseHelper = unShieldedHelper(RIGHT);
        assertThat(baseHelper.x(), is(INITIAL_X + EXPECTED_OFFSET));
        assertThat(baseHelper.y(), is(INITIAL_Y));
    }

    @Test()
    public void shouldMoveLeftBase() {
        baseHelper = unShieldedHelper(LEFT);
        baseHelper.move(300, 400);
        assertThat(baseHelper.x(), is(300 - EXPECTED_OFFSET));
        assertThat(baseHelper.y(), is(400));
    }

    @Test()
    public void shouldMoveRightBase() {
        baseHelper = unShieldedHelper(RIGHT);
        baseHelper.move(300, 400);
        assertThat(baseHelper.x(), is(300 + EXPECTED_OFFSET));
        assertThat(baseHelper.y(), is(400));
    }

    @Test()
    public void destroyedHelperShouldNotMove() {
        baseHelper = unShieldedHelper(RIGHT);
        baseHelper.destroy();
        baseHelper.move(300, 400);

        // confirm helper has not moved
        assertThat(baseHelper.x(), is(INITIAL_X + EXPECTED_OFFSET));
        assertThat(baseHelper.y(), is(INITIAL_Y));
    }

    @Test()
    public void shouldConstructWithShield() {
        baseHelper = shieldedHelper(LEFT);
        List<ISprite> sprites = baseHelper.allSprites();
        verifyShieldExists(sprites);
    }

    @Test()
    public void shouldAddShield() {
        baseHelper = unShieldedHelper(LEFT);
        List<ISprite> sprites = baseHelper.allSprites();
        verifyShieldDoesNotExists(sprites);

        baseHelper.addShield(SHIELD_SYNC_OFFSET);
        sprites = baseHelper.allSprites();
        verifyShieldExists(sprites);
    }

    @Test()
    public void shouldRemoveShield() {
        baseHelper = shieldedHelper(LEFT);
        List<ISprite> sprites = baseHelper.allSprites();
        verifyShieldExists(sprites);

        baseHelper.removeShield();
        sprites = baseHelper.allSprites();
        verifyShieldDoesNotExists(sprites);
    }

    @Test()
    public void baseShouldExplodeWhenDestroyed() throws NoSuchFieldException, IllegalAccessException {
        baseHelper = unShieldedHelper(LEFT);
        baseHelper.destroy();
        assertThat(helperState(baseHelper), is(EXPLODING));
    }

    @Test()
    public void shouldTellPrimaryThatHelperBaseIsDestroyedAfterExploding() {
        baseHelper = shieldedHelper(LEFT);
        baseHelper.destroy();
        baseHelper.animate(1f);
        verify(primaryBase, times(1)).helperRemoved(LEFT);
    }

    @Test()
    public void unshieldedBaseShouldExplodeWhenHitByMissile() throws NoSuchFieldException, IllegalAccessException {
        baseHelper = unShieldedHelper(LEFT);
        baseHelper.onHitBy(mock(IAlienMissile.class));
        assertThat(helperState(baseHelper), is(EXPLODING));
    }

    @Test()
    public void shieldedBaseShouldNotExplodeWhenHitByMissile() throws NoSuchFieldException, IllegalAccessException {
        baseHelper = shieldedHelper(LEFT);
        baseHelper.onHitBy(mock(IAlienMissile.class));
        assertThat(helperState(baseHelper), is(ACTIVE));
    }

    @Test()
    public void unshieldedBaseShouldExplodeWhenHitByAlien() throws NoSuchFieldException, IllegalAccessException {
        baseHelper = unShieldedHelper(LEFT);
        baseHelper.onHitBy(mock(IAlien.class));
        assertThat(helperState(baseHelper), is(EXPLODING));
    }

    @Test()
    public void shieldedBaseShouldNotExplodeWhenHitByAlien() throws NoSuchFieldException, IllegalAccessException {
        baseHelper = shieldedHelper(LEFT);
        baseHelper.onHitBy(mock(IAlien.class));
        assertThat(helperState(baseHelper), is(ACTIVE));
    }

    @Test()
    public void shouldCallPrimaryBasePowerUp() {
        IPowerUp energyPowerUp = new PowerUp(GameSpriteIdentifier.POWERUP_BATTERY, 0, 0, PowerUpType.ENERGY);
        baseHelper = shieldedHelper(LEFT);
        baseHelper.collectPowerUp(energyPowerUp);
        verify(primaryBase, times(1)).collectPowerUp(energyPowerUp);
    }

    @Test()
    public void shouldFireMissile() {
        baseHelper = shieldedHelper(LEFT);
        BaseMissileBean missile = baseHelper.fire(SIMPLE);
        assertThat(missile.getMissiles().size() > 0, is(true));
        for (IBaseMissile aMissile : missile.getMissiles()) {
            assertThat(aMissile instanceof BaseMissileSimple, is(true));
        }
    }

    // Verify that helper base has a shield
    private void verifyShieldExists(List<ISprite> sprites) {
        assertThat(sprites.size(), is(2));
        int countShields = 0;
        for (ISprite sprite : sprites) {
            if (sprite instanceof IBaseShield) {
                countShields++;
                IBaseShield shield = (IBaseShield) sprite;
                assertThat(shield.getSynchronisation(), is(SHIELD_SYNC_OFFSET));
            }
        }
        assertThat(countShields, is(1));
    }

    // Verify that helper base does not have a shield
    private void verifyShieldDoesNotExists(List<ISprite> sprites) {
        assertThat(sprites.size(), is(1));
        int countShields = 0;
        for (ISprite sprite : sprites) {
            if (sprite instanceof IBaseShield) {
                countShields++;
            }
        }
        assertThat(countShields, is(0));
    }

    // use reflection to get helper internal state
    private BaseState helperState(IBaseHelper baseHelper) throws NoSuchFieldException, IllegalAccessException {
        Field f = baseHelper.getClass().getDeclaredField("state");
        f.setAccessible(true);
        return (BaseState) f.get(baseHelper);
    }

    // use reflection to create a helper using the private constructor
    private IBaseHelper createHelper(HelperSide side, boolean shielded, float shieldSyncTime) {
        try {
            Constructor<BaseHelper> constructor = BaseHelper.class.getDeclaredConstructor(
                    IBasePrimary.class,
                    IGameHandler.class,
                    HelperSide.class,
                    boolean.class,
                    float.class);
            constructor.setAccessible(true);
            BaseHelper helper = constructor.newInstance(
                    primaryBase,
                    model,
                    side,
                    shielded,
                    shieldSyncTime);
            return helper;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}