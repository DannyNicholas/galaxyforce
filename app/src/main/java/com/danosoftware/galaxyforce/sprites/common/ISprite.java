package com.danosoftware.galaxyforce.sprites.common;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public interface ISprite {

    void changeType(ISpriteIdentifier type);

    int width();

    int height();

    int halfWidth();

    int halfHeight();

    int rotation();

    int x();

    int y();

    ISpriteIdentifier spriteId();
}
