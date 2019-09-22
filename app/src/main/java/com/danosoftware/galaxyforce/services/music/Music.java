package com.danosoftware.galaxyforce.services.music;

public enum Music {

    MAIN_TITLE("8-bit-mysterious-dungeon.m4a"),
    GAME_LOOP("upbeat-chiptune-loop.m4a");

    private final String fileName;

    Music(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
