package tv.twitch.android.shared.chat.observables;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import tv.orange.core.Core;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.logs.ChatLogs;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.ErrorCode;
import tv.twitch.android.core.mvp.presenter.BasePresenter;
import tv.twitch.android.models.channel.ChannelInfo;
import tv.twitch.android.sdk.ChannelState;
import tv.twitch.android.shared.chat.pub.events.MessagesReceivedEvent;

public class ChatConnectionController extends BasePresenter {
    private PublishSubject<MessagesReceivedEvent> messagesSubject;
    private int viewerId;
    private final ChatHookProvider chp = ChatHookProvider.get(); // TODO: __INJECT_FIELD

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

    public final void setViewerId(int id) { // TODO: __INJECT_METHOD
        viewerId = id;
    }

    private final void connect(int i) {
        throw new VirtualImpl();
    }

    public final void disconnect(ChannelInfo channelInfo) {
        throw new VirtualImpl();
    }

    public Flowable<MessagesReceivedEvent> observeMessagesReceived() {
        Observable<MessagesReceivedEvent> flowable = null;

        /* ... */

        flowable = chp.filterMessages(flowable); // TODO: __INJECT_CODE

        return flowable.toFlowable(BackpressureStrategy.BUFFER);
    }

    /* ... */
}