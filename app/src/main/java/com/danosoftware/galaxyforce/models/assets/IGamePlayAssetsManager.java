package com.danosoftware.galaxyforce.models.assets;

import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Life;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.refactor.Flag;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;

import java.util.List;

/**
 * Manages all in-game assets
 */
public interface IGamePlayAssetsManager {

    void animate(float deltaTime);

    void setLevelFlags(int wave);

    void setLives(int lives);

    void addPowerUp(PowerUpBean powerUp);

    void fireBaseMissiles(BaseMissileBean missiles);

    void fireAlienMissiles(AlienMissileBean missiles);

    List<IAlienMissile> getAliensMissiles();

    List<IBaseMissile> getBaseMissiles();

    List<IPowerUp> getPowerUps();

    List<Star> getStars();

    List<Flag> getFlags();

    List<Life> getLives();

    /**
     * Update base's energy bar assets.
     * Typically used to update the energy levels after gaining or losing power.
     */
    void updateEnergyBar(int energy);

    /**
     * Get base's energy bar assets.
     */
    List<ISprite> getEnergyBar();
}
