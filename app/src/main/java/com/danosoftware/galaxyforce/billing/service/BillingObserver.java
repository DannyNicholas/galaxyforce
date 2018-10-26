package com.danosoftware.galaxyforce.billing.service;

public interface BillingObserver
{

    /**
     * Notify a billing service observer that the states of billing service's
     * purchasable products have changed. The observer may need to check the
     * billing service for any product changes at the next opportunity.
     */
    void billingProductsStateChange();

}
