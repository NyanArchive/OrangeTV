package tv.twitch.android.shared.leaderboards.webview;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.chat.ChatBroadcaster;

public class LeaderboardsWebViewPresenter {
    /* ... */

    public final boolean shouldFetchLeaderboards(ChatBroadcaster chatBroadcaster) {
        if (UI.getHideLeaderboards()) { // TODO: __INJECT_CODE
            return false;
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
