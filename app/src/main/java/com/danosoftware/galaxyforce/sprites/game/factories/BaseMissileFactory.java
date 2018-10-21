package com.danosoftware.galaxyforce.sprites.game.factories;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sound.Sound;
import com.danosoftware.galaxyforce.sound.SoundEffect;
import com.danosoftware.galaxyforce.sound.SoundEffectBank;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileBlast;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileFast;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileGuided;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileLaser;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.BaseMissileSimple;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.refactor.IBaseSprite;

import java.util.ArrayList;
import java.util.List;

public class BaseMissileFactory {
    /* initialise sound effects */
    private final static Sound SIMPLE_MISSILE_SOUND;

    static {
        /* create reference to sound effects */
        SoundEffectBank soundBank = SoundEffectBankSingleton.getInstance();
        SIMPLE_MISSILE_SOUND = soundBank.get(SoundEffect.BASE_FIRE);
    }

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
            IBaseSprite base,
            BaseMissileType baseMissileType,
            GameHandler model) {
        List<IBaseMissile> baseMissiles = new ArrayList<>();
        Sound sound;

        switch (baseMissileType) {
            case BLAST:
                // create multiple missiles that fire in different directions
                for (float angle = 0; angle <= Math.PI; angle += BLAST_ANGLE_DELTA) {
                    baseMissiles.add(createMissile(base, baseMissileType, 0, model, angle));
                }
                sound = SIMPLE_MISSILE_SOUND;
                break;

            case FAST:
            case GUIDED:
            case SIMPLE:
            case LASER:
                // create single missile with no x offset
                baseMissiles.add(createMissile(base, baseMissileType, 0, model, 0));
                sound = SIMPLE_MISSILE_SOUND;
                break;

            case PARALLEL:
            case SPRAY:
                // create two missiles with x offsets
                baseMissiles.add(createMissile(base, baseMissileType, PARALLEL_FIRE_X_OFFSET, model, 0));
                baseMissiles.add(createMissile(base, baseMissileType, -PARALLEL_FIRE_X_OFFSET, model, 0));
                sound = SIMPLE_MISSILE_SOUND;
                break;

            default:
                throw new IllegalArgumentException("Unsupported Base Missile Type: '" + baseMissileType.name() + "'.");
        }

        return new BaseMissileBean(baseMissiles, sound);
    }

    private static IBaseMissile createMissile(
            IBaseSprite base,
            BaseMissileType baseMissileType,
            int xOffset,
            GameHandler model,
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
                throw new IllegalArgumentException("Unsupported Base Missile Type: '" + baseMissileType.name() + "'.");

        }

        // correct y position based on sprite height and direction
        int correctedY;

//        switch (direction)
//        {
//        case UP:
//            correctedY = base.getY() + (base.getHeight() / 2) + (missile.getHeight() / 2) - FIRE_Y_OFFSET;
//            break;
//        case DOWN:
//            correctedY = base.getY() - (base.getHeight() / 2) - (missile.getHeight() / 2) + FIRE_Y_OFFSET;
//            break;
//        default:
//            throw new IllegalArgumentException("Unsupported Direction Type: '" + direction.name() + "'.");
//        }

//        missile.setY(correctedY);

        // return new missile
        return missile;
    }
}
