package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteState;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.textures.TextureRegion;

public interface IMovingSprite extends ISprite {

    void move(int x, int y);

    void rotate(int rotation);

    int rotation();
}
