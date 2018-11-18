package com.danosoftware.galaxyforce.textures;

public class TextureDetail {

    public final String name;
    public final int xPos;
    public final int yPos;
    public final int width;
    public final int height;

    public TextureDetail(String name, String xPos, String yPos, String width, String height) {
        this.name = name;
        this.xPos = convertNumeric(xPos);
        this.yPos = convertNumeric(yPos);
        this.width = convertNumeric(width);
        this.height = convertNumeric(height);
    }

    public TextureDetail(String name, int xPos, int yPos, int width, int height) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
    }

    // converts string to int
    // returns 0 if NumberFormatException is thrown
    private static int convertNumeric(String str) {
        int num = 0;

        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return 0;
        }

        return num;
    }

}
