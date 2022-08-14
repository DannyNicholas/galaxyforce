package com.danosoftware.galaxyforce.sprites.common;

import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;

public interface ISprite {

  void changeType(SpriteDetails type);

  int width();

  int height();

  int halfWidth();

  int halfHeight();

  float rotation();

  float x();

  float y();

  SpriteDetails spriteDetails();
}
