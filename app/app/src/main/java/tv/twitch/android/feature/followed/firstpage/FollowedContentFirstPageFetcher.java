package tv.twitch.android.feature.followed.firstpage;

import java.util.Collections;
import java.util.List;

import io.reactivex.Maybe;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.feature.followed.model.FollowingContentItemCollection;
import tv.twitch.android.feature.followed.model.FollowingContentResponse;
import tv.twitch.android.models.GameModel;
import tv.twitch.android.shared.api.pub.FollowedFirstPageContent;

public class FollowedContentFirstPageFetcher {
    private FollowedGamesFetcher gamesFetcher;
    private FollowedStreamsFetcher streamsFetcher;

    /* ... */

    private final void cacheFirstPageContent(FollowedFirstPageContent followedFirstPageContent) { // TODO: __REPLACE_METHOD
        if (!UI.getHideGames()) {
            this.gamesFetcher.cachePrefetchGames(followedFirstPageContent);
        }

        this.streamsFetcher.cachePrefetchStreams(followedFirstPageContent);
    }

    public final Maybe<List<GameModel>> fetchMoreGames() { // TODO: __REPLACE_METHOD
        if (UI.getHideGames()) {
            return Maybe.empty();
        }

        return this.gamesFetcher.fetchMore();
    }

    public final List<FollowingContentItemCollection> getCachedContent() {
        List<GameModel> games;
        if (UI.getHideGames()) { // TODO: __INJECT_CODE
            games = Collections.emptyList();
        } else {
            games = this.gamesFetcher.getCachedContent();
        }

        /* ... */

        throw new VirtualImpl();
    }

    private static final FollowingContentResponse refresh$lambda(FollowedFirstPageContent p0) {
        List<GameModel> games;
        if (UI.getHideGames()) { // TODO: __INJECT_CODE
            games = Collections.emptyList();
        } else {
            games = null;
        }

        /* ... */

        throw new VirtualImpl();
    }

    public final boolean hasMoreGames() {
        if (UI.getHideGames()) { // TODO: __INJECT_CODE
            return false;
        }

        /* ... */

        throw new VirtualImpl();
    }
}
