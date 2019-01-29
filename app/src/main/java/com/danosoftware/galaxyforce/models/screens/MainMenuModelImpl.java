package com.danosoftware.galaxyforce.models.screens;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.BillingObserver;
import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.billing.PurchaseState;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.splash.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.starfield.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MainMenuModelImpl implements Model, ButtonModel, BillingObserver {

    /* logger tag */
    private static final String LOCAL_TAG = "MainMenuModelImpl";

    private final Game game;

    // sprites
    private final List<Star> stars;
    private final ISprite logo;

    // current visible buttons
    private final List<SpriteTextButton> buttons;

    private final Controller controller;
    private final BillingService billingService;

    /*
     * Should we rebuild the buttons?
     * Normally triggered by a change in state from a billing thread.
     */
    private volatile boolean rebuildButtons;

    public MainMenuModelImpl(Game game, Controller controller, BillingService billingService) {
        this.game = game;
        this.controller = controller;
        this.billingService = billingService;
        this.buttons = new ArrayList<>();
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);
        this.logo = new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE);

        // register this model with the billing service
        billingService.registerPurchasesObserver(this);

        // build on-screen buttons
        buildButtons();
        this.rebuildButtons = false;
    }

    private void buildButtons() {
        // clear any current touch controllers prior to adding buttons
        controller.clearTouchControllers();

        buttons.clear();

        // add mandatory buttons
        addMandatoryButtons();

        // add optional billing buttons
        addOptionalBillingButtons();
    }

    /**
     * add three permanent menu buttons
     */
    private void addMandatoryButtons() {
        addNewMenuButton(3, "PLAY", ButtonType.PLAY);
        addNewMenuButton(2, "OPTIONS", ButtonType.OPTIONS);
        addNewMenuButton(1, "ABOUT", ButtonType.ABOUT);
    }

    /**
     * add an optional billing button depending on billing state
     */
    private void addOptionalBillingButtons() {
        /*
         * if the full version has NOT been purchased then add the upgrade
         * button
         */
        if (billingService.getFullGamePurchaseState() == PurchaseState.NOT_PURCHASED) {
            addNewMenuButton(0, "UPGRADE", ButtonType.UPGRADE);
        }
    }

    /**
     * add wanted menu button using the supplied row, label and type.
     */
    private void addNewMenuButton(int row, String label, ButtonType buttonType) {

        // create button
        MenuButton button = new MenuButton(
                this,
                GameConstants.GAME_WIDTH / 2,
                100 + (row * 170),
                label,
                buttonType,
                MenuSpriteIdentifier.MAIN_MENU,
                MenuSpriteIdentifier.MAIN_MENU_PRESSED);

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(button));

        // add new button to list
        buttons.add(button);
    }

    @Override
    public List<ISprite> getSprites() {

        List<ISprite> sprites = new ArrayList<>();
        sprites.addAll(stars);
        sprites.add(logo);

        for (SpriteTextButton button : buttons) {
            sprites.add(button.getSprite());
        }

        return sprites;
    }

    @Override
    public List<Text> getText() {

        List<Text> text = new ArrayList<>();
        for (SpriteTextButton button : buttons) {
            text.add(button.getText());
        }

        return text;
    }

    @Override
    public void update(float deltaTime) {
        // move stars
        moveStars(deltaTime);

        // do we need to rebuild menu buttons?
        if (rebuildButtons) {
            buildButtons();
            rebuildButtons = false;
        }
    }

    @Override
    public void dispose() {
        billingService.unregisterPurchasesObserver(this);
    }

    private void moveStars(float deltaTime) {
        for (Star eachStar : stars) {
            eachStar.animate(deltaTime);
        }
    }

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {
            case ABOUT:
                Log.i(LOCAL_TAG, "About.");
                game.changeToScreen(ScreenType.ABOUT);
                break;
            case OPTIONS:
                Log.i(LOCAL_TAG, "Options.");
                game.changeToReturningScreen(ScreenType.OPTIONS);
                break;
            case PLAY:
                Log.i(LOCAL_TAG, "Play.");
                game.changeToScreen(ScreenType.SELECT_LEVEL);
                break;
            case UPGRADE:
                Log.i(LOCAL_TAG, "Upgrade.");
                game.changeToReturningScreen(ScreenType.UPGRADE_FULL_VERSION);
                break;
            default:
                // not valid option - do nothing
                break;
        }
    }

    @Override
    public void goBack() {
        // No action. Main menu does not change back button behaviour
        // and allows application exit.
    }

    @Override
    public void resume() {
        // no implementation
    }

    @Override
    public void pause() {
        // no implementation
    }

    /**
     * model must rebuild sprites based on state of the billing service's
     * products on next update.
     * <p>
     * this method will be called by a billing thread after a purchase update.
     * This is triggered by a purchase or when the application starts
     * or resumes from the background.
     *
     * @param state - latest state of full game purchase product
     */
    @Override
    public void onFullGamePurchaseStateChange(PurchaseState state) {
        Log.d(GameConstants.LOG_TAG, "Received full game purchase update: " + state.name());
        this.rebuildButtons = true;
    }
}
