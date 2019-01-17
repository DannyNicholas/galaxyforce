package com.danosoftware.galaxyforce.models.screens;

import android.util.Log;

import com.android.billingclient.api.SkuDetails;
import com.danosoftware.galaxyforce.billing.service.new_service.BillingObserver;
import com.danosoftware.galaxyforce.billing.service.new_service.BillingService;
import com.danosoftware.galaxyforce.billing.service.new_service.PurchaseState;
import com.danosoftware.galaxyforce.billing.service.new_service.SkuDetailsListener;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.splash.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.starfield.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;

import java.util.ArrayList;
import java.util.List;

public class UnlockFullVersionModelImpl implements Model, BillingObserver, ButtonModel, SkuDetailsListener {

    /* logger tag */
    private static final String LOCAL_TAG = "UnlockFullVersionModel";

    private final Game game;
    private final List<Star> stars;
    private final ISprite logo;

    // messages to display on the screen
    private final List<Text> messages;

    private ModelState modelState;
    private final Controller controller;
    private final BillingService billingService;

    // details of the full game unlock purchase
    private volatile SkuDetails skuDetails;

    /*
     * Should we rebuild the screen sprites?
     * Normally triggered by a change in state from a billing thread.
     */
    private volatile boolean reBuildSprites;

    // all visible buttons
    private final List<SpriteTextButton> buttons;

