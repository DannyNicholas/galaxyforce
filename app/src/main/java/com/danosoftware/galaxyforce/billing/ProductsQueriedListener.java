package com.danosoftware.galaxyforce.billing;

import com.android.billingclient.api.ProductDetails;
import java.util.List;

/**
 * Listener that supplies list of queried product details.
 */
public interface ProductsQueriedListener {

  void onProductsQueried(List<ProductDetails> productDetails);
}
