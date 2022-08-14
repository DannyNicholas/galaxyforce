package com.danosoftware.galaxyforce.billing;

import com.android.billingclient.api.Purchase;
import java.util.List;

/**
 * Listener to the updates that happen when purchases list is updated.
 */
public interface BillingUpdatesListener {

  void onPurchasesUpdated(List<Purchase> purchases);
}
