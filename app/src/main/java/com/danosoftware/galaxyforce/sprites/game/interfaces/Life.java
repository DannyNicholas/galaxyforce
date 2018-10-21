package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractSprite;

import java.util.ArrayList;
import java.util.List;

public class Life extends AbstractSprite
{
    // flags height and width
    private static final int LIVES_HEIGHT = GameSpriteIdentifier.LIVES.getProperties().getHeight();
    private static final int LIVES_WIDTH = GameSpriteIdentifier.LIVES.getProperties().getWidth();

    // start x,y position of lives
    private static final int LIVES_START_X = 0 + (LIVES_WIDTH / 2);
    private static final int LIVES_START_Y = (LIVES_HEIGHT / 2) + 10;

    public Life(int x, int y, ISpriteIdentifier spriteId)
    {
        super(spriteId, x, y);
    }

    /**
     * Creates a list of lives.
     */
    public static List<Life> getLives(int lives)
    {
        List<Life> livesList = new ArrayList<Life>();

        int flagXPosition = LIVES_START_X;

        // add the number of energy bars needed to the list
        for (int i = 0; i < lives; i++)
        {
            livesList.add(new Life(flagXPosition, LIVES_START_Y, GameSpriteIdentifier.LIVES));
            flagXPosition += LIVES_WIDTH;
        }

        return livesList;
    }
}
