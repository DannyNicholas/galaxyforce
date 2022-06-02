package com.danosoftware.galaxyforce.models.screens;

import static com.danosoftware.galaxyforce.constants.GameConstants.DEFAULT_BACKGROUND_COLOUR;

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
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.sprites.common.IMovingSprite;
import com.danosoftware.galaxyforce.sprites.game.splash.BaseMovingSprite;
import com.danosoftware.galaxyforce.sprites.game.splash.LogoMovingSprite;
import com.danosoftware.galaxyforce.sprites.game.splash.PlanetMovingSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.providers.BasicMenuSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.MenuSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.SpriteProvider;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;
import com.danosoftware.galaxyforce.text.TextProvider;

public class SplashModelImpl implements Model, TouchScreenModel, BillingObserver {

  // how long splash screen should be displayed for (in seconds)
  private static final float SPLASH_SCREEN_WAIT = 4.5f;
  // delay before text is displayed
  private static final float DELAY_IN_SECONDS_BEFORE_TEXT_DISPLAYED = 3.5f;
  // variables to track planet and logo movements
  private static final int START_PLANET_Y_POS = -(267 / 2);
  private static final int START_LOGO_Y_POS = GameConstants.GAME_HEIGHT + (184 / 2);
  private final Game game;
  private final BillingService billingService;
  private final TextProvider textProvider;
  private final MenuSpriteProvider spriteProvider;
  // version name of this package
  private final String versionName;
  private final IMovingSprite planet;
  private final IMovingSprite logo;
  private final IMovingSprite base;
  // how long splash screen has been displayed for so far (in seconds)
  private float splashScreenTime;
  /*
   * Should we rebuild the screen text?
   * Normally triggered by a change in state from a billing thread.
   */
  private volatile boolean reBuildText;

  // have we requested to move to main menu? (we only want to request once)
  // required to avoid triggering multiple requests (while waiting for main menu screen to be created).
  private boolean requestedMainMenu;

  public SplashModelImpl(Game game,
      Controller controller,
      BillingService billingService,
      String versionName,
      SoundPlayerService sounds) {

    this.game = game;
    this.billingService = billingService;
    this.versionName = versionName;
    this.textProvider = new TextProvider();
    this.spriteProvider = new BasicMenuSpriteProvider();
    this.splashScreenTime = 0f;
    this.reBuildText = true;
    this.planet = new PlanetMovingSprite(
        GameConstants.SCREEN_MID_X,
        START_PLANET_Y_POS,
        SpriteDetails.PLUTO);
    this.logo = new LogoMovingSprite(
        GameConstants.SCREEN_MID_X,
        START_LOGO_Y_POS,
        SpriteDetails.GALAXY_FORCE);
    this.base = new BaseMovingSprite(
        sounds);

    spriteProvider.add(planet);
    spriteProvider.add(base);
    spriteProvider.add(logo);

    // add button that covers the entire screen
    Button screenTouch = new ScreenTouch(this);
    controller.addTouchController(new DetectButtonTouch(screenTouch));

    // register this model with the billing service
    billingService.registerPurchasesObserver(this);

    requestedMainMenu = false;
  }

  /**
   * Update on-screen text messages.
   */
  private void buildTextMessages() {

    textProvider.clear();

    /*
     * Add text to indicate whether full game has been purchased
     */
    final PurchaseState fullGamePurchaseState = billingService.getFullGamePurchaseState();
    if (fullGamePurchaseState == PurchaseState.NOT_PURCHASED
        || fullGamePurchaseState == PurchaseState.PENDING) {
      textProvider.add(
          Text.newTextRelativePositionX(
              "FREE TRIAL",
              TextPositionX.CENTRE,
              150));
    } else if (fullGamePurchaseState == PurchaseState.PURCHASED) {
      textProvider.add(
          Text.newTextRelativePositionX(
              "FULL GAME",
              TextPositionX.CENTRE,
              150));
    }

    // add version name if it exists
    if (versionName != null) {
      textProvider.add(
          Text.newUntrustedTextRelativePositionX(
              "VERSION " + versionName,
              TextPositionX.CENTRE,
              100));
    }
  }

  @Override
  public TextProvider getTextProvider() {
    return textProvider;
  }

  @Override
  public SpriteProvider getSpriteProvider() {
    return spriteProvider;
  }

  @Override
  public void update(float deltaTime) {

    // increment splash screen time count by deltaTime
    splashScreenTime += deltaTime;

    // display version state if...
    // set time has elapsed and a text requires re-building
    // this will also be built if a billing state update is received
    if (reBuildText && splashScreenTime > DELAY_IN_SECONDS_BEFORE_TEXT_DISPLAYED) {
      buildTextMessages();
      reBuildText = false;
    }

    planet.animate(deltaTime);
    logo.animate(deltaTime);
    base.animate(deltaTime);

    // if splash screen has been shown for required time, move to main menu
    if (!requestedMainMenu && splashScreenTime >= SPLASH_SCREEN_WAIT) {
      requestedMainMenu = true;
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
    requestedMainMenu = true;
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
  public RgbColour background() {
    return DEFAULT_BACKGROUND_COLOUR;
  }

  @Override
  public boolean animateStars() {
    return true;
  }

  @Override
  public void pause() {
    // no action for this model
  }

  /**
   * model must rebuild text based on state of the billing service's products on next update.
   * <p>
   * this method will be called by a billing thread after a purchase update. This is triggered by a
   * purchase or when the application starts or resumes from the background.
   *
   * @param state - latest state of full game purchase product
   */
  @Override
  public void onFullGamePurchaseStateChange(PurchaseState state) {
    Log.d(GameConstants.LOG_TAG, "Received full game purchase update: " + state.name());
    reBuildText = true;
  }
}
