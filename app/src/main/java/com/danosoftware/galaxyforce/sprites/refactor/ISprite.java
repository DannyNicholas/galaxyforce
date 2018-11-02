package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public interface ISprite {

    void changeType(ISpriteIdentifier type);

    int width();

    int height();

    int rotation();

    int x();

    int y();

    ISpriteIdentifier spriteId();
}
