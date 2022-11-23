package tv.twitch.android.feature.followed;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import kotlin.jvm.functions.Function1;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.fetchers.NoParamDynamicContentFetcher;
import tv.twitch.android.core.fetchers.RefreshPolicy;
import tv.twitch.android.models.streams.StreamModelContainer;

public class RecommendedStreamsFetcher extends NoParamDynamicContentFetcher {
    /* ... */

    public RecommendedStreamsFetcher(RefreshPolicy refreshPolicy) {
        super(refreshPolicy);

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */

    public Single<List<? extends StreamModelContainer.RecommendationStreamModel>> getQuerySingle(String str) {
        if (UI.getHideRecommendedStreams()) { // TODO: __INJECT_CODE
            setHasMoreContent(false);
            return Single.just(Collections.emptyList());
        }

        throw new VirtualImpl();
    }

    @Override
    public Object getCacheKey() {
        throw new VirtualImpl();
    }

    @Override
    public Function1 getTransformToCache() {
        throw new VirtualImpl();
    }

    /* ... */
}
