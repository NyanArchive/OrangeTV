package tv.twitch.android.feature.followed;

import java.util.Collections;

import io.reactivex.Single;
import kotlin.jvm.functions.Function1;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.fetchers.NoParamDynamicContentFetcher;
import tv.twitch.android.core.fetchers.RefreshPolicy;
import tv.twitch.android.models.resumewatching.ResumeWatchingResponse;

public class ResumeWatchingVideosFetcher extends NoParamDynamicContentFetcher {
    /* ... */

    public ResumeWatchingVideosFetcher(RefreshPolicy refreshPolicy) {
        super(refreshPolicy);

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */

    public Single<ResumeWatchingResponse> getQuerySingle(String str) {
        if (UI.getHideResumeWatching()) { // TODO: __INJECT_CODE
            setHasMoreContent(false);
            return Single.just(new ResumeWatchingResponse(Collections.emptyList(), Collections.emptyMap()));
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
