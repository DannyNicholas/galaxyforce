package com.danosoftware.galaxyforce.models.screens.game;

import com.danosoftware.galaxyforce.models.assets.AlienMissilesDto;
import com.danosoftware.galaxyforce.models.assets.BaseMissilesDto;
import com.danosoftware.galaxyforce.models.assets.PowerUpsDto;
import com.danosoftware.galaxyforce.models.assets.SpawnedAliensDto;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;

public interface GameModel {

    /**
     * Returns the current base
     */
    IBasePrimary getBase();

    /**
     * Pause the current game model.
     */
    void pause();

    /**
     * Add a new power up sprite to the game. Usually triggered when an alien is
     * destroyed.
     */
    void addPowerUp(PowerUpsDto powerUp);

    /**
     * Fire base missiles. Add new missiles being fired by bases.
     */
    void fireBaseMissiles(BaseMissilesDto missiles);

    /**
     * Fire alien missiles. Add new missiles being fired by aliens.
     */
    void fireAlienMissiles(AlienMissilesDto missiles);

    /**
     * Return an actively selected active alien.
     */
    IAlien chooseActiveAlien();

    /**
     * Spawns new aliens, which are added to the game.
     * <p>
     * e.g. a mothership that creates new aliens
     */
    void spawnAliens(SpawnedAliensDto aliens);

    /**
     * Return number of lives remaining
     */
    int getLives();

    /**
     * Add an extra base life to the game.
     */
    void addLife();
}
