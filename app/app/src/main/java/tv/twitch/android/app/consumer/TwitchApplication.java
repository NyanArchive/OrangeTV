package tv.twitch.android.app.consumer;

import tv.orange.core.Core;

public class TwitchApplication {
    public Core orange; // TODO: __INJECT_FIELD

    public Core getOrange() { // TODO: __INJECT_METHOD
        return orange;
    }

    public void onCreate() {
        /* ... */

        orange = Core.Companion.create(); // TODO: __INJECT_CODE
        orange.initialize(); // TODO: __INJECT_CODE

        /* ... */
    }
}