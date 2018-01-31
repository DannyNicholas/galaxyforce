package com.danosoftware.galaxyforce.screen;

import javax.microedition.khronos.opengles.GL10;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.interfaces.LevelModel;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Sprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class SelectLevelScreen extends AbstractScreen
{
    /*
     * contains reference to level model needed for local override. This a
     * version of a model that has extra methods required for this screen.
     */
    private final LevelModel levelModel;

    public SelectLevelScreen(LevelModel model, Controller controller, TextureMap textureMap, GLGraphics glGraphics, Camera2D camera,
            SpriteBatcher batcher)
    {
        /* use superclass constructor to create screen */
        super(model, controller, textureMap, glGraphics, camera, batcher);

        levelModel = model;
    }

    /*
     * Overridden version that keeps stars in position as screen wipes from left
     * to right.
     */
    @Override
    public void draw(float deltaTime)
    {
        GL10 gl = glGraphics.getGl();

        /* clear colour buffer */
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // scroll screen by model's current scroll speed
        camera.position.x = (GameConstants.GAME_WIDTH / 2) + levelModel.getScrollPosition();

        camera.setViewportAndMatrices();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(texture);

        /*
         * gets static sprites from model (e.g. stars) - these sprites must not
         * scroll with other elements so offset stars by current camera offset.
         */
        float cameraOffset = levelModel.getScrollPosition();
        for (Sprite eachSprite : levelModel.getStaticSprites())
        {
            if (eachSprite.isVisible())
            {
                batcher.drawSprite(eachSprite.getX() + cameraOffset, eachSprite.getY(), eachSprite.getWidth(), eachSprite.getHeight(),
                        eachSprite.getTextureRegion());
            }
        }

        // gets sprites from model
        for (Sprite eachSprite : model.getSprites())
        {
            batcher.drawSprite(eachSprite.getX(), eachSprite.getY(), eachSprite.getWidth(), eachSprite.getHeight(),
                    eachSprite.getTextureRegion());
        }

        /*
         * gets static text from model - this text must not scroll with other
         * elements so offset stars by current camera offset.
         */
        for (Text eachText : levelModel.getStaticText())
        {
            gameFont.drawText(batcher, eachText.getText(), eachText.getX() + cameraOffset, eachText.getY(), eachText.getTextPositionX(),
                    eachText.getTextPositionY());
        }

        // draw any text
        for (Text eachText : model.getText())
        {
            gameFont.drawText(batcher, eachText.getText(), eachText.getX(), eachText.getY(), eachText.getTextPositionX(),
                    eachText.getTextPositionY());
        }

        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }
}
