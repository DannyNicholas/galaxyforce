package com.danosoftware.galaxyforce.billing;

import static com.danosoftware.galaxyforce.billing.KeyUtilities.getPublicKey;

import android.app.Activity;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.ProductType;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryProductDetailsParams.Product;
import com.android.billingclient.api.QueryPurchasesParams;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all the interactions with Play Store (via Billing library), maintains connection to it
 * through BillingClient and caches temporary states/data if needed
 */
public class BillingManagerImpl implements BillingManager, PurchasesUpdatedListener {

  private static final String TAG = "BillingManager";

  /*
   * BASE_64_ENCODED_PUBLIC_KEY is the APPLICATION'S PUBLIC KEY
   * (from the Google Play developer console). This is not a
   * developer public key, it's the app-specific public key.
   */
  private static final String BASE_64_ENCODED_PUBLIC_KEY = getPublicKey();

  private enum BillingClientState {
    PENDING, CONNECTED, DISCONNECTED, CLOSED
  }

  private final List<BillingUpdatesListener> billingUpdateListeners;
  private final Activity mActivity;

  // organise purchases by order id.
  // in the case of a pending purchase that upgrades to a full purchase we only want the latest purchase.
  // we use a map to replace any previous purchases with the same order id.
  private final Map<String, Purchase> mPurchases = new HashMap<>();

  private final BillingClient mBillingClient;
  private BillingClientState clientState;
  private final BillingClientQueue taskQueue;

  public BillingManagerImpl(
      final Activity activity) {
    this.mActivity = activity;
    this.billingUpdateListeners = new ArrayList<>();
    this.taskQueue = new BillingClientQueue();

    Log.d(TAG, "Creating Billing client.");
    this.mBillingClient = BillingClient
        .newBuilder(mActivity)
        .setListener(this)
        .enablePendingPurchases()
        .build();
    clientSetUpAsync();
  }

