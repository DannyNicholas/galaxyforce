package com.danosoftware.galaxyforce.game.handlers;

import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.sound.SoundPlayer;
import com.danosoftware.galaxyforce.sprites.game.interfaces.EnergyBar;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Life;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.refactor.Flag;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;

import java.util.ArrayList;
import java.util.List;

public class GamePlayAssetsManager implements IGamePlayAssetsManager{

    // sound player that provide sound effects
    private final SoundPlayer soundPlayer;

    private List<IAlienMissile> aliensMissiles;
    private List<IBaseMissile> baseMissiles;
    private List<IPowerUp> powerUps;
    private final List<Star> stars;
    private List<Flag> flags;
    private List<Life> lives;

    // base's energy bar
    private final EnergyBar energyBar;

    public GamePlayAssetsManager(
            List<Star> stars,
            SoundPlayer soundPlayer) {

        this.soundPlayer = soundPlayer;
        this.aliensMissiles = new ArrayList<>();
        this.baseMissiles = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.stars = stars;
        this.flags = new ArrayList<>();
        this.lives = new ArrayList<>();
        this.energyBar = new EnergyBar();
    }

    @Override
    public void animate(float deltaTime) {

        List<IBaseMissile> nonDestroyedBaseMissiles = new ArrayList<>();
        for (IBaseMissile baseMissile : baseMissiles) {
            baseMissile.animate(deltaTime);
            if (!baseMissile.isDestroyed()) {
                nonDestroyedBaseMissiles.add(baseMissile);
            }
        }
        baseMissiles = nonDestroyedBaseMissiles;

        List<IAlienMissile> nonDestroyedAlienMissiles = new ArrayList<>();
        for (IAlienMissile alienMissile : aliensMissiles) {
            alienMissile.animate(deltaTime);
            if (!alienMissile.isDestroyed()) {
                nonDestroyedAlienMissiles.add(alienMissile);
            }
        }
        aliensMissiles = nonDestroyedAlienMissiles;

        List<IPowerUp> nonDestroyedPowerUps = new ArrayList<>();
        for (IPowerUp powerUp : powerUps) {
            powerUp.animate(deltaTime);
            if (!powerUp.isDestroyed()) {
                nonDestroyedPowerUps.add(powerUp);
            }
        }
        powerUps = nonDestroyedPowerUps;

        for (Star star : stars) {
            star.animate(deltaTime);
        }
    }

    @Override
    public void setLevelFlags(int wave) {
        flags = Flag.getFlagList(wave);
    }

    @Override
    public void setLives(int livesRemaining) {
        lives = Life.getLives(livesRemaining);
    }

    @Override
    public void addPowerUp(PowerUpBean powerUp) {
        powerUps.add(powerUp.getPowerUp());
    }

    @Override
    public void fireBaseMissiles(BaseMissileBean missiles) {
        baseMissiles.addAll(missiles.getMissiles());
    }

    @Override
    public void fireAlienMissiles(AlienMissileBean missiles) {
        aliensMissiles.addAll(missiles.getMissiles());
    }

    @Override
    public List<IAlienMissile> getAliensMissiles() {
        return aliensMissiles;
    }

    @Override
    public List<IBaseMissile> getBaseMissiles() {
        return baseMissiles;
    }

    @Override
    public List<IPowerUp> getPowerUps() {
        return powerUps;
    }

    @Override
    public List<Star> getStars() {
        return stars;
    }

    @Override
    public List<Flag> getFlags() {
        return flags;
    }

    @Override
    public List<Life> getLives() {
        return lives;
    }

    @Override
    public void updateEnergyBar(int energy) {
        energyBar.updateEnergy(energy);
    }

    @Override
    public List<ISprite> getEnergyBar() {
        return energyBar.getEnergyBar();
    }
}
