package com.danosoftware.galaxyforce.sprites.common;

public interface IMovingSprite extends ISprite {

    void move(int x, int y);

    void animate(float deltaTime);
}
