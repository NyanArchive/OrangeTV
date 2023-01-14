package tv.twitch.android.shared.ads.eligibility;

import io.reactivex.Single;
import tv.twitch.android.shared.ads.models.AdRequestInfo;

public class AdEligibilityFetcher {
    /* ... */

    public final Single<Boolean> shouldRequestAd(final AdRequestInfo adRequestInfo) { // TODO: __REPLACE_METHOD
        return Single.just(Boolean.FALSE);
    }

    /* ... */
}
