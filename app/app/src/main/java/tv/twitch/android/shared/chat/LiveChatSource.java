package tv.twitch.android.shared.chat;

import androidx.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import tv.orange.core.Logger;
import tv.oranges.features.chathistory.bridge.ILiveChatSource;

public class LiveChatSource implements ILiveChatSource { // TODO: __IMPLEMENT
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void addDisposable(@NonNull Disposable disposable) { // TODO: __INJECT_METHOD
        disposables.add(disposable);
    }

    @Override
    public void addChatHistoryMessage(@NonNull String line) { // TODO: __INJECT_METHOD
        Logger.INSTANCE.debug(line);
    }
}
