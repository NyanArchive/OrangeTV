package tv.twitch.android.models;

import tv.orange.features.stv.StvAvatars;

public class UserModel {
    private String logoURL;
    private String name;

    /* ... */

    public final String getLogoURL() { // TODO: __REPLACE_METHOD
        return StvAvatars.get().hookProfileImageUrl(this.logoURL, name);
    }

    /* ... */
}