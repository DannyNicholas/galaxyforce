package com.danosoftware.galaxyforce.sprites.game.aliens;

import static com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState.ACTIVE;
import static com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState.DESTROYED;
import static com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState.EXPLODING;
import static com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState.WAITING;

import com.danosoftware.galaxyforce.sprites.common.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviour;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienCharacter;

public abstract class AbstractAlien extends AbstractCollidingSprite implements IAlien {

  /*
   * ******************************************************
   * PRIVATE INSTANCE VARIABLES
   * ******************************************************
   */

  /* reference to how alien explodes */
  private final ExplodeBehaviour explodeBehaviour;

  /* reference to how and when alien fires missiles */
  private final FireBehaviour fireBehaviour;

  /* reference to how alien creates power-ups */
  private final PowerUpBehaviour powerUpBehaviour;

  /* reference to how alien spawns other aliens */
  private final SpawnBehaviour spawnBehaviour;

  /* reference to how alien behaves when hit */
  private final HitBehaviour hitBehaviour;

  /* reference to how alien spins */
  private final SpinningBehaviour spinningBehaviour;

  /* state time used to help select the current animation frame */
  private float stateTime;

  // current energy level
  private int energy;

  /* has alien been destroyed */
  AlienState state;

  // alien character
  private AlienCharacter character;

  protected AbstractAlien(
      AlienCharacter character,
      float x,
      float y,
      int energy,
      FireBehaviour fireBehaviour,
      PowerUpBehaviour powerUpBehaviour,
      SpawnBehaviour spawnBehaviour,
      HitBehaviour hitBehaviour,
      ExplodeBehaviour explodeBehaviour,
      SpinningBehaviour spinningBehaviour) {

    super(
        character.getAnimation().getKeyFrame(
            0,
            Animation.ANIMATION_LOOPING),
        x,
        y);
    this.character = character;
    state = ACTIVE;
    this.energy = energy;
    this.explodeBehaviour = explodeBehaviour;
    this.fireBehaviour = fireBehaviour;
    this.powerUpBehaviour = powerUpBehaviour;
    this.spawnBehaviour = spawnBehaviour;
    this.hitBehaviour = hitBehaviour;
    this.spinningBehaviour = spinningBehaviour;
    this.stateTime = 0f;
  }

  @Override
  public AlienCharacter character() {
    return character;
  }

  @Override
  public void onHitBy(IBaseMissile baseMissile) {
    baseMissile.destroy();
    energy -= 1;
    if (energy <= 0) {
      explode();
    } else {
      hitBehaviour.startHit(stateTime);
    }
  }

  protected boolean isExploding() {
    return state == EXPLODING;
  }

  @Override
  public void explode() {
    explodeBehaviour.startExplosion(this);
    state = EXPLODING;
    powerUpBehaviour.releasePowerUp(this);
  }

  @Override
  public boolean isActive() {
    return (state == ACTIVE);
  }

  protected void activate() {
    state = ACTIVE;
  }

  @Override
  public boolean isVisible() {
    return (state == ACTIVE || state == EXPLODING);
  }

  @Override
  public void destroy() {
    state = DESTROYED;
  }

  @Override
  public boolean isDestroyed() {
    return (state == DESTROYED);
  }

  protected void waiting() {
    state = WAITING;
  }

  protected boolean isWaiting() {
    return (state == WAITING);
  }

  protected void changeCharacter(AlienCharacter character) {
    this.character = character;
  }

  @Override
  public void animate(float deltaTime) {

    if (state == ACTIVE) {

      // if alien is spinning then update alien
      if (spinningBehaviour.isSpinning()) {
        spinningBehaviour.spin(this, deltaTime);
      }

      // if alien is ready to fire - then fire!!
      if (fireBehaviour.readyToFire(deltaTime)) {
        fireBehaviour.fire(this);
      }

      // if alien is ready to spawn - spawn!!
      if (spawnBehaviour.readyToSpawn(deltaTime)) {
        spawnBehaviour.spawn(this);
      }

      // increase state time by delta
      stateTime += deltaTime;

      // if hit then continue hit animation
      if (hitBehaviour.isHit()) {
        changeType(hitBehaviour.getHit(character.getHitAnimation(), deltaTime));
      } else {
        // set base sprite using animation loop and time through animation
        changeType(
            character.getAnimation().getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));
      }
    }

    // if exploding then animate or set destroyed once finished
    if (state == EXPLODING) {

      // if alien is spinning then continue to spin while exploding
      if (spinningBehaviour.isSpinning()) {
        spinningBehaviour.spin(this, deltaTime);
      }

      if (explodeBehaviour.finishedExploding()) {
        destroy();
      } else {
        changeType(explodeBehaviour.getExplosion(deltaTime));
      }
    }
  }
}
