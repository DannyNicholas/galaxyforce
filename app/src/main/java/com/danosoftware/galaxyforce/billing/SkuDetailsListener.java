package com.danosoftware.galaxyforce.billing;

import com.android.billingclient.api.SkuDetails;

public interface SkuDetailsListener {

    void onSkuDetailsRetrieved(SkuDetails skuDetails);
}
