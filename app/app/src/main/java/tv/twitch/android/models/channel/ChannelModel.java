package tv.twitch.android.models.channel;

import tv.orange.features.stv.StvAvatars;
import tv.orange.models.exception.VirtualImpl;

public class ChannelModel {
    private String name;
    private String logo;

    /* ... */

    public final String getLogo() { // TODO: __REPLACE_CLASS
        return StvAvatars.get().hookProfileImageUrl(this.logo, name);
    }

    public int getId() {
        throw new VirtualImpl();
    }

    /* ... */
}
