package com.danosoftware.galaxyforce.game.handlers;

import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.enumerations.TextPositionY;
import com.danosoftware.galaxyforce.game.beans.AlienMissileBean;
import com.danosoftware.galaxyforce.game.beans.BaseMissileBean;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.game.beans.SpawnedAlienBean;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.view.FPSCounter;

import java.util.List;

/**
 * Game Handler decorator that adds frame-rate calculations and display
 * functionality.
 */
public class GameHandlerFrameRateDecorator implements GameHandler {

    // decorated game handler
    private final GameHandler gameHandler;

    // FPS counter
    private final FPSCounter fpsCounter;

    // FPS display text
    private Text tempFps;

    public GameHandlerFrameRateDecorator(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
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
        List<Text> text = gameHandler.getText();
        text.add(tempFps);

        return text;
    }

    @Override
    public void update(float deltaTime) {
        gameHandler.update(deltaTime);

        // update fps text
        tempFps = createFpsText();
    }

    @Override
    public IBasePrimary getBase() {
        return gameHandler.getBase();
    }

    @Override
    public void baseReady() {
        gameHandler.baseReady();
    }

    @Override
    public void initialise() {
        gameHandler.initialise();
    }

    @Override
    public List<ISprite> getSprites() {
        return gameHandler.getSprites();
    }

    @Override
    public void dispose() {
        gameHandler.dispose();
    }

    @Override
    public void goBack() {
        gameHandler.goBack();
    }

    @Override
    public void pause() {
        gameHandler.pause();
    }

    @Override
    public void resume() {
        gameHandler.resume();
    }

    @Override
    public List<ISprite> getPausedSprites() {
        return gameHandler.getPausedSprites();
    }

    @Override
    public void addPowerUp(PowerUpBean powerUp) {
        gameHandler.addPowerUp(powerUp);
    }

    @Override
    public void fireBaseMissiles(BaseMissileBean missiles) {
        gameHandler.fireBaseMissiles(missiles);
    }

    @Override
    public void fireAlienMissiles(AlienMissileBean missiles) {
        gameHandler.fireAlienMissiles(missiles);
    }

    @Override
    public IAlien chooseActiveAlien() {
        return gameHandler.chooseActiveAlien();
    }

    @Override
    public void spawnAliens(SpawnedAlienBean aliens) {
        gameHandler.spawnAliens(aliens);
    }

    @Override
    public int getLives() {
        return gameHandler.getLives();
    }

    @Override
    public void flashText(Text text, boolean flashState) {
        gameHandler.flashText(text, flashState);
    }
}
