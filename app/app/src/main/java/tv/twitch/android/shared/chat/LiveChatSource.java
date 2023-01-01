package tv.twitch.android.shared.chat;

import androidx.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.chathistory.bridge.ILiveChatSource;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.chat.ChatModNoticeEvents;
import tv.twitch.android.shared.chat.chatsource.IClickableUsernameSpanListener;
import tv.twitch.android.shared.chat.events.ChatChannelUpdateEvents;
import tv.twitch.android.shared.chat.messagefactory.MessageListAdapterBinder;
import tv.twitch.android.shared.chat.parser.ExtensionMessageKt;
import tv.twitch.chat.ChatMessageInfo;

public class LiveChatSource implements ILiveChatSource { // TODO: __IMPLEMENT
    private MessageListAdapterBinder messageListAdapterBinder;
    private CompositeDisposable disposables;

    /* ... */

    public final void addExtensionChatMessage(int p0, ExtensionMessageKt p1, IClickableUsernameSpanListener p2) {
        /* ... */

        throw new VirtualImpl();
    }

    /* ... */

    @Override
    public void addDisposable(@NonNull Disposable disposable) { // TODO: __INJECT_METHOD
        disposables.add(disposable);
    }

    @Override
    public void addChatHistoryMessage(@NonNull ChatMessageInfo message, int channelId) { // TODO: __INJECT_METHOD
        messageListAdapterBinder.addChatHistoryMessage(message, channelId);
    }

    private final void onModNoticeReceived(ChatModNoticeEvents var1) {
        if (var1 instanceof ChatModNoticeEvents.MessageDeletedEvent) {
            /* ... */
        } else if (var1 instanceof ChatModNoticeEvents.UserMessageDeletedEvent) {
            /* ... */
        } else {
            boolean var3 = var1 instanceof ChatModNoticeEvents.UserBannedEvent;
            if (var3) {
            } else if (var1 instanceof ChatModNoticeEvents.UserUnbannedEvent) {
            } else {
                if (var1 instanceof ChatModNoticeEvents.ChatClearedEvent) {
                    if (!ChatHookProvider.getPreventModClear()) { // TODO: __INJECT_CODE
                        throw new VirtualImpl();
                    }
                } else if (var1 instanceof ChatModNoticeEvents.MessageOrCheerApprovedEvent) {
                } else if (var1 instanceof ChatModNoticeEvents.MessageOrCheerCaughtEvent) {
                } else if (var1 instanceof ChatModNoticeEvents.MessageDeniedEvent) {
                } else if (var1 instanceof ChatModNoticeEvents.SentCheerDeniedEvent) {
                } else {
                }
            }
        }

        /* ... */

        throw new VirtualImpl();
    }

    private final void onChannelUpdateReceived(ChatChannelUpdateEvents var1) {
        if (var1 instanceof ChatChannelUpdateEvents.MessagesClearedEvent) {
            if (true) {
                /* ... */
                if (!ChatHookProvider.getPreventModClear()) { // TODO: __INJECT_CODE
                    throw new VirtualImpl();
                }
                /* ... */
            }
        } else if (var1 instanceof ChatChannelUpdateEvents.UserMessagesClearedEvent) {
            /* ... */
        } else if (var1 instanceof ChatChannelUpdateEvents.MessageClearedEvent) {
            /* ... */
        } else {
            /* ... */
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
