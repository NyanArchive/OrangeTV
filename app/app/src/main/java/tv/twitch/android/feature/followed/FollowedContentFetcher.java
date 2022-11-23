package tv.twitch.android.feature.followed;

import java.util.Collections;
import java.util.List;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.feature.followed.model.FollowingContentResponse;
import tv.twitch.android.models.GameModel;
import tv.twitch.android.shared.api.pub.FollowedFirstPageContent;

public class FollowedContentFetcher {
    /* ... */

    public static final FollowingContentResponse observePrefetchContent$lambda(FollowedFirstPageContent it) {
        List<GameModel> games;
        if (UI.getHideGames()) { // TODO: __INJECT_CODE
            games = Collections.emptyList();
        } else {
            games = null;
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
