package tv.twitch.android.shared.billing.debug;

import android.app.Activity;

import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;

import tv.orange.core.LoggerImpl;

public class DebugBillingClient {
    private BillingResult billingResultOk;

    /* ... */

    public BillingResult launchBillingFlow(final Activity activity, BillingFlowParams params) { // TODO: __REPLACE_METHOD
        LoggerImpl.INSTANCE.devDebug(params);

        return billingResultOk;
    }

    /* ... */
}
