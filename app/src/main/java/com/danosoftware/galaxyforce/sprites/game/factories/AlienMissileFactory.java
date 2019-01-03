package com.danosoftware.galaxyforce.sprites.game.factories;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileRotated;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileSimple;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;

import java.util.ArrayList;
import java.util.List;

public class AlienMissileFactory {

    /**
     * Return a list of missiles based on the type. Missile initial position
     * will be based on the base's position. Direction will be used to determine
     * initial position and direction of travel.
     */
    public static AlienMissileBean createAlienMissile(IBasePrimary base, IAlien alien, AlienMissileType missileType) {

        List<IAlienMissile> missiles = new ArrayList<>();

        /*
         * create missile's starting x and y positions based on alien position
         * and direction
         */
        int x = alien.x();
        int y = alien.y() + (alien.height() / 2);

        /*
         * Create new missiles
         */
        switch (missileType) {

            case SIMPLE:

                missiles.add(new AlienMissileSimple(x, y));
                return new AlienMissileBean(missiles, SoundEffect.ALIEN_FIRE);

            case ROTATED:

                missiles.add(new AlienMissileRotated(x, y, base));
                return new AlienMissileBean(missiles, SoundEffect.ALIEN_FIRE);

            default:

                throw new IllegalStateException("Unsupported missile type '" + missileType.name() + "'.");
        }
    }
}
