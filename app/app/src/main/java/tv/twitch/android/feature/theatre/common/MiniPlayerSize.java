package tv.twitch.android.feature.theatre.common;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;

public class MiniPlayerSize {
    /* ... */

    public final int getWidth() { // TODO: __INJECT_METHOD
        int res = 0;

        /* ... */

        res = UI.hookMiniPlayerWidth(res);

        throw new VirtualImpl();
    }

    /* ... */
}
