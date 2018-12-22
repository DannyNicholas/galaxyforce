package com.danosoftware.galaxyforce.models.common;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.danosoftware.galaxyforce.buttons.button.Button;
import com.danosoftware.galaxyforce.buttons.button.ScreenTouch;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.touch_screen.TouchScreenModel;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.services.PackageManagers;
import com.danosoftware.galaxyforce.sprites.game.interfaces.RotatingSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AboutModelImpl implements Model, TouchScreenModel {

    private final Game game;

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

    public AboutModelImpl(Game game, Controller controller) {
        this.game = game;
        this.allSprites = new ArrayList<>();
        this.rotatedSprites = new ArrayList<>();
        this.allText = new ArrayList<>();
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);
        this.modelState = ModelState.RUNNING;

        // add model sprites
        addSprites();

        // add version number
        addVersion();

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
            game.changeToScreen(ScreenType.MAIN_MENU);
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
    public void goBack() {
        this.modelState = ModelState.GO_BACK;
    }
}
