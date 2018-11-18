package com.danosoftware.galaxyforce.services;

import android.content.pm.PackageManager;
import android.util.Log;

public class PackageManagers {

    /* logger tag */
    private static final String TAG = "PackageManagers";

    // private constructor
    private PackageManagers() {

    }

    // static reference to package manager
    private static PackageManager packageMgr = null;

    // static reference to package name
    private static String packageName = null;

    // return the manager
    public static PackageManager getPackageMgr() {
        return packageMgr;
    }

    // return the name
    public static String getPackageName() {
        return packageName;
    }

    // static factory to reference new package manager instance
    public static void newPackageMgr(PackageManager aPackageMgr, String aPackageName) {
        if (aPackageMgr == null) {
            Log.e(TAG, "Supplied PackageManager object was null.");
            throw new IllegalArgumentException("Supplied PackageManager object can not be null.");
        }

        if (aPackageName == null) {
            Log.e(TAG, "Supplied PackageName object was null.");
            throw new IllegalArgumentException("Supplied PackageName object can not be null.");
        }

        packageMgr = aPackageMgr;
        packageName = aPackageName;
    }

}
