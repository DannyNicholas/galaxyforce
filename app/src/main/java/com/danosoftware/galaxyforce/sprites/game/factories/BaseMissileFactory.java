package com.danosoftware.galaxyforce.sprites.game.factories;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.bases.IBase;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileBlast;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileFast;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileGuided;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileLaser;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileSimple;
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
    public static BaseMissileBean createBaseMissile(
            IBase base,
            BaseMissileType baseMissileType,
            GameModel model) {
        List<IBaseMissile> baseMissiles = new ArrayList<>();
        final SoundEffect effect;

        switch (baseMissileType) {
            case BLAST:
                // create multiple missiles that fire in different directions
                for (float angle = 0; angle <= Math.PI; angle += BLAST_ANGLE_DELTA) {
                    baseMissiles.add(createMissile(base, baseMissileType, 0, model, angle));
                }
                effect = SoundEffect.BASE_FIRE;
                break;

            case FAST:
            case GUIDED:
            case SIMPLE:
            case LASER:
                // create single missile with no x offset
                baseMissiles.add(createMissile(base, baseMissileType, 0, model, 0));
                effect = SoundEffect.BASE_FIRE;
                break;

            case PARALLEL:
            case SPRAY:
                // create two missiles with x offsets
                baseMissiles.add(createMissile(base, baseMissileType, PARALLEL_FIRE_X_OFFSET, model, 0));
                baseMissiles.add(createMissile(base, baseMissileType, -PARALLEL_FIRE_X_OFFSET, model, 0));
                effect = SoundEffect.BASE_FIRE;
                break;

            default:
                throw new GalaxyForceException("Unsupported Base Missile Type: '" + baseMissileType.name() + "'.");
        }

        return new BaseMissileBean(baseMissiles, effect);
    }

    private static IBaseMissile createMissile(
            IBase base,
            BaseMissileType baseMissileType,
            int xOffset,
            GameModel model,
            float angle) {

        final IBaseMissile missile;

        // set missile default starting x and y positions.
        int x = base.x() + xOffset;
        int y = base.y() + (base.height() / 2) - FIRE_Y_OFFSET;

        // create correct instance of base missile
        switch (baseMissileType) {
            case BLAST:
                missile = new BaseMissileBlast(x, y, angle);
                break;

            case FAST:
                missile = new BaseMissileFast(x, y);
                break;

            case GUIDED:
                missile = new BaseMissileGuided(x, y, model);
                break;

            case LASER:
                missile = new BaseMissileLaser(x, y);
                break;

            case PARALLEL:
                missile = new BaseMissileSimple(x, y);
                break;

            case SIMPLE:
                missile = new BaseMissileSimple(x, y);
                break;

            case SPRAY:
                missile = new BaseMissileGuided(x, y, model);
                break;

            default:
                throw new GalaxyForceException("Unsupported Base Missile Type: '" + baseMissileType.name() + "'.");

        }

        return missile;
    }
}
