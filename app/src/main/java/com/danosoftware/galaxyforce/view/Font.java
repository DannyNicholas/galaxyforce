package com.danosoftware.galaxyforce.view;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.enumerations.TextPositionY;
import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureRegion;

public class Font {
    public final Texture texture;
    public final int glyphWidth;
    public final int glyphHeight;
    public final TextureRegion[] glyphs = new TextureRegion[96];
    public final String charsInMap;

    /**
     * font constructor setting up fonts using the texture map
     *
     * @param texture      - texture map containing fonts
     * @param offsetX      - position of fonts in texture map
     * @param offsetY      - position of fonts in texture map
     * @param glyphsPerRow - font characters per row
     * @param glyphWidth   - width of a font character
     * @param glyphHeight  - height of a font character
     */
    public Font(Texture texture, int offsetX, int offsetY, int glyphsPerRow, int glyphWidth, int glyphHeight) {
        this.texture = texture;
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        this.charsInMap = null;

        int x = offsetX;
        int y = offsetY;
        for (int i = 0; i < 96; i++) {
            glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
            x += glyphWidth;
            if (x == offsetX + glyphsPerRow * glyphWidth) {
                x = offsetX;
                y += glyphHeight;
            }
        }
    }

    /**
     * font constructor setting up fonts using the texture map. alternative
     * constructor where list of characters in map is passed
     *
     * @param texture      - texture map containing fonts
     * @param offsetX      - position of fonts in texture map
     * @param offsetY      - position of fonts in texture map
     * @param glyphsPerRow - font characters per row
     * @param glyphWidth   - width of a font character
     * @param glyphHeight  - height of a font character
     * @param charsInMap   - string of valid characters
     */
    public Font(Texture texture, int offsetX, int offsetY, int glyphsPerRow, int glyphWidth, int glyphHeight, String charsInMap) {
        this.texture = texture;
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        this.charsInMap = charsInMap;

        int x = offsetX;
        int y = offsetY;
        for (int i = 0; i < charsInMap.length(); i++) {
            glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
            x += glyphWidth;
            if (x == offsetX + glyphsPerRow * glyphWidth) {
                x = offsetX;
                y += glyphHeight;
            }
        }
    }

    // drawText method that calculates x and y from Position enums (e.g. LEFT,
    // BOTTOM) if supplied. otherwise uses absolute x, y values.
    //
    public void drawText(SpriteBatcher batcher, String text, float x, float y, TextPositionX textPosX, TextPositionY textPosY) {

        // choose the correct drawText method depending on whether text
        // position enums have been set.
        //
        if (textPosX != null && textPosY != null) {
            drawText(batcher, text, calculateX(textPosX, text), calculateY(textPosY));
        } else if (textPosX != null) {
            drawText(batcher, text, calculateX(textPosX, text), y);
        } else if (textPosY != null) {
            drawText(batcher, text, x, calculateY(textPosY));
        } else {
            drawText(batcher, text, calculateCentreX(text, x), y);
        }
    }

    public void drawText(SpriteBatcher batcher, String text, float x, float y) {
        int len = text.length();
        for (int i = 0; i < len; i++) {

            int c;

            // if character map was supplied find current character's position
            // in map
            if (charsInMap != null) {
                // returns index of current character within the character map
                c = charsInMap.indexOf(text.charAt(i));

                if (c == -1) {
                    continue;
                }

            }
            // otherwise get index based on ASCII value
            else {
                c = text.charAt(i) - ' ';

                if (c < 0 || c > glyphs.length - 1) {
                    continue;
                }
            }

            TextureRegion glyph = glyphs[c];
            batcher.drawSprite(x, y, glyphWidth, glyphHeight, glyph);
            x += glyphWidth;
        }
    }

    // calculate x position using text width and screen width based on if text
    // required at LEFT, CENTRE or RIGHT of screen.
    //
    private float calculateX(TextPositionX posX, String text) {
        float x;

        // calculate total width of text
        int textLength = text.length() * glyphWidth;

        // text position must be offset by half a character glyph's width
        // x position represents the centre of the first character.
        float offset = (glyphWidth / 2);

        switch (posX) {
            case CENTRE:
                x = ((GameConstants.GAME_WIDTH - textLength) / 2) + offset;
                break;
            case LEFT:
                x = offset;
                break;
            case RIGHT:
                x = (GameConstants.GAME_WIDTH - textLength) + offset;
                break;
            default:
                x = 0;
                break;
        }

        return x;
    }

    // calculate y position using text height and screen height based on if text
    // required at TOP, CENTRE or BOTTOM of screen.
    //
    private float calculateY(TextPositionY posY) {
        float y;

        // text position must be offset by half a character glyph's height
        // y position represents the centre of the text.
        float offset = (glyphWidth / 2);

        switch (posY) {
            case TOP:
                y = GameConstants.GAME_HEIGHT - offset;
                break;
            case CENTRE:
                y = GameConstants.GAME_HEIGHT / 2;
                break;
            case BOTTOM:
                y = offset;
                break;
            default:
                y = 0;
                break;
        }

        return y;
    }

    // calculate new adjusted x position so that text is centred on the provided
    // x. this takes into account the number of characters and adjusts x so that
    // the whole text string is centred. An unadjusted x will only centre on the
    // first character.
    private float calculateCentreX(String text, float x) {

        // offsets text positions by half text width for number of text
        // characters minus one. so...
        // 1 character - no offset
        // 2 characters - offset by half width
        // 3 characters - offset by one width
        // ...etc...
        float offset = (glyphWidth / 2) * (text.length() - 1);

        return x - offset;
    }

}
