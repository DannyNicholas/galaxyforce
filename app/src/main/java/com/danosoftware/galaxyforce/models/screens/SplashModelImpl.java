package com.danosoftware.galaxyforce.models.screens;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.BillingObserver;
import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.billing.PurchaseState;
import com.danosoftware.galaxyforce.buttons.button.Button;
import com.danosoftware.galaxyforce.buttons.button.ScreenTouch;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.TouchScreenModel;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.splash.SplashSprite;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;

import java.util.ArrayList;
import java.util.List;

public class SplashModelImpl implements Model, TouchScreenModel, BillingObserver {

    private final Game game;
    private final BillingService billingService;

    private final List<Text> text;
    private final List<ISprite> sprites;

    // how long splash screen has been displayed for so far (in seconds)
    private float splashScreenTime;

    // how long splash screen should be displayed for (in seconds)
    private static final float SPLASH_SCREEN_WAIT = 4f;

    // version name of this package
    private final String versionName;

    /*
     * Should we rebuild the screen text?
     * Normally triggered by a change in state from a billing thread.
     */
    private volatile boolean reBuildText;

    public SplashModelImpl(Game game,
                           Controller controller,
                           BillingService billingService,
                           String versionName) {

        this.game = game;
        this.billingService = billingService;
        this.versionName = versionName;
        this.sprites = new ArrayList<>();
        this.text = new ArrayList<>();
        this.splashScreenTime = 0f;
        this.reBuildText = false;

        sprites.add(new SplashSprite(
                GameConstants.SCREEN_MID_X,
                GameConstants.SCREEN_MID_Y,
                MenuSpriteIdentifier.PLUTO));

        sprites.add(new SplashSprite(
                GameConstants.SCREEN_MID_X,
                GameConstants.SCREEN_MID_Y,
                MenuSpriteIdentifier.GALAXY_FORCE));

        buildTextMessages();

        // add button that covers the entire screen
        Button screenTouch = new ScreenTouch(this);
        controller.addTouchController(new DetectButtonTouch(screenTouch));

        // register this model with the billing service
        billingService.registerPurchasesObserver(this);
    }

    /**
     * Update on-sceen text messages.
     */
    private void buildTextMessages() {

        text.clear();

        /*
         * Add text to indicate whether full game has been purchased
         */
        if (billingService.getFullGamePurchaseState() == PurchaseState.NOT_PURCHASED) {
            text.add(
                    Text.newTextRelativePositionX(
                            "FREE TRIAL",
                            TextPositionX.CENTRE,
                            150));
        } else if (billingService.getFullGamePurchaseState() == PurchaseState.PURCHASED) {
            text.add(
                    Text.newTextRelativePositionX(
                            "FULL GAME",
                            TextPositionX.CENTRE,
                            150));
        }

        // add version name if it exists
        if (versionName != null) {
            text.add(
                    Text.newTextRelativePositionX(
                            "VERSION " + versionName,
                            TextPositionX.CENTRE,
                            100));
        }
    }


    @Override
    public List<ISprite> getSprites() {
        return sprites;
    }

    @Override
    public List<Text> getText() {
        return text;
    }

    @Override
    public void update(float deltaTime) {

        if (reBuildText) {
            buildTextMessages();
            reBuildText = false;
        }

        // increment splash screen time count by deltaTime
        splashScreenTime = splashScreenTime + deltaTime;

        // if splash screen has been shown for required time, move to main menu
        if (splashScreenTime >= SPLASH_SCREEN_WAIT) {
            game.changeToScreen(ScreenType.MAIN_MENU);
        }
    }

    @Override
    public void dispose() {
        // unregister as observer of billing state changes
        billingService.unregisterPurchasesObserver(this);
    }

    @Override
    public void screenTouched() {
        // if screen pressed, then go to main menu
        game.changeToScreen(ScreenType.MAIN_MENU);
    }

    @Override
    public void goBack() {
        // No action. Splash screen does not change back button behaviour.
    }

    @Override
    public void resume() {
        // no action for this model
    }

    @Override
    public void pause() {
        // no action for this model
    }

    /**
     * model must rebuild text based on state of the billing service's
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
        reBuildText = true;
    }
}
