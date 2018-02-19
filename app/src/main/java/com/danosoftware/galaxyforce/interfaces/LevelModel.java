package com.danosoftware.galaxyforce.interfaces;

import com.danosoftware.galaxyforce.sprites.game.interfaces.Sprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.List;

public interface LevelModel extends Model
{

    /**
     * Returns position of screen scroll. Moves when changing zones.
     * 
     * @return speed of screen scroll
     */
    public float getScrollPosition();

    /**
     * Some sprites (e.g. starfield) will be rendered separately to level
     * buttons and will not be effected by scrolling so should be provided in a
     * separate method.
     * 
     * @return list of static sprites
     */
    public List<Sprite> getStaticSprites();

    /**
     * Some text will be rendered separately to level button text and will not
     * be effected by scrolling so should be provided in a separate method.
     * 
     * @return list of static text
     */
    public List<Text> getStaticText();

}
