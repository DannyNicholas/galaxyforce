package com.danosoftware.galaxyforce.enumerations;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileDownwards;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileGuided;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileRotated;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enumerations for missile types along with factories for creating
 * instances of missiles and sound effects.
 */
public enum AlienMissileType {

    DOWNWARDS(
            new MissileFactory() {
                @Override
                public List<IAlienMissile> createMissiles(
                        int x,
                        int y,
                        int offset,
                        AlienMissileCharacter missileCharacter,
                        AlienMissileSpeed missileSpeed,
                        IBasePrimary base) {

                    return Collections.singletonList(
                            (IAlienMissile) new AlienMissileDownwards(
                                    x,
                                    y - offset,
                                    missileCharacter.getAnimation(),
                                    missileSpeed));
                }

                @Override
                public SoundEffect getSoundEffect(AlienMissileCharacter missileCharacter) {
                    return missileCharacter.getSound();
                }
            }
    ),

    ROTATED(
            new MissileFactory() {
                @Override
                public List<IAlienMissile> createMissiles(
                        int x,
                        int y,
                        int offset,
                        AlienMissileCharacter missileCharacter,
                        AlienMissileSpeed missileSpeed,
                        IBasePrimary base) {
                    return Collections.singletonList(
                            (IAlienMissile) new AlienMissileRotated(
                                    x,
                                    y - offset,
                                    missileCharacter.getAnimation(),
                                    missileSpeed,
                                    base));
                }

                @Override
                public SoundEffect getSoundEffect(AlienMissileCharacter missileCharacter) {
                    return missileCharacter.getSound();
                }
            }
    ),

    GUIDED(
            new MissileFactory() {
                @Override
                public List<IAlienMissile> createMissiles(
                        int x,
                        int y,
                        int offset,
                        AlienMissileCharacter missileCharacter,
                        AlienMissileSpeed missileSpeed,
                        IBasePrimary base) {
                    return Collections.singletonList(
                            (IAlienMissile) new AlienMissileGuided(
                                    x,
                                    y - offset,
                                    missileCharacter.getAnimation(),
                                    missileSpeed,
                                    base));
                }

                @Override
                public SoundEffect getSoundEffect(AlienMissileCharacter missileCharacter) {
                    return missileCharacter.getSound();
                }
            }
    ),

    SPRAY(
            new MissileFactory() {
                @Override
                public List<IAlienMissile> createMissiles(
                        int x,
                        int y,
                        int offset,
                        AlienMissileCharacter missileCharacter,
                        AlienMissileSpeed missileSpeed,
                        IBasePrimary base) {
                    return Arrays.asList(
                            (IAlienMissile) new AlienMissileRotated(
                                    x,
                                    y,
                                    missileCharacter.getAnimation(),
                                    missileSpeed,
                                    (float) Math.atan2(-1, -1)),
                            (IAlienMissile) new AlienMissileRotated(
                                    x,
                                    y,
                                    missileCharacter.getAnimation(),
                                    missileSpeed,
                                    (float) Math.atan2(-1, 1)),
                            (IAlienMissile) new AlienMissileRotated(
                                    x,
                                    y,
                                    missileCharacter.getAnimation(),
                                    missileSpeed,
                                    (float) Math.atan2(1, -1)),
                            (IAlienMissile) new AlienMissileRotated(
                                    x,
                                    y,
                                    missileCharacter.getAnimation(),
                                    missileSpeed,
                                    (float) Math.atan2(1, 1)));
                }

                @Override
                public SoundEffect getSoundEffect(AlienMissileCharacter missileCharacter) {
                    return missileCharacter.getSound();
                }
            }
    );

    // holds a factory capable of creating missiles.
    // each enum has it's own specific factory.
    private final MissileFactory missileFactory;

    AlienMissileType(final MissileFactory missileFactory) {
        this.missileFactory = missileFactory;
    }


    // create missiles using the enum specific factory
    public List<IAlienMissile> getMissiles(int x,
                                           int y,
                                           int offset,
                                           AlienMissileCharacter missileCharacter,
                                           AlienMissileSpeed missileSpeed,
                                           IBasePrimary base) {
        return missileFactory.createMissiles(
                x,
                y,
                offset,
                missileCharacter,
                missileSpeed,
                base);
    }

    // create missile sound effect using the enum specific factory
    public SoundEffect getSound(AlienMissileCharacter missileCharacter) {
        return missileFactory.getSoundEffect(missileCharacter);
    }

    // interface of a missile factory instance
    private interface MissileFactory {

        /**
         * Create missiles for missile type
         */
        List<IAlienMissile> createMissiles(
                int x,
                int y,
                int offset,
                AlienMissileCharacter missileCharacter,
                AlienMissileSpeed missileSpeed,
                IBasePrimary base);

        /**
         * Create sound effects for missile type
         */
        SoundEffect getSoundEffect(
                AlienMissileCharacter missileCharacter
        );
    }
}
