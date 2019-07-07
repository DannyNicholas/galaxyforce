package com.danosoftware.galaxyforce.sprites.game.factories;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.models.assets.AlienMissilesDto;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;

public class AlienMissileFactory {

    /**
     * Return a list of missiles based on the type. Missile initial position
     * will be based on the base's position. Direction will be used to determine
     * initial position and direction of travel.
     */
    public static AlienMissilesDto createAlienMissile(
            IBasePrimary base,
            IAlien alien,
            AlienMissileType missileType,
            AlienMissileSpeed missileSpeed,
            AlienMissileCharacter missileCharacter) {

        /*
         * create missile's starting x and y positions and offset based
         * on alien size.
         */
        int x = alien.x();
        int y = alien.y();
        int offset = (alien.height() / 2);

        /*
         * Create new missiles
         */
        return new AlienMissilesDto(
                missileType.getMissiles(
                        x,
                        y,
                        offset,
                        missileCharacter,
                        missileSpeed,
                        base),
                missileType.getSound(
                        missileCharacter));
    }
}
