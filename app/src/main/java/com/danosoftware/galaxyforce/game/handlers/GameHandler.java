package com.danosoftware.galaxyforce.game.handlers;

import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.game.beans.SpawnedAlienBean;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;

import java.util.List;

public interface GameHandler extends PlayModel {

    /**
     * Returns the current base
     */
    IBasePrimary getBase();

    /**
     * Called by the base when it is in position and ready to start.
     * Allows model to start/continue the wave with the new base.
     */
    void baseReady();

    /**
     * Pause the current game model.
     */
    void pause();

    /**
     * Get list of sprites to be shown when paused
     */
    List<ISprite> getPausedSprites();

    /**
     * Add a new power up sprite to the game. Usually triggered when an alien is
     * destroyed.
     */
    void addPowerUp(PowerUpBean powerUp);


    /**
     * Fire base missiles. Add new missiles being fired by bases.
     */
    void fireBaseMissiles(BaseMissileBean missiles);

    /**
     * Fire alien missiles. Add new missiles being fired by aliens.
     */
    void fireAlienMissiles(AlienMissileBean missiles);

    /**
     * Return an actively selected active alien.
     */
    IAlien chooseActiveAlien();

    /**
     * Spawns new aliens, which are added to the game.
     * <p>
     * e.g. a mothership that creates new aliens
     */
    void spawnAliens(SpawnedAlienBean aliens);

    /**
     * Return number of lives remaining
     */
    int getLives();

    /**
     * Update following a base's energy change.
     * Typically used to update the energy bar assets.
     */
    void energyUpdate(int energy);

    /**
     * Add an extra base life to the game.
     */
    void addLife();
}
