package com.danosoftware.galaxyforce.text;

import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;

import java.util.regex.Pattern;

/* used to store any game text. contains a string of the actual text and the x, y position */
public class Text {

    /*
     * instance variables for text's x,y position - can be null if text position
     * enums are used
     */
    private final int x;
    private final int y;

    /* reference to X enum position (e.g. LEFT) */
    private final TextPositionX textPositionX;

    /* reference to Y enum position (e.g. BOTTOM) */
    private final TextPositionY textPositionY;

    /* reference to text String */
    private final String text;

    // static factory for text in absolute position
    public static Text newTextAbsolutePosition(String text, int x, int y) {
        return new Text(text, x, y, null, null);
    }

    // static factory for text using relative position enum
    public static Text newTextRelativePositionBoth(String text, TextPositionX textPositionX, TextPositionY textPositionY) {
        return new Text(text, 0, 0, textPositionX, textPositionY);
    }

    // static factory for text using relative position enum
    public static Text newTextRelativePositionX(String text, TextPositionX textPositionX, int y) {
        return new Text(text, 0, y, textPositionX, null);
    }

    // private constructor as static factories are used
    private Text(String text, int x, int y, TextPositionX textPositionX, TextPositionY textPositionY) {
        this.text = removeInvalidCharacters(text);
        this.x = x;
        this.y = y;
        this.textPositionX = textPositionX;
        this.textPositionY = textPositionY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }

    public TextPositionX getTextPositionX() {
        return textPositionX;
    }

    public TextPositionY getTextPositionY() {
        return textPositionY;
    }

    /**
     * Takes the original supplied text string and removes any invalid
     * characters. In this case, this will be any characters that don't exist in
     * the font character map.
     */
    private String removeInvalidCharacters(String text) {
        // string that contains only valid characters
        String validCharacters = GameConstants.FONT_CHARACTER_MAP;

        // string to hold invalid characters to be removed
        StringBuilder charsToRemove = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {

            // returns index of current character within the character map
            // returns -1 if it can't be found.
            char nextChar = text.charAt(i);
            int charIndex = validCharacters.indexOf(nextChar);

            /*
             * if character isn't valid then mark it for removal from text
             * string. Only add it to the list of characters to remove if it
             * hasn't been seen before.
             */
            if (charIndex == -1 && charsToRemove.indexOf(Character.toString(nextChar)) == -1) {
                Log.w(GameConstants.LOG_TAG, "Text: Invalid Character: '" + nextChar + "'. ASCII Code: '" + (int) nextChar
                        + "'. Removing from text.");

                charsToRemove.append(nextChar);
            }
        }

        /*
         * If invalid characters need to be removed, then create a regular
         * expression pattern to remove them from the original string.
         */
        if (charsToRemove.length() > 0) {
            Log.d(GameConstants.LOG_TAG, "Text: Removing Invalid Characters: '" + charsToRemove + "' from '" + text + "'.");

            // create regular expression to remove unwanted characters
            String pattern = "[" + Pattern.quote(charsToRemove.toString()) + "]";
            text = text.replaceAll(pattern, "");

            Log.d(GameConstants.LOG_TAG, "Text: Corrected text: '" + text + "'.");
        }

        return text;
    }
}
