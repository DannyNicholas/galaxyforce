package com.danosoftware.galaxyforce.sprites.refactor;

public interface IMovingSprite extends ISprite {

    void move(int x, int y);

    void rotate(int rotation);

    void animate(float deltaTime);
}
