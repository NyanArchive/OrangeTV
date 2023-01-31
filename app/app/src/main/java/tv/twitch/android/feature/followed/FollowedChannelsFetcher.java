package tv.twitch.android.feature.followed;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Maybe;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.fetchers.BaseFetcher;
import tv.twitch.android.core.fetchers.RefreshPolicy;
import tv.twitch.android.models.FollowedUserModel;

public class FollowedChannelsFetcher extends BaseFetcher {
    private boolean hasFetchedAll;

    /* ... */

    public FollowedChannelsFetcher(RefreshPolicy refreshPolicy, ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap1) {
        super(refreshPolicy, concurrentHashMap, concurrentHashMap1);
    }

    /* ... */

    private final Maybe<List<FollowedUserModel>> fetchFollowedUsers() {
        if (UI.getHideOfflineChannels()) { // TODO: __INJECT_CODE
            hasFetchedAll = true;
            return Maybe.just(Collections.emptyList());
        }

        /* ... */

        throw new VirtualImpl();
    }

    public final boolean hasMoreChannels() {
        if (UI.getHideOfflineChannels()) { // TODO: __INJECT_CODE
            hasFetchedAll = true;
            return false;
        }

        /* ... */

        throw new VirtualImpl();
    }

    public final Maybe<Boolean> getUserHasFollows() {
        if (UI.getHideOfflineChannels()) { // TODO: __INJECT_CODE
            hasFetchedAll = true;
            return Maybe.just(Boolean.FALSE);
        }

        /* ... */

        throw new VirtualImpl();
    }


    /* ... */
}
