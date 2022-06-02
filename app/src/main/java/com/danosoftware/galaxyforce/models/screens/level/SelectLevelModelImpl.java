package com.danosoftware.galaxyforce.models.screens.level;

import static com.danosoftware.galaxyforce.constants.GameConstants.DEFAULT_BACKGROUND_COLOUR;

import android.util.Log;
import com.danosoftware.galaxyforce.billing.BillingObserver;
import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.billing.PurchaseState;
import com.danosoftware.galaxyforce.buttons.sprite_button.NextZone;
import com.danosoftware.galaxyforce.buttons.sprite_button.PreviousZone;
import com.danosoftware.galaxyforce.buttons.sprite_button.SpriteButton;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SelectLevel;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.models.swipe.SelectLevelSwipe;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.controllers.touch.SwipeTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.screens.ModelState;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.savedgame.HighestLevelChangeObserver;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.providers.ScrollableSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.SpriteProvider;
import com.danosoftware.galaxyforce.text.ScrollableTextProvider;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectLevelModelImpl implements LevelModel, SelectLevelModel, BillingObserver,
    HighestLevelChangeObserver {

  /* logger tag */
  private static final String LOCAL_TAG = "SelectLevelModelImpl";

  private final Game game;

  // map of zone number to x position
  private final Map<Integer, Integer> zoneXPosition;
  // on-screen components
  private final List<SpriteButton> buttons;
  private final List<SpriteTextButton> textButtons;
  private final List<Text> messages;
  private final ScrollableTextProvider textProvider;
  private final ScrollableSpriteProvider spriteProvider;

  /* reference to controller */
  private final Controller controller;
  // reference to the billing service
  private final BillingService billingService;
  // save game service
  private final SavedGame savedGame;
  // current screen x position
  private float xPosition;
  // target screen x position
  private float xTarget;
  // delta screen x position (used when processing swipe deltas)
  private float xOffset;
  // current zone
  private int zone;
  // max wave unlocked
  private volatile int maxWaveUnlocked;
  private ModelState modelState;
  /*
   * Should we rebuild the screen sprites?
   * Normally triggered by a change in state from a billing thread.
   */
  private volatile boolean reBuildAssets;

  public SelectLevelModelImpl(
      Game game,
      Controller controller,
      BillingService billingService,
      SavedGame savedGame) {
    this.game = game;
    this.controller = controller;
    this.billingService = billingService;
    this.savedGame = savedGame;
    this.modelState = ModelState.RUNNING;
    this.reBuildAssets = false;
    this.messages = new ArrayList<>();
    this.textProvider = new ScrollableTextProvider();
    this.spriteProvider = new ScrollableSpriteProvider();
    this.buttons = new ArrayList<>();
    this.textButtons = new ArrayList<>();

    /*
     * calculate zone from highest wave reached - must use double to avoid
     * integer division problems.
     */
    this.maxWaveUnlocked = savedGame.getGameLevel();
    this.zone = (int) Math.ceil((double) maxWaveUnlocked / GameConstants.WAVES_PER_ZONE);

    // create map of zone numbers to zone page x positions
    this.zoneXPosition = new HashMap<>();
    for (int zone = 0; zone < GameConstants.MAX_ZONES; zone++) {
      zoneXPosition.put(zone + 1, (zone + 1) * GameConstants.GAME_WIDTH);
    }

    // register this model with the billing service
    billingService.registerPurchasesObserver(this);

    // register for any changes in highest level unlocked
    savedGame.registerHighestLevelChangeObserver(this);

    // refresh sprites and controllers
    refreshAssets();
  }

  /**
   * Build all sprites and controllers required for screen. Normally called when screen is being
   * set-up or after any changes to upgrade buttons following a billing state change.
   */
  private void refreshAssets() {

    // clear any current touch controllers prior to adding buttons
    controller.clearTouchControllers();

    // add a swipe controller
    SelectLevelSwipe swipe = new SelectLevelSwipe(this);
    controller.addTouchController(new SwipeTouch(swipe));

    buttons.clear();
    textButtons.clear();
    messages.clear();

    /*
     * Calculate the maximum wave to show unlocked.
     *
     * If the full game is purchased or not purchased, show the highest wave reached.
     * The billing logic will force the user to upgrade if they attempt to play
     * beyond a free wave and have not purchased.
     *
     * However, if the billing state is not ready, only show the highest wave reached
     * up to (but not beyond) the max free waves. Without knowing the purchase state
     * we are unable to handle any attempts to play beyond the free waves.
     *
     * This state will be modified following any purchase updates that force the screen to
     * refresh.
     */
    final int maxWaveToShowUnlocked;
    if (billingService.getFullGamePurchaseState() == PurchaseState.NOT_READY) {
      maxWaveToShowUnlocked = Math.min(maxWaveUnlocked, GameConstants.MAX_FREE_WAVE);
    } else {
      maxWaveToShowUnlocked = maxWaveUnlocked;
    }

    // create a page for each zone
    for (int zone = 0; zone < GameConstants.MAX_ZONES; zone++) {
      createZonePage(
          zone + 1,
          maxWaveToShowUnlocked);
    }

    this.xPosition = zoneXPosition.get(zone);
    this.xTarget = zoneXPosition.get(zone);
    this.xOffset = 0;

    // update sprites to display
    spriteProvider.clear();
    for (SpriteButton button : buttons) {
      spriteProvider.add(button.getSprite());
    }
    for (SpriteTextButton button : textButtons) {
      spriteProvider.add(button.getSprite());
    }

    // update text to display
    textProvider.clear();
    for (SpriteTextButton button : textButtons) {
      textProvider.add(button.getText());
    }
    textProvider.addAll(messages);
  }

  /**
   * Create a page model for a zone.
   */
  private void createZonePage(int zone, int maxWaveToShowUnlocked) {

    // get page x position for current zone
    int xPosition = zoneXPosition.get(zone);

    // first wave number to appear on the zone page
    int waveStart = ((zone - 1) * GameConstants.WAVES_PER_ZONE) + 1;

    for (int i = 0; i < GameConstants.WAVES_PER_ZONE; i++) {
      // calculate row and column of next button
      int column = i % 3;
      int row = 3 - (i / 3);

      SelectLevel.LockStatus lockedStatus;
      if ((i + waveStart) <= maxWaveToShowUnlocked) {
        lockedStatus = SelectLevel.LockStatus.UNLOCKED;
      } else {
        lockedStatus = SelectLevel.LockStatus.LOCKED;
      }

      // create a new select wave button for current row and column
      SelectLevel selectWave = new SelectLevel(
          this,
          xPosition + 100 + (column * 170),
          100 + (row * 170),
          i + waveStart,
          lockedStatus);
      controller.addTouchController(new DetectButtonTouch(selectWave));

      // add new wave's button to list
      textButtons.add(selectWave);
    }

    // add previous zone to list of sprites
    if (zone > 1) {
      int column = 0;
      int row = 4;
      SpriteButton prevButton = new PreviousZone(
          this,
          xPosition + 100 + (column * 170),
          100 + (row * 170),
          zone,
          SpriteDetails.PREVIOUS_LEVEL,
          SpriteDetails.PREVIOUS_LEVEL_PRESSED);
      controller.addTouchController(new DetectButtonTouch(prevButton));
      buttons.add(prevButton);
    }

    // add next zone to list of sprites
    if (zone < GameConstants.MAX_ZONES) {
      int column = 2;
      int row = 4;
      SpriteButton nextButton = new NextZone(
          this,
          xPosition + 100 + (column * 170),
          100 + (row * 170),
          zone,
          SpriteDetails.NEXT_LEVEL,
          SpriteDetails.NEXT_LEVEL_PRESSED);
      controller.addTouchController(new DetectButtonTouch(nextButton));
      buttons.add(nextButton);
    }

    // add zone text
    int column = 1;
    int row = 4;
    messages.add(Text.newTextAbsolutePosition(
        "ZONE " + zone,
        xPosition + 100 + (column * 170),
        100 + (row * 170)));
  }

  @Override
  public TextProvider getTextProvider() {
    textProvider.updateScrollPosition(xPosition);
    return textProvider;
  }

  @Override
  public SpriteProvider getSpriteProvider() {
    spriteProvider.updateScrollPosition(xPosition);
    return spriteProvider;
  }

  @Override
  public void update(float deltaTime) {
    if (modelState == ModelState.GO_BACK) {
      game.changeToScreen(ScreenType.MAIN_MENU);
      modelState = ModelState.RUNNING;
    }

    // calculate screen scroll speed based on distance from target.
    float speed = (xTarget - xPosition) * 10;
    xPosition = xPosition + (speed * deltaTime);

    /*
     * refresh screen sprites. triggered following the billing state change.
     * may result in text and button changes following a successful purchase.
     */
    if (reBuildAssets) {
      refreshAssets();
      reBuildAssets = false;
    }
  }

  @Override
  public void dispose() {
    billingService.unregisterPurchasesObserver(this);
    savedGame.unregisterHighestLevelChangeObserver(this);
  }

  @Override
  public void setLevel(int level) {
    final PurchaseState fullGamePurchaseState = billingService.getFullGamePurchaseState();
    if (level > GameConstants.MAX_FREE_WAVE
        && (fullGamePurchaseState == PurchaseState.NOT_PURCHASED
        || fullGamePurchaseState == PurchaseState.PENDING)) {
      Log.i(LOCAL_TAG, "Exceeded maximum free wave. Must upgrade.");
      game.changeToReturningScreen(ScreenType.UPGRADE_FULL_VERSION);
    } else {
      Log.i(LOCAL_TAG, "Selected wave: " + level);
      game.changeToGameScreen(level);
    }
  }

  @Override
  public void changeZone(int newZone) {
    zone = newZone;
    // set x position target based on new zone
    xTarget = zoneXPosition.get(newZone);
  }

  @Override
  public float getScrollPosition() {
    return xPosition;
  }

  @Override
  public void swipeUpdate(float xDelta) {
    // update current screen offset based on new swipe delta
    xOffset = xOffset + xDelta;

    // set screen target based on updated delta
    xTarget = zoneXPosition.get(zone) + xOffset;
  }

  @Override
  public void swipeStart() {
    // reset screen offset when swiping
    xOffset = 0;
  }

  @Override
  public void swipeFinish(float xDelta) {
    // update current screen offset based on new swipe delta
    xOffset = xOffset + xDelta;

    // work out current x position based on offset
    int currentXPosition = zoneXPosition.get(zone) + (int) xOffset;

    // set-up default values for min distance and nearest zone
    int min_distance = Integer.MAX_VALUE;
    int nearest_zone = 1;

    // go through all zones and work out nearest zone to current position
    for (Integer zone : zoneXPosition.keySet()) {
      int distance = Math.abs(currentXPosition - zoneXPosition.get(zone));
      if (distance < min_distance) {
        min_distance = distance;
        nearest_zone = zone;
      }
    }

    // move to nearest zone
    changeZone(nearest_zone);
  }

  @Override
  public void goBack() {
    this.modelState = ModelState.GO_BACK;
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
    this.reBuildAssets = true;
  }

  // called if the highest unlocked level has changed
  @Override
  public void onHighestLevelUnlockedChange(int level) {
    Log.d(GameConstants.LOG_TAG, "Received updated highest level unlocked: " + level);
    this.maxWaveUnlocked = level;
    this.reBuildAssets = true;
  }
}
