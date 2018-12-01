package com.danosoftware.galaxyforce.models.screens;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.BillingObserver;
import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.models.button.ButtonModel;
import com.danosoftware.galaxyforce.models.button.ButtonType;
import com.danosoftware.galaxyforce.models.common.Model;
import com.danosoftware.galaxyforce.screen.Screen;
import com.danosoftware.galaxyforce.screen.ScreenFactory;
import com.danosoftware.galaxyforce.screen.ScreenFactory.ScreenType;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MainMenuModelImpl implements Model, ButtonModel, BillingObserver {
    /* logger tag */
    private static final String LOCAL_TAG = "MainModelModelImpl";

    // references to stars
    private List<Star> stars = null;

    // reference to all sprites in model
    private final List<ISprite> allSprites;

    // reference to all button sprites in model
    private final List<ISprite> buttons;

    // reference to logo sprite
    private ISprite logo;

    // reference to all text objects in model
    List<Text> allText;

    /* reference to controller */
    private final Controller controller;

    // reference to the billing service
    private final IBillingService billingService;

    /*
     * Should the model check the billing service for any changed products? The
     * model should check the billing service's products initially and then
     * following any notifications from the billing service.
     */
    private boolean checkBillingProducts;

    public MainMenuModelImpl(Controller controller, IBillingService billingService) {
        this.controller = controller;
        this.billingService = billingService;

        this.allSprites = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.allText = new ArrayList<>();

        // model must check the billing service's products on next update
        this.checkBillingProducts = true;

        // register this model with the billing service
        // billingService.registerProductObserver(this);

        /* set-up initial random position of stars */
        // stars = Star.setupStars(GameConstants.GAME_WIDTH,
        // GameConstants.GAME_HEIGHT, SelectScreenSpriteProperty.STAR,
        // SelectScreenSpriteProperty.STAR_ANIMATIONS);
        // allSprites.addAll(stars);

        // set-up levels
        // initialise();
    }

    @Override
    public void initialise() {
        /* set-up initial random position of stars */
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);

        /*
         * position logo half-way between top of screen (960) and top of play
         * button (674)
         */
        this.logo = new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE);

        // build screen sprites
        refreshSprites();

    }

    /**
     * Build all sprites required for screen. Normally called when screen is
     * being set-up or after any changes to upgrade buttons following a billing
     * state change.
     */
    private void refreshSprites() {
        allSprites.clear();
        allText.clear();

        allSprites.addAll(stars);
        allSprites.add(logo);

        // clear any current touch controllers prior to adding buttons
        controller.clearTouchControllers();

        // add mandatory buttons
        addMandatoryButtons();

        /*
         * if the full version has NOT been purchased then add the upgrade
         * button
         */
        if (billingService.isNotPurchased(GameConstants.FULL_GAME_PRODUCT_ID)) {
            // add upgrade button
            addUpgradeButton();
        }
        /*
         * if the all levels unlocks have NOT been purchased then add the unlock
         * button
         */
        else if (billingService.isPurchased(GameConstants.FULL_GAME_PRODUCT_ID)
                && billingService.isNotPurchased(GameConstants.ALL_LEVELS_PRODUCT_ID)) {
            // add unlock button
            addUnlockLevelsButton();
        }
    }

    /**
     * add three permanent menu buttons
     */
    private void addMandatoryButtons() {
        // add wanted buttons
        addNewMenuButton(3, "PLAY", ButtonType.PLAY);
        addNewMenuButton(2, "OPTIONS", ButtonType.OPTIONS);
        addNewMenuButton(1, "ABOUT", ButtonType.ABOUT);
    }

    /**
     * add upgrade button
     */
    private void addUpgradeButton() {
        addNewMenuButton(0, "UPGRADE", ButtonType.UPGRADE);
    }

    /**
     * add unlock button
     */
    private void addUnlockLevelsButton() {
        addNewMenuButton(0, "UNLOCK ALL", ButtonType.UNLOCK_ALL_LEVELS);
    }

    /**
     * add wanted menu button using the supplied row, label and type.
     */
    private void addNewMenuButton(int row, String label, ButtonType buttonType) {
        MenuButton button = new MenuButton(this, controller, GameConstants.GAME_WIDTH / 2, 100 + (row * 170), label, buttonType,
                MenuSpriteIdentifier.MAIN_MENU, MenuSpriteIdentifier.MAIN_MENU_PRESSED);

        // add new button's sprite to list of sprites
        buttons.add(button.getSprite());

        allSprites.add(button.getSprite());

        // add new button's text to list of text objects
        allText.add(button.getText());
    }

    @Override
    public List<ISprite> getSprites() {
        return allSprites;
    }

    @Override
    public List<Text> getText() {
        return allText;
    }

    @Override
    public void update(float deltaTime) {
        // move stars
        moveStars(deltaTime);

        // do we need to check billing service for product states
        if (checkBillingProducts) {
            // firstly set to false so we don't repeatedly check this
            checkBillingProducts = false;

            /*
             * refresh screen sprites following the billing state change. the
             * billing buttons displayed are likely to change.
             */
            refreshSprites();
        }
    }

    @Override
    public void dispose() {
        // unregister as observer of billing state changes
        // billingService.unregisterProductObserver(this);
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
                Screen aboutScreen = ScreenFactory.newScreen(ScreenType.ABOUT);
                Games.getGame().setScreen(aboutScreen);
                break;
            case OPTIONS:
                Log.i(LOCAL_TAG, "Options.");
                Screen optionsScreen = ScreenFactory.newScreen(ScreenType.OPTIONS);
                Games.getGame().setReturningScreen(optionsScreen);
                break;
            case PLAY:
                Log.i(LOCAL_TAG, "Play.");
                Screen selectLevelScreen = ScreenFactory.newScreen(ScreenType.SELECT_LEVEL);
                Games.getGame().setScreen(selectLevelScreen);
                break;
            case UPGRADE:
                Log.i(LOCAL_TAG, "Upgrade.");
                Screen unlockFullVersionScreen = ScreenFactory.newScreen(ScreenType.UPGRADE_FULL_VERSION);
                // Games.getGame().setScreen(unlockFullVersionScreen);
                Games.getGame().setReturningScreen(unlockFullVersionScreen);
                break;
            case UNLOCK_ALL_LEVELS:
                Log.i(LOCAL_TAG, "Unlock All Levels.");
                Screen unlockAllZonesScreen = ScreenFactory.newScreen(ScreenType.UPGRADE_ALL_ZONES);
                // Games.getGame().setScreen(unlockAllZonesScreen);
                Games.getGame().setReturningScreen(unlockAllZonesScreen);
                break;
            default:
                // not valid option - do nothing
                break;
        }

        // TODO Auto-generated method stub

    }

    @Override
    public void goBack() {
        // No action. Main menu does not change back button behaviour.
    }

    @Override
    public void resume() {
        /*
         * register this model with the billing service. do this after a resume
         * so it re-registers when starting, after pausing or when returning
         * from the payment screen.
         */
        billingService.registerProductObserver(this);

        // force a check of the billing service's products on next update
        this.checkBillingProducts = true;
    }

    @Override
    public void pause() {
        // unregister as observer of billing state changes
        billingService.unregisterProductObserver(this);
    }

    @Override
    public void billingProductsStateChange() {
        /*
         * model must check the billing service's products on next update.
         *
         * don't take any other action at this stage. this method will be called
         * by a billing thread. to keep implementation simple just take the
         * necessary action in the next update in the main thread.
         */
        Log.d(GameConstants.LOG_TAG, LOCAL_TAG + ": Received billing products state change message.");
        this.checkBillingProducts = true;
    }
}
