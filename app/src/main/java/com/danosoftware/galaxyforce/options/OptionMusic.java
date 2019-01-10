package com.danosoftware.galaxyforce.options;

public enum OptionMusic implements Option {

    ON("ON"), OFF("OFF");

    private final String text;

    OptionMusic(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
