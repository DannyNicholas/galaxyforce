package com.danosoftware.galaxyforce.utilities;

import com.danosoftware.galaxyforce.view.Vector2;

public class Circle {
    public final Vector2 center = new Vector2();
    public float radius;

    public Circle(float x, float y, float radius) {
        this.center.set(x, y);
        this.radius = radius;
    }
}
