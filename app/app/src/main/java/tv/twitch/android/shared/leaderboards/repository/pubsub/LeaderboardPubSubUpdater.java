package tv.twitch.android.shared.leaderboards.repository.pubsub;

import java.util.List;

import io.reactivex.Single;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.leaderboard.ChannelLeaderboardRank;
import tv.twitch.android.shared.leaderboards.model.LeaderboardPubSubRankingUpdate;

public class LeaderboardPubSubUpdater {
    /* ... */

    private final Single<LeaderboardPubSubRankingUpdate> normalizePubSubRanking(LeaderboardPubSubUpdate leaderboardPubSubUpdate, List<ChannelLeaderboardRank> list) {
        /* ... */

        LeaderboardPubSubEntry entry = null;
        if (!entry.getUserId().equals("") && entry.getUserId().equals("0")) { // TODO: __INJECT_CODE
            //
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}