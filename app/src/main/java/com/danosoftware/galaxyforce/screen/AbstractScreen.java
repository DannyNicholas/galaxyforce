package com.danosoftware.galaxyforce.screen;

import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.services.file.FileIO;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteProperties;
import com.danosoftware.galaxyforce.text.Font;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.textures.Textures;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

import javax.microedition.khronos.opengles.GL10;

public abstract class AbstractScreen implements IScreen {

    /* logger tag */
    private static final String LOCAL_TAG = "Screen";

    /**
     * Reference to model and controller. Each screen will have different
     * implementations of models and controllers.
     * <p>
     * All screen implementations of this abstract class must construct their
     * models and controller after calling this abstract super constructor.
     * <p>
     * Some models/controllers require views to be created before they can be
     * constructed.
     */
    final Model model;
    private final Controller controller;

    // reference to openGL graphics
    final GLGraphics glGraphics;

    private final FileIO fileIO;

    // reference to graphics texture map - set on resume
    Texture texture;

    // sprite batcher used for displaying sprites
    final SpriteBatcher batcher;

    // camera used for display views
    final Camera2D camera;

    // font used for displaying text sprites
    Font gameFont;

    // TextureState identifies the texture map being used
    private final TextureMap textureMap;

    AbstractScreen(
            Model model,
            Controller controller,
            TextureMap textureMap,
            GLGraphics glGraphics,
            FileIO fileIO,
            Camera2D camera,
            SpriteBatcher batcher) {

        this.textureMap = textureMap;
        this.glGraphics = glGraphics;
        this.fileIO = fileIO;
        this.batcher = batcher;
        this.camera = camera;
        this.controller = controller;
        this.model = model;
    }

    @Override
    public void draw(float deltaTime) {
        GL10 gl = glGraphics.getGl();

        /* clear colour buffer */
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.setViewportAndMatrices();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(texture);

        // gets sprites from model
        for (ISprite sprite : model.getSprites()) {
            ISpriteIdentifier spriteId = sprite.spriteId();
            ISpriteProperties props = spriteId.getProperties();

            if (sprite.rotation() != 0) {
                // use sprite with rotation method
                batcher.drawSprite(
                        sprite.x(),
                        sprite.y(),
                        props.getWidth(),
                        props.getHeight(),
                        sprite.rotation(),
                        props.getTextureRegion());
            } else {
                // use normal sprite method
                batcher.drawSprite(
                        sprite.x(),
                        sprite.y(),
                        props.getWidth(),
                        props.getHeight(),
                        props.getTextureRegion());
            }
        }

        // draw any text
        for (Text text : model.getText()) {
            gameFont.drawText(
                    batcher,
                    text.getText(),
                    text.getX(),
                    text.getY(),
                    text.getTextPositionX(),
                    text.getTextPositionY());
        }

        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void update(float deltaTime) {
        controller.update(deltaTime);
        model.update(deltaTime);
    }

    @Override
    public void pause() {
        // pause model if whole game is paused (e.g. user presses home button)
        model.pause();

        /*
         * dispose of texture when screen paused. it will be reloaded next time
         * screen resumes
         */
        texture.dispose();
    }

    @Override
    public void resume() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Screen Resume.");

        /*
         * set-up texture map for screen. this will cause texture to be
         * re-loaded. re-loading must happen each time screen is resumed as
         * textures can be disposed by OpenGL when the game is paused.
         */
        this.texture = Textures.newTexture(glGraphics, fileIO, textureMap);

        /*
         * create each sprite's individual properties (e.g. width, height) from
         * the xml file and create texture regions for sprite display. must be
         * called after a new texture is re-loaded and before sprites can be
         * displayed.
         */
        for (ISpriteIdentifier sprite : textureMap.getSpriteIdentifiers()) {
            sprite.updateProperties(texture);
        }

        // set-up fonts - can be null if sprite map has no fonts
        ISpriteIdentifier fontId = textureMap.getFontIdentifier();

        if (fontId != null) {
            this.gameFont = new Font(
                    texture,
                    fontId.getProperties().getxPos(),
                    fontId.getProperties().getyPos(),
                    GameConstants.FONT_GLYPHS_PER_ROW,
                    GameConstants.FONT_GLYPHS_WIDTH,
                    GameConstants.FONT_GLYPHS_HEIGHT,
                    GameConstants.FONT_CHARACTER_MAP);
        }

        model.resume();
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
