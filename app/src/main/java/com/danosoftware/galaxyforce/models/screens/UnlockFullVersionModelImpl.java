package com.danosoftware.galaxyforce.models.screens;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.BillingObserver;
import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class UnlockFullVersionModelImpl implements Model, BillingObserver, ButtonModel {
    /* logger tag */
    private static final String LOCAL_TAG = "UnlockFullVersionModel";

    private final Game game;

    // references to stars
    private List<Star> stars;

    // reference to all sprites in model
    private final List<ISprite> allSprites;

    private ModelState modelState;

    // reference to all text objects in model
    private final List<Text> allText;

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

    // reference to logo sprite
    private ISprite logo = null;

    // reference to all button sprites in model
    private final List<ISprite> buttons;

    public UnlockFullVersionModelImpl(Game game, Controller controller, IBillingService billingService) {
        this.game = game;
        this.controller = controller;
        this.billingService = billingService;
        this.allSprites = new ArrayList<>();
        this.allText = new ArrayList<>();
        this.buttons = new ArrayList<>();

        // register this model with the billing service
        billingService.registerProductObserver(this);

        // model must check the billing service's products on next update
        this.checkBillingProducts = true;
    }

    @Override
    public void initialise() {
        /* set-up initial random position of stars */
        stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);

        /* set-up logo */
        this.logo = new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE);

        // update the screen with text and add any buttons
        refreshSprites(true);
    }

    /**
     * Build all sprites required for screen. Normally called when screen is
     * being set-up or after any changes to upgrade buttons following a billing
     * state change.
     */
    private void refreshSprites(boolean showButtons) {
        allSprites.clear();
        allText.clear();

        allSprites.addAll(stars);
        allSprites.add(logo);

        // clear any current touch controllers prior to adding buttons
        controller.clearTouchControllers();

        /*
         * if the full version has NOT been purchased then add the upgrade
         * button and text
         */
        if (billingService.isNotPurchased(GameConstants.FULL_GAME_PRODUCT_ID)) {
            prepareUpgradeFullVersion(showButtons);
        }
        /*
         * if the full version has been purchased then display successful
         * upgrade text
         */
        else if (billingService.isPurchased(GameConstants.FULL_GAME_PRODUCT_ID)) {
            prepareUpgradeFullVersionSuccess();
        }
    }

    private void prepareUpgradeFullVersion(boolean showButtons) {
        // prepare text for screen
        int maxFreeZones = GameConstants.MAX_FREE_ZONE;

        allText.add(Text.newTextRelativePositionX("TO PLAY BEYOND", TextPositionX.CENTRE, 600 + 50));
        allText.add(Text.newTextRelativePositionX(maxFreeZones + " FREE ZONES", TextPositionX.CENTRE, 600));

        allText.add(Text.newTextRelativePositionX("UPGRADE TO", TextPositionX.CENTRE, 450 + 50));
        allText.add(Text.newTextRelativePositionX("FULL VERSION", TextPositionX.CENTRE, 450));

        String price = billingService.getPrice(GameConstants.FULL_GAME_PRODUCT_ID);

        if (price != null) {
            allText.add(Text.newTextRelativePositionX("PRICE", TextPositionX.CENTRE, 300 + 50));
            allText.add(Text.newTextRelativePositionX(price, TextPositionX.CENTRE, 300));
        }

        // allows button to be removed if async purchase process is in progress
        if (showButtons) {
            // add upgrade button
            addNewMenuButton(0, "UPGRADE", ButtonType.UPGRADE);
        }
    }

    private void prepareUpgradeFullVersionSuccess() {
        allText.add(Text.newTextRelativePositionX("CONGRATULATIONS", TextPositionX.CENTRE, 600));

        allText.add(Text.newTextRelativePositionX("FULL VERSION", TextPositionX.CENTRE, 450 + 50));
        allText.add(Text.newTextRelativePositionX("UNLOCKED", TextPositionX.CENTRE, 450));
    }

    /**
     * add wanted menu button using the supplied row, label and type.
     */
    private void addNewMenuButton(int row, String label, ButtonType buttonType) {
        MenuButton button = new MenuButton(this, GameConstants.GAME_WIDTH / 2, 100 + (row * 170), label, buttonType,
                MenuSpriteIdentifier.MAIN_MENU, MenuSpriteIdentifier.MAIN_MENU_PRESSED);

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(button));

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
        // return to main menu
        // if (getState() == ModelState.GO_BACK)
        // {
        // Screen screen = ScreenFactory.newScreen(ScreenType.MAIN_MENU);
        // Games.getGame().changeToScreen(screen);
        // return;
        // }

        // return to previous screen
        if (getState() == ModelState.GO_BACK) {
            // return back to previous screen
            game.screenReturn();
            return;
        }

        // move stars
        moveStars(deltaTime);

        // do we need to check billing service for product states
        if (checkBillingProducts) {
            // firstly set to false so we don't repeatedly check this
            checkBillingProducts = false;

            /*
             * refresh screen sprites following the billing state change. the
             * text will change following a successful purchase.
             */
            refreshSprites(true);
        }
    }

    @Override
    public void dispose() {
        // unregister as observer of billing state changes
        billingService.unregisterProductObserver(this);
    }

    @Override
    public void resume() {
        // no action for this model
    }

    @Override
    public void pause() {
        // no action for this model
    }

    private void moveStars(float deltaTime) {
        for (Star eachStar : stars) {
            eachStar.animate(deltaTime);
        }
    }

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {
            case UPGRADE:
                Log.d(GameConstants.LOG_TAG, LOCAL_TAG + ": Start Upgrade Purchase.");

                /*
                 * remove purchase button to avoid user trying to click it again
                 * during an async update delay following the purchase
                 */
                refreshSprites(false);

                // purchase product
                billingService.purchase(GameConstants.FULL_GAME_PRODUCT_ID);

                break;
            default:
                Log.e(LOCAL_TAG, "Unsupported button: '" + buttonType + "'.");
                break;
        }
    }

    @Override
    public void goBack() {
        setState(ModelState.GO_BACK);
    }

    private void setState(ModelState modelState) {
        this.modelState = modelState;
    }

    private ModelState getState() {
        return modelState;
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
