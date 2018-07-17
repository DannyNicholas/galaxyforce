package com.danosoftware.galaxyforce.billing.service;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.danosoftware.galaxyforce.BuildConfig;
import com.danosoftware.galaxyforce.R;
import com.danosoftware.galaxyforce.billing.IabHelper;
import com.danosoftware.galaxyforce.billing.IabResult;
import com.danosoftware.galaxyforce.billing.Inventory;
import com.danosoftware.galaxyforce.billing.Purchase;
import com.danosoftware.galaxyforce.constants.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BillingServiceImpl implements IBillingService
{
    /* logger tag */
    private static final String BILLING_TAG = "BillingService";

    /* payload for the purchase flow - ideally should be generated string */
    private static final String PAYLOAD = "Galaxy-Force-Purchase-Payload";

    /*
     * 
     * START OF DEVELOPMENT OVERRIDE
     * 
     * FIXME - Danny - Static product over-ride in development mode only!! Avoid
     * limitations of not being to test billing in development mode.
     */
    private static final Map<String, ProductState> staticDevMap = new HashMap<String, ProductState>()
    {
        private static final long serialVersionUID = 1L;

        {
            put(GameConstants.FULL_GAME_PRODUCT_ID, ProductState.PURCHASED);
            put(GameConstants.ALL_LEVELS_PRODUCT_ID, ProductState.NOT_PURCHASED);
        }
    };

    /**
     * 
     * BILLING KNOWN BUGS AND ISSUES
     * 
     * 
     * When testing with static responses, there are known problems with
     * Security.verifyPurchase() that reports that purchase verification failed.
     * This happens because static responses have no valid signature.
     * 
     * Code should work with real purchases but not static purchases. Method may
     * need replacing for testing. See below for fix:
     * 
     * http://stackoverflow.com/questions
     * /19534210/in-app-billing-not-working-after
     * -update-google-store/19539213#19539213
     * 
     * Sometime get IllegalStateException: Can't start async operation (xxx)
     * because another async operation(xxx) is in progress. This requires catch
     * blocks to manually catch and clear flags. See...
     * 
     * http://stackoverflow.com/questions/15628155/android
     * -in-app-billing-cant-start-launchpurchaseflow-because-launchpurchaseflo
     * 
     */

    /*
     * NOTE: Would advise not using static responses as they rarely work as
     * expected. See issues above.
     */

    /* test static responses - for testing only!! */
    // public static final String TEST_PRODUCT_PURCHASED =
    // "android.test.purchased";
    // public static final String TEST_PRODUCT_CANCELLED =
    // "android.test.canceled";
    // public static final String TEST_PRODUCT_REFUNDED =
    // "android.test.refunded";
    // public static final String TEST_PRODUCT_UNAVAILABLE =
    // "android.test.item_unavailable";

    /* product IDs - testing only!! */
    // public static final String FULL_GAME_PRODUCT_ID = TEST_PRODUCT_PURCHASED;
    // public static final String ALL_LEVELS_PRODUCT_ID =
    // TEST_PRODUCT_UNAVAILABLE;

    // state of billing service
    private enum BillingState
    {
        NOT_READY, READY, FAILURE
    }

    // state of products
    public enum ProductState
    {
        NOT_PURCHASED, PURCHASED, FAILURE
    }

    // reference to in-app billing service
    private IabHelper mHelper;

    // reference to in-app billing service
    private BillingState state;

    // reference to activity context
    private Activity activity;

    /*
     * map of product keys and current state. need to be concurrent map as will
     * update map in a billing thread following a refresh request.
     */
    private final ConcurrentMap<String, ProductState> products;

    /*
     * map of product keys and current price. need to be concurrent map as will
     * update map in a billing thread following a refresh request.
     */
    private final ConcurrentMap<String, String> prices;

    /*
     * set of observers to be notified following any product state changes.
     * Access to the list may be from the main thread and billing helper thread
     * so may need to be synchronised. A set ensures the same observer can't
     * accidentally register twice.
     */
    private Set<BillingObserver> observers;

    // private constructor
    public BillingServiceImpl(Activity activity)
    {
        this.activity = activity;

        // initialise state to not ready
        this.state = BillingState.NOT_READY;

        // set-up IAB helper using computed public key
        this.mHelper = new IabHelper(activity, getPublicKey());

        // enable debug logging (for a production application, you should set
        // this to false).
        mHelper.enableDebugLogging(true);

        // set-up map of product Ids and states
        this.products = new ConcurrentHashMap<String, ProductState>();

        // set-up map of product Ids and prices
        this.prices = new ConcurrentHashMap<String, String>();

        // set-up set of observers listening for product state changes
        this.observers = new HashSet<BillingObserver>();

        // setup billing service
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
        {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess())
                {
                    // billing service could not be set-up
                    Log.e(GameConstants.LOG_TAG, BILLING_TAG + ": Problem setting up In-app Billing: " + result);
                    state = BillingState.FAILURE;
                }
                else
                {
                    // IAB is fully set up!
                    Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Successfully set-up In-app Billing: " + result);
                    state = BillingState.READY;

                    // once set-up, we can request a product refresh
                    refreshProductStates();
                }
            }
        });
    }

    @Override
    public void destroy()
    {
        if (mHelper != null)
        {
            mHelper.dispose();
            mHelper = null;
            Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Destroy In-app Billing.");
        }
    }

    /*
     * Register an observer for any product state refreshes. Normally called
     * when a observer is constructed.
     * 
     * Synchronized to avoid adding observer in main thread while notifying
     * observers in billing thread.
     */
    @Override
    public synchronized void registerProductObserver(BillingObserver billingObserver)
    {
        Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Register Billing Observer '" + billingObserver + "'.");
        observers.add(billingObserver);
    }

    /*
     * Unregister an observer for any product state refreshes. Normally called
     * when a observer is disposed.
     * 
     * Synchronized to avoid removing observer in main thread while notifying
     * observers in billing thread.
     */
    @Override
    public synchronized void unregisterProductObserver(BillingObserver billingObserver)
    {
        Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Unregister Billing Observer '" + billingObserver + "'.");
        observers.remove(billingObserver);
    }

    /**
     * Refresh the product states of all known billing products. Querying
     * billing service is an asynchronous activity so refreshing may not happen
     * immediately.
     */
    @Override
    public void refreshProductStates()
    {
        /*
         * only refresh once billing service is set-up. trying to refresh before
         * service is ready will cause an IllegalStateException by the IAB
         * helper. this may happen when activity calls on resume for the first
         * time.
         * 
         * to avoid the product states not being refreshed on start-up, the
         * billing set-up method will also request a refresh once initial set-up
         * is complete.
         */
        if (state == BillingState.READY)
        {
            List<String> additionalSkuList = new ArrayList<String>();
            additionalSkuList.add(GameConstants.FULL_GAME_PRODUCT_ID);
            additionalSkuList.add(GameConstants.ALL_LEVELS_PRODUCT_ID);

            // set-up listener for inventory request
            QueryFinishedListener mQueryFinishedListener = new QueryFinishedListener(additionalSkuList);

            /*
             * execute async query request. result will callback listener when
             * finished.
             */
            try
            {
                mHelper.queryInventoryAsync(true, additionalSkuList, mQueryFinishedListener);
            }
            /*
             * Illegal State Exceptions can be thrown if the purchase fails but
             * the IabHelper still believes the async purchase is in progress.
             * In these scenarios catch the exception and clear the async flag.
             */
            catch (IllegalStateException e)
            {
                Log.e(GameConstants.LOG_TAG, BILLING_TAG + ": IllegalStateException while refreshing products. Clearing async flag.");
                mHelper.flagEndAsync();
            }
        }
    }

    @Override
    public boolean isPurchased(String productId)
    {
        return products.get(productId) == ProductState.PURCHASED;
    }

    @Override
    public boolean isNotPurchased(String productId)
    {
        return products.get(productId) == ProductState.NOT_PURCHASED;
    }

    @Override
    public String getPrice(String productId)
    {
        // return price of product. will return null if price is not yet known
        return prices.get(productId);
    }

    @Override
    public void purchase(String productId)
    {
        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new PurchaseFinishedListener(productId);

        try
        {
            mHelper.launchPurchaseFlow(activity, productId, GameConstants.BILLING_REQUEST, mPurchaseFinishedListener, PAYLOAD);
        }
        /*
         * Illegal State Exceptions can be thrown if another async process is in
         * progress or previous async purchase failed. In these scenarios catch
         * the exception and clear the async flag.
         */
        catch (IllegalStateException e)
        {
            Log.e(GameConstants.LOG_TAG, BILLING_TAG + ": IllegalStateException while starting purchase flow for '" + productId
                    + "'. Clearing async flag.");
            mHelper.flagEndAsync();
        }
    }

    @Override
    public boolean processActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (mHelper == null || state != BillingState.READY)
        {
            Log.w(GameConstants.LOG_TAG, BILLING_TAG + ": Unable to handle activity request. Billing Service not available.");
            return false;
        }

        // Pass on the activity result and return result
        Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Activity request passed onto IAB Helper.");
        return mHelper.handleActivityResult(requestCode, resultCode, data);
    }

    /**
     * Consume the purchased product Id.
     * 
     * @param purchase
     * @param productId
     */
    private void consume(Purchase purchase, String productId)
    {
        IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new ConsumeFinishedListener(productId);

        try
        {
            mHelper.consumeAsync(purchase, mConsumeFinishedListener);
        }
        /*
         * Illegal State Exceptions can be thrown if another async process is in
         * progress or previous async purchase failed. In these scenarios catch
         * the exception and clear the async flag.
         */
        catch (IllegalStateException e)
        {
            Log.e(GameConstants.LOG_TAG, BILLING_TAG + ": IllegalStateException while starting consume for '" + productId
                    + "'. Clearing async flag.");
            mHelper.flagEndAsync();
        }
    }

    /**
     * Reverses the supplied string. Used to help hide the real in-app billing
     * public key.
     * 
     * @param original
     * @return
     */
    private String reverse(String original)
    {
        return new StringBuilder(original).reverse().toString();
    }

    /**
     * Computes the IAB public key.
     * 
     * @return
     */
    private String getPublicKey()
    {
        /*
         * in-app billing public key - reversed and broken in sections to hide real
         * key. Key parts also placed to random order to further hide real key.
        */
//        final String KEY_PART1 = activity.getResources().getText(R.string.public_key1).toString();
//        final String KEY_PART2 = activity.getResources().getText(R.string.public_key2).toString();
//        final String KEY_PART3 = activity.getResources().getText(R.string.public_key3).toString();
//        final String KEY_PART4 = activity.getResources().getText(R.string.public_key4).toString();

        final String KEY_PART1 = BuildConfig.public_key1;
        final String KEY_PART2 = BuildConfig.public_key2;
        final String KEY_PART3 = BuildConfig.public_key3;
        final String KEY_PART4 = BuildConfig.public_key4;

        // build reversed version of key
        String reversedKey = KEY_PART1 + KEY_PART2 + KEY_PART3 + KEY_PART4;

        // reverse key back to correct order
        return reverse(reversedKey);
    }

    /**
     * Splits a public key into sections. Used when you want to turn an original
     * public key into a split reversed key usable by this billing service.
     * 
     * This method should never be called in production but is available when a
     * new key needs to be split.
     */
    @SuppressWarnings("unused")
    private void splitPublicKey(String publicKey)
    {
        // firstly reverse entire key
        String reverse = reverse(publicKey);

        // calculate how many characters will be in each of 4 parts
        int totallength = reverse.length();
        int lengthPerPart = totallength / 4;

        Log.i(GameConstants.LOG_TAG, BILLING_TAG + ": Total Length = " + totallength);
        Log.i(GameConstants.LOG_TAG, BILLING_TAG + ": Length per Part = " + lengthPerPart);

        // ouput each part
        for (int i = 0; i < totallength; i = i + lengthPerPart)
        {
            Log.i(GameConstants.LOG_TAG, BILLING_TAG + ": Part: " + ((i / lengthPerPart) + 1));

            String part = reverse.substring(i, i + lengthPerPart);
            Log.i(GameConstants.LOG_TAG, BILLING_TAG + ": String: " + part);
            Log.i(GameConstants.LOG_TAG, BILLING_TAG + ": Length: " + part.length());
        }
    }

    /**
     * Internal class that acts as a listener to any query requests to the IAB
     * service.
     */
    private class QueryFinishedListener implements IabHelper.QueryInventoryFinishedListener
    {
        // list of product Ids that are bring queried for
        private final List<String> productList;

        private QueryFinishedListener(List<String> productId)
        {
            this.productList = productId;
        }

        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {
            if (result.isFailure())
            {
                // handle error
                Log.e(GameConstants.LOG_TAG, BILLING_TAG + ": Failed to get items!");

                for (String productId : productList)
                {
                    ProductState productState = ProductState.FAILURE;

                    /*
                     * only update map with failure if key doesn't exist. in the
                     * event of a failure we should leave the existing purchase
                     * state (if one exists).
                     */
                    ProductState currentState = products.putIfAbsent(productId, productState);

                    Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Product: " + productId + " = " + currentState);

                    /*
                     * START OF DEVELOPMENT OVERRIDE
                     * 
                     * FIXME - Danny - manual override for testing in
                     * development mode (will normally fail in dev mode).
                     */
                    if (BuildConfig.DEBUG)
                    {
                        ProductState devProductState = staticDevMap.get(productId);
                        if (devProductState != null)
                        {
                            productState = devProductState;
                            Log.w(GameConstants.LOG_TAG, BILLING_TAG + ": Debug Override. Manually Setting Product: " + productId + " = "
                                    + productState);
                        }

                        // enter status into map
                        products.put(productId, productState);
                        prices.put(productId, "#TEST");
                    }
                    /*
                     * END OF DEVELOPMENT OVERRIDE
                     */

                }

                return;
            }

            // we now have a valid result. check status of each product
            for (String productId : productList)
            {
                ProductState productState;
                if (inventory.hasPurchase(productId))
                {
                    productState = ProductState.PURCHASED;

                    /*
                     * we may want to consume the purchase to allow repeated
                     * testing of billing. will cause async issues for multiple
                     * consumes.
                     */
                    // consume(inventory.getPurchase(productId), productId);
                }
                else
                {
                    productState = ProductState.NOT_PURCHASED;
                }

                // get price
                String price = inventory.getSkuDetails(productId).getPrice();
                String currencyCode = inventory.getSkuDetails(productId).getCurrencyCode();

                String combinedPrice = null;
                if (price != null && currencyCode != null)
                {
                    combinedPrice = currencyCode + " " + price;
                }

                // enter status into map
                products.put(productId, productState);
                prices.put(productId, combinedPrice);

                Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Product: " + productId + " = " + productState);
            }

            /*
             * notify models of billing state change. this is called in a
             * billing thread so must be synchronized to avoid new observers
             * being added/removed by the main thread at same time.
             */
            synchronized (this)
            {
                for (BillingObserver anObserver : observers)
                {
                    anObserver.billingProductsStateChange();
                }
            }
        }
    }

    /**
     * Internal class that acts as a listener to any query purchases to the IAB
     * service.
     */
    private class PurchaseFinishedListener implements IabHelper.OnIabPurchaseFinishedListener
    {
        private final String productId;

        private PurchaseFinishedListener(String productId)
        {
            this.productId = productId;
        }

        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Purhase result received.");

            if (result.isFailure())
            {
                // this could occur after an attempt to try and purchase a
                // product you already own.
                Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Error purchasing: " + result);
                return;
            }
            else if (purchase.getSku().equals(productId))
            {
                /*
                 * there is no need to refresh product states here. onResume()
                 * will be called after a purchase that will trigger a refresh.
                 */
                Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Successful purchase of: " + productId);
            }
        }
    }

    /**
     * Internal class that acts as a listener to any purchase consumes to the
     * IAB service.
     */
    private class ConsumeFinishedListener implements IabHelper.OnConsumeFinishedListener
    {
        private final String productId;

        private ConsumeFinishedListener(String productId)
        {
            this.productId = productId;
        }

        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result)
        {
            if (result.isSuccess())
            {
                Log.d(GameConstants.LOG_TAG, BILLING_TAG + ": Successful consume of: " + productId);
            }
            else
            {
                Log.e(GameConstants.LOG_TAG, BILLING_TAG + ": Failed to consume: " + productId);
            }
        }
    }
}
