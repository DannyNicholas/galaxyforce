package com.danosoftware.galaxyforce.waves;

import java.util.Arrays;
import java.util.List;

public enum ChangingAlienCharacter {

  FOXY(new AlienCharacter[]{
      AlienCharacter.FOXY_SHRINKING_LARGE,
      AlienCharacter.FOXY_SHRINKING_MEDIUM,
      AlienCharacter.FOXY_SHRINKING_NORMAL,
      AlienCharacter.FOXY_SHRINKING_SMALL
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
