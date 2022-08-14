package com.danosoftware.galaxyforce.sprites.common;

public interface IMovingSprite extends ISprite {

  void move(float x, float y);

  void moveByDelta(float xDelta, float yDelta);

  void moveY(float y);

  void moveYByDelta(float yDelta);

  void moveXByDelta(float xDelta);

  void animate(float deltaTime);

  void rotate(float rotation);
}
