package com.danosoftware.galaxyforce.models.screens.game;

import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.game.beans.SpawnedAlienBean;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;
import com.danosoftware.galaxyforce.text.TextPositionY;
import com.danosoftware.galaxyforce.view.FPSCounter;

import java.util.ArrayList;
import java.util.List;

/**
 * Game Handler decorator that adds frame-rate calculations and display
 * functionality.
 */
public class GamePlayModelFrameRateDecorator implements Model, GameModel {

    // decorated game model
    private final GameModel gameModel;

    // decorated model
    private final Model model;

    // FPS counter
    private final FPSCounter fpsCounter;

    // FPS display text
    private Text tempFps;

    public GamePlayModelFrameRateDecorator(GamePlayModelImpl gameHandler) {
        this.gameModel = gameHandler;
        this.model = gameHandler;
        this.fpsCounter = new FPSCounter();
        this.tempFps = createFpsText();
    }

    private Text createFpsText() {
        return Text.newTextRelativePositionBoth(
                "FPS " + fpsCounter.getValue(),
                TextPositionX.CENTRE,
                TextPositionY.TOP);
    }

    @Override
    public List<Text> getText() {
        List<Text> text = new ArrayList<>();
        text.addAll(model.getText());
        text.add(tempFps);

        return text;
    }

    @Override
    public void update(float deltaTime) {
        model.update(deltaTime);

        // update fps text
        tempFps = createFpsText();
    }

    @Override
    public IBasePrimary getBase() {
        return gameModel.getBase();
    }

    @Override
    public List<ISprite> getSprites() {
        return model.getSprites();
    }

    @Override
    public void dispose() {
        model.dispose();
    }

    @Override
    public void goBack() {
        model.goBack();
    }

    @Override
    public void pause() {
        gameModel.pause();
    }

    @Override
    public void resume() {
        model.resume();
    }

    @Override
    public void addPowerUp(PowerUpBean powerUp) {
        gameModel.addPowerUp(powerUp);
    }

    @Override
    public void fireBaseMissiles(BaseMissileBean missiles) {
        gameModel.fireBaseMissiles(missiles);
    }

    @Override
    public void fireAlienMissiles(AlienMissileBean missiles) {
        gameModel.fireAlienMissiles(missiles);
    }

    @Override
    public IAlien chooseActiveAlien() {
        return gameModel.chooseActiveAlien();
    }

    @Override
    public void spawnAliens(SpawnedAlienBean aliens) {
        gameModel.spawnAliens(aliens);
    }

    @Override
    public int getLives() {
        return gameModel.getLives();
    }

    @Override
    public void energyUpdate(int energy) {
        gameModel.energyUpdate(energy);
    }

    @Override
    public void addLife() {
        gameModel.addLife();
    }
}
