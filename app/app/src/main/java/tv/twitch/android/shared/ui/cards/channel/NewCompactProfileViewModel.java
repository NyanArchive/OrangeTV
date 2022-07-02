package tv.twitch.android.shared.ui.cards.channel;

import tv.orange.features.stv.AvatarsHookProvider;

public class NewCompactProfileViewModel {
    private final String channelName = null;
    private final String imageUrl = null;

    /* ... */

    public final String getImageUrl() { // TODO: __REPLACE_METHOD
        return AvatarsHookProvider.get().hookProfileImageUrl(this.imageUrl, this.channelName);
    }

    /* ... */
}

