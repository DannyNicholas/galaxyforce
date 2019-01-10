package com.danosoftware.galaxyforce.utilities;

import com.danosoftware.galaxyforce.view.Vector2;

public class Rectangle {
    public final Vector2 lowerLeft;
    public final float width;
    public final float height;

    public Rectangle(float x, float y, float width, float height) {
        this.lowerLeft = new Vector2(x, y);
        this.width = width;
        this.height = height;
    }
}
