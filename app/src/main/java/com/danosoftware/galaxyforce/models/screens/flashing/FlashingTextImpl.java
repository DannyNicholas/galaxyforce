package com.danosoftware.galaxyforce.models.screens.flashing;

import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class FlashingTextImpl implements FlashingText {

    // reference to flashing text
    private final List<Text> flashingText;

    // time delay between flash on/off in seconds
    private final float flashDelay;

    // time since flash state last changed
    private float timeSinceflashStateChange;

    // current flash state - show text or hide text
    private boolean showText;

    public FlashingTextImpl(List<Text> flashingText, float flashDelay) {
        this.flashingText = flashingText;
        this.flashDelay = flashDelay;
        this.timeSinceflashStateChange = 0f;
        this.showText = true;
    }

    @Override
    public void update(float deltaTime) {

        timeSinceflashStateChange += deltaTime;

        /*
         * check to see if flash state should change.
         */
        if (timeSinceflashStateChange > flashDelay) {
            // invert current flash state
            showText = (!showText);

            // reset time since state change
            timeSinceflashStateChange = 0f;
        }

    }

    @Override
    public List<Text> text() {

        List<Text> text = new ArrayList<>();
        if (showText) {
            text.addAll(flashingText);
        }
        return text;
    }
}
