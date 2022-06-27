package tv.twitch.android.shared.chat;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import tv.oranges.features.chathistory.bridge.ILiveChatSource;
import tv.twitch.android.shared.chat.chatsource.IClickableUsernameSpanListener;
import tv.twitch.android.shared.chat.parser.ExtensionMessageKt;

public class LiveChatSource implements ILiveChatSource { // TODO: __IMPLEMENT
    private CompositeDisposable disposables;

    public final void addExtensionChatMessage(int p0, ExtensionMessageKt p1, IClickableUsernameSpanListener p2) {
        /* ... */
    }

    @Override
    public void addDisposable(@NonNull Disposable disposable) { // TODO: __INJECT_METHOD
        disposables.add(disposable);
    }

    @Override
    public void addChatHistoryMessage(int channelId, @NonNull ExtensionMessageKt line) { // TODO: __INJECT_METHOD
        addExtensionChatMessage(channelId, line, null);
    }

    @Override
    public void addChatHistoryMessages(int channelId, @NonNull List<ExtensionMessageKt> messages) { // TODO: __INJECT_METHOD
        for (ExtensionMessageKt message : messages) {
            addChatHistoryMessage(channelId, message);
        }
    }
}
