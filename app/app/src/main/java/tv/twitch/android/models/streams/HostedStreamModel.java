package tv.twitch.android.models.streams;

import tv.orange.features.stv.AvatarsHookProvider;

public class HostedStreamModel {
    private String channelLogoURL;
    private String name;

    /* ... */

    public String getChannelLogoURL() { // TODO: __REPLACE_METHOD
        return AvatarsHookProvider.get().hookProfileImageUrl(this.channelLogoURL, this.name);
    }

    /* ... */
}