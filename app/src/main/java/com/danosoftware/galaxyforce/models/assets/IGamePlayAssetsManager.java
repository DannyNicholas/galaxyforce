package com.danosoftware.galaxyforce.models.assets;

import com.danosoftware.galaxyforce.sprites.game.assets.Flag;
import com.danosoftware.galaxyforce.sprites.game.assets.Life;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.game.starfield.Star;

import java.util.List;

/**
 * Manages all in-game assets
 */
public interface IGamePlayAssetsManager {

    void animate(float deltaTime);

    void setLevelFlags(int wave);

    void setLives(int lives);

    void addPowerUp(PowerUpsDto powerUp);

    void fireBaseMissiles(BaseMissilesDto missiles);

    void fireAlienMissiles(AlienMissilesDto missiles);

    List<IAlienMissile> getAliensMissiles();

    List<IBaseMissile> getBaseMissiles();

    List<IPowerUp> getPowerUps();

    List<Star> getStars();

    List<Flag> getFlags();

    List<Life> getLives();

    boolean alienMissilesDestroyed();
}
