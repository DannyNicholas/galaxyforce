package com.danosoftware.galaxyforce.sprites.game.behaviours.fire;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.models.assets.AlienMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienMissileFactory;

/**
 * Missile Fire Behaviour that fires on fixed cycles set by
 * the frequency (time in seconds between firing).
 * <p>
 * However, each instance randomises it's start time to
 * avoid all aliens firing their missiles at exactly the same time.
 */
public class FireCyclicWithRandomStart implements FireBehaviour {

    // frequency of missile fires in seconds
    private final double missileFireFrequency;

    // time in seconds until next fire
    private float timeUntilNextFire;

    /* reference to game model */
    private final GameModel model;

    // missile creation props
    private final AlienMissileType missileType;
    private final AlienMissileSpeed missileSpeed;
    private final AlienMissileCharacter missileCharacter;

    FireCyclicWithRandomStart(
            final GameModel model,
            final AlienMissileType missileType,
            final AlienMissileSpeed missileSpeed,
            final AlienMissileCharacter missileCharacter,
            final float missileFireFrequency) {
        this.model = model;
        this.missileType = missileType;
        this.missileSpeed = missileSpeed;
        this.missileCharacter = missileCharacter;
        this.missileFireFrequency = missileFireFrequency;

        // randomise time until alien can fire.
        // between 0 to missileFireFrequency.
        this.timeUntilNextFire = missileFireFrequency * (float) Math.random();
    }

    @Override
    public boolean readyToFire(float deltaTime) {
        timeUntilNextFire -= deltaTime;
        return (timeUntilNextFire < 0);
    }

    @Override
    public void fire(IAlien alien) {
        // reset countdown timer
        timeUntilNextFire += missileFireFrequency;

        // send missiles to model
        AlienMissilesDto missiles = AlienMissileFactory.createAlienMissile(
                model.getBase(),
                alien,
                missileType,
                missileSpeed,
                missileCharacter);
        model.fireAlienMissiles(missiles);
    }
}
