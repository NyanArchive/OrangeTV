package tv.twitch.android.shared.chat.creatorpinnedchatmessage.presenters;

import tv.orange.core.Core;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.presenter.RxPresenter;

public class CreatorPinnedChatMessagePresenter extends RxPresenter {
    /* ... */

    public void onActive() {
        if (Core.isPinnedChatMessageEnabled()) { // TODO: __REPLACE_METHOD
            super.onActive();
            setupPinnedMessage();
        }
    }

    private final void setupPinnedMessage() {
        throw new VirtualImpl();
    }

    /* ... */
}
