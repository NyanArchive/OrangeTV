package tv.twitch.android.shared.chomments.impl;

import tv.orange.core.Core;
import tv.twitch.android.models.channel.ChannelModel;
import tv.twitch.android.models.videos.VodModel;

public class ChommentsFetcherImpl {
    public void prepareForVod(VodModel vod, ChannelModel channelModel) {
        /* ... */

        Core.get().onConnectedToChannel(channelModel.getId()); // TODO: __INJECT_CODE
    }
}