    public UnlockFullVersionModelImpl(
            Game game,
            Controller controller,
            BillingService billingService) {

        this.game = game;
        this.controller = controller;
        this.billingService = billingService;
        this.modelState = ModelState.RUNNING;
        this.buttons = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);
        this.logo = new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE);
        this.reBuildSprites = false;

        // register this model with the billing service
        billingService.registerPurchasesObserver(this);

        // query upgrade price by requesting the full-game purchase SKU details asyncronously.
        // onSkuDetailsRetrieved() will be invoked when SKU details are available.
        billingService.queryFullGameSkuDetailsAsync(this);

        // update the screen with text and add any buttons
        reBuildSprites(true);
    }

    /**
     * Build billing buttons and messages required for screen.
     * Normally called when screen is being set-up or after any
     * changes to upgrade buttons following a billing state change.
     */
    private void reBuildSprites(boolean showButtons) {

        // clear any current touch controllers prior to modifying buttons
        controller.clearTouchControllers();

        messages.clear();
        buttons.clear();

        /*
         * if the full version has NOT been purchased then add the upgrade
         * button and text
         */
        if (billingService.getFullGamePurchaseState() == PurchaseState.NOT_PURCHASED) {
            prepareUpgradeFullVersion(showButtons);
        }
        /*
         * if the full version has been purchased then display successful
         * upgrade text
         */
        else if (billingService.getFullGamePurchaseState() == PurchaseState.PURCHASED) {
            prepareUpgradeFullVersionSuccess();
        }
        /*
         * Purchases not ready.
         * This state should rarely/never occur. if so, should hopefully only be a
         * temporary state until purchases are returned asynchronously.
         */
        else if (billingService.getFullGamePurchaseState() == PurchaseState.NOT_READY) {
            prepareUnknownPurchaseState();
        }
    }

    private void prepareUpgradeFullVersion(boolean showButtons) {

        int maxFreeZones = GameConstants.MAX_FREE_WAVE;

        messages.add(
                Text.newTextRelativePositionX(
                        "TO PLAY BEYOND",
                        TextPositionX.CENTRE,
                        600 + 50));
        messages.add(
                Text.newTextRelativePositionX(
                        maxFreeZones + " FREE ZONES",
                        TextPositionX.CENTRE,
                        600));
        messages.add(
                Text.newTextRelativePositionX(
                        "UPGRADE TO",
                        TextPositionX.CENTRE,
                        450 + 50));
        messages.add(
                Text.newTextRelativePositionX(
                        "FULL VERSION",
                        TextPositionX.CENTRE,
                        450));

        if (skuDetails != null) {
            messages.add(
                    Text.newTextRelativePositionX(
                            "PRICE",
                            TextPositionX.CENTRE,
                            300 + 50));
            messages.add(
                    Text.newTextRelativePositionX(
                            skuDetails.getPrice(),
                            TextPositionX.CENTRE,
                            300));

            // allows button to be removed if async purchase process is in progress
            if (showButtons) {
                // add upgrade button
                addNewMenuButton(0, "UPGRADE", ButtonType.UPGRADE);
            }
        }
    }

    private void prepareUnknownPurchaseState() {

        int maxFreeZones = GameConstants.MAX_FREE_WAVE;

        messages.add(
                Text.newTextRelativePositionX(
                        "TO PLAY BEYOND",
                        TextPositionX.CENTRE,
                        600 + 50));
        messages.add(
                Text.newTextRelativePositionX(
                        maxFreeZones + " FREE ZONES",
                        TextPositionX.CENTRE,
                        600));
        messages.add(
                Text.newTextRelativePositionX(
                        "UPGRADE TO",
                        TextPositionX.CENTRE,
                        450 + 50));
        messages.add(
                Text.newTextRelativePositionX(
                        "FULL VERSION",
                        TextPositionX.CENTRE,
                        450));

        messages.add(
                Text.newTextRelativePositionX(
                        "SORRY! UPGRADE",
                        TextPositionX.CENTRE,
                        300 + 50));
        messages.add(
                Text.newTextRelativePositionX(
                        "NOT AVAILABLE",
                        TextPositionX.CENTRE,
                        300));

        addNewMenuButton(0, "BACK", ButtonType.EXIT);
    }

    private void prepareUpgradeFullVersionSuccess() {

        messages.add(
                Text.newTextRelativePositionX(
                        "CONGRATULATIONS",
                        TextPositionX.CENTRE,
                        600));
        messages.add(
                Text.newTextRelativePositionX(
                        "FULL VERSION",
                        TextPositionX.CENTRE,
                        450 + 50));
        messages.add(
                Text.newTextRelativePositionX(
                        "UNLOCKED",
                        TextPositionX.CENTRE,
                        450));

        addNewMenuButton(0, "BACK", ButtonType.EXIT);
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

        /// add new button to list
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
        text.addAll(messages);

        return text;
    }

    @Override
    public void update(float deltaTime) {

        // return to previous screen
        if (modelState == ModelState.GO_BACK) {
            // return back to previous screen
            game.screenReturn();
            return;
        }

        // move stars
        for (Star eachStar : stars) {
            eachStar.animate(deltaTime);
        }

        /*
         * refresh screen sprites. triggered following the billing state change.
         * may result in text and button changes following a successful purchase.
         */
        if (reBuildSprites) {
            reBuildSprites(true);
            reBuildSprites = false;
        }
    }

    @Override
    public void dispose() {
        // unregister as observer of billing state changes
        billingService.unregisterPurchasesObserver(this);
    }

    @Override
    public void resume() {
        // no action for this model
    }

    @Override
    public void pause() {
        // no action for this model
    }

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {
            case UPGRADE:
                Log.d(GameConstants.LOG_TAG, "Start Upgrade Purchase.");

                /*
                 * remove purchase button to avoid user trying to click it again
                 * during an async update delay following the purchase
                 */
                reBuildSprites(false);

                // purchase product
                billingService.purchaseFullGame(skuDetails);

                break;

            case EXIT:
                Log.d(GameConstants.LOG_TAG, "Exit Upgrade Purchase.");
                goBack();
                break;
            default:
                Log.e(LOCAL_TAG, "Unsupported button: '" + buttonType + "'.");
                break;
        }
    }

    @Override
    public void goBack() {
        this.modelState = ModelState.GO_BACK;
    }

    @Override
    public void onSkuDetailsRetrieved(SkuDetails skuDetails) {
        if (skuDetails != null) {
            this.skuDetails = skuDetails;
            this.reBuildSprites = true;
            Log.d(LOCAL_TAG, "Retrieved SkuDetails: " + skuDetails);
        } else {
            Log.w(GameConstants.LOG_TAG, "Null skuDetails received.");
        }
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
        this.reBuildSprites = true;
    }
}
