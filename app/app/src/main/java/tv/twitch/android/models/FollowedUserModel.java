package tv.twitch.android.models;

import tv.orange.features.stv.AvatarsHookProvider;

public class FollowedUserModel {
    private final String name = null;
    private final String profileImageUrl = null;

    /* ... */

    public String getProfileImageUrl() { // TODO: __REPLACE_METHOD
        return AvatarsHookProvider.get().hookProfileImageUrl(this.profileImageUrl, name);
    }

    /* ... */
}
