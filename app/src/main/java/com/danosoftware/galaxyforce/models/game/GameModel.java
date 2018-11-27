package com.danosoftware.galaxyforce.models.game;

import com.danosoftware.galaxyforce.models.Model;

public interface GameModel extends Model {
    /**
     * Start a new game.
     */
    void play();

    /**
     * Ends a game. Supplies the wave we were on when game ended.
     */
    void gameOver(int wave);

    /**
     * Display game options.
     */
    void options();

    /**
     * Quit game.
     */
    void quit();

    /**
     * Resume game. This is not the same as the model resume method that gets
     * called by the parent screen. This is a game resume method that must only
     * get called after a game pause.
     */
    void resumeAfterPause();
}
