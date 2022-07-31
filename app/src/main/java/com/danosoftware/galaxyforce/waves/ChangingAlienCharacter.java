package com.danosoftware.galaxyforce.waves;

import java.util.Arrays;
import java.util.List;

public enum ChangingAlienCharacter {

  FOXY(new AlienCharacter[]{
      AlienCharacter.FOXY_LARGE,
      AlienCharacter.FOXY_MEDIUM,
      AlienCharacter.FOXY,
      AlienCharacter.FOXY_SMALL
  });

  // alien animation frames
  private final AlienCharacter[] characters;

  ChangingAlienCharacter(
      final AlienCharacter[] characters) {
    this.characters = characters;
  }

  public List<AlienCharacter> getCharacters() {
    return Arrays.asList(characters);
  }
}
