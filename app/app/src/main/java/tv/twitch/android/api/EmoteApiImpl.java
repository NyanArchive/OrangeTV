package tv.twitch.android.api;

import io.reactivex.Single;
import tv.orange.features.chat.ChatHookProvider;
import tv.twitch.android.models.emotes.EmoteCardModelResponse;

public class EmoteApiImpl {
    public Single<EmoteCardModelResponse> getEmoteCardModelResponse(String emoteId) {
        EmoteCardModelResponse response = ChatHookProvider.get().hookEmoteCardModelResponse(emoteId); // TODO: __INJECT_CODE
        if (response != null) {
            return Single.just(response);
        }

        return null;
    }
}
