package tv.twitch.android.api;

import io.reactivex.Single;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.models.emotes.EmoteCardModelResponse;

public class EmoteApiImpl {
    /* ... */

    public Single<EmoteCardModelResponse> getEmoteCardModelResponse(String emoteId) {
        EmoteCardModelResponse response = ChatHookProvider.get().hookEmoteCardModelResponse(emoteId); // TODO: __INJECT_CODE
        if (response != null) { // TODO: __INJECT_CODE
            return Single.just(response);
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
