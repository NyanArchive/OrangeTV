package tv.twitch.android.shared.emotes.emotepicker;

import java.util.List;

import io.reactivex.Flowable;
import kotlin.Pair;
import tv.orange.features.chat.ChatHookProvider;
import tv.twitch.android.core.mvp.presenter.PresenterState;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState;
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiSet;

public class EmotePickerPresenter {
    /* ... */

    public static final class EmotePickerState implements PresenterState, ViewDelegateState {
        /* ... */
    }

    /* ... */

    private Flowable<Pair<EmoteUiSet, List<EmoteUiSet>>> createAllEmoteSetsFlowable(final Integer num, final EmotePickerSource emotePickerSource) {
        /* ... */

        Flowable<Pair<EmoteUiSet, List<EmoteUiSet>>> map = null;

        /* ... */

        map = ChatHookProvider.get().hookEmoteSetsFlowable(map, num); // TODO: __INJECT_CODE

        return map;
    }

    /* ... */
}
