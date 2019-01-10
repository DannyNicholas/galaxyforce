package com.danosoftware.galaxyforce.options;

public enum OptionSound implements Option {

    ON("ON"), OFF("OFF");

    private final String text;

    OptionSound(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
