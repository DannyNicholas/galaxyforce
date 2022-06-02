package com.danosoftware.galaxyforce.billing;

import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams.Product;
import java.util.List;

public interface BillingManager {

  void queryProductDetailsAsync(
      List<Product> products,
      ProductsQueriedListener listener);

  void queryPurchasesAsync();

  void initiatePurchaseFlowAsync(ProductDetails productDetails);

  void consumePurchaseAsync(Purchase purchase);

  void registerPurchasesListener(BillingUpdatesListener listener);

  void unregisterPurchasesListener(BillingUpdatesListener listener);

  void destroy();
}
