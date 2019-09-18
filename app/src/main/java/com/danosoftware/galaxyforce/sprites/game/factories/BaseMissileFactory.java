package com.danosoftware.galaxyforce.sprites.game.factories;

import com.danosoftware.galaxyforce.enumerations.BaseMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.BaseMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.bases.IBase;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileBlast;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileGuided;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileLaser;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileUpwards;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;

import java.util.ArrayList;
import java.util.List;

public class BaseMissileFactory {

    // angle delta between blast missiles directions in radians
    private static final float BLAST_ANGLE_DELTA = (float) (Math.PI / 8);

    // parallel fire x offset from centre of base
    private static final int PARALLEL_FIRE_X_OFFSET = 25;

    // fire y offset from top of base
    private static final int FIRE_Y_OFFSET = 2;

    /**
     * Return a list of missiles based on the type. Missile initial position
     * will be based on the base's position.
     */
    public static BaseMissilesDto createBaseMissile(
            final IBase base,
            final BaseMissileType baseMissileType,
            final GameModel model) {

        final List<IBaseMissile> baseMissiles = new ArrayList<>();
        final BaseMissileCharacter missileCharacter = baseMissileType.getCharacter();
        final SoundEffect effect = missileCharacter.getSound();

        // set missile default starting x and y positions.
        final int x = base.x();
        final int y = base.y() + (base.height() / 2) - FIRE_Y_OFFSET;

        switch (baseMissileType) {
            case BLAST:
                // create multiple missiles that fire in different directions
                for (float angle = 0; angle <= Math.PI; angle += BLAST_ANGLE_DELTA) {
                    baseMissiles.add(
                            new BaseMissileBlast(
                                    x,
                                    y,
                                    missileCharacter.getAnimation(),
                                    angle,
                                    BaseMissileSpeed.NORMAL));
                }
                break;
            case GUIDED:
                // create single guided missile
                baseMissiles.add(
                        new BaseMissileGuided(
                                x,
                                y,
                                missileCharacter.getAnimation(),
                                BaseMissileSpeed.NORMAL,
                                model));
                break;
            case NORMAL:
            case FAST:
                // create single upwards missile
                baseMissiles.add(
                        new BaseMissileUpwards(
                                x,
                                y,
                                missileCharacter.getAnimation(),
                                BaseMissileSpeed.NORMAL));
                break;
            case LASER:
                // create single laser missile
                baseMissiles.add(
                        new BaseMissileLaser(
                                x,
                                y,
                                missileCharacter.getAnimation(),
                                BaseMissileSpeed.NORMAL));
                break;
            case PARALLEL:
                // create two parallel missiles with x offsets
                baseMissiles.add(
                        new BaseMissileUpwards(
                                x + PARALLEL_FIRE_X_OFFSET,
                                y,
                                missileCharacter.getAnimation(),
                                BaseMissileSpeed.NORMAL));
                baseMissiles.add(
                        new BaseMissileUpwards(
                                x - PARALLEL_FIRE_X_OFFSET,
                                y,
                                missileCharacter.getAnimation(),
                                BaseMissileSpeed.NORMAL));
                break;
            case SPRAY:
                // create two guided missiles with x offsets
                baseMissiles.add(
                        new BaseMissileGuided(
                                x + PARALLEL_FIRE_X_OFFSET,
                                y,
                                missileCharacter.getAnimation(),
                                BaseMissileSpeed.NORMAL,
                                model));
                baseMissiles.add(
                        new BaseMissileGuided(
                                x - PARALLEL_FIRE_X_OFFSET,
                                y,
                                missileCharacter.getAnimation(),
                                BaseMissileSpeed.NORMAL,
                                model));
                break;
            default:
                throw new GalaxyForceException("Unsupported Base Missile Type: '" + baseMissileType.name() + "'.");
        }

        return new BaseMissilesDto(baseMissiles, effect);
    }
}
