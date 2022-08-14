package com.danosoftware.galaxyforce.screen;

import static com.danosoftware.galaxyforce.constants.GameConstants.BACKGROUND_ALPHA;

import android.opengl.GLES20;
import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.starfield.StarField;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.providers.SpriteProvider;
import com.danosoftware.galaxyforce.tasks.OnTaskCompleteListener;
import com.danosoftware.galaxyforce.tasks.TaskCallback;
import com.danosoftware.galaxyforce.tasks.TaskService;
import com.danosoftware.galaxyforce.text.Font;
import com.danosoftware.galaxyforce.text.TextProvider;
import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.textures.TextureRegion;
import com.danosoftware.galaxyforce.textures.TextureService;
import com.danosoftware.galaxyforce.textures.TextureWithFont;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLShaderHelper;
import com.danosoftware.galaxyforce.view.SpriteBatcher;
import com.danosoftware.galaxyforce.view.StarBatcher;

public abstract class AbstractScreen implements IScreen, OnTaskCompleteListener<TextureWithFont> {

  /* logger tag */
  private static final String LOCAL_TAG = "Screen";

  private enum ScreenState {
    PREPARING, PREPARED, RUNNING
  }

  /**
   * Reference to model and controller. Each screen will have different implementations of models
   * and controllers.
   * <p>
   * All screen implementations of this abstract class must construct their models and controller
   * after calling this abstract super constructor.
   * <p>
   * Some models/controllers require views to be created before they can be constructed.
   */
  final Model model;
  // sprite batcher used for displaying sprites
  final SpriteBatcher batcher;
  final StarBatcher starBatcher;

  private final StarField starField;
  // camera used for display views
  final Camera2D camera;
  private final Controller controller;
  private final TextureService textureService;
  // TextureState identifies the texture map being used
  private final TextureMap textureMap;
  // reference to graphics texture map - set on resume
  Texture texture;
  // font used for displaying text sprites
  Font gameFont;
  private final TaskService taskService;
  private ScreenState screenState;
  private final boolean animateStars;

  AbstractScreen(
      Model model,
      Controller controller,
      TextureService textureService,
      TextureMap textureMap,
      Camera2D camera,
      SpriteBatcher batcher,
      StarBatcher starBatcher,
      TaskService taskService,
      StarField starField) {

    this.textureService = textureService;
    this.textureMap = textureMap;
    this.batcher = batcher;
    this.camera = camera;
    this.controller = controller;
    this.model = model;
    this.starBatcher = starBatcher;
    this.taskService = taskService;
    this.screenState = ScreenState.PREPARING;
    this.starField = starField;
    this.animateStars = model.animateStars();
  }

  @Override
  public void draw() {

    // clear screen
    final RgbColour backgroundColour = model.background();
    GLES20.glClearColor(
        backgroundColour.getRed(),
        backgroundColour.getGreen(),
        backgroundColour.getBlue(),
        BACKGROUND_ALPHA);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

    // Enable alpha blending and blend based on the fragment's alpha value
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    drawStars();
    drawSprites();

    // Turn alpha blending off
    GLES20.glDisable(GLES20.GL_BLEND);
  }

  /**
   * Draw Starfield
   */
  void drawStars() {
    GLShaderHelper.setPointShaderProgram();
    camera.setViewportAndMatrices();
    starBatcher.drawStars();
  }

  /**
   * Draw sprites and sprite text
   */
  void drawSprites() {

    if (texture == null) {
      return;
    }

    // count sprites to draw
    final SpriteProvider spriteProvider = model.getSpriteProvider();
    final TextProvider textProvider = model.getTextProvider();
    final int spriteCount = spriteProvider.count() + textProvider.count();

    // Use our sprite shader program for GL
    GLShaderHelper.setSpriteShaderProgram();

    camera.setViewportAndMatrices();

    batcher.beginBatch(texture, spriteCount);

    // gets sprites from model
    for (ISprite sprite : spriteProvider) {
      SpriteDetails spriteDetails = sprite.spriteDetails();
      TextureRegion textureRegion = spriteDetails.getTextureRegion();

      if (textureRegion != null) {
        if (sprite.rotation() != 0) {
          // use sprite with rotation method
          batcher.drawSprite(
              sprite.x(),
              sprite.y(),
              sprite.width(),
              sprite.height(),
              sprite.rotation(),
              textureRegion);
        } else {
          // use normal sprite method
          batcher.drawSprite(
              sprite.x(),
              sprite.y(),
              sprite.width(),
              sprite.height(),
              textureRegion);
        }
      }
    }

    // draw text
    if (gameFont != null) {
      drawText(batcher, textProvider);
    }

    batcher.endBatch();
  }

  // draw text to the screen
  void drawText(SpriteBatcher batcher, TextProvider textProvider) {

    if (textProvider.count() == 0) {
      return;
    }

    // draw all cached characters
    gameFont.drawText(batcher, textProvider.characters());
  }

  @Override
  public void update(float deltaTime) {

    // move stars
    if (animateStars) {
      starField.animate(deltaTime);
    }

    switch (screenState) {
      case PREPARING:
        // no action - waiting for screen to be ready
        break;
      case PREPARED:
        model.resume();
        controller.update(0f);
        model.update(0f);
        screenState = ScreenState.RUNNING;
        break;
      case RUNNING:
        controller.update(deltaTime);
        model.update(deltaTime);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + screenState);
    }
  }

  @Override
  public void pause() {
    // pause model if whole game is paused (e.g. user presses home button)
    model.pause();
  }

  @Override
  public void resume() {
    Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Screen Resume.");

    /*
     * set-up texture map for screen. this will cause texture to be
     * re-loaded. re-loading must happen each time screen is resumed as
     * textures can be disposed by OpenGL when the game is paused.
     *
     * loading textures will also set-up the sprite's texture regions
     * (used to draw sprite from texture map) and dimensions
     * (e.g width and height).
     */
    screenState = ScreenState.PREPARING;
    createTextureAndFontAsync(textureMap);
  }

  // create texture and font from wanted texture map asynchronously.
  // callback when ready.
  private void createTextureAndFontAsync(TextureMap newTextureMap) {
    TaskCallback<TextureWithFont> callback = new TaskCallback<>(
        () -> {
          final Texture texture = textureService.getOrCreateTexture(newTextureMap);
          final Font font = textureService.getOrCreateFont(textureMap);
          return new TextureWithFont(texture, font);
        },
        this);
    taskService.execute(callback);
  }

  // callback for texture and font once created.
  @Override
  public void onCompletion(TextureWithFont textureWithFont) {
    this.texture = textureWithFont.getTexture();
    this.gameFont = textureWithFont.getFont();
    TextProvider textProvider = model.getTextProvider();
    textProvider.updateFont(gameFont);
    screenState = ScreenState.PREPARED;
  }

  @Override
  public void dispose() {
    model.dispose();
  }

  @Override
  public boolean handleBackButton() {
    model.goBack();
    return true;
  }
}
