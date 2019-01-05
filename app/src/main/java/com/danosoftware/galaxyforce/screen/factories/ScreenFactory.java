package com.danosoftware.galaxyforce.screen.factories;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.common.ControllerImpl;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.interfaces.FileIO;
import com.danosoftware.galaxyforce.interfaces.Input;
import com.danosoftware.galaxyforce.models.screens.AboutModelImpl;
import com.danosoftware.galaxyforce.models.screens.GameCompleteModelImpl;
import com.danosoftware.galaxyforce.models.screens.MainMenuModelImpl;
import com.danosoftware.galaxyforce.models.screens.SplashModelImpl;
import com.danosoftware.galaxyforce.models.screens.UnlockAllZonesModelImpl;
import com.danosoftware.galaxyforce.models.screens.UnlockFullVersionModelImpl;
import com.danosoftware.galaxyforce.models.screens.game.GameModelImpl;
import com.danosoftware.galaxyforce.models.screens.level.SelectLevelModelImpl;
import com.danosoftware.galaxyforce.models.screens.options.OptionsModelImpl;
import com.danosoftware.galaxyforce.screen.ExitingScreen;
import com.danosoftware.galaxyforce.screen.IScreen;
import com.danosoftware.galaxyforce.screen.Screen;
import com.danosoftware.galaxyforce.screen.SelectLevelScreen;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class ScreenFactory {

    private static final int MAX_SPRITES = 1000;

    private final SpriteBatcher batcher;
    private final Camera2D camera;
    private final GLGraphics glGraphics;
    private final FileIO fileIO;
    private final IBillingService billingService;
    private final ConfigurationService configurationService;
    private final SoundPlayerService sounds;
    private final VibrationService vibrator;
    private final SavedGame savedGame;
    private final Game game;
    private final Input input;
    private final String versionName;

    public ScreenFactory(
            GLGraphics glGraphics,
            FileIO fileIO,
            IBillingService billingService,
            ConfigurationService configurationService,
            SoundPlayerService sounds,
            VibrationService vibrator,
            SavedGame savedGame,
            Game game,
            Input input,
            String versionName) {

        this.glGraphics = glGraphics;
        this.fileIO = fileIO;
        this.billingService = billingService;
        this.configurationService = configurationService;
        this.sounds = sounds;
        this.vibrator = vibrator;
        this.savedGame = savedGame;
        this.game = game;
        this.input = input;
        this.versionName = versionName;
        this.batcher = new SpriteBatcher(glGraphics, MAX_SPRITES);
        this.camera = new Camera2D(glGraphics, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
    }

    public IScreen newScreen(ScreenType screenType) {

        // each screen needs it's own instance of a controller
        // to handle specific user interactions.
        Controller controller = new ControllerImpl(input, camera);

        switch (screenType) {

            case SPLASH:
                return new ExitingScreen(
                        new SplashModelImpl(game, controller),
                        controller,
                        TextureMap.SPLASH,
                        glGraphics,
                        fileIO,
                        camera,
                        batcher);

            case MAIN_MENU:
                return new ExitingScreen(
                        new MainMenuModelImpl(game, controller, billingService),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        fileIO,
                        camera,
                        batcher);

            case OPTIONS:
                return new Screen(
                        new OptionsModelImpl(game, controller, configurationService, sounds, vibrator),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        fileIO,
                        camera,
                        batcher);

            case ABOUT:
                return new Screen(
                        new AboutModelImpl(game, controller, versionName),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        fileIO,
                        camera,
                        batcher);

            case SELECT_LEVEL:
                return new SelectLevelScreen(
                        new SelectLevelModelImpl(game, controller, billingService, savedGame),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        fileIO,
                        camera,
                        batcher);

            case UPGRADE_FULL_VERSION:
                return new Screen(
                        new UnlockFullVersionModelImpl(game, controller, billingService),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        fileIO,
                        camera,
                        batcher);

            case UPGRADE_ALL_ZONES:
                return new Screen(
                        new UnlockAllZonesModelImpl(game, controller, billingService),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        fileIO,
                        camera,
                        batcher);

            case GAME_COMPLETE:
                return new Screen(
                        new GameCompleteModelImpl(game, controller),
                        controller,
                        TextureMap.MENU,
                        glGraphics,
                        fileIO,
                        camera,
                        batcher);

            default:
                throw new IllegalArgumentException("Unsupported screen type: '" + screenType + "'.");
        }
    }

    public IScreen newGameScreen(int startingWave) {

        // each screen needs it's own instance of a controller
        // to handle specific user interactions.
        Controller controller = new ControllerImpl(input, camera);

        return new Screen(
                new GameModelImpl(game, controller, startingWave, billingService, sounds, savedGame),
                controller,
                TextureMap.GAME,
                glGraphics,
                fileIO,
                camera,
                batcher);
    }
}
