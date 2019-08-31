package com.danosoftware.galaxyforce.sprites.common;

public interface IMovingSprite extends ISprite {

    void move(int x, int y);

    void moveByDelta(int xDelta, int yDelta);

    void moveY(int y);

    void moveYByDelta(int yDelta);

    void animate(float deltaTime);

    void rotate(int rotation);
}
