package com.danosoftware.galaxyforce.billing.service.new_service;

import java.util.Arrays;
import java.util.List;

public class BillingConstants {

    // SKUs for our application: full game upgrade (non-consumable)
    public static final String SKU_FULL_GAME = "galaxy_force_full_game_unlock";
    private static final String[] IN_APP_SKUS = {SKU_FULL_GAME};

    private BillingConstants() {
    }

    /**
     * Returns the list of all SKUs for the application
     */
    public static List<String> getSkuList() {
        return Arrays.asList(IN_APP_SKUS);
    }
}
