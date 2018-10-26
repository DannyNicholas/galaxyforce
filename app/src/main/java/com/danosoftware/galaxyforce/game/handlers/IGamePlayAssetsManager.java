package com.danosoftware.galaxyforce.game.handlers;

import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Life;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.refactor.Flag;

import java.util.List;

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

}