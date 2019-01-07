package com.danosoftware.galaxyforce.models.screens;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.BillingObserver;
import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;

import java.util.ArrayList;
import java.util.List;

public class UnlockAllZonesModelImpl implements Model, BillingObserver, ButtonModel {

    /* logger tag */
    private static final String LOCAL_TAG = "UnlockAllZonesModelImpl";

    private final Game game;
    private final List<Star> stars;
    private final ISprite logo;

    // messages to display on the screen
    private final List<Text> messages;

    private ModelState modelState;
    private final Controller controller;
    private final IBillingService billingService;

    /*
     * Should we rebuild the screen sprites?
     * Normally triggered by a change in state from a billing thread.
     */
    private volatile boolean reBuildSprites;

    // all visible buttons
    private final List<SpriteTextButton> buttons;

    public UnlockAllZonesModelImpl(Game game, Controller controller, IBillingService billingService) {
        this.game = game;
        this.controller = controller;
        this.billingService = billingService;
        this.modelState = ModelState.RUNNING;
        this.buttons = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);
        this.logo = new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE);
        this.reBuildSprites = false;

        // update the screen with text and add any buttons
        reBuildSprites(true);

        // register this model with the billing service
        billingService.registerProductObserver(this);
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
         * if the unlock all zones has NOT been purchased then add the upgrade
         * button and text
         */
        if (billingService.isNotPurchased(GameConstants.ALL_LEVELS_PRODUCT_ID)) {
            prepareUnlockAllZones(showButtons);
        }
        /*
         * if the unlock all zones has been purchased then display successful
         * upgrade text
         */
        else if (billingService.isPurchased(GameConstants.ALL_LEVELS_PRODUCT_ID)) {
            prepareUnlockAllZonesSuccess();
        }
    }

    private void prepareUnlockAllZones(boolean showButtons) {

        int maxZones = GameConstants.MAX_WAVES;

        messages.add(
                Text.newTextRelativePositionX(
                        "PLAY ANY ZONE",
                        TextPositionX.CENTRE,
                        600));
        messages.add(
                Text.newTextRelativePositionX(
                        "UNLOCK ALL",
                        TextPositionX.CENTRE,
                        450 + 50));
        messages.add(
                Text.newTextRelativePositionX(
                        maxZones + " ZONES",
                        TextPositionX.CENTRE,
                        450));

        String price = billingService.getPrice(
                GameConstants.ALL_LEVELS_PRODUCT_ID);

        if (price != null) {
            messages.add(
                    Text.newTextRelativePositionX(
                            "PRICE",
                            TextPositionX.CENTRE,
                            300 + 50));
            messages.add(
                    Text.newTextRelativePositionX(
                            price,
                            TextPositionX.CENTRE,
                            300));
        }

        // only show unlock button if async purchase not already in-progress
        if (showButtons) {
            addNewMenuButton(0, "UNLOCK ALL", ButtonType.UNLOCK_ALL_LEVELS);
        }
    }

    private void prepareUnlockAllZonesSuccess() {

        int maxZones = GameConstants.MAX_WAVES;

        messages.add(
                Text.newTextRelativePositionX(
                        "CONGRATULATIONS",
                        TextPositionX.CENTRE,
                        600));
        messages.add(
                Text.newTextRelativePositionX(
                        "ALL " + maxZones + " ZONES",
                        TextPositionX.CENTRE,
                        450 + 50));
        messages.add(
                Text.newTextRelativePositionX(
                        "UNLOCKED",
                        TextPositionX.CENTRE,
                        450));
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

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {
            case UNLOCK_ALL_LEVELS:
                Log.d(GameConstants.LOG_TAG, LOCAL_TAG + ": Start Unlock All Purchase.");

                /*
                 * remove purchase button to avoid user trying to click it again
                 * during an async update delay following the purchase
                 */
                reBuildSprites(false);

                // purchase product
                billingService.purchase(GameConstants.ALL_LEVELS_PRODUCT_ID);

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
    public void billingProductsStateChange() {
        /*
         * model must rebuild sprites based on state of the billing service's
         * products on next update.
         *
         * this method will be called by a billing thread.
         */
        Log.d(GameConstants.LOG_TAG, LOCAL_TAG + ": Received billing products state change message.");
        this.reBuildSprites = true;
    }

}
