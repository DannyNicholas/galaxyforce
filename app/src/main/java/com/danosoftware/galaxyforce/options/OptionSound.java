package com.danosoftware.galaxyforce.options;

public enum OptionSound implements Option
{

    ON("ON"), OFF("OFF");

    private String text = null;

    OptionSound(String text)
    {
        this.text = text;
    }

    @Override
    public String getText()
    {
        return text;
    }

}
