package tv.twitch.android.feature.theatre.common;

import android.view.ViewGroup;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;

public class PlayerCoordinatorViewDelegate {
    private ViewGroup landscapeChatContainer;

    /* ... */

    public PlayerCoordinatorViewDelegate() {
        /* ... */

        UI.get().changeLandscapeChatContainerOpacity(landscapeChatContainer); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    /* ... */
}
