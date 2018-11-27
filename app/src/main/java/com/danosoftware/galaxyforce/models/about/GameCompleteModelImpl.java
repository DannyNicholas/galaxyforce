package com.danosoftware.galaxyforce.models.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.danosoftware.galaxyforce.buttons.button.Button;
import com.danosoftware.galaxyforce.buttons.button.ScreenTouch;
import com.danosoftware.galaxyforce.buttons.sprite_button.SpriteButton;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.models.about.AboutModel;
import com.danosoftware.galaxyforce.models.screens.ButtonType;
import com.danosoftware.galaxyforce.screen.Screen;
import com.danosoftware.galaxyforce.models.touch_screen.TouchScreenModel;
import com.danosoftware.galaxyforce.screen.ScreenFactory;
import com.danosoftware.galaxyforce.screen.ScreenFactory.ScreenType;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.sprites.about.SocialButton;
import com.danosoftware.galaxyforce.sprites.game.interfaces.RotatingSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GameCompleteModelImpl implements TouchScreenModel, AboutModel {
    /* logger tag */
    private static final String TAG = "GameCompleteImpl";

    // references to stars
    private List<Star> stars;

    // reference to all sprites in model
    private final List<ISprite> allSprites;

    // reference to all sprites in model to be rotated
    private final List<RotatingSprite> rotatedSprites;

    private ModelState modelState;

    // reference to all text objects in model
    private final List<Text> allText;

    /* reference to controller */
    private final Controller controller;

    /* reference to screen button */
    private final Context context;

    public GameCompleteModelImpl(Controller controller, Context context) {
        this.controller = controller;
        this.context = context;
        this.allSprites = new ArrayList<>();
        this.rotatedSprites = new ArrayList<>();
        this.allText = new ArrayList<>();
    }

    @Override
    public void initialise() {

        /* set-up initial random position of stars */
        stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);
        allSprites.addAll(stars);

        for (int column = 0; column < 3; column++) {
            RotatingSprite base = new RotatingSprite(100 + (column * 170), 580, MenuSpriteIdentifier.BASE);
            rotatedSprites.add(base);
            allSprites.add(base);
        }

        allSprites.add(new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE));

        allText.add(Text.newTextRelativePositionX("GAME COMPLETED!", TextPositionX.CENTRE, 175 + (3 * 170)));

        allText.add(Text.newTextAbsolutePosition("LIKE US", 290, 100 + (2 * 170)));
        allText.add(Text.newTextAbsolutePosition("FOLLOW US", 320, 100 + (1 * 170)));
        allText.add(Text.newTextAbsolutePosition("OUR WEBSITE", 350, 100));

        addNewMenuButton(2, ButtonType.FACEBOOK, MenuSpriteIdentifier.FACEBOOK, MenuSpriteIdentifier.FACEBOOK_PRESSED);
        addNewMenuButton(1, ButtonType.TWITTER, MenuSpriteIdentifier.TWITTER, MenuSpriteIdentifier.TWITTER_PRESSED);
        addNewMenuButton(0, ButtonType.WEBSITE, MenuSpriteIdentifier.LEVEL_FRAME_LOCKED, MenuSpriteIdentifier.LEVEL_FRAME_LOCKED_PRESSED);

        // add button that covers the entire screen
        Button screenTouch = new ScreenTouch(this);
        controller.addTouchController(new DetectButtonTouch(screenTouch));
    }

    private void addNewMenuButton(int row, ButtonType buttonType, MenuSpriteIdentifier buttonUp, MenuSpriteIdentifier buttonDown) {
        SpriteButton button = new SocialButton(this, controller, 100, 100 + (row * 170), buttonType, buttonUp, buttonDown);

        // add new button's sprite to list of sprites
        allSprites.add(button.getSprite());
    }

    @Override
    public List<ISprite> getSprites() {
        return allSprites;
    }

    @Override
    public List<Text> getText() {
        return allText;
    }

    @Override
    public void update(float deltaTime) {
        if (getState() == ModelState.GO_BACK) {
            Screen screen = ScreenFactory.newScreen(ScreenType.MAIN_MENU);
            Games.getGame().setScreen(screen);
        }

        // move stars
        moveStars(deltaTime);

        // rotate sprites
        for (RotatingSprite eachSprite : rotatedSprites) {
            eachSprite.animate(deltaTime);
        }
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // no action for this model
    }

    @Override
    public void pause() {
        // no action for this model
    }

    private void moveStars(float deltaTime) {
        for (Star eachStar : stars) {
            eachStar.animate(deltaTime);
        }
    }

    @Override
    public void screenTouched() {
        setState(ModelState.GO_BACK);
    }

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {

            case FACEBOOK:
                followUrl("https://www.facebook.com/danosoftware");
                break;
            case TWITTER:
                followUrl("https://twitter.com/DanoSoftware");
                break;
            case WEBSITE:
                followUrl("http://danosoftware.com");
                break;
            default:
                Log.e(TAG, "Unsupported button: '" + buttonType + "'.");
                break;

        }

    }

    @Override
    public void goBack() {
        setState(ModelState.GO_BACK);
    }

    private void setState(ModelState modelState) {
        this.modelState = modelState;
    }

    private ModelState getState() {
        return modelState;
    }

    private void followUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(browser);
    }

}
