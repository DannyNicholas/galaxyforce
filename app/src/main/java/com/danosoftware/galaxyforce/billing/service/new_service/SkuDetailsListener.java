package com.danosoftware.galaxyforce.billing.service.new_service;

import com.android.billingclient.api.SkuDetails;

public interface SkuDetailsListener {

    void onSkuDetailsRetrieved(SkuDetails skuDetails);
}
