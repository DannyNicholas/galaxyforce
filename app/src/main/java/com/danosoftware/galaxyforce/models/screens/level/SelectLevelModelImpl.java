package com.danosoftware.galaxyforce.models.screens.level;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.BillingObserver;
import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.buttons.sprite_button.NextZone;
import com.danosoftware.galaxyforce.buttons.sprite_button.PreviousZone;
import com.danosoftware.galaxyforce.buttons.sprite_button.SpriteButton;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SelectLevel;
import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.models.swipe.SelectLevelSwipe;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.controllers.touch.SwipeTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.models.screens.ModelState;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.SwipeMenuButton;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectLevelModelImpl implements LevelModel, SelectLevelModel, ButtonModel, BillingObserver {

    /* logger tag */
    private static final String LOCAL_TAG = "SelectLevelModelImpl";

    private final Game game;

    // map of zone number to x position
    private final Map<Integer, Integer> zoneXPosition;

    // current screen x position
    private float xPosition;

    // target screen x position
    private float xTarget;

    // delta screen x position (used when processing swipe deltas)
    private float xOffset;

    // current zone
    private int zone;

    // max level unlocked
    private final int maxLevelUnlocked;

    // on-screen components
    private final List<Star> stars;
    private final List<SpriteButton> buttons;
    private final List<SpriteTextButton> textButtons;
    private final List<SpriteTextButton> staticTextButtons;
    private final List<Text> messages;

    private ModelState modelState;

    /* reference to controller */
    private final Controller controller;

    // reference to the billing service
    private final IBillingService billingService;

    /*
     * Should we rebuild the screen sprites?
     * Normally triggered by a change in state from a billing thread.
     */
    private volatile boolean reBuildAssets;

    public SelectLevelModelImpl(
            Game game,
            Controller controller,
            IBillingService billingService,
            SavedGame savedGame) {
        this.game = game;
        this.controller = controller;
        this.billingService = billingService;
        this.modelState = ModelState.RUNNING;
        this.reBuildAssets = false;
        this.messages = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.textButtons = new ArrayList<>();
        this.staticTextButtons = new ArrayList<>();

        /*
         * calculate zone from highest level reached - must use double to avoid
         * integer division problems.
         */
        this.maxLevelUnlocked = savedGame.getGameLevel();
        this.zone = (int) Math.ceil((double) maxLevelUnlocked / GameConstants.WAVES_PER_ZONE);

        /* set-up initial random position of stars */
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);

        // create map of zone numbers to zone page x positions
        this.zoneXPosition = new HashMap<>();
        for (int zone = 0; zone < GameConstants.MAX_ZONES; zone++) {
            zoneXPosition.put(zone + 1, (zone + 1) * GameConstants.GAME_WIDTH);
        }

        // register this model with the billing service
        billingService.registerProductObserver(this);

        // refresh sprites and controllers
        refreshAssets();
    }

    /**
     * Build all sprites and controllers required for screen. Normally called when screen is
     * being set-up or after any changes to upgrade buttons following a billing
     * state change.
     */
    private void refreshAssets() {

        // clear any current touch controllers prior to adding buttons
        controller.clearTouchControllers();

        // add a swipe controller
        SelectLevelSwipe swipe = new SelectLevelSwipe(this);
        controller.addTouchController(new SwipeTouch(swipe));

        buttons.clear();
        textButtons.clear();
        staticTextButtons.clear();
        messages.clear();

        // create a page for each zone
        for (int zone = 0; zone < GameConstants.MAX_ZONES; zone++) {
            createZonePage(zone + 1);
        }

        this.xPosition = zoneXPosition.get(zone);
        this.xTarget = zoneXPosition.get(zone);
        this.xOffset = 0;

        /*
         * if the all levels unlocks have NOT been purchased then add the unlock
         * button
         */
        if (billingService.isPurchased(GameConstants.FULL_GAME_PRODUCT_ID)
                && billingService.isNotPurchased(GameConstants.ALL_LEVELS_PRODUCT_ID)) {
            // add unlock button
            addUnlockLevelsButton();
        }
    }

    /**
     * Create a page model for a zone.
     */
    private void createZonePage(int zone) {

        // get page x position for current zone
        int xPosition = zoneXPosition.get(zone);

        // first level number to appear on the page
        int levelStart = ((zone - 1) * GameConstants.WAVES_PER_ZONE) + 1;

        int column;
        int row;

        /*
         * if the all levels unlocks have been purchased then make all levels
         * available.
         */
        final int availableLevel;
        if (billingService.isPurchased(GameConstants.FULL_GAME_PRODUCT_ID)
                && billingService.isPurchased(GameConstants.ALL_LEVELS_PRODUCT_ID)) {
            availableLevel = GameConstants.MAX_WAVES;
        } else {
            availableLevel = maxLevelUnlocked;
        }

        for (int i = 0; i < GameConstants.WAVES_PER_ZONE; i++) {
            // calculate row and column of next button
            column = i % 3;
            row = 3 - (i / 3);

            // is level button unlocked?
            SelectLevel.LockStatus lockedStatus;
            if ((i + levelStart) <= availableLevel) {
                lockedStatus = SelectLevel.LockStatus.UNLOCKED;
            } else {
                lockedStatus = SelectLevel.LockStatus.LOCKED;
            }

            // create a new select level button for current row and column
            SelectLevel selectLevel = new SelectLevel(this, xPosition + 100 + (column * 170), 100 + (row * 170),
                    i + levelStart, lockedStatus);
            controller.addTouchController(new DetectButtonTouch(selectLevel));

            // add new level's button to list
            textButtons.add(selectLevel);
        }

        // add previous zone to list of sprites
        if (zone > 1) {
            column = 0;
            row = 4;
            SpriteButton prevButton = new PreviousZone(this, xPosition + 100 + (column * 170), 100 + (row * 170), zone,
                    MenuSpriteIdentifier.PREVIOUS_LEVEL, MenuSpriteIdentifier.PREVIOUS_LEVEL_PRESSED);
            controller.addTouchController(new DetectButtonTouch(prevButton));
            buttons.add(prevButton);
        }

        // add next zone to list of sprites
        if (zone < GameConstants.MAX_ZONES) {
            column = 2;
            row = 4;
            SpriteButton nextButton = new NextZone(this, xPosition + 100 + (column * 170), 100 + (row * 170), zone,
                    MenuSpriteIdentifier.NEXT_LEVEL, MenuSpriteIdentifier.NEXT_LEVEL_PRESSED);
            controller.addTouchController(new DetectButtonTouch(nextButton));
            buttons.add(nextButton);
        }

        // add zone text
        column = 1;
        row = 4;
        messages.add(Text.newTextAbsolutePosition("ZONE " + zone, xPosition + 100 + (column * 170), 100 + (row * 170)));
    }

    @Override
    public List<ISprite> getSprites() {
        List<ISprite> sprites = new ArrayList<>();
        for (SpriteButton button : buttons) {
            sprites.add(button.getSprite());
        }
        for (SpriteTextButton button : textButtons) {
            sprites.add(button.getSprite());
        }

        return sprites;
    }

    @Override
    public List<ISprite> getStaticSprites() {
        List<ISprite> sprites = new ArrayList<>();
        sprites.addAll(stars);
        for (SpriteTextButton button : staticTextButtons) {
            sprites.add(button.getSprite());
        }

        return sprites;
    }

    @Override
    public List<Text> getStaticText() {
        List<Text> text = new ArrayList<>();
        for (SpriteTextButton button : staticTextButtons) {
            text.add(button.getText());
        }

        return text;
    }

    @Override
    public List<Text> getText() {
        List<Text> text = new ArrayList<>();
        for (SpriteTextButton button : textButtons) {
            text.add(button.getText());
        }
        text.addAll(messages);

        return text;
    }

    @Override
    public void update(float deltaTime) {
        if (modelState == ModelState.GO_BACK) {
            game.changeToScreen(ScreenType.MAIN_MENU);
        }

        // calculate screen level scroll speed based on distance from target.
        float speed = (xTarget - xPosition) * 10;
        xPosition = xPosition + (speed * deltaTime);

        // move stars
        for (Star eachStar : stars) {
            eachStar.animate(deltaTime);
        }

        /*
         * refresh screen sprites. triggered following the billing state change.
         * may result in text and button changes following a successful purchase.
         */
        if (reBuildAssets) {
            refreshAssets();
            reBuildAssets = false;
        }
    }

    @Override
    public void dispose() {
        // unregister as observer of billing state changes
        billingService.unregisterProductObserver(this);
    }

    @Override
    public void setLevel(int level) {
        if (level > GameConstants.MAX_FREE_ZONE && billingService.isNotPurchased(GameConstants.FULL_GAME_PRODUCT_ID)) {
            Log.i(LOCAL_TAG, "Exceeded maximum free zone. Must upgrade.");
            game.changeToReturningScreen(ScreenType.UPGRADE_FULL_VERSION);
        } else {
            Log.i(LOCAL_TAG, "Selected Level: " + level);
            game.changeToGameScreen(level);
        }
    }

    @Override
    public void changeZone(int newZone) {
        zone = newZone;
        // set x position target based on new zone
        xTarget = zoneXPosition.get(newZone);
    }

    @Override
    public float getScrollPosition() {
        return xPosition;
    }

    @Override
    public void swipeUpdate(float xDelta) {
        // update current screen offset based on new swipe delta
        xOffset = xOffset + xDelta;

        // set screen target based on updated delta
        xTarget = zoneXPosition.get(zone) + xOffset;
    }

    @Override
    public void swipeStart() {
        // reset screen offset when swiping
        xOffset = 0;
    }

    @Override
    public void swipeFinish(float xDelta) {
        // update current screen offset based on new swipe delta
        xOffset = xOffset + xDelta;

        // work out current x position based on offset
        int currentXPosition = zoneXPosition.get(zone) + (int) xOffset;

        // set-up default values for min distance and nearest zone
        int min_distance = Integer.MAX_VALUE;
        int nearest_zone = 1;

        // go through all zones and work out nearest zone to current position
        for (Integer zone : zoneXPosition.keySet()) {
            int distance = Math.abs(currentXPosition - zoneXPosition.get(zone));
            if (distance < min_distance) {
                min_distance = distance;
                nearest_zone = zone;
            }
        }

        // move to nearest zone
        changeZone(nearest_zone);
    }

    @Override
    public void goBack() {
        this.modelState = ModelState.GO_BACK;
    }

    @Override
    public void resume() {
        // no implementation
    }

    @Override
    public void pause() {
        // no implementation
    }

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {
            case UNLOCK_ALL_LEVELS:
                Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Unlock All Levels.");
                game.changeToReturningScreen(ScreenType.UPGRADE_ALL_ZONES);
                break;
            default:
                Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Unsupported button type:'" + buttonType.name() + "'.");
                break;
        }
    }

    /**
     * add unlock button
     */
    private void addUnlockLevelsButton() {
        int rowY = 100 + (int) (4.7f * 170);

        SwipeMenuButton button = new SwipeMenuButton(
                this,
                this,
                GameConstants.GAME_WIDTH / 2,
                rowY,
                "UNLOCK ALL",
                ButtonType.UNLOCK_ALL_LEVELS,
                MenuSpriteIdentifier.MAIN_MENU,
                MenuSpriteIdentifier.MAIN_MENU_PRESSED);

        controller.addTouchController(new DetectButtonTouch(button));

        /*
         * add new button to list.
         * this button will not be shifted as user swipes.
         */
        staticTextButtons.add(button);
    }

    @Override
    public void billingProductsStateChange() {
        /*
         * model must check the billing service's products on next update.
         *
         * don't take any other action at this stage. this method will be called
         * by a billing thread. to keep implementation simple just take the
         * necessary action in the next update in the main thread.
         */
        Log.d(GameConstants.LOG_TAG, LOCAL_TAG + ": Received billing products state change message.");
        this.reBuildAssets = true;
    }
}
