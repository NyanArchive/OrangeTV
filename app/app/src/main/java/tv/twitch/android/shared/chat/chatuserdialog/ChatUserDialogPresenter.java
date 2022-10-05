package tv.twitch.android.shared.chat.chatuserdialog;

import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import tv.orange.features.logs.ChatLogs;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.social.ChatUser;

public class ChatUserDialogPresenter {
    private FragmentActivity activity;
    private ChatUserDialogInfo info;
    private Function0<Unit> onDismissListener;
    private ChatUserDialogViewDelegate viewDelegate;

    /* ... */

    private final void setupClickHandlers(ChatUser chatUser) {
        setupLocalLogsClickHandler(activity, viewDelegate.getLocalLogs(), info, onDismissListener, chatUser); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */

    private final void setupLocalLogsClickHandler(FragmentActivity activity, TextView localLogs, ChatUserDialogInfo info, Function0<Unit> onDismissListener, ChatUser chatUser) { // TODO: __INJECT_METHOD
        ChatLogs.get().setupLocalLogsClickHandler(activity, viewDelegate.getLocalLogs(), info, onDismissListener, chatUser);
    }
}
