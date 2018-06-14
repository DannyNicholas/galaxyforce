package com.danosoftware.galaxyforce.sprites.refactor;

import android.util.Log;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteState;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.textures.TextureRegion;
import com.danosoftware.galaxyforce.utilities.Reversed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flag extends AbstractSprite
{
    /* logger tag */
    private static final String TAG = "Flag";

    // flags height and width
    private static final int FLAGS_HEIGHT = GameSpriteIdentifier.FLAG_1.getProperties().getHeight();
    private static final int FLAGS_WIDTH = GameSpriteIdentifier.FLAG_1.getProperties().getWidth();

    // start x,y position of flags
    private static final int FLAGS_START_X = 0 + (FLAGS_WIDTH / 2);
    private static final int FLAGS_START_Y = GameConstants.GAME_HEIGHT - (FLAGS_HEIGHT / 2);

    // constants for each supported flag
    private static final ISpriteIdentifier FLAG_100 = GameSpriteIdentifier.FLAG_100;
    private static final ISpriteIdentifier FLAG_50 = GameSpriteIdentifier.FLAG_50;
    private static final ISpriteIdentifier FLAG_10 = GameSpriteIdentifier.FLAG_10;
    private static final ISpriteIdentifier FLAG_5 = GameSpriteIdentifier.FLAG_5;
    private static final ISpriteIdentifier FLAG_1 = GameSpriteIdentifier.FLAG_1;

    // static map of level values to level flags
    private static final Map<Integer, ISpriteIdentifier> flagMap = new HashMap<Integer, ISpriteIdentifier>();
    static
    {
        flagMap.put(100, Flag.FLAG_100);
        flagMap.put(50, Flag.FLAG_50);
        flagMap.put(10, Flag.FLAG_10);
        flagMap.put(5, Flag.FLAG_5);
        flagMap.put(1, Flag.FLAG_1);
    }

    public Flag(ISpriteIdentifier spriteId, int x, int y)
    {
        super(spriteId, x, y);
    }

    /**
     * Creates a map of used to illustrate the level using a set of flags. The
     * map key is the flag number and the map value contains the number of flags
     * needed of this type.
     * 
     * Flags used are 100, 50, 10, 5 and 1.
     * 
     * Example: Level 276 would be represented by 2 x 100 flags, 1 x 50 flag, 2
     * x 10 flags, 1 x 5 flag and 1 x 1 flag.
     */
    public static List<Flag> getFlagList(int levelNumber)
    {
        List<Flag> flags = new ArrayList<Flag>();

        int remainder = levelNumber;
        int flagXPosition = FLAGS_START_X;

        // get keys as sorted list so it can be iterated through in reverse
        // order
        List<Integer> flagKeysAsList = new ArrayList<Integer>(flagMap.keySet());
        Collections.sort(flagKeysAsList);

        // calculate how many flags of each flag type are needed
        for (int flag : Reversed.reversed(flagKeysAsList))
        {
            int numberOfFlags = (int) remainder / flag;
            remainder = remainder - (numberOfFlags * flag);

            Log.d(TAG, "Flag: " + flag + ". Number Of: " + numberOfFlags);

            ISpriteIdentifier flagSprite = flagMap.get(flag);

            // add the number of flags needed to the list
            for (int i = 0; i < numberOfFlags; i++)
            {
                flags.add(new Flag(flagSprite, flagXPosition, FLAGS_START_Y));
                flagXPosition += FLAGS_WIDTH;
            }
        }

        return flags;
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public void rotate(int angle) {

    }

    @Override
    public void changeType(ISpriteIdentifier type) {

    }

    @Override
    public void changeStatus(SpriteState state) {

    }

    @Override
    public int width() {
        return 0;
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public int rotation() {
        return 0;
    }

    @Override
    public int x() {
        return 0;
    }

    @Override
    public int y() {
        return 0;
    }

    @Override
    public TextureRegion textureRegion() {
        return null;
    }
}
