package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteState;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.textures.TextureRegion;

public interface ISprite {

    void move(int x, int y);

    void rotate(int angle);

    void changeType(ISpriteIdentifier type);

    void changeStatus(SpriteState state);

    int width();

    int height();

    int rotation();

    int x();

    int y();

    TextureRegion textureRegion();
}
