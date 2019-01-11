package com.danosoftware.galaxyforce.billing.service.new_service;

import com.danosoftware.galaxyforce.BuildConfig;

public class KeyUtilities {

    /**
     * Reverses the supplied string. Used to help hide the real in-app billing
     * public key.
     */
    private static String reverse(String original) {
        return new StringBuilder(original).reverse().toString();
    }

    /**
     * Computes the IAB public key.
     *
     * @return
     */
    public static String getPublicKey() {
        /*
         * in-app billing public key - reversed and broken in sections to hide real
         * key. Key parts also placed to random order to further hide real key.
         */
//        final String KEY_PART1 = activity.getResources().getText(R.string.public_key1).toString();
//        final String KEY_PART2 = activity.getResources().getText(R.string.public_key2).toString();
//        final String KEY_PART3 = activity.getResources().getText(R.string.public_key3).toString();
//        final String KEY_PART4 = activity.getResources().getText(R.string.public_key4).toString();

        final String KEY_PART1 = BuildConfig.public_key1;
        final String KEY_PART2 = BuildConfig.public_key2;
        final String KEY_PART3 = BuildConfig.public_key3;
        final String KEY_PART4 = BuildConfig.public_key4;

        // build reversed version of key
        String reversedKey = KEY_PART1 + KEY_PART2 + KEY_PART3 + KEY_PART4;

        // reverse key back to correct order
        return reverse(reversedKey);
    }
}
