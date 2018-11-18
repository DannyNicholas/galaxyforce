package com.danosoftware.galaxyforce.sprites.game.implementations;

import com.danosoftware.galaxyforce.game.handlers.PlayModel;
import com.danosoftware.galaxyforce.sprites.game.interfaces.FlashingText;
import com.danosoftware.galaxyforce.text.Text;

public class FlashingTextImpl implements FlashingText {
    /* reference to flashing text */
    private final Text flashingText;

    /* time delay between flash on/off in seconds */
    private final float flashDelay;

    /* reference to model. */
    private final PlayModel model;

    /* time since flash state last changed */
    private float timeSinceflashStateChange = 0f;

    /* current flash state - show text or hide text. */
    private boolean flashState;

    public FlashingTextImpl(Text flashingText, float flashDelay, PlayModel model) {
        this.flashingText = flashingText;
        this.flashDelay = flashDelay;
        this.model = model;
        this.flashState = true;

        // turn on flashing text
        model.flashText(flashingText, flashState);
    }

    @Override
    public void update(float deltaTime) {

        timeSinceflashStateChange += deltaTime;

        /*
         * check to see if flash state should change.
         */
        if (timeSinceflashStateChange > flashDelay) {
            // invert current flash state
            flashState = (!flashState);

            // reset time since state change
            timeSinceflashStateChange = 0f;

            // update model
            model.flashText(flashingText, flashState);
        }

    }
}
