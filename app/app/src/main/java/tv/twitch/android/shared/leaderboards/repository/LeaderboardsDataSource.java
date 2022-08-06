package tv.twitch.android.shared.leaderboards.repository;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.chat.ChatBroadcaster;
import tv.twitch.android.models.leaderboard.ChannelLeaderboards;

public class LeaderboardsDataSource {
    /* ... */

    public final boolean shouldFetchLeaderboards(ChatBroadcaster chatBroadcaster) {
        if (UI.getHideLeaderboards()) { // TODO: __INJECT_CODE
            return false;
        }

        /* ... */

        throw new VirtualImpl();
    }

    public final void observePubSubUpdates(ChannelLeaderboards channelLeaderboards) {
        if (UI.getHideLeaderboards()) { // TODO: __INJECT_CODE
            return;
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
