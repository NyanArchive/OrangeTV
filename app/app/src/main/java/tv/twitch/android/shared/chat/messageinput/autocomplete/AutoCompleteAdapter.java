package tv.twitch.android.shared.chat.messageinput.autocomplete;

import android.view.View;
import android.view.ViewGroup;

import tv.orange.features.chat.bridge.OrangeEmoteModel;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.emotes.EmoteModel;

public class AutoCompleteAdapter {
    /* ... */

    public View getView(int i, View view, ViewGroup viewGroup) {
        EmoteModel model = null;

        if (model instanceof OrangeEmoteModel) { // TODO: __INJECT_CODE
            String url = ((OrangeEmoteModel) model).getEmoteUrl();
            url.toString();
        }

        throw new VirtualImpl();
    }

    /* ... */
}
