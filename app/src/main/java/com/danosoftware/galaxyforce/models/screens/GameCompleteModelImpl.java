package com.danosoftware.galaxyforce.models.screens;

import static com.danosoftware.galaxyforce.constants.GameConstants.DEFAULT_BACKGROUND_COLOUR;

import com.danosoftware.galaxyforce.buttons.button.Button;
import com.danosoftware.galaxyforce.buttons.button.ScreenTouch;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.TouchScreenModel;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.common.RotatingSprite;
import com.danosoftware.galaxyforce.sprites.game.splash.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.starfield.StarField;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;

import java.util.ArrayList;
import java.util.List;

public class GameCompleteModelImpl implements Model, TouchScreenModel {

  /* logger tag */
  private static final String TAG = "GameCompleteImpl";

  private final Game game;

  // stars
  private final StarField starField;

  // reference to all sprites in model
  private final List<ISprite> allSprites;

  // reference to all sprites in model to be rotated
  private final List<RotatingSprite> rotatedSprites;
  // reference to all text objects in model
  private final List<Text> allText;
  private ModelState modelState;

  public GameCompleteModelImpl(
      Game game,
      Controller controller,
      StarField starField) {
    this.game = game;
    this.starField = starField;
    this.allSprites = new ArrayList<>();
    this.rotatedSprites = new ArrayList<>();
    this.allText = new ArrayList<>();
    this.modelState = ModelState.RUNNING;

    // add model sprites
    addSprites();

    // add button that covers the entire screen
    Button screenTouch = new ScreenTouch(this);
    controller.addTouchController(new DetectButtonTouch(screenTouch));
  }

  private void addSprites() {
    for (int column = 0; column < 3; column++) {
      RotatingSprite base = new RotatingSprite(100 + (column * 170), 580,
          MenuSpriteIdentifier.BASE);
      rotatedSprites.add(base);
      allSprites.add(base);
    }

    allSprites
        .add(new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE));

    allText.add(
        Text.newTextRelativePositionX("GAME COMPLETED!", TextPositionX.CENTRE, 175 + (3 * 170)));
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
    if (modelState == ModelState.GO_BACK) {
      game.changeToScreen(ScreenType.MAIN_MENU);
    }

    // move stars
    starField.animate(deltaTime);

    // rotate sprites
    for (RotatingSprite eachSprite : rotatedSprites) {
      eachSprite.animate(deltaTime);
    }
  }

  @Override
  public void dispose() {
    // no action for this model
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
  public void pause() {
    // no action for this model
  }

  @Override
  public void screenTouched() {
    this.modelState = ModelState.GO_BACK;
  }

  @Override
  public void goBack() {
    this.modelState = ModelState.GO_BACK;
  }
}
