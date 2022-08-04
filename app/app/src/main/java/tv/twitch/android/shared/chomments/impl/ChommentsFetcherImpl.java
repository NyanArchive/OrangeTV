package tv.twitch.android.shared.chomments.impl;

import tv.orange.core.Core;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.models.channel.ChannelModel;
import tv.twitch.android.models.videos.VodModel;

public class ChommentsFetcherImpl {
    /* ... */

    public void prepareForVod(VodModel vod, ChannelModel channelModel) {
        /* ... */

        Core.getFeature(Core.class).onConnectingToChannel(channelModel.getId()); // TODO: __INJECT_CODE
        Core.getFeature(Core.class).onConnectedToChannel(channelModel.getId()); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}