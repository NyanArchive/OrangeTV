package tv.twitch.android.shared.chat.observables;

import io.reactivex.subjects.PublishSubject;
import tv.orange.core.Core;
import tv.orange.features.logs.ChatLogs;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.ErrorCode;
import tv.twitch.android.core.mvp.presenter.BasePresenter;
import tv.twitch.android.provider.chat.events.MessagesReceivedEvent;
import tv.twitch.android.sdk.ChannelState;

public class ChatConnectionController extends BasePresenter {
    private PublishSubject<MessagesReceivedEvent> messagesSubject;

    /* ... */

    public ChatConnectionController() {
        /* ... */

        ChatLogs.get().subscribeToMessages(this, messagesSubject); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    public final void handleChannelStateChange(int i, ChannelState channelState, ErrorCode errorCode) {
        if (i == 1) {
            /* ... */

            Core.get().onConnectingToChannel(i); // TODO: __INJECT_CODE
        }
        if (i == 2) {
            /* ... */

            Core.get().onConnectedToChannel(i); // TODO: __INJECT_CODE
        }

        throw new VirtualImpl();
    }

    /* ... */
}