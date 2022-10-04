package tv.twitch.android.shared.emotes.emotepicker;

import tv.orange.features.chat.ChatHookProvider;
import tv.orange.models.exception.VirtualImpl;

public class EmotePickerPresenter$directSubscribeForEmoteClickedEvents$1 {
    /* ... */

    public final void invoke(EmotePickerPresenter.ClickEvent clickEvent) {
        if (ChatHookProvider.get().hookEmotePickerPresenterLongEmoteClick(clickEvent)) { // TODO: __INJECT_CODE
            return;
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}