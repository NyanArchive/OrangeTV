package tv.twitch.android.shared.autoplay;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.Playable;

public class RecommendationAutoPlayPresenter<T extends Playable> {
    /* ... */

    public final void prepareRecommendationForCurrentModel(T var1) {
        /* ... */

        if (!UI.isAutoplayDisabled()) {
            // ISubscriptionHelper.DefaultImpls.asyncSubscribe$default(this, this.recommendationFetcher.fetchRecommendation(var1), (DisposeOn)null, new NamelessClass_5(this), 1, (Object)null);
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
