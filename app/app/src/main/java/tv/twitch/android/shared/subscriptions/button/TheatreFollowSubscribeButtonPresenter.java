package tv.twitch.android.shared.subscriptions.button;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.presenter.RxPresenter;

public class TheatreFollowSubscribeButtonPresenter extends RxPresenter {
    /* ... */

    public final void show() {
        if (UI.getHideFSB()) { // TODO: __INJECT_CODE
            return;
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
