package com.danosoftware.galaxyforce.options;

public enum OptionGooglePlay implements Option {

    ON("ON"), OFF("OFF");

    private final String text;

    OptionGooglePlay(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
