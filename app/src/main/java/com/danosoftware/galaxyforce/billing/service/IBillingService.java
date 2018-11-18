package com.danosoftware.galaxyforce.billing.service;

import android.content.Intent;

public interface IBillingService {
    /**
     * Register observer for any product state refreshes. Normally called when a
     * observer is constructed.
     *
     * @param billingObserver
     */
    void registerProductObserver(BillingObserver billingObserver);

    /**
     * Unregister observer for any product state refreshes. Normally called when
     * a observer is disposed.
     *
     * @param billingObserver
     */
    void unregisterProductObserver(BillingObserver billingObserver);

    /**
     * Refresh the product states of all known billing products. Querying
     * billing service is an asynchronous activity so refreshing may not happen
     * immediately.
     */
    void refreshProductStates();

    /**
     * Is supplied product Id purchased?
     * <p>
     * A negative response does not necessarily mean the product has not been
     * purchased as the purchase state may be unknown. This can only be
     * confirmed by calling isNotPurchased().
     *
     * @param productId
     */
    boolean isPurchased(String productId);

    /**
     * Is supplied product Id not purchased?
     * <p>
     * A negative response does not necessarily mean the product has been
     * purchased as the purchase state may be unknown. This can only be
     * confirmed by calling isPurchased().
     *
     * @param productId
     */
    boolean isNotPurchased(String productId);

    /**
     * Get the price of the product Id.
     * <p>
     * May return null if price is not yet known.
     *
     * @param productId
     * @return
     */
    String getPrice(String productId);

    /**
     * Purchase supplied product Id.
     *
     * @param productId
     */
    void purchase(String productId);

    /**
     * Process the results returned back to the billing service. Typically this
     * gets called with the results of a purchase request.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    boolean processActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * Destroy billing service. Must be called when activity is finished to
     * avoid degrading device.
     */
    void destroy();
}