package com.danosoftware.galaxyforce.models.screens.game;

import static com.danosoftware.galaxyforce.constants.GameConstants.DEFAULT_BACKGROUND_COLOUR;

import android.util.Log;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.models.screens.flashing.FlashingText;
import com.danosoftware.galaxyforce.models.screens.flashing.FlashingTextImpl;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.providers.BasicMenuSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.MenuSpriteProvider;
import com.danosoftware.galaxyforce.sprites.providers.SpriteProvider;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextChangeListener;
import com.danosoftware.galaxyforce.text.TextPositionX;
import com.danosoftware.galaxyforce.text.TextProvider;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameOverModelImpl implements Model, ButtonModel, TextChangeListener {

  /*
   * ******************************************************
   * PRIVATE STATIC VARIABLES
   * ******************************************************
   */

  /* logger tag */
  private static final String TAG = "GameOverModelImpl";
  private final Game game;

  /*
   * ******************************************************
   * PRIVATE INSTANCE VARIABLES
   * ******************************************************
   */
  /* reference to pause menu buttons */
  private final List<SpriteTextButton> menuButtons;
  private final TextProvider textProvider;
  private final MenuSpriteProvider spriteProvider;
  /* reference to flashing game over text */
  private final FlashingText flashingGameOverText;
  private boolean updateText;
  private boolean updateSprites;
  private final int lastWave;
  /* reference to current state */
  private GameOverState modelState;

  public GameOverModelImpl(
      Game game,
      Controller controller,
      int lastWave) {
    this.game = game;
    this.lastWave = lastWave;
    this.menuButtons = new ArrayList<>();
    this.modelState = GameOverState.RUNNING;
    this.textProvider = new TextProvider();
    this.spriteProvider = new BasicMenuSpriteProvider();

    // build menu buttons
    addNewMenuButton(controller, 3, "PLAY", ButtonType.PLAY);
    addNewMenuButton(controller, 2, "OPTIONS", ButtonType.OPTIONS);
    addNewMenuButton(controller, 1, "EXIT", ButtonType.EXIT);

    // add flashing game over text
    Text gameOver = Text.newTextRelativePositionX(
        "GAME OVER",
        TextPositionX.CENTRE,
        100 + (4 * 170));
    this.flashingGameOverText = new FlashingTextImpl(
        Collections.singletonList(gameOver),
        0.5f,
        this);
    this.updateText = true;
    this.updateSprites = true;
  }

  /*
   * ******************************************************
   *
   * PUBLIC CONSTRUCTOR
   *
   * ******************************************************
   */

  /*
   * ******************************************************
   * PUBLIC INTERFACE METHODS
   * ******************************************************
   */

  @Override
  public TextProvider getTextProvider() {
    if (updateText) {
      textProvider.clear();
      for (SpriteTextButton eachButton : menuButtons) {
        textProvider.add(eachButton.getText());
      }
      textProvider.addAll(flashingGameOverText.text());
      updateText = false;
    }
    return textProvider;
  }

  @Override
  public SpriteProvider getSpriteProvider() {
    if (updateSprites) {
      spriteProvider.clear();
      for (SpriteTextButton eachButton : menuButtons) {
        spriteProvider.add(eachButton.getSprite());
      }
      updateSprites = false;
    }
    return spriteProvider;
  }

  @Override
  public void update(float deltaTime) {
    switch (modelState) {

      case RUNNING:
        // normal state before any buttons are pressed
        break;

      case EXIT:
        // exit game. go to select level screen
        game.changeToScreen(ScreenType.SELECT_LEVEL);
        this.modelState = GameOverState.RUNNING;
        break;

      case NEW_GAME:
        // after game over, restart on last level reached (if valid)
        final int nextWave;
        if (!WaveUtilities.isValidWave(lastWave)) {
          nextWave = 1;
        } else {
          nextWave = lastWave;
        }
        game.changeToGameScreen(nextWave);
        this.modelState = GameOverState.RUNNING;
        break;

      case OPTIONS:
        // go to options screen - will return back when done
        game.changeToReturningScreen(ScreenType.OPTIONS);
        this.modelState = GameOverState.RUNNING;
        break;

      default:
        Log.e(TAG, "Illegal Model State: " + modelState.name());
        throw new GalaxyForceException("Illegal Model State: " + modelState.name());
    }

    flashingGameOverText.update(deltaTime);
  }

  @Override
  public void dispose() {
    // no action
  }

  @Override
  public void processButton(ButtonType buttonType) {
    switch (buttonType) {

      case EXIT:
        Log.i(TAG, "'Main Menu' selected.");
        this.modelState = GameOverState.EXIT;
        break;
      case PLAY:
        Log.i(TAG, "'Play' selected.");
        this.modelState = GameOverState.NEW_GAME;
        break;
      case OPTIONS:
        Log.i(TAG, "'Options' selected.");
        this.modelState = GameOverState.OPTIONS;
        break;

      default:
        Log.e(TAG, "Illegal Button Type: " + buttonType.name());
        throw new GalaxyForceException("Illegal Button Type: " + buttonType.name());
    }
  }

  @Override
  public void goBack() {
    Log.i(TAG, "'Back Button' selected.");
    this.modelState = GameOverState.EXIT;
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

  private void addNewMenuButton(Controller controller, int row, String label,
      ButtonType buttonType) {
    MenuButton button = new MenuButton(
        this,
        GameConstants.GAME_WIDTH / 2,
        100 + (row * 170),
        label,
        buttonType,
        SpriteDetails.MENU_BUTTON_UP,
        SpriteDetails.MENU_BUTTON_DOWN);

    // add a new menu button to controller's list of touch controllers
    controller.addTouchController(new DetectButtonTouch(button));

    // add new button to list
    menuButtons.add(button);

    // trigger update
    updateText = true;
    updateSprites = true;
  }

  @Override
  public void onTextChange() {
    updateText = true;
  }

  /*
   * ******************************************************
   * PRIVATE HELPER METHODS
   * ******************************************************
   */

  private enum GameOverState {
    RUNNING, NEW_GAME, OPTIONS, EXIT
  }
}
