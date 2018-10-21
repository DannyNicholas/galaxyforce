package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EnergyBar {
    /* logger tag */
    private static final String TAG = "EnergyBar";

    // energy block height
    private static final int ENERGY_HEIGHT = GameSpriteIdentifier.ENERGY_GREEN.getProperties().getHeight();

    // energy block width - add 4 pixels to allow 2 pixel gap at either end when
    // displayed
    private static final int ENERGY_WIDTH = GameSpriteIdentifier.ENERGY_GREEN.getProperties().getWidth() + 4;

    // start x,y position of flags
    private static final int ENERGY_START_X = 142 + (ENERGY_WIDTH / 2);
    private static final int ENERGY_START_Y = (ENERGY_HEIGHT / 2) + 10;

    // constants for each energy bar
    private static final ISpriteIdentifier CRITICAL = GameSpriteIdentifier.ENERGY_RED;
    private static final ISpriteIdentifier DANGER = GameSpriteIdentifier.ENERGY_ORANGE;
    private static final ISpriteIdentifier WARNING = GameSpriteIdentifier.ENERGY_YELLOW;
    private static final ISpriteIdentifier GOOD = GameSpriteIdentifier.ENERGY_GREEN;

    // constants for energy levels status
    private static final Integer CRITICAL_LEVEL = 1;
    private static final Integer DANGER_LEVEL = 2;
    private static final Integer WARNING_LEVEL = 4;
    private static final Integer GOOD_LEVEL = 6;

    private static final Integer MAX_LEVEL = 8;

    // variable to hold the current energy level
    private int energyLevel;

    // energy bar sprites
    private List<ISprite> energyBar;

    // static map of energy levels to status
    private static final Map<Integer, ISpriteIdentifier> energyMap = new TreeMap<>();

    static {
        energyMap.put(GOOD_LEVEL, GOOD);
        energyMap.put(WARNING_LEVEL, WARNING);
        energyMap.put(DANGER_LEVEL, DANGER);
        energyMap.put(CRITICAL_LEVEL, CRITICAL);
    }

    public EnergyBar() {
        resetEnergy();
    }

    /**
     * Reset the energy levels to maximum.
     */
    public void resetEnergy() {
        energyLevel = MAX_LEVEL;
        energyBar = buildEnergyBar();
    }

    /**
     * Decrease the energy levels by delta. Can't drop below zero.
     */
    public int decreaseEnergy(int delta) {
        energyLevel = energyLevel - delta;

        if (energyLevel < 0) {
            energyLevel = 0;
        }
        energyBar = buildEnergyBar();
        return energyLevel;
    }

    /**
     * Increase the energy levels by delta. Can't exceed maximum level.
     */
    public int increaseEnergy(int delta) {
        energyLevel = energyLevel + delta;

        if (energyLevel > MAX_LEVEL) {
            energyLevel = MAX_LEVEL;
        }
        energyBar = buildEnergyBar();
        return energyLevel;
    }

    /**
     * Returns the current energy bar.
     */
    public List<ISprite> getEnergyBar() {
        return energyBar;
    }

    /**
     * Creates a list of energy bars of the appropriate colour using the current
     * number of energy bars remaining.
     */
    private List<ISprite> buildEnergyBar() {
        List<ISprite> energyBarList = new ArrayList<>();

        // iterate through all energy levels in map and choose correct sprite
        ISpriteIdentifier energySprite = CRITICAL;
        for (Integer mapEnergyLevel : energyMap.keySet()) {
            if (this.energyLevel >= mapEnergyLevel) {
                energySprite = energyMap.get(mapEnergyLevel);
            }
        }

        int barXPosition = ENERGY_START_X;

        // add left of energy bar
        energyBarList.add(new Energy(barXPosition, ENERGY_START_Y, GameSpriteIdentifier.ENERGY_BAR_LEFT));
        barXPosition += ENERGY_WIDTH;

        // add the energy bar outline
        for (int i = 0; i < MAX_LEVEL - 2; i++) {
            energyBarList.add(new Energy(barXPosition, ENERGY_START_Y, GameSpriteIdentifier.ENERGY_BAR_MIDDLE));
            barXPosition += ENERGY_WIDTH;
        }

        // add right of energy bar
        energyBarList.add(new Energy(barXPosition, ENERGY_START_Y, GameSpriteIdentifier.ENERGY_BAR_RIGHT));

        int flagXPosition = ENERGY_START_X;

        // add the number of energy bars needed to the list
        for (int i = 0; i < energyLevel; i++) {
            energyBarList.add(new Energy(flagXPosition, ENERGY_START_Y, energySprite));
            flagXPosition += ENERGY_WIDTH;
        }

        return energyBarList;
    }
}
