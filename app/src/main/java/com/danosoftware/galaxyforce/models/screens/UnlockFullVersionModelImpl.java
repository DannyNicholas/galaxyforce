package com.danosoftware.galaxyforce.models.screens;

import static com.danosoftware.galaxyforce.constants.GameConstants.DEFAULT_BACKGROUND_COLOUR;

import android.util.Log;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetails.OneTimePurchaseOfferDetails;
import com.danosoftware.galaxyforce.billing.BillingObserver;
import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.billing.ProductDetailsListener;
import com.danosoftware.galaxyforce.billing.PurchaseState;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.models.screens.flashing.FlashingText;
import com.danosoftware.galaxyforce.models.screens.flashing.FlashingTextImpl;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.splash.SplashSprite;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.providers.BasicMenuSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.MenuSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.SpriteProvider;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextChangeListener;
import com.danosoftware.galaxyforce.text.TextPositionX;
import com.danosoftware.galaxyforce.text.TextProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UnlockFullVersionModelImpl implements Model, BillingObserver, ButtonModel,
    ProductDetailsListener, TextChangeListener {

  /* logger tag */
  private static final String LOCAL_TAG = "UnlockFullVersionModel";

  private final Game game;
  private final ISprite logo;

  // messages to display on the screen
  private final List<Text> messages;
  private final TextProvider textProvider;
  private final MenuSpriteProvider spriteProvider;
  private final Controller controller;
  private final BillingService billingService;
  // all visible buttons
  private final List<SpriteTextButton> buttons;
  private FlashingText flashingText;
  private boolean updateText;
  private boolean updateSprites;
  private ModelState modelState;
  // details of the full game unlock purchase
  private volatile ProductDetails productDetails;
  /*
   * Should we rebuild the screen sprites?
   * Normally triggered by a change in state from a billing thread.
   */
  private volatile boolean reBuildSprites;

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
    this.textProvider = new TextProvider();
    this.spriteProvider = new BasicMenuSpriteProvider();
    this.flashingText = null;
    this.logo = new SplashSprite(GameConstants.SCREEN_MID_X, 817,
        SpriteDetails.GALAXY_FORCE);
    this.reBuildSprites = false;

    // register this model with the billing service
    billingService.registerPurchasesObserver(this);

    // update the screen with text and add any buttons
    reBuildSprites(true);
  }

  /**
   * Build billing buttons and messages required for screen. Normally called when screen is being
   * set-up or after any changes to upgrade buttons following a billing state change.
   */
  private void reBuildSprites(boolean showButtons) {

    // clear any current touch controllers prior to modifying buttons
    controller.clearTouchControllers();

    messages.clear();
    buttons.clear();
    flashingText = null;

    /*
     * if the full version has NOT been purchased then add the upgrade
     * button and text
     */
    final PurchaseState fullGamePurchaseState = billingService.getFullGamePurchaseState();
    if (fullGamePurchaseState == PurchaseState.NOT_PURCHASED) {
      prepareUpgradeFullVersion(showButtons);
    }
    /*
     * if the full version has been purchased then display successful
     * upgrade text
     */
    else if (fullGamePurchaseState == PurchaseState.PURCHASED) {
      prepareUpgradeFullVersionSuccess();
    }
    /*
     * Purchase is pending - may succeed or fail at this stage.
     */
    else if (fullGamePurchaseState == PurchaseState.PENDING) {
      preparePendingPurchaseState();
    }
    /*
     * Purchases not ready.
     * This state should rarely/never occur. if so, should hopefully only be a
     * temporary state until purchases are returned asynchronously.
     */
    else if (fullGamePurchaseState == PurchaseState.NOT_READY) {
      prepareUnknownPurchaseState();
    }

    // trigger refresh of model sprites/text
    updateText = true;
    updateSprites = true;
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

    if (productDetails != null) {
      OneTimePurchaseOfferDetails purchaseDetails = productDetails.getOneTimePurchaseOfferDetails();
      messages.add(
          Text.newTextRelativePositionX(
              "PRICE",
              TextPositionX.CENTRE,
              300 + 50));
      messages.add(
          Text.newUntrustedTextRelativePositionX(
              purchaseDetails.getPriceCurrencyCode() // GBP
                  + " " +
                  purchaseDetails.getFormattedPrice(), // e.g. £0.78
              TextPositionX.CENTRE,
              300));

      // allows button to be removed if async purchase process is in progress
      if (showButtons) {
        // add upgrade button
        addNewMenuButton(0, "UPGRADE", ButtonType.UPGRADE);
      }
    } else {
      this.flashingText = new FlashingTextImpl(
          Collections.singletonList(Text.newTextRelativePositionX(
              "PLEASE WAIT...",
              TextPositionX.CENTRE,
              200)),
          0.5f,
          this,
          false);
    }
  }

  private void preparePendingPurchaseState() {

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

    this.flashingText = new FlashingTextImpl(
        Arrays.asList(
            Text.newTextRelativePositionX(
                "YOUR PURCHASE",
                TextPositionX.CENTRE,
                300 + 50),
            Text.newTextRelativePositionX(
                "IS PENDING",
                TextPositionX.CENTRE,
                300)),
        0.5f,
        this,
        false);

    addNewMenuButton(0, "BACK", ButtonType.EXIT);
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

    this.flashingText = new FlashingTextImpl(
        Arrays.asList(
            Text.newTextRelativePositionX(
                "SORRY! UPGRADE",
                TextPositionX.CENTRE,
                300 + 50),
            Text.newTextRelativePositionX(
                "NOT AVAILABLE",
                TextPositionX.CENTRE,
                300)),
        0.5f,
        this,
        false);

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
        SpriteDetails.MAIN_MENU,
        SpriteDetails.MAIN_MENU_PRESSED);

    // add a new menu button to controller's list of touch controllers
    controller.addTouchController(new DetectButtonTouch(button));

    /// add new button to list
    buttons.add(button);
  }

  @Override
  public TextProvider getTextProvider() {
    if (updateText) {
      textProvider.clear();
      for (SpriteTextButton button : buttons) {
        textProvider.add(button.getText());
      }
      textProvider.addAll(messages);
      if (flashingText != null) {
        textProvider.addAll(flashingText.text());
      }
      updateText = false;
    }
    return textProvider;
  }

  @Override
  public SpriteProvider getSpriteProvider() {
    if (updateSprites) {
      spriteProvider.clear();
      spriteProvider.add(logo);
      for (SpriteTextButton button : buttons) {
        spriteProvider.add(button.getSprite());
      }
      updateSprites = false;
    }
    return spriteProvider;
  }

  @Override
  public void update(float deltaTime) {

    if (flashingText != null) {
      flashingText.update(deltaTime);
    }

    // return to previous screen
    if (modelState == ModelState.GO_BACK) {
      // return back to previous screen
      game.screenReturn();
      return;
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
    // query upgrade price by requesting the full-game purchase product details asynchronously.
    // onProductDetailsRetrieved() will be called when product details are available.
    billingService.queryFullGameProductDetailsAsync(this);
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
        billingService.purchaseFullGame(productDetails);

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

  /**
   * Implementation of {@link ProductDetailsListener}
   * <p>
   * This method will be called-back with the details of any available product details that can be
   * purchased.
   *
   * @param productDetails - product details retrieved
   */
  @Override
  public void onProductDetailsRetrieved(ProductDetails productDetails) {
    if (productDetails != null) {
      this.productDetails = productDetails;
      this.reBuildSprites = true;
      Log.d(LOCAL_TAG, "Retrieved Product Details: " + productDetails);
    } else {
      Log.w(GameConstants.LOG_TAG, "Null Product Details received.");
    }
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
    this.reBuildSprites = true;
  }

  @Override
  public void onTextChange() {
    updateText = true;
  }
}
