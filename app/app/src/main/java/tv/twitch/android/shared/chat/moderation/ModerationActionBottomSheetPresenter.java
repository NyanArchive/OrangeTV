package tv.twitch.android.shared.chat.moderation;

import androidx.fragment.app.FragmentActivity;

import tv.orange.features.logs.ChatLogs;
import tv.orange.models.exception.VirtualImpl;

public class ModerationActionBottomSheetPresenter {
    private FragmentActivity activity;

    /* ... */

    public final void requestAction(ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent moderationActionButtonEvent) {
        if (ChatLogs.get().showModLogs(activity, moderationActionButtonEvent)) { // TODO: __INJECT_CODE
            return;
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}