package com.danosoftware.galaxyforce.screen.factories;

import android.content.res.AssetManager;

import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.common.ControllerImpl;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.input.Input;
import com.danosoftware.galaxyforce.models.screens.GameCompleteModelImpl;
import com.danosoftware.galaxyforce.models.screens.MainMenuModelImpl;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.models.screens.SplashModelImpl;
import com.danosoftware.galaxyforce.models.screens.UnlockFullVersionModelImpl;
import com.danosoftware.galaxyforce.models.screens.game.GameOverModelImpl;
import com.danosoftware.galaxyforce.models.screens.game.GamePausedModelImpl;
import com.danosoftware.galaxyforce.models.screens.game.GamePlayModelFrameRateDecorator;
import com.danosoftware.galaxyforce.models.screens.game.GamePlayModelImpl;
import com.danosoftware.galaxyforce.models.screens.level.SelectLevelModelImpl;
import com.danosoftware.galaxyforce.models.screens.options.OptionsModelImpl;
import com.danosoftware.galaxyforce.screen.ExitingScreen;
import com.danosoftware.galaxyforce.screen.IScreen;
import com.danosoftware.galaxyforce.screen.Screen;
import com.danosoftware.galaxyforce.screen.SelectLevelScreen;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.services.file.FileIO;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.starfield.Star;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.SHOW_FPS;

public class ScreenFactory {

    private static final int MAX_SPRITES = 1000;

    private final SpriteBatcher batcher;
    private final Camera2D camera;
    private final GLGraphics glGraphics;
    private final FileIO fileIO;
    private final BillingService billingService;
    private final ConfigurationService configurationService;
    private final SoundPlayerService sounds;
    private final VibrationService vibrator;
    private final SavedGame savedGame;
    private final AssetManager assets;
    private final Game game;
    private final Input input;
    private final String versionName;
    private final List<Star> stars;

    public ScreenFactory(
            GLGraphics glGraphics,
            FileIO fileIO,
            BillingService billingService,
            ConfigurationService configurationService,
            SoundPlayerService sounds,
            VibrationService vibrator,
            SavedGame savedGame,
            AssetManager assets,
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
        this.assets = assets;
        this.game = game;
        this.input = input;
        this.versionName = versionName;
        this.batcher = new SpriteBatcher(glGraphics, MAX_SPRITES);
        this.camera = new Camera2D(glGraphics, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, GameSpriteIdentifier.STAR_ANIMATIONS);
    }

    public IScreen newScreen(ScreenType screenType) {

        // each screen needs it's own instance of a controller
        // to handle specific user interactions.
        Controller controller = new ControllerImpl(input, camera);

        switch (screenType) {

            case SPLASH:
                return new ExitingScreen(
                        new SplashModelImpl(game, controller, billingService, versionName),
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
        Controller controller = new ControllerImpl(input, camera);
        Model gameModel = createGameModel(controller, startingWave);
        return new Screen(
                gameModel,
                controller,
                TextureMap.GAME,
                glGraphics,
                fileIO,
                camera,
                batcher);
    }

    /**
     * Returns a normal game model or a Frames-per-Second decorated version.
     */
    private Model createGameModel(Controller controller, int startingWave) {
        Model gameModel = new GamePlayModelImpl(
                game,
                controller,
                stars,
                startingWave,
                billingService,
                sounds,
                vibrator,
                savedGame,
                assets);

        if (SHOW_FPS) {
            return new GamePlayModelFrameRateDecorator((GamePlayModelImpl) gameModel);
        }

        return gameModel;
    }

    public IScreen newPausedGameScreen(List<ISprite> pausedSprites) {
        Controller controller = new ControllerImpl(input, camera);
        return new Screen(
                new GamePausedModelImpl(game, controller, pausedSprites),
                controller,
                TextureMap.GAME,
                glGraphics,
                fileIO,
                camera,
                batcher);
    }

    public IScreen newGameOverScreen(int previousWave) {
        Controller controller = new ControllerImpl(input, camera);
        return new Screen(
                new GameOverModelImpl(game, controller, stars, previousWave),
                controller,
                TextureMap.GAME,
                glGraphics,
                fileIO,
                camera,
                batcher);
    }


}
