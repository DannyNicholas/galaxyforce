package com.danosoftware.galaxyforce.models.screens;

import static com.danosoftware.galaxyforce.constants.GameConstants.DEFAULT_BACKGROUND_COLOUR;
import static com.danosoftware.galaxyforce.constants.GameConstants.LOGO_Y_POS;
import static com.danosoftware.galaxyforce.constants.GameConstants.PLANET_Y_POS;

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
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.splash.SplashSprite;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.providers.BasicMenuSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.MenuSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.SpriteProvider;
import com.danosoftware.galaxyforce.text.TextProvider;
import java.util.ArrayList;
import java.util.List;

public class MainMenuModelImpl implements Model, ButtonModel, BillingObserver {

  /* logger tag */
  private static final String LOCAL_TAG = "MainMenuModelImpl";

  private final Game game;

  // sprites
  private final ISprite logo;
  private final ISprite planet;

  // current visible buttons
  private final List<SpriteTextButton> buttons;

  private final Controller controller;
  private final BillingService billingService;

  private final TextProvider textProvider;
  private final MenuSpriteProvider spriteProvider;

  /*
   * Should we rebuild the buttons?
   * Normally triggered by a change in state from a billing thread.
   */
  private volatile boolean rebuildButtons;

  public MainMenuModelImpl(
      Game game,
      Controller controller,
      BillingService billingService) {
    this.game = game;
    this.controller = controller;
    this.billingService = billingService;
    this.buttons = new ArrayList<>();
    this.textProvider = new TextProvider();
    this.spriteProvider = new BasicMenuSpriteProvider();
    this.logo = new SplashSprite(GameConstants.SCREEN_MID_X, LOGO_Y_POS,
        SpriteDetails.GALAXY_FORCE);
    this.planet = new SplashSprite(GameConstants.SCREEN_MID_X, PLANET_Y_POS,
        SpriteDetails.PLUTO);

    // register this model with the billing service
    billingService.registerPurchasesObserver(this);

    // build on-screen buttons
    buildButtons();
    this.rebuildButtons = false;

    // build sprites
    buildSprites();
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

  private void buildSprites() {
    spriteProvider.clear();
    textProvider.clear();

    spriteProvider.add(planet);
    spriteProvider.add(logo);

    for (SpriteTextButton button : buttons) {
      spriteProvider.add(button.getSprite());
      textProvider.add(button.getText());
    }
  }

  /**
   * add three permanent menu buttons
   */
  private void addMandatoryButtons() {
    addNewMenuButton(3, "PLAY", ButtonType.PLAY);
    addNewMenuButton(2, "OPTIONS", ButtonType.OPTIONS);
  }

  /**
   * add an optional billing button depending on billing state
   */
  private void addOptionalBillingButtons() {
    /*
     * if the full version has NOT been purchased then add the upgrade
     * button.
     * We still show the upgrade button if the purchase is pending.
     * The upgrade screen will tell the user that their purchase is still
     * pending (may need some action from them).
     */
    final PurchaseState fullGamePurchaseState = billingService.getFullGamePurchaseState();
    if (fullGamePurchaseState == PurchaseState.NOT_PURCHASED ||
        fullGamePurchaseState == PurchaseState.PENDING) {
      addNewMenuButton(1, "UPGRADE", ButtonType.UPGRADE);
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
        SpriteDetails.MAIN_MENU,
        SpriteDetails.MAIN_MENU_PRESSED);

    // add a new menu button to controller's list of touch controllers
    controller.addTouchController(new DetectButtonTouch(button));

    // add new button to list
    buttons.add(button);
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
    // do we need to rebuild menu buttons and sprites?
    if (rebuildButtons) {
      buildButtons();
      rebuildButtons = false;
      buildSprites();
    }
  }

  @Override
  public void dispose() {
    billingService.unregisterPurchasesObserver(this);
  }

  @Override
  public void processButton(ButtonType buttonType) {
    switch (buttonType) {
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
    // no action
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
    // no implementation
  }

  /**
   * model must rebuild sprites based on state of the billing service's products on next update.
   * <p>
   * this method will be called by a billing thread after a purchase update. This is triggered by a
   * purchase or when the application starts or resumes from the background.
   *
   * @param state - latest state of full game purchase product
   */
  @Override
  public void onFullGamePurchaseStateChange(PurchaseState state) {
    Log.d(GameConstants.LOG_TAG, "Received full game purchase update: " + state.name());
    this.rebuildButtons = true;
  }
}
