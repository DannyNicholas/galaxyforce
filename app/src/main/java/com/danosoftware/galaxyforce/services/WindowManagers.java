package com.danosoftware.galaxyforce.services;

import android.view.WindowManager;

public class WindowManagers {

    // private constructor
    private WindowManagers() {

    }

    // static reference to window manager
    private static WindowManager windowMgr = null;

    // return the implementation
    public static WindowManager getWindowMgr() {
        return windowMgr;
    }

    // static factory to reference new window manager instance
    public static void newWindowMgr(WindowManager aWindowMgr) {
        if (aWindowMgr == null) {
            throw new IllegalArgumentException("Supplied WindowManager object can not be null.");
        }

        windowMgr = aWindowMgr;
    }

}
