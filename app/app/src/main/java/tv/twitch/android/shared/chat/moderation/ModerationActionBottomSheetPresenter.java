package tv.twitch.android.shared.chat.moderation;

import androidx.fragment.app.FragmentActivity;

import tv.orange.features.logs.LogsHook;
import tv.orange.models.VirtualImpl;

public class ModerationActionBottomSheetPresenter {
    private FragmentActivity activity;

    /* ... */

    public final void requestAction(ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent moderationActionButtonEvent) {
        if (LogsHook.get().showModLogs(activity, moderationActionButtonEvent)) { // TODO: __INJECT_CODE
            return;
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}