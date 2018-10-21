package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.enumerations.Direction;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package invaders;

/**
 * 
 * @author Danny
 */
public abstract class SpriteBase extends ExplodingSprite
{
    public SpriteBase(int xStart, int yStart, ISpriteIdentifier spriteId, ExplodeBehaviour explodeBehaviour, int energy, int hitEnergy)
    {
        // default is that all bases are immediately visible
        super(xStart, yStart, spriteId, explodeBehaviour, energy, hitEnergy, true);
    }

    /**
     * Handles sprite animations during a direction change.
     * 
     * @param directionChangeStateTime
     */
    public abstract void updateDirectionChange(float directionChangeStateTime);

    /**
     * Implementations returns the base's current missile type when base fires.
     * 
     * @return current base missile
     */
    public abstract BaseMissileBean fire(Direction direction);

    /**
     * Returns the base's current direction.
     * 
     * @return
     */
    public abstract Direction getDirection();

    /**
     * Moves base by the supplied weighting
     * 
     * @param weightingX
     * @param weightingY
     * @param deltaTime
     */
    public abstract void moveBase(float weightingX, float weightingY, float deltaTime);

    /**
     * Sets the current base missile type. The missile type change is not
     * permanent so the timeActive parameter specifies how many seconds this
     * missile type change lasts for. The missile delay specifies the time
     * between each missile fire.
     * 
     * @param baseMissileType
     * @param baseMissileDelay
     * @param timeActive
     */
    public abstract void setBaseMissileType(BaseMissileType baseMissileType, float baseMissileDelay, float timeActive);

    /**
     * Adds a shield to this base. timeActive parameter specifies how many
     * seconds this shield is active for.
     * 
     * In order to keep all shields animating in sync, the synchronisation time
     * can also be set for a new shield. Normally helper bases will be in sync
     * with the primary base.
     * 
     * @param timeActive
     * @param syncTime
     */
    public abstract void addShield(float timeActive, float syncTime);

    /**
     * Removes shield from this base.
     * 
     * @param timeActive
     */
    public abstract void removeShield();

    /**
     * Add energy to this sprite.
     * 
     * New energy level can not exceed the original energy level.
     * 
     * @param energy
     *            - the amount of energy to add
     */
    public abstract void addEnergy(int addEnergy);

    /**
     * Start flipping the base.
     */
    public abstract void flip();

    /**
     * Complete flipping the base.
     */
    public abstract void flipComplete();

    /**
     * Add a new helper base. Helper base will be informed on events by it's
     * parent base.
     * 
     * @param helperBase
     */
    public abstract void registerHelperBase(SpriteBase helperBase);

    /**
     * Remove a new helper base. Helper base will no longer be informed on
     * events by it's parent base. Usually called during process of destroying a
     * helper base.
     * 
     * @param helperBase
     */
    public abstract void deRegisterHelperBase(SpriteBase helperBase);

    /**
     * Touch bounds of the moving base. Changes as base moves.
     */
    public abstract Rectangle getTouchBounds();
}
