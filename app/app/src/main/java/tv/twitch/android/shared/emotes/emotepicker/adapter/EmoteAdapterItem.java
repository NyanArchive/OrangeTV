package tv.twitch.android.shared.emotes.emotepicker.adapter;

import tv.orange.features.chat.bridge.EmoteUiModelExt;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiModel;

public class EmoteAdapterItem {
    /* ... */

    public void bindToViewHolder(Object viewHolder) {
        /* ... */

        // TODO: __HOOK_URL
        EmoteUiModel model = null;
        if (model instanceof EmoteUiModelExt.EmoteUiModelWithUrl) {
            String url = ((EmoteUiModelExt.EmoteUiModelWithUrl) model).getUrl(); // TODO: __INJECT_CODE
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}