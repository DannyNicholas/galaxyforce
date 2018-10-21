package com.danosoftware.galaxyforce.screen;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controller.game.ControllerGame;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.menus.MenuController;
import com.danosoftware.galaxyforce.interfaces.Game;
import com.danosoftware.galaxyforce.interfaces.Input;
import com.danosoftware.galaxyforce.interfaces.LevelModel;
import com.danosoftware.galaxyforce.interfaces.Model;
import com.danosoftware.galaxyforce.interfaces.Screen;
import com.danosoftware.galaxyforce.model.screens.AboutModelImpl;
import com.danosoftware.galaxyforce.model.screens.GameCompleteModelImpl;
import com.danosoftware.galaxyforce.model.screens.GameModelImpl;
import com.danosoftware.galaxyforce.model.screens.MainMenuModelImpl;
import com.danosoftware.galaxyforce.model.screens.OptionsModelImpl;
import com.danosoftware.galaxyforce.model.screens.SelectLevelModelImpl;
import com.danosoftware.galaxyforce.model.screens.SplashModelImpl;
import com.danosoftware.galaxyforce.model.screens.UnlockAllZonesModelImpl;
import com.danosoftware.galaxyforce.model.screens.UnlockFullVersionModelImpl;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.services.Inputs;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class ScreenFactory
{

    private static final int MAX_SPRITES = 1000;

    public enum ScreenType
    {
        SPLASH, MAIN_MENU, OPTIONS, ABOUT, SELECT_LEVEL, UPGRADE_FULL_VERSION, UPGRADE_ALL_ZONES, GAME_COMPLETE
    };

    private ScreenFactory()
    {
    }

    public static Screen newScreen(ScreenType screenType)
    {
        // screen to be returned
        Screen screen = null;

        /* set-up view */
        Game game = Games.getGame();
        GLGraphics glGraphics = game.getGlGraphics();

        SpriteBatcher batcher = new SpriteBatcher(glGraphics, MAX_SPRITES);
        Camera2D camera = new Camera2D(glGraphics, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);

        // game screen specifics
        Input input = Inputs.getInput();

        TextureMap textureMap;
        Controller controller;
        Model model;

        switch (screenType)
        {

        case SPLASH:

            /* texture map containing sprite identifiers and properties */
            textureMap = TextureMap.SPLASH;

            /* set-up controller */
            controller = new MenuController(input, camera);

            /* set-up model */
            model = new SplashModelImpl(controller);

            /* create screen */
            screen = new SplashScreen(model, controller, textureMap, glGraphics, camera, batcher);

            break;

        case MAIN_MENU:

            /* texture map containing sprite identifiers and properties */
            textureMap = TextureMap.MENU;

            /* set-up controller */
            controller = new MenuController(input, camera);

            /* set-up model */
            model = new MainMenuModelImpl(controller, game.getBillingService());

            /* create screen */
            screen = new MainMenuScreen(model, controller, textureMap, glGraphics, camera, batcher);

            break;

        case OPTIONS:

            /* texture map containing sprite identifiers and properties */
            textureMap = TextureMap.MENU;

            /* set-up controller */
            controller = new MenuController(input, camera);

            /* set-up model */
            model = new OptionsModelImpl(controller);

            /* create screen */
            screen = new OptionsScreen(model, controller, textureMap, glGraphics, camera, batcher);

            break;

        case ABOUT:

            /* texture map containing sprite identifiers and properties */
            textureMap = TextureMap.MENU;

            /* set-up controller */
            controller = new MenuController(input, camera);

            /* set-up model */
            model = new AboutModelImpl(controller, game.getContext());

            /* create screen */
            screen = new AboutScreen(model, controller, textureMap, glGraphics, camera, batcher);

            break;

        case SELECT_LEVEL:

            /* texture map containing sprite identifiers and properties */
            textureMap = TextureMap.MENU;

            /* set-up controller */
            controller = new MenuController(input, camera);

            /* set-up model */
            LevelModel levelModel = new SelectLevelModelImpl(controller, game.getBillingService());

            /* create screen */
            screen = new SelectLevelScreen(levelModel, controller, textureMap, glGraphics, camera, batcher);

            break;

        case UPGRADE_FULL_VERSION:

            /* texture map containing sprite identifiers and properties */
            textureMap = TextureMap.MENU;

            /* set-up controller */
            controller = new MenuController(input, camera);

            /* set-up model */
            model = new UnlockFullVersionModelImpl(controller, game.getBillingService());

            /* create screen */
            screen = new UnlockFullVersionScreen(model, controller, textureMap, glGraphics, camera, batcher);

            break;

        case UPGRADE_ALL_ZONES:

            /* texture map containing sprite identifiers and properties */
            textureMap = TextureMap.MENU;

            /* set-up controller */
            controller = new MenuController(input, camera);

            /* set-up model */
            model = new UnlockAllZonesModelImpl(controller, game.getBillingService());

            /* create screen */
            screen = new UnlockAllZonesScreen(model, controller, textureMap, glGraphics, camera, batcher);

            break;

        case GAME_COMPLETE:

            /* texture map containing sprite identifiers and properties */
            textureMap = TextureMap.MENU;

            /* set-up controller */
            controller = new MenuController(input, camera);

            /* set-up model */
            model = new GameCompleteModelImpl(controller, game.getContext());

            /* create screen */
            screen = new GameCompleteScreen(model, controller, textureMap, glGraphics, camera, batcher);

            break;

        default:

            throw new IllegalArgumentException("Unsupported screen type: '" + screenType + "'.");

        }

        return screen;
    }

    public static Screen newGameScreen(int startingWave)
    {
        /* set-up view */
        Game game = Games.getGame();
        GLGraphics glGraphics = game.getGlGraphics();

        SpriteBatcher batcher = new SpriteBatcher(glGraphics, MAX_SPRITES);
        Camera2D camera = new Camera2D(glGraphics, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);

        /* texture map containing sprite identifiers and properties */
        TextureMap textureMap = TextureMap.GAME;

        /* set-up controller */
        Input input = Inputs.getInput();
        Controller controller = new ControllerGame(input, camera);
//        Controller controller = controllerBase;

        /* set-up model */
        Model model = new GameModelImpl(controller, startingWave, game.getBillingService());

        /* create screen */
        return new GameScreen(model, controller, textureMap, glGraphics, camera, batcher);
    }
}
