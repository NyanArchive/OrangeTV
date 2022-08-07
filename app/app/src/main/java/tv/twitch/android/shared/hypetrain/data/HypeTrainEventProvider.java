package tv.twitch.android.shared.hypetrain.data;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;

public class HypeTrainEventProvider {
    /* ... */

    public void onActive() {
        if (!UI.getDisableHypeTrain()) { // TODO: __INJECT_CODE

            /* ... */

            throw new VirtualImpl();
        }
    }

    /* ... */
}
