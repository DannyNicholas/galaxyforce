package com.danosoftware.galaxyforce.waves;

public final class AlienCharacterWithEnergy {

  private final AlienCharacter alienCharacter;
  private final int energy;

  public AlienCharacterWithEnergy(AlienCharacter alienCharacter, int energy) {
    this.alienCharacter = alienCharacter;
    this.energy = energy;
  }

  public AlienCharacter getAlienCharacter() {
    return this.alienCharacter;
  }

  public int getEnergy() {
    return this.energy;
  }
}
