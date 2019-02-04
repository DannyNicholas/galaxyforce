package com.danosoftware.galaxyforce.services.music;

public enum Music {

    MAIN_TITLE("music.mp3");

    private final String fileName;

    Music(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
