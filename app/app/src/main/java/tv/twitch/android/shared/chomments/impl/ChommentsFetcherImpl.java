package tv.twitch.android.shared.chomments.impl;

import java.util.List;

import io.reactivex.Maybe;
import tv.orange.core.Core;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.vodsync.VodSync;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.channel.ChannelModel;
import tv.twitch.android.models.chomments.ChommentModel;
import tv.twitch.android.models.videos.VodModel;

public class ChommentsFetcherImpl {
    private VodModel vodModel;

    /* ... */

    public void prepareForVod(VodModel vod, ChannelModel channelModel) {
        /* ... */

        Core.get().onConnectingToChannel(channelModel.getId()); // TODO: __INJECT_CODE
        Core.get().onConnectedToChannel(channelModel.getId()); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public Maybe<List<ChommentModel>> fetchChommentsForTimestamp(String str, int i) {
        i = VodSync.get().hookChommentTimestamp(vodModel, i); // TODO: __INJECT_CODE
        if (i < 0 || ChatHookProvider.isChatKilled()) { // TODO: __INJECT_CODE
            return Maybe.empty();
        }

        throw new VirtualImpl();
    }

    /* ... */
}