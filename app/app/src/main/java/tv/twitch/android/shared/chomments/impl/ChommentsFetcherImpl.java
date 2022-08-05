package tv.twitch.android.shared.chomments.impl;

import tv.orange.core.Core;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.channel.ChannelModel;
import tv.twitch.android.models.videos.VodModel;

public class ChommentsFetcherImpl {
    /* ... */

    public void prepareForVod(VodModel vod, ChannelModel channelModel) {
        /* ... */

        Core.get().onConnectingToChannel(channelModel.getId()); // TODO: __INJECT_CODE
        Core.get().onConnectedToChannel(channelModel.getId()); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}