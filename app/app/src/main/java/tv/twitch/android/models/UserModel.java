package tv.twitch.android.models;

import tv.orange.features.stv.AvatarsHookProvider;

public class UserModel {
    private String logoURL;
    private String name;

    /* ... */

    public final String getLogoURL() { // TODO: __REPLACE_METHOD
        return AvatarsHookProvider.get().hookProfileImageUrl(this.logoURL, name);
    }

    /* ... */
}