  /**
   * Start connection to client and handle call-backs following connection or disconnection.
   */
  private void clientSetUpAsync() {
    Log.d(TAG, "Connecting to Billing Client.");
    this.clientState = BillingClientState.PENDING;
    mBillingClient.startConnection(new BillingClientStateListener() {
      @Override
      public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
            && mBillingClient.isReady()) {
          clientState = BillingClientState.CONNECTED;
          Log.d(TAG, "Billing Client connected.");
          taskQueue.releaseTasks();
        } else {
          Log.d(TAG,
              "Billing Client connection failed with code: " + billingResult.getResponseCode());
          clientState = BillingClientState.DISCONNECTED;
          taskQueue.delayTasks();
        }
      }

      @Override
      public void onBillingServiceDisconnected() {
        clientState = BillingClientState.DISCONNECTED;
        Log.d(TAG, "Billing Service Disconnected.");
        taskQueue.delayTasks();
      }
    });
  }

  private void executeWhenClientReady(Runnable task) {
    switch (clientState) {
      case CONNECTED:
        // client is connected, execute task immediately.
        task.run();
        break;
      case PENDING:
        // client is still connecting, delay task until connected.
        taskQueue.delay(task);
        break;
      case DISCONNECTED:
        // client is disconnected, add task to queue and attempt client connection.
        // task will be run once client is connected.
        Log.d(TAG, "Billing Client is unavailable. Will re-attempt connection...");
        taskQueue.delay(task);
        clientSetUpAsync();
        break;
      default:
        Log.e(TAG, "Unexpected client state: " + clientState);
        break;
    }
  }

  @Override
  public void queryProductDetailsAsync(
      final List<Product> products,
      final ProductsQueriedListener listener) {

    executeWhenClientReady(() -> {
      Log.d(TAG, "Querying product details for products: " + products);

      mBillingClient.queryProductDetailsAsync(
          QueryProductDetailsParams
              .newBuilder()
              .setProductList(products)
              .build(),
          (billingResult, productDetails) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
              listener.onProductsQueried(productDetails);
            } else {
              Log.w(TAG,
                  "Unsuccessful Product Details query. Error code: "
                      + billingResult.getResponseCode());
            }
          });
    });
  }

  /**
   * Start a new purchase for the supplied product.
   * <p>
   * Once completed, will call-back to onPurchasesUpdated()
   *
   * @param productDetails - wanted product
   */
  @Override
  public void initiatePurchaseFlowAsync(final ProductDetails productDetails) {

    executeWhenClientReady(() -> {
      Log.d(TAG, "Launching in-app purchase flow for: " + productDetails.getProductId());

      mBillingClient.launchBillingFlow(
          mActivity,
          BillingFlowParams
              .newBuilder()
              .setProductDetailsParamsList(
                  Collections.singletonList(
                      ProductDetailsParams
                          .newBuilder()
                          .setProductDetails(productDetails)
                          .build()))
              .build());
    });
  }

  /**
   * Implementation of {@link PurchasesUpdatedListener}
   * <p>
   * On a successful purchase (triggered by launchBillingFlow()), the billing library will call-back
   * to this method.
   * <p>
   * Verify the purchases and then notify the purchase listeners.
   */
  @Override
  public void onPurchasesUpdated(
      @NonNull BillingResult billingResult,
      @Nullable List<Purchase> purchases) {

    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
        && purchases != null) {

      // verify and acknowledge purchases
      for (Purchase purchase : purchases) {
        handlePurchase(purchase);
      }

      // notify listeners of updated purchases
      notifyPurchaseListeners();

    } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
      Log.i(TAG, "Purchase cancelled by user");
    } else {
      Log.w(TAG, "Purchase failed with error code: " + billingResult.getResponseCode());
    }
  }

  /**
   * Query purchases asynchronously and then notify purchase listeners.
   */
  @Override
  public void queryPurchasesAsync() {

    executeWhenClientReady(() -> {
      Log.d(TAG, "Querying existing purchases");

      mBillingClient.queryPurchasesAsync(
          QueryPurchasesParams
              .newBuilder()
              .setProductType(ProductType.INAPP)
              .build(),
          (billingResult, purchases) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

              Log.d(TAG,
                  "Purchases queried successfully. " + purchases.size() + " purchase/s found.");

              // refresh list of all valid purchases
              mPurchases.clear();
              for (Purchase purchase : purchases) {
                handlePurchase(purchase);
              }

              // notify listeners of updated purchases
              notifyPurchaseListeners();

            } else {
              Log.w(TAG,
                  "Purchase query failed with billing response: "
                      + billingResult.getResponseCode());
            }
          }
      );
    });
  }

  /**
   * Consume purchase asynchronously and then notify purchase listeners.
   *
   * @param purchase - purchase to be consumed
   */
  @Override
  public void consumePurchaseAsync(final Purchase purchase) {

    executeWhenClientReady(() -> {
      Log.d(TAG, "Consuming purchase: " + purchase);

      mBillingClient.consumeAsync(
          ConsumeParams
              .newBuilder()
              .setPurchaseToken(purchase.getPurchaseToken())
              .build(),
          (billingResult, purchaseTkn) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
              Log.i(TAG, "Successfully consumed purchase token: " + purchaseTkn);

              // remove purchase
              mPurchases.remove(purchase.getOrderId());

              // notify purchase listeners
              notifyPurchaseListeners();
            } else {
              Log.e(TAG, "Failed to consume purchase token: " + purchaseTkn + ". Billing result: "
                  + billingResult
                  .getResponseCode());
            }
          });
    });
  }

  private void notifyPurchaseListeners() {
    for (BillingUpdatesListener listeners : billingUpdateListeners) {
      listeners.onPurchasesUpdated(new ArrayList<>(mPurchases.values()));
    }
  }

  /**
   * Clear the resources
   */
  @Override
  public void destroy() {
    Log.d(TAG, "Destroying the Billing Manager.");
    if (mBillingClient.isReady()) {
      mBillingClient.endConnection();
      clientState = BillingClientState.CLOSED;
    }
  }

  /**
   * Handles a purchase:
   * <p>
   * 1) Verifies the signature.
   * <p>
   * 2) Checks the purchase state.
   * <p>
   * 3) Adds to collection of purchases.
   * <p>
   * 4) Acknowledges the purchase (if needed).
   *
   * @param purchase Purchase to be handled
   */
  private void handlePurchase(final Purchase purchase) {
    if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
      Log.w(TAG, "Got a purchase: " + purchase + "; but signature is bad. Skipping...");
      return;
    }

    if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
      Log.d(TAG, "Got a verified purchase: " + purchase);
      mPurchases.put(purchase.getOrderId(), purchase);

      // we must acknowledge a purchase (async) or it will be automatically cancelled
      if (!purchase.isAcknowledged()) {
        mBillingClient.acknowledgePurchase(
            AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build(),
            billingResult -> {
              if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.i(TAG, "Acknowledged purchase: " + purchase);
              } else {
                Log.w(TAG, "Failed to acknowledge purchase: " + purchase);
              }
            });
      }
    } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
      // a pending purchase may take some time to approve
      Log.d(TAG, "Got a verified pending purchase: " + purchase);
      mPurchases.put(purchase.getOrderId(), purchase);
    } else {
      Log.w(TAG, "Purchase " + purchase + " is not purchased. Purchase state: "
          + purchase.getPurchaseState());
    }
  }

  /**
   * Verifies that the purchase was signed correctly for this developer's public key.
   * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
   * replace this method with "constant true" if they decompile/rebuild your app.
   * </p>
   */
  private boolean verifyValidSignature(String signedData, String signature) {
    try {
      return Security.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, signedData, signature);
    } catch (IOException e) {
      Log.e(TAG, "Got an exception trying to validate a purchase: " + e);
      return false;
    }
  }

  @Override
  public synchronized void registerPurchasesListener(BillingUpdatesListener listener) {
    Log.d(TAG, "Register Billing Listener '" + listener + "'.");
    billingUpdateListeners.add(listener);
  }

  @Override
  public synchronized void unregisterPurchasesListener(BillingUpdatesListener listener) {
    Log.d(TAG, "Unregister Billing Listener '" + listener + "'.");
    billingUpdateListeners.remove(listener);
  }
}

