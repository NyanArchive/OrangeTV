package tv.twitch.android.shared.ui.cards.channel;

import tv.orange.features.stv.StvAvatars;

public class NewCompactProfileViewModel {
    private final String channelName = null;
    private final String imageUrl = null;

    /* ... */

    public final String getImageUrl() { // TODO: __REPLACE_METHOD
        return StvAvatars.get().hookProfileImageUrl(this.imageUrl, this.channelName);
    }

    /* ... */
}

