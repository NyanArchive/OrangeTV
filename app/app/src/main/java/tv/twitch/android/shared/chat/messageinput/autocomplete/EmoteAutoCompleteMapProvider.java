package tv.twitch.android.shared.chat.messageinput.autocomplete;

import io.reactivex.disposables.CompositeDisposable;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.shared.emotes.emotepicker.EmoteFetcher;
import tv.twitch.android.util.RxHelperKt;

public class EmoteAutoCompleteMapProvider {
    private final CompositeDisposable compositeDisposable = null;

    /* ... */

    public EmoteAutoCompleteMapProvider(EmoteFetcher emoteFetcher) {
        /* ... */

        RxHelperKt.plusAssign(compositeDisposable, RxHelperKt.safeSubscribe(ChatHookProvider.get().hookAutoCompleteMapProvider(emoteFetcher.getAllUserEmotesObservable()), null)); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
