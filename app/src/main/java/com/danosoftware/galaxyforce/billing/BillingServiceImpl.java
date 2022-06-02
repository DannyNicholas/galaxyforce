package com.danosoftware.galaxyforce.billing;

import android.util.Log;
import com.android.billingclient.api.BillingClient.ProductType;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams.Product;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BillingServiceImpl implements BillingService, BillingUpdatesListener {

  private static final String TAG = "BillingService";

  // product ID for our application: full game upgrade (non-consumable)
  private static final String FULL_GAME_PRODUCT_ID = "galaxy_force_full_game_unlock";

  /*
   * set of observers to be notified following any purchase state changes.
   */
  private final Set<BillingObserver> observers;

  private volatile PurchaseState fullGamePurchaseState;
  private final BillingManager billingManager;

  public BillingServiceImpl(BillingManager billingManager) {
    this.billingManager = billingManager;
    this.fullGamePurchaseState = PurchaseState.NOT_READY;
    this.observers = new HashSet<>();

    // register this service to be called with any
    // purchase updates from the billing manager.
    billingManager.registerPurchasesListener(this);
  }

  /**
   * Query purchases already owned.
   * <p>
   * This method is normally called when the game is resuming (either on initial start-up or
   * re-starting after being in background).
   * <p>
   * On completion, will call-back with purchases owned to onPurchasesUpdated()
   */
  @Override
  public void queryPurchasesAsync() {
    billingManager.queryPurchasesAsync();
  }

  /**
   * Implementation of {@link BillingUpdatesListener}
   * <p>
   * Handle any updates to the list items of purchased by the user.
   * <p>
   * Called by BillingManager when purchases have changed. Could be triggered by a request to
   * re-query all purchases or if a new item has been purchased by a user.
   * <p>
   * Until this method is called, we have no idea what items have been purchased by the user.
   * <p>
   * The re-query process will be initiated when resuming the application (to also handle purchase
   * changes while the application was in the background).
   * <p>
   * This game will never consume any purchases but if consumption is needed, use:
   * billingManager.consumePurchaseAsync(purchase); ... after successful purchase.
   */
  @Override
  public void onPurchasesUpdated(List<Purchase> purchases) {
    // default to "not purchased" in case the expected full game purchase is not in supplied list
    this.fullGamePurchaseState = PurchaseState.NOT_PURCHASED;

    Log.i(TAG, "Purchases Received: " + purchases.size());

    for (Purchase purchase : purchases) {
      List<String> products = purchase.getProducts();
      if (products.contains(FULL_GAME_PRODUCT_ID)) {
        switch (purchase.getPurchaseState()) {
          case Purchase.PurchaseState.PURCHASED:
            Log.i(TAG, "Full Game Purchased: '" + products + "'");
            this.fullGamePurchaseState = PurchaseState.PURCHASED;
            break;
          case Purchase.PurchaseState.PENDING:
            Log.i(TAG, "Full Game Pending: '" + products + "'");
            this.fullGamePurchaseState = PurchaseState.PENDING;
            break;
          default:
            this.fullGamePurchaseState = PurchaseState.NOT_PURCHASED;
            break;
        }
      } else {
        String errorMsg = "Unknown Purchased Product: '" + purchase.getProducts() + "'";
        Log.e(TAG, errorMsg);
      }
    }

    /*
     * notify models of purchase state updates. this is called in a
     * billing thread so must be synchronized to avoid new observers
     * being added/removed by the main thread at same time.
     */
    synchronized (this) {
      for (BillingObserver observer : observers) {
        Log.i(TAG, "Sending Purchase State Change to " + observer);
        observer.onFullGamePurchaseStateChange(fullGamePurchaseState);
      }
    }
  }

  /**
   * Retrieve state of full game purchase.
   * <p>
   * Will only return a definite response once onPurchaseUpdated() has been called by the Billing
   * Manager.
   * <p>
   * Calling clients must handle NOT_READY state appropriately.
   */
  @Override
  public PurchaseState getFullGamePurchaseState() {
    Log.i(TAG, "Full Game Purchase State: " + fullGamePurchaseState.name());
    return fullGamePurchaseState;
  }

  /**
   * Asynchronously retrieve requested SKU details for the full-game purchase SKU.
   * <p>
   * If the response contains the expected SKU details, return these back to the supplied listener.
   *
   * @param listener - supplied listener to receive/process the SKU details
   */
  @Override
  public void queryFullGameProductDetailsAsync(
      final ProductDetailsListener listener) {

    // query asynchronously for product details of full game product.
    // Provides a listener to be called when query completes.
    billingManager.queryProductDetailsAsync(
        Collections.singletonList(
            Product.newBuilder()
                .setProductId(FULL_GAME_PRODUCT_ID)
                .setProductType(ProductType.INAPP)
                .build()
        ),
        (productDetails) -> {
          for (ProductDetails details : productDetails) {
            if (details.getProductId().equals(FULL_GAME_PRODUCT_ID)) {
              listener.onProductDetailsRetrieved(details);
            }
          }
        });
  }

  @Override
  public void purchaseFullGame(ProductDetails details) {

    final String productId = details.getProductId();
    final PurchaseState fullGamePurchaseState = getFullGamePurchaseState();

    if (!productId.equals(FULL_GAME_PRODUCT_ID)) {
      Log.e(TAG, "Unable to purchase Full Game. Incorrect ProductDetails supplied.");
    } else if (fullGamePurchaseState == PurchaseState.NOT_READY) {
      Log.e(TAG, "Unable to purchase Full Game. Previous purchases are not ready.");
    } else if (fullGamePurchaseState == PurchaseState.PURCHASED) {
      Log.e(TAG, "Unable to purchase Full Game. User has already purchased this.");
    } else if (fullGamePurchaseState == PurchaseState.PENDING) {
      Log.e(TAG, "Unable to purchase Full Game. An existing purchase is still pending.");
    } else {
      Log.i(TAG, "Requesting purchase of: '" + productId + "'");
      billingManager.initiatePurchaseFlowAsync(details);
    }
  }

  /*
   * Register an observer for any purchase state refreshes. Normally called
   * when a observer is constructed.
   *
   * Synchronized to avoid adding observer in main thread while notifying
   * observers in billing thread.
   */
  @Override
  public synchronized void registerPurchasesObserver(BillingObserver billingObserver) {
    Log.d(TAG, "Register Billing Observer '" + billingObserver + "'.");
    observers.add(billingObserver);
  }

  /*
   * Unregister an observer for any purchase state refreshes. Normally called
   * when a observer is disposed.
   *
   * Synchronized to avoid removing observer in main thread while notifying
   * observers in billing thread.
   */
  @Override
  public synchronized void unregisterPurchasesObserver(BillingObserver billingObserver) {
    Log.d(TAG, "Unregister Billing Observer '" + billingObserver + "'.");
    observers.remove(billingObserver);
  }

  @Override
  public void destroy() {
    billingManager.unregisterPurchasesListener(this);
    billingManager.destroy();
  }
}
