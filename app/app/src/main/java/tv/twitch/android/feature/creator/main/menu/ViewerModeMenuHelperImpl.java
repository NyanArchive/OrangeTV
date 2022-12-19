package tv.twitch.android.feature.creator.main.menu;

import android.view.Menu;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;

public class ViewerModeMenuHelperImpl {
    /* ... */

    public void onPrepareMenu(Menu menu) {
        boolean isVisible = UI.get().isCreatorButtonVisible(true);

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
