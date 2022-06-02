package com.danosoftware.galaxyforce.billing;

import com.android.billingclient.api.ProductDetails;

public interface BillingService {

  /**
   * Query any purchases already owned.
   */
  void queryPurchasesAsync();

  /**
   * Return the current state of full-game purchase.
   */
  PurchaseState getFullGamePurchaseState();

  /**
   * Asynchronously query the full game's Product Details. Call the supplied listener when the
   * Product Details are available.
   *
   * @param listener - listener to receive/process Product Details
   */
  void queryFullGameProductDetailsAsync(ProductDetailsListener listener);

  /**
   * Purchase the Full Game using the supplied Product Details.
   * <p>
   * Implementations must check the Product Details are valid and represent a Full Game purchase.
   *
   * @param details - Product Details for a full game purchase
   */
  void purchaseFullGame(ProductDetails details);

  /**
   * Register observer for any purchase state changes.
   */
  void registerPurchasesObserver(BillingObserver billingObserver);

  /**
   * Unregister observer for any purchase state changes.
   */
  void unregisterPurchasesObserver(BillingObserver billingObserver);

  /**
   * Destroy billing service on application close.
   */
  void destroy();
}
