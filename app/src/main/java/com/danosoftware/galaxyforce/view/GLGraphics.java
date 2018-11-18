package com.danosoftware.galaxyforce.view;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

public class GLGraphics {
    private GLSurfaceView glView = null;
    private GL10 gl = null;

    public GLGraphics(GLSurfaceView glView) {
        this.glView = glView;
    }

    public GL10 getGl() {
        return gl;
    }

    public void setGl(GL10 gl) {
        this.gl = gl;
    }

    public int getWidth() {
        return glView.getWidth();
    }

    public int getHeight() {
        return glView.getHeight();
    }

}
