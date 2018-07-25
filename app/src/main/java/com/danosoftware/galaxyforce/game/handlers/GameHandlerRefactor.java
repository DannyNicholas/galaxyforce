package com.danosoftware.galaxyforce.game.handlers;

import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.game.beans.SpawnedAlienBean;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Sprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;

import java.util.List;

public interface GameHandlerRefactor extends PlayModel
{
//    public SpriteBase getBase();
//
//    public float getBaseX();
//
//    public float getBaseY();

    /**
     * Returns the bounds of the base. Used by button classes that use the base
     * as a button (e.g. when base itself is a button used to flip base).
     * 
     * @return Rectangle representing the base's bounds area.
     */
//    public Rectangle getBaseTouchBounds();

//    public void flipBase();

    /**
     * Add a new direction listener to be notified following any direction
     * changes
     * 
     * @param listener
     *            to be notified following a direction change
     */
//    public void addDirectionListener(DirectionListener listener);

    /**
     * Pause the current game model.
     */
    public void pause();

    /**
     * Get list of sprites to be shown when paused
     */
    public List<Sprite> getPausedSprites();

    /**
     * Add a new power up sprite to the game. Usually triggered when an alien is
     * destroyed.
     * 
     * @param powerUp
     */
    public void addPowerUp(PowerUpBean powerUp);

    /**
     * Add a new shield sprite to the game. Usually triggered when base or
     * helper base gets a shield.
     * 
     * @param powerUp
     */
//    public void addShield(SpriteShield shield);

    /**
     * Add a new power up sprite to the game. Usually triggered when base or
     * helper base is destroyed or shield expires.
     * 
     * @param powerUp
     */
//    public void removeShield(SpriteShield shield);

    /**
     * Fire base missiles. Add new missiles being fired by bases.
     * 
     * @param missiles
     */
    public void fireBaseMissiles(BaseMissileBean missiles);

    /**
     * Fire alien missiles. Add new missiles being fired by aliens.
     * 
     * @param missiles
     */
    public void fireAlienMissiles(AlienMissileBean missiles);

    /**
     * Return an actively selected active alien.
     * 
     * @return
     */
    public SpriteAlien chooseActiveAlien();

    /**
     * Spawns new aliens, which are added to the game.
     * 
     * e.g. a mothership that creates new aliens
     */
    public void spawnAliens(SpawnedAlienBean aliens);

    /**
     * Return number of lives remaining
     */
    public int getLives();
}
