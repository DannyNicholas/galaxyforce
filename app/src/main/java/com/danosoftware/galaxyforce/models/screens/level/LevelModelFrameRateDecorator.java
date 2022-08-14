package com.danosoftware.galaxyforce.models.screens.level;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.sprites.providers.SpriteProvider;
import com.danosoftware.galaxyforce.text.ScrollableTextProvider;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextChangeListener;
import com.danosoftware.galaxyforce.text.TextProvider;
import com.danosoftware.galaxyforce.view.FPSCounter;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorator that adds frame-rate calculations and display functionality.
 */
public class LevelModelFrameRateDecorator implements LevelModel, TextChangeListener {

  private final static int HALF_GLYPH_WIDTH = 30 / 2;
  private final static int HALF_GLYPH_HEIGHT = 38 / 2;

  // decorated model
  private final LevelModel model;

  // FPS counter
  private final FPSCounter fpsCounter;

  // text provider that includes model text and FPS text
  private final ScrollableTextProvider textProvider;
  private List<Text> modelText;

  // has FPS text changed?
  private boolean updateText;

  public LevelModelFrameRateDecorator(LevelModel model) {
    this.model = model;
    this.fpsCounter = new FPSCounter(this);
    this.textProvider = new ScrollableTextProvider();
    this.modelText = new ArrayList<>();
    this.updateText = false;
  }

  private Text createFpsText() {
    // Use absolute x,y for text to allow camera offsets to work correctly
    String text = "FPS " + fpsCounter.getValue();
    return Text.newTextAbsolutePosition(
        text,
        (GameConstants.GAME_WIDTH + (GameConstants.GAME_WIDTH - text.length()) / 2)
            + HALF_GLYPH_WIDTH,
        GameConstants.GAME_HEIGHT - HALF_GLYPH_HEIGHT);
  }

  @Override
  public void update(float deltaTime) {
    model.update(deltaTime);

    // update fps
    fpsCounter.update();
  }

  @Override
  public TextProvider getTextProvider() {
    // update text if FPS or model text has changed
    TextProvider modelTextProvider = model.getTextProvider();
    List<Text> latestModelText = modelTextProvider.text();
    if (updateText || !modelText.equals(latestModelText)) {
      textProvider.clear();
      textProvider.addAll(modelTextProvider.text());
      textProvider.add(createFpsText());
      updateText = false;
      modelText = new ArrayList<>(latestModelText);
    }
    float scrollPosition = model.getScrollPosition();
    textProvider.updateScrollPosition(scrollPosition);
    return textProvider;
  }

  @Override
  public SpriteProvider getSpriteProvider() {
    return model.getSpriteProvider();
  }

  @Override
  public void dispose() {
    model.dispose();
  }

  @Override
  public void goBack() {
    model.goBack();
  }

  @Override
  public void pause() {
    model.pause();
  }

  @Override
  public void resume() {
    model.resume();
  }

  @Override
  public RgbColour background() {
    return model.background();
  }

  @Override
  public boolean animateStars() {
    return model.animateStars();
  }

  @Override
  public float getScrollPosition() {
    return model.getScrollPosition();
  }

  @Override
  public void onTextChange() {
    updateText = true;
  }
}
