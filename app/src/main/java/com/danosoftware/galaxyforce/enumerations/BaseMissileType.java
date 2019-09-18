package com.danosoftware.galaxyforce.enumerations;

public enum BaseMissileType {

    NORMAL(
            BaseMissileCharacter.MISSILE),
    FAST(
            BaseMissileCharacter.MISSILE),
    BLAST(
            BaseMissileCharacter.BLAST),
    GUIDED(
            BaseMissileCharacter.ROCKET),
    PARALLEL(
            BaseMissileCharacter.MISSILE),
    SPRAY(
            BaseMissileCharacter.ROCKET),
    LASER(
            BaseMissileCharacter.LASER);

    // character of missile type
    private final BaseMissileCharacter character;


    BaseMissileType(BaseMissileCharacter character) {
        this.character = character;
    }

    public BaseMissileCharacter getCharacter() {
        return character;
    }
}
