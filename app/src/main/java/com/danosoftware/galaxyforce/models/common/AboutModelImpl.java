package com.danosoftware.galaxyforce.models.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
import com.danosoftware.galaxyforce.models.button.ButtonModel;
import com.danosoftware.galaxyforce.models.button.ButtonType;
import com.danosoftware.galaxyforce.models.touch_screen.TouchScreenModel;
import com.danosoftware.galaxyforce.screen.Screen;
import com.danosoftware.galaxyforce.screen.ScreenFactory;
import com.danosoftware.galaxyforce.screen.ScreenFactory.ScreenType;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.services.PackageManagers;
import com.danosoftware.galaxyforce.sprites.about.SocialButton;
import com.danosoftware.galaxyforce.sprites.game.interfaces.RotatingSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AboutModelImpl implements Model, TouchScreenModel, ButtonModel {

    /* logger tag */
    private static final String TAG = "AboutModelImpl";

    // references to stars
    private final List<Star> stars;

    // reference to all sprites in model
    private final List<ISprite> allSprites;

    // reference to all sprites in model to be rotated
    private final List<RotatingSprite> rotatedSprites;

    private ModelState modelState;

    // reference to all text objects in model
    private final List<Text> allText;

    /* reference to screen button */
    private final Context context;

    public AboutModelImpl(Controller controller, Context context) {
        this.context = context;
        this.allSprites = new ArrayList<>();
        this.rotatedSprites = new ArrayList<>();
        this.allText = new ArrayList<>();
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);
        this.modelState = ModelState.RUNNING;

        // add model sprites
        addSprites();

        // add version number
        addVersion();

        // add social media buttons
        addNewMenuButton(controller, 2, ButtonType.FACEBOOK, MenuSpriteIdentifier.FACEBOOK, MenuSpriteIdentifier.FACEBOOK_PRESSED);
        addNewMenuButton(controller, 1, ButtonType.TWITTER, MenuSpriteIdentifier.TWITTER, MenuSpriteIdentifier.TWITTER_PRESSED);
        addNewMenuButton(controller, 0, ButtonType.WEBSITE, MenuSpriteIdentifier.LEVEL_FRAME_LOCKED, MenuSpriteIdentifier.LEVEL_FRAME_LOCKED_PRESSED);

        // add button that covers the entire screen
        Button screenTouch = new ScreenTouch(this);
        controller.addTouchController(new DetectButtonTouch(screenTouch));
    }

    @Override
    public void initialise() {
    }

    private void addSprites() {

        allSprites.addAll(stars);

        for (int column = 0; column < 3; column++) {
            RotatingSprite base = new RotatingSprite(100 + (column * 170), 580, MenuSpriteIdentifier.BASE);
            rotatedSprites.add(base);
            allSprites.add(base);
        }

        allSprites.add(new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE));

        allText.add(Text.newTextAbsolutePosition("LIKE US", 290, 100 + (2 * 170)));
        allText.add(Text.newTextAbsolutePosition("FOLLOW US", 320, 100 + (1 * 170)));
        allText.add(Text.newTextAbsolutePosition("OUR WEBSITE", 350, 100));
    }

    private void addVersion() {

        // get package manager, name and then use them to get version number
        PackageManager packageMgr = PackageManagers.getPackageMgr();
        String packageName = PackageManagers.getPackageName();

        if (packageMgr != null && packageName != null) {
            try {
                PackageInfo info = packageMgr.getPackageInfo(packageName, 0);
                if (info != null) {
                    allText.add(Text.newTextRelativePositionX("VERSION " + info.versionName, TextPositionX.CENTRE, 175 + (3 * 170)));
                }

            } catch (NameNotFoundException e) {
                // no action - but version number won't be displayed
            }
        }
    }

    private void addNewMenuButton(
            Controller controller,
            int row,
            ButtonType buttonType,
            MenuSpriteIdentifier buttonUp,
            MenuSpriteIdentifier buttonDown) {

        SpriteButton button = new SocialButton(this, 100, 100 + (row * 170), buttonType, buttonUp, buttonDown);
        controller.addTouchController(new DetectButtonTouch(button));

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
        if (modelState == ModelState.GO_BACK) {
            Screen screen = ScreenFactory.newScreen(ScreenType.MAIN_MENU);
            Games.getGame().setScreen(screen);
        }

        // move stars
        for (Star eachStar : stars) {
            eachStar.animate(deltaTime);
        }

        // rotate sprites
        for (RotatingSprite sprite : rotatedSprites) {
            sprite.animate(deltaTime);
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

    @Override
    public void screenTouched() {
        this.modelState = ModelState.GO_BACK;
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
        this.modelState = ModelState.GO_BACK;
    }

    private void followUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(browser);
    }
}
