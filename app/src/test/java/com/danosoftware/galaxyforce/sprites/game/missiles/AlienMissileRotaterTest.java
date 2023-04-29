package com.danosoftware.galaxyforce.sprites.game.missiles;

import static com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileRotater.calculateAngle;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileRotateCalculation;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies angles calculated from missile position to base position.
 */
public class AlienMissileRotaterTest {

  private final static float MISSILE_X = 100f;
  private final static float MISSILE_Y = 100f;
  private final IAlienMissile missile = mock(IAlienMissile.class);
  private final static float TWO_PI = (float) (2f * Math.PI);

  @BeforeEach
  public void setUp() {
    when(missile.x()).thenReturn(MISSILE_X);
    when(missile.y()).thenReturn(MISSILE_Y);
  }

  @Test
  public void angleShouldBeDownwardsIfBaseIsNull() {
    final AlienMissileRotateCalculation calculation = calculateAngle(missile, null);
    assertThat(calculation.getAngle(), equalTo((float) Math.atan2(-1, 0)));
    assertThat((int) calculation.getRotation(), equalTo(0));
  }

  @Test
  public void verifyAngleWhenBaseIsDirectlyBelowMissile() {
    IBasePrimary base = mock(IBasePrimary.class);
    when(base.x()).thenReturn(MISSILE_X);
    when(base.y()).thenReturn(MISSILE_Y - 100);

    final AlienMissileRotateCalculation calculation = calculateAngle(missile, base);
    assertThat(calculation.getAngle(), equalTo((float) Math.atan2(-1, 0)));
    assertThat((int) calculation.getRotation(), equalTo(0));
  }

  @Test
  public void verifyAngleWhenBaseIsDirectlyAboveMissile() {
    IBasePrimary base = mock(IBasePrimary.class);
    when(base.x()).thenReturn(MISSILE_X);
    when(base.y()).thenReturn(MISSILE_Y + 100);

    final AlienMissileRotateCalculation calculation = calculateAngle(missile, base);
    assertThat(calculation.getAngle(), equalTo((float) Math.atan2(1, 0)));
    assertThat((int) calculation.getRotation(), equalTo(180));
  }

  @Test
  public void verifyAngleWhenBaseIsDirectlyRightOfMissile() {
    IBasePrimary base = mock(IBasePrimary.class);
    when(base.x()).thenReturn(MISSILE_X + 100);
    when(base.y()).thenReturn(MISSILE_Y);

    final AlienMissileRotateCalculation calculation = calculateAngle(missile, base);
    assertThat(calculation.getAngle(), equalTo((float) Math.atan2(0, 1)));
    assertThat((int) calculation.getRotation(), equalTo(90));
  }

  @Test
  public void verifyAngleWhenBaseIsDirectlyLeftOfMissile() {
    IBasePrimary base = mock(IBasePrimary.class);
    when(base.x()).thenReturn(MISSILE_X - 100);
    when(base.y()).thenReturn(MISSILE_Y);

    final AlienMissileRotateCalculation calculation = calculateAngle(missile, base);
    // adjusted by 2PI to fix bug so missile travels more direct path
    assertThat(calculation.getAngle(), equalTo((float) (Math.atan2(0, -1) - TWO_PI)));
    assertThat((int) calculation.getRotation(), equalTo(180 + 90 - 360));
  }


  @Test
  public void verifyAngleWhenBaseIsDownAndRightFromMissile() {
    IBasePrimary base = mock(IBasePrimary.class);
    when(base.x()).thenReturn(MISSILE_X + 100);
    when(base.y()).thenReturn(MISSILE_Y - 100);

    final AlienMissileRotateCalculation calculation = calculateAngle(missile, base);
    assertThat(calculation.getAngle(), equalTo((float) Math.atan2(-1, 1)));
    assertThat((int) calculation.getRotation(), equalTo(45));
  }

  @Test
  public void verifyAngleWhenBaseIsDownAndLeftFromMissile() {
    IBasePrimary base = mock(IBasePrimary.class);
    when(base.x()).thenReturn(MISSILE_X - 100);
    when(base.y()).thenReturn(MISSILE_Y - 100);

    final AlienMissileRotateCalculation calculation = calculateAngle(missile, base);
    assertThat(calculation.getAngle(), equalTo((float) Math.atan2(-1, -1)));
    assertThat((int) calculation.getRotation(), equalTo(-45));
  }

  @Test
  public void verifyAngleWhenBaseIsUpAndRightFromMissile() {
    IBasePrimary base = mock(IBasePrimary.class);
    when(base.x()).thenReturn(MISSILE_X + 100);
    when(base.y()).thenReturn(MISSILE_Y + 100);

    final AlienMissileRotateCalculation calculation = calculateAngle(missile, base);
    assertThat(calculation.getAngle(), equalTo((float) Math.atan2(1, 1)));
    assertThat((int) calculation.getRotation(), equalTo(90 + 45));
  }

  @Test
  public void verifyAngleWhenBaseIsUpAndLeftFromMissile() {
    IBasePrimary base = mock(IBasePrimary.class);
    when(base.x()).thenReturn(MISSILE_X - 100);
    when(base.y()).thenReturn(MISSILE_Y + 100);

    final AlienMissileRotateCalculation calculation = calculateAngle(missile, base);
    // adjusted by 2PI to fix bug so missile travels more direct path
    assertThat(calculation.getAngle(), equalTo((float) (Math.atan2(1, -1) - TWO_PI)));
    assertThat((int) calculation.getRotation(), equalTo(180 + 45 - 360));
  }
}
