package com.danosoftware.galaxyforce.billing;

import com.android.billingclient.api.ProductDetails;

public interface ProductDetailsListener {

  void onProductDetailsRetrieved(ProductDetails productDetails);
}
