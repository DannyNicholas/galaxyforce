package com.danosoftware.galaxyforce.screen;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.common.ControllerImpl;
import com.danosoftware.galaxyforce.interfaces.Game;
import com.danosoftware.galaxyforce.interfaces.Input;
import com.danosoftware.galaxyforce.models.about.AboutModelImpl;
import com.danosoftware.galaxyforce.models.about.GameCompleteModelImpl;
import com.danosoftware.galaxyforce.models.game.GameModelImpl;
import com.danosoftware.galaxyforce.models.screens.MainMenuModelImpl;
import com.danosoftware.galaxyforce.models.options.OptionsModelImpl;
import com.danosoftware.galaxyforce.models.level.SelectLevelModelImpl;
import com.danosoftware.galaxyforce.models.screens.SplashModelImpl;
import com.danosoftware.galaxyforce.models.screens.UnlockAllZonesModelImpl;
import com.danosoftware.galaxyforce.models.screens.UnlockFullVersionModelImpl;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.services.Inputs;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class ScreenFactory {

    private static final int MAX_SPRITES = 1000;

    public enum ScreenType {
        SPLASH, MAIN_MENU, OPTIONS, ABOUT, SELECT_LEVEL, UPGRADE_FULL_VERSION, UPGRADE_ALL_ZONES, GAME_COMPLETE
    }

    private ScreenFactory() {
    }

    public static Screen newScreen(ScreenType screenType) {

        final Game game = Games.getGame();
        final GLGraphics glGraphics = game.getGlGraphics();
        final SpriteBatcher batcher = new SpriteBatcher(glGraphics, MAX_SPRITES);
        final Camera2D camera = new Camera2D(glGraphics, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        final Input input = Inputs.getInput();
        final Controller controller = new ControllerImpl(input, camera);

        switch (screenType) {

            case SPLASH:
                return new SplashScreen(
                        new SplashModelImpl(controller),
                        new ControllerImpl(input, camera),
                        TextureMap.SPLASH,
                        glGraphics,
                        camera,
                        batcher);

            case MAIN_MENU:
                return new MainMenuScreen(
                        new MainMenuModelImpl(controller, game.getBillingService()),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        camera,
                        batcher);

            case OPTIONS:
                return new OptionsScreen(
                        new OptionsModelImpl(controller),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        camera,
                        batcher);

            case ABOUT:
                return new AboutScreen(
                        new AboutModelImpl(controller, game.getContext()),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        camera,
                        batcher);

            case SELECT_LEVEL:
                return new SelectLevelScreen(
                        new SelectLevelModelImpl(controller, game.getBillingService()),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        camera,
                        batcher);

            case UPGRADE_FULL_VERSION:
                return new UnlockFullVersionScreen(
                        new UnlockFullVersionModelImpl(controller, game.getBillingService()),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        camera,
                        batcher);

            case UPGRADE_ALL_ZONES:
                return new UnlockAllZonesScreen(
                        new UnlockAllZonesModelImpl(controller, game.getBillingService()),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        camera,
                        batcher);

            case GAME_COMPLETE:
                return new GameCompleteScreen(
                        new GameCompleteModelImpl(controller, game.getContext()),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        camera,
                        batcher);

            default:
                throw new IllegalArgumentException("Unsupported screen type: '" + screenType + "'.");
        }
    }

    public static Screen newGameScreen(int startingWave) {

        final Game game = Games.getGame();
        final GLGraphics glGraphics = game.getGlGraphics();
        final SpriteBatcher batcher = new SpriteBatcher(glGraphics, MAX_SPRITES);
        final Camera2D camera = new Camera2D(glGraphics, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        final Input input = Inputs.getInput();
        final Controller controller = new ControllerImpl(input, camera);

        return new GameScreen(
                new GameModelImpl(controller, startingWave, game.getBillingService()),
                controller,
                TextureMap.GAME,
                glGraphics,
                camera,
                batcher);
    }
}
