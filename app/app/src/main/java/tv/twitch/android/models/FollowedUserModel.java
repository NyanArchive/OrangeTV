package tv.twitch.android.models;

import tv.orange.features.stv.StvAvatars;

public class FollowedUserModel {
    private final String name = null;
    private final String profileImageUrl = null;

    /* ... */

    public String getProfileImageUrl() { // TODO: __REPLACE_METHOD
        return StvAvatars.get().hookProfileImageUrl(this.profileImageUrl, name);
    }

    /* ... */
}
