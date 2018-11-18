package com.danosoftware.galaxyforce;

import android.app.Application;
import android.content.Context;

/**
 * Main application allows context to be instantiated when the application
 * is created. This context is then available statically anywhere across
 * the application.
 */
public class MainApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MainApplication.context;
    }
}
