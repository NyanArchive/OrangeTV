package tv.twitch.android.models.streams;

import tv.orange.features.stv.StvAvatars;

public class HostedStreamModel {
    private String channelLogoURL;
    private String name;

    /* ... */

    public String getChannelLogoURL() { // TODO: __REPLACE_METHOD
        return StvAvatars.get().hookProfileImageUrl(this.channelLogoURL, this.name);
    }

    /* ... */
}