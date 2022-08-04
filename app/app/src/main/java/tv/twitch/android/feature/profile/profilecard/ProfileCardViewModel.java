package tv.twitch.android.feature.profile.profilecard;

import tv.orange.features.stv.StvAvatars;

public class ProfileCardViewModel {
    private final String channelName = null;
    private final String profileImageUrl = null;

    /* ... */

    public final String getProfileImageUrl() { // TODO: __REPLACE_METHOD
        return StvAvatars.get().hookProfileImageUrl(this.profileImageUrl, this.channelName);
    }

    /* ... */
}