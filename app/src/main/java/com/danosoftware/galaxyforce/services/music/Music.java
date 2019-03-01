package com.danosoftware.galaxyforce.services.music;

public enum Music {

    MAIN_TITLE("chiptuneVengeance.mp3"),
    GAME_LOOP("upbeat-chiptune-loop.m4a");

    private final String fileName;

    Music(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
