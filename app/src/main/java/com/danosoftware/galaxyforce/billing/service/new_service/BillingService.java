package com.danosoftware.galaxyforce.billing.service.new_service;

import com.android.billingclient.api.SkuDetails;

public interface BillingService {

    PurchaseState getFullGamePurchaseState();

    void queryFullGameSkuDetailsAsync(SkuDetailsListener listener);

    void purchaseFullGame(SkuDetails details);
}
