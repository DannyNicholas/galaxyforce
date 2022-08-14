package com.danosoftware.galaxyforce.sprites.game.bases.explode;

import java.util.List;

public interface IBaseMultiExploder extends IBaseExploder {

  /**
   * Get the supporting multi-explosion sprites.
   */
  List<IBaseExplosion> getMultiExplosion();
}
