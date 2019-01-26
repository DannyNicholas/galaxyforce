package com.danosoftware.galaxyforce.billing;

public interface BillingObserver {

    /**
     * Notify a billing service observer that the state of the
     * full game purchasable product has refreshed (possibly
     * following a requery of products or a new purchase).
     *
     * @param state - latest state of full game purchase product
     */
    void onFullGamePurchaseStateChange(PurchaseState state);

}
