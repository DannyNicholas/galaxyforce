package com.danosoftware.galaxyforce.sprites.game.behaviours;

import com.danosoftware.galaxyforce.sprites.game.interfaces.ExplodingSprite;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;

/**
 * Make sprite switch to an alternative sprite when hit for a short time
 * before returning to the original sprite.
 */
public class HitBehaviourSwitch implements HitBehaviour {

    private final static float FLASH_DELAY_INVERT = 0.15f;
    private final static float FLASH_DELAY_TOTAL = FLASH_DELAY_INVERT * 3f;

    private boolean hit;
    private boolean switched;
    private float timeSinceFirstHit;
    private final ISprite sprite;
    private final ISpriteIdentifier original;
    private final ISpriteIdentifier alternative;

    public HitBehaviourSwitch(
            ISprite sprite,
            ISpriteIdentifier original,
            ISpriteIdentifier alternative) {

        this.sprite = sprite;
        this.original = original;
        this.alternative = alternative;
        this.hit = false;
        this.switched = false;
        this.timeSinceFirstHit = 0f;
    }

    // switch to alternative
    @Override
    public void startHit() {
        hit = true;
        switched = true;
        timeSinceFirstHit = 0f;
        selectSprite();
    }

    @Override
    public boolean isHit() {
        return hit;
    }

    // after delay switch sprites
    @Override
    public void updateHit(float deltaTime) {
        timeSinceFirstHit = timeSinceFirstHit + deltaTime;

        // invert sprite after every delay
        if (timeSinceFirstHit > FLASH_DELAY_INVERT) {
            switched = (switched ? false : true);
            selectSprite();
        }

        // has hit time finished
        if (timeSinceFirstHit > FLASH_DELAY_TOTAL) {
            reset();
        }
    }

    @Override
    public void reset() {
        hit = false;
        this.switched = false;
        selectSprite();
    }

    private void selectSprite() {
        ISpriteIdentifier id = (switched ? alternative : original);
        sprite.changeType(id);
    }
}
