package com.danosoftware.galaxyforce.sprites.game.bases;

import android.util.Log;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseState;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.HelperSide;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileUpwards;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.game.powerups.PowerUp;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureDetail;
import com.danosoftware.galaxyforce.textures.TextureService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.danosoftware.galaxyforce.enumerations.BaseMissileType.NORMAL;
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
@PrepareForTest({Log.class, TextureService.class})
public class BaseHelperTest {

    private static final int INITIAL_X = 100;
    private static final int INITIAL_Y = 200;
    private static final int EXPECTED_HELPER_X_OFFSET = 64;
    private static final int EXPECTED_HELPER_Y_OFFSET = 18;
    private static final boolean SHIELD_UP = true;
    private static final boolean SHIELD_DOWN = false;
    private static final float SHIELD_SYNC_OFFSET = 0.5f;

    private final IBasePrimary primaryBase = mock(IBasePrimary.class);
    private final GameModel model = mock(GameModel.class);
    private final SoundPlayerService sounds = mock(SoundPlayerService.class);
    private final VibrationService vibrator = mock(VibrationService.class);

    private IBaseHelper baseHelper;

    @Before
    public void setup() {
        // mock any static android logging
        mockStatic(Log.class);

        final TextureDetail mockTextureDetail = new TextureDetail("mock", "0", "0", "0", "0");
        Texture mockTexture = mock(Texture.class);
        when(mockTexture.getTextureDetail(any(String.class))).thenReturn(mockTextureDetail);
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
        assertThat(baseHelper.x(), is(INITIAL_X - EXPECTED_HELPER_X_OFFSET));
        assertThat(baseHelper.y(), is(INITIAL_Y + EXPECTED_HELPER_Y_OFFSET));
    }

    @Test()
    public void shouldConstructRightBaseInExpectedPosition() {
        baseHelper = unShieldedHelper(RIGHT);
        assertThat(baseHelper.x(), is(INITIAL_X + EXPECTED_HELPER_X_OFFSET));
        assertThat(baseHelper.y(), is(INITIAL_Y + EXPECTED_HELPER_Y_OFFSET));
    }

    @Test()
    public void shouldMoveLeftBase() {
        baseHelper = unShieldedHelper(LEFT);
        baseHelper.move(300, 400);
        assertThat(baseHelper.x(), is(300 - EXPECTED_HELPER_X_OFFSET));
        assertThat(baseHelper.y(), is(400 + EXPECTED_HELPER_Y_OFFSET));
    }

    @Test()
    public void shouldMoveRightBase() {
        baseHelper = unShieldedHelper(RIGHT);
        baseHelper.move(300, 400);
        assertThat(baseHelper.x(), is(300 + EXPECTED_HELPER_X_OFFSET));
        assertThat(baseHelper.y(), is(400 + EXPECTED_HELPER_Y_OFFSET));
    }

    @Test()
    public void destroyedHelperShouldNotMove() {
        baseHelper = unShieldedHelper(RIGHT);
        baseHelper.destroy();
        baseHelper.move(300, 400);

        // confirm helper has not moved
        assertThat(baseHelper.x(), is(INITIAL_X + EXPECTED_HELPER_X_OFFSET));
        assertThat(baseHelper.y(), is(INITIAL_Y + EXPECTED_HELPER_Y_OFFSET));
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

        baseHelper.addSynchronisedShield(SHIELD_SYNC_OFFSET);
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
        IPowerUp parallelMissilePowerUp = new PowerUp(GameSpriteIdentifier.POWERUP_MISSILE_PARALLEL, 0, 0, PowerUpType.MISSILE_PARALLEL);
        baseHelper = shieldedHelper(LEFT);
        baseHelper.collectPowerUp(parallelMissilePowerUp);
        verify(primaryBase, times(1)).collectPowerUp(parallelMissilePowerUp);
    }

    @Test()
    public void shouldFireMissile() {
        baseHelper = shieldedHelper(LEFT);
        BaseMissilesDto missile = baseHelper.fire(NORMAL);
        assertThat(missile.getMissiles().size() > 0, is(true));
        for (IBaseMissile aMissile : missile.getMissiles()) {
            assertThat(aMissile instanceof BaseMissileUpwards, is(true));
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
                    GameModel.class,
                    SoundPlayerService.class,
                    VibrationService.class,
                    HelperSide.class,
                    boolean.class,
                    float.class);
            constructor.setAccessible(true);
            return constructor.newInstance(
                    primaryBase,
                    model,
                    sounds,
                    vibrator,
                    side,
                    shielded,
                    shieldSyncTime);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}