package tv.twitch.android.shared.subscriptions.purchasers;

import android.app.Activity;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import tv.orange.core.Core;
import tv.twitch.android.shared.subscriptions.db.SubscriptionPurchaseEntity;

public class GooglePlaySubscriptionPurchaser {
    /* ... */

    public static final SingleSource purchase$lambda(GooglePlaySubscriptionPurchaser this$0, Activity activity, SubscriptionPurchaseEntity purchaseEntity) {
        Single<SubscriptionPurchaseResponse> res = null;

        /* ... */

        return Core.injectBilling(activity, res, purchaseEntity); // TODO: __INJECT_BILLING
    }

    /* ... */
}
