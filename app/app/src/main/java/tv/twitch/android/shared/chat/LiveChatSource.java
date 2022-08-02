package tv.twitch.android.shared.chat;

import androidx.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import tv.orange.models.VirtualImpl;
import tv.oranges.features.chathistory.bridge.ILiveChatSource;
import tv.twitch.android.shared.chat.chatsource.IClickableUsernameSpanListener;
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

    /* ... */
}
