package com.danosoftware.galaxyforce.model.screens;

import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.BillingObserver;
import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.buttons.impl.NextZone;
import com.danosoftware.galaxyforce.buttons.impl.PreviousZone;
import com.danosoftware.galaxyforce.buttons.impl.SelectLevel;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.menus.SelectLevelSwipe;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.interfaces.LevelModel;
import com.danosoftware.galaxyforce.interfaces.Screen;
import com.danosoftware.galaxyforce.screen.ScreenFactory;
import com.danosoftware.galaxyforce.screen.ScreenFactory.ScreenType;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.services.SavedGame;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.mainmenu.SwipeMenuButton;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectLevelModelImpl implements LevelModel, SelectLevelModel, MenuButtonModel, BillingObserver
{
    /* logger tag */
    private static final String LOCAL_TAG = "SelectLevelModelImpl";

    private List<SelectLevel> levels = null;

    private SelectLevelSwipe swipe = null;

    // map of zone number to x position
    private Map<Integer, Integer> zoneXPosition = null;

    // current screen x position
    private float xPosition;

    // target screen x position
    private float xTarget;

    // delta screen x position (used when processing swipe deltas)
    private float xOffset;

    // current zone
    private int zone;

    // references to stars
    private List<Star> stars = null;

    // reference to all sprites in model
    private List<ISprite> allSprites = null;
    private List<ISprite> staticSprites = null;

    // reference to all button sprites in model
    // private final List<Sprite> buttons;

    private ModelState modelState;

    // reference to all text objects in model
    List<Text> allText = null;
    List<Text> staticText = null;

    /* reference to controller */
    private Controller controller = null;

    // reference to saved game singleton
    private final SavedGame savedGame;

    // reference to the billing service
    private final IBillingService billingService;

    /*
     * Should the model check the billing service for any changed products? The
     * model should check the billing service's products initially and then
     * following any notifications from the billing service.
     */
    private boolean checkBillingProducts;

    public SelectLevelModelImpl(Controller controller, IBillingService billingService)
    {
        this.levels = new ArrayList<>();
        this.controller = controller;
        this.billingService = billingService;

        // model must check the billing service's products on next update
        this.checkBillingProducts = true;

        // register this model with the billing service
        // billingService.registerProductObserver(this);

        this.allSprites = new ArrayList<>();
        this.staticSprites = new ArrayList<>();
        // this.buttons = new ArrayList<Sprite>();
        this.allText = new ArrayList<Text>();
        this.staticText = new ArrayList<Text>();
        this.zoneXPosition = new HashMap<Integer, Integer>();

        // create a new swipe controller - may not to keep a reference to this
        // after creation as swipe controller will call back model when needed
        // swipe = new SelectLevelSwipe(this, controller);

        // set-up levels
        // initialise();

        // this.xPosition = zoneXPosition.get(zone);
        // this.xTarget = zoneXPosition.get(zone);
        // this.xOffset = 0;

        this.savedGame = SavedGame.getInstance();

        /*
         * calculate zone from highest level reached - must use double to avoid
         * integer division problems.
         */
        int maxLevelUnlocked = savedGame.getGameLevel();
        this.zone = (int) Math.ceil((double) maxLevelUnlocked / GameConstants.WAVES_PER_ZONE);

        /* set-up initial random position of stars */
        stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);
    }

    @Override
    public void initialise()
    {

        // build screen sprites
        refreshSprites();

        /* set-up initial random position of stars */
        // stars = Star.setupStars(GameConstants.GAME_WIDTH,
        // GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);

        // staticSprites.addAll(stars);

        // create a page for each zone. each zone requires the zone, starting
        // level number and the x position for the page.

        // for (int zone = 0; zone < GameConstants.MAX_ZONES; zone++)
        // {
        // createZonePage(zone + 1, (zone * GameConstants.WAVES_PER_ZONE) + 1,
        // (zone + 1) * GameConstants.GAME_WIDTH);
        // }
        //
        // this.xPosition = zoneXPosition.get(zone);
        // this.xTarget = zoneXPosition.get(zone);
        // this.xOffset = 0;

        // addUnlockLevelsButton();
        // staticSprites.addAll(buttons);
    }

    /**
     * Build all sprites required for screen. Normally called when screen is
     * being set-up or after any changes to upgrade buttons following a billing
     * state change.
     */
    private void refreshSprites()
    {
        allSprites.clear();
        allText.clear();
        staticSprites.clear();
        staticText.clear();

        // clear any current touch controllers prior to adding buttons
        controller.clearTouchControllers();

        // create a new swipe controller - may not to keep a reference to this
        // after creation as swipe controller will call back model when needed
        swipe = new SelectLevelSwipe(this, controller);

        staticSprites.addAll(stars);

        // create a page for each zone. each zone requires the zone, starting
        // level number and the x position for the page.

        for (int zone = 0; zone < GameConstants.MAX_ZONES; zone++)
        {
            createZonePage(zone + 1, (zone * GameConstants.WAVES_PER_ZONE) + 1, (zone + 1) * GameConstants.GAME_WIDTH);
        }

        this.xPosition = zoneXPosition.get(zone);
        this.xTarget = zoneXPosition.get(zone);
        this.xOffset = 0;

        // addUnlockLevelsButton();
        // staticSprites.addAll(buttons);

        /*
         * if the all levels unlocks have NOT been purchased then add the unlock
         * button
         */
        if (billingService.isPurchased(GameConstants.FULL_GAME_PRODUCT_ID)
                && billingService.isNotPurchased(GameConstants.ALL_LEVELS_PRODUCT_ID))
        {
            // add unlock button
            addUnlockLevelsButton();
        }
    }

    /**
     * Create a page model for a zone.
     * 
     * @param levelStart
     *            - first level number to appear on the page
     * @param xPosition
     *            - x position of the page.
     */
    private void createZonePage(int zone, int levelStart, int xPosition)
    {
        // add current zone and related x position to map
        zoneXPosition.put(zone, xPosition);

        int column = 0;
        int row = 0;

        int maxLevelUnlocked = savedGame.getGameLevel();

        /*
         * if the all levels unlocks have been purchased then make all levels
         * available.
         */
        if (billingService.isPurchased(GameConstants.FULL_GAME_PRODUCT_ID)
                && billingService.isPurchased(GameConstants.ALL_LEVELS_PRODUCT_ID))
        {
            maxLevelUnlocked = GameConstants.MAX_WAVES;
        }

        for (int i = 0; i < GameConstants.WAVES_PER_ZONE; i++)
        {
            // calculate row and column of next button
            column = i % 3;
            row = 3 - (i / 3);

            // is level button unlocked?
            SelectLevel.LockStatus lockedStatus = null;
            if ((i + levelStart) <= maxLevelUnlocked)
            {
                lockedStatus = SelectLevel.LockStatus.UNLOCKED;
            }
            else
            {
                lockedStatus = SelectLevel.LockStatus.LOCKED;
            }

            // create a new select level model for current row and column
            SelectLevel selectLevel = new SelectLevel(this, controller, xPosition + 100 + (column * 170), 100 + (row * 170),
                    i + levelStart, lockedStatus);

            // add new level to list of levels
            levels.add(selectLevel);

            // add new level's sprite to list of sprites
            allSprites.add(selectLevel.getSprite());

            // add new level's text to list of text objects
            allText.add(selectLevel.getText());
        }

        // add previous zone to list of sprites
        if (zone > 1)
        {
            column = 0;
            row = 4;
            allSprites.add(new PreviousZone(this, controller, xPosition + 100 + (column * 170), 100 + (row * 170), zone,
                    MenuSpriteIdentifier.PREVIOUS_LEVEL, MenuSpriteIdentifier.PREVIOUS_LEVEL_PRESSED).getSprite());
        }

        // add next zone to list of sprites
        if (zone < GameConstants.MAX_ZONES)
        {
            column = 2;
            row = 4;
            allSprites.add(new NextZone(this, controller, xPosition + 100 + (column * 170), 100 + (row * 170), zone,
                    MenuSpriteIdentifier.NEXT_LEVEL, MenuSpriteIdentifier.NEXT_LEVEL_PRESSED).getSprite());
        }

        // add zone text
        column = 1;
        row = 4;
        allText.add(Text.newTextAbsolutePosition("ZONE " + zone, xPosition + 100 + (column * 170), 100 + (row * 170)));
    }

    @Override
    public List<ISprite> getSprites()
    {
        return allSprites;
    }

    @Override
    public List<ISprite> getStaticSprites()
    {
        return staticSprites;
    }

    @Override
    public List<Text> getStaticText()
    {
        return staticText;
    }

    @Override
    public List<Text> getText()
    {
        return allText;
    }

    @Override
    public void update(float deltaTime)
    {
        if (getState() == ModelState.GO_BACK)
        {
            Screen screen = ScreenFactory.newScreen(ScreenType.MAIN_MENU);
            Games.getGame().setScreen(screen);
        }

        // calculate screen level scroll speed based on distance from target.
        float speed = (xTarget - xPosition) * 10;
        xPosition = xPosition + (speed * deltaTime);

        // move stars
        moveStars(deltaTime);

        // do we need to check billing service for product states
        if (checkBillingProducts)
        {
            // firstly set to false so we don't repeatedly check this
            checkBillingProducts = false;

            /*
             * refresh screen sprites following the billing state change. the
             * billing buttons displayed are likely to change.
             */
            refreshSprites();
        }
    }

    @Override
    public void dispose()
    {
        // unregister as observer of billing state changes
        // billingService.unregisterProductObserver(this);
    }

    @Override
    public void setLevel(int level)
    {
        if (level > GameConstants.MAX_FREE_ZONE && billingService.isNotPurchased(GameConstants.FULL_GAME_PRODUCT_ID))
        {
            Log.i(LOCAL_TAG, "Exceeded maximum free zone. Must upgrade.");
            Screen unlockFullVersionScreen = ScreenFactory.newScreen(ScreenType.UPGRADE_FULL_VERSION);
            Games.getGame().setReturningScreen(unlockFullVersionScreen);
        }
        else
        {
            Log.i(LOCAL_TAG, "Selected Level: " + level);
            Screen screen = ScreenFactory.newGameScreen(level);
            Games.getGame().setScreen(screen);
        }
    }

    @Override
    public void changeZone(int newZone)
    {
        zone = newZone;
        // set x position target based on new zone
        xTarget = zoneXPosition.get(newZone);
    }

    @Override
    public float getScrollPosition()
    {
        return xPosition;
    }

    @Override
    public void swipeUpdate(float xDelta)
    {
        // update current screen offset based on new swipe delta
        xOffset = xOffset + xDelta;

        // set screen target based on updated delta
        xTarget = zoneXPosition.get(zone) + xOffset;
    }

    @Override
    public void swipeStart()
    {
        // reset screen offset when swiping
        xOffset = 0;
    }

    @Override
    public void swipeFinish(float xDelta)
    {
        // update current screen offset based on new swipe delta
        xOffset = xOffset + xDelta;

        // work out current x position based on offset
        int currentXPosition = zoneXPosition.get(zone) + (int) xOffset;

        // set-up default values for min distance and nearest zone
        int min_distance = Integer.MAX_VALUE;
        int nearest_zone = 1;

        // go through all zones and work out nearest zone to current position
        for (Integer zone : zoneXPosition.keySet())
        {
            int distance = Math.abs(currentXPosition - zoneXPosition.get(zone));
            if (distance < min_distance)
            {
                min_distance = distance;
                nearest_zone = zone;
            }
        }

        // move to nearest zone
        changeZone(nearest_zone);
    }

    @Override
    public void goBack()
    {
        setState(ModelState.GO_BACK);
    }

    @Override
    public void resume()
    {
        // register this model with the billing service
        billingService.registerProductObserver(this);

        // force a check of the billing service's products on next update
        this.checkBillingProducts = true;
    }

    @Override
    public void pause()
    {
        // unregister as observer of billing state changes
        billingService.unregisterProductObserver(this);
    }

    @Override
    public void processButton(ButtonType buttonType)
    {
        switch (buttonType)
        {
        case UNLOCK_ALL_LEVELS:
            Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Unlock All Levels.");
            Screen unlockAllZonesScreen = ScreenFactory.newScreen(ScreenType.UPGRADE_ALL_ZONES);
            // Games.getGame().setScreen(unlockAllZonesScreen);
            Games.getGame().setReturningScreen(unlockAllZonesScreen);
            break;
        default:
            Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Unsupported button type:'" + buttonType.name() + "'.");
            break;
        }

    }

    /**
     * add unlock button
     */
    private void addUnlockLevelsButton()
    {
        int rowY = 100 + (int) (4.7f * 170);

        SwipeMenuButton button = new SwipeMenuButton(this, this, controller, GameConstants.GAME_WIDTH / 2, rowY, "UNLOCK ALL",
                ButtonType.UNLOCK_ALL_LEVELS, MenuSpriteIdentifier.MAIN_MENU, MenuSpriteIdentifier.MAIN_MENU_PRESSED);

        /*
         * add new button's sprite to list of sprites. we don't add this button
         * to all sprites as they will be shifted as user swipes.
         */
        staticSprites.add(button.getSprite());

        // add new button's text to list of text objects
        staticText.add(button.getText());
    }

    private void moveStars(float deltaTime)
    {
        for (Star eachStar : stars)
        {
            eachStar.animate(deltaTime);
        }
    }

    private void setState(ModelState modelState)
    {
        this.modelState = modelState;
    }

    private ModelState getState()
    {
        return modelState;
    }

    @Override
    public void billingProductsStateChange()
    {
        /*
         * model must check the billing service's products on next update.
         * 
         * don't take any other action at this stage. this method will be called
         * by a billing thread. to keep implementation simple just take the
         * necessary action in the next update in the main thread.
         */
        Log.d(GameConstants.LOG_TAG, LOCAL_TAG + ": Received billing products state change message.");
        this.checkBillingProducts = true;
    }
}
