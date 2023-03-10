package tv.twitch.android.shared.subscriptions.purchasers;

import android.app.Activity;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import tv.orange.core.CoreHook;
import tv.twitch.android.shared.subscriptions.db.SubscriptionPurchaseEntity;

public class GooglePlaySubscriptionPurchaser$purchase$3 {
    final Activity $activity = null;
    final GooglePlaySubscriptionPurchaser this$0 = null;

    /* ... */

    public final SingleSource<? extends SubscriptionPurchaseResponse> invoke(SubscriptionPurchaseEntity purchaseEntity) {
        Single<SubscriptionPurchaseResponse> res = null;

        /* ... */

        return CoreHook.injectBilling($activity, res, purchaseEntity); // TODO: __INJECT_BILLING
    }
}
