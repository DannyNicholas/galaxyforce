package com.danosoftware.galaxyforce.billing;

import com.android.billingclient.api.SkuDetails;

public interface BillingService {

    /**
     * Return the current state of full-game purchase.
     */
    PurchaseState getFullGamePurchaseState();

    /**
     * Asynchronously query the full game's Sku Details.
     * Call the supplied listener when the Sku Details are available.
     *
     * @param listener - listener to receive/process Sku Details
     */
    void queryFullGameSkuDetailsAsync(SkuDetailsListener listener);

    /**
     * Purchase the Full Game using the supplied Sku Details.
     * <p>
     * Implementations must check the Sku Details are valid and
     * represent a Full Game purchase.
     *
     * @param details - Sku Details for a full game purchase
     */
    void purchaseFullGame(SkuDetails details);

    /**
     * Register observer for any purchase state changes.
     */
    void registerPurchasesObserver(BillingObserver billingObserver);

    /**
     * Unregister observer for any purchase state changes.
     */
    void unregisterPurchasesObserver(BillingObserver billingObserver);
}
