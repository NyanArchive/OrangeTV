package tv.twitch.android.shared.chat.viewerlist;

import android.text.TextUtils;

import java.util.List;

import tv.orange.features.usersearch.UserSearch;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.chat.Chatters;

public final class ViewerListAdapterBinder {
    private Chatters orgChatters; // TODO: __INJECT_FIELD
    private String searchUserText; // TODO: __INJECT_FIELD

    /* ... */

    public final void setViewers(Chatters chatters) { // TODO: __REPLACE_METHOD
        orgChatters = chatters;
        filterChatters();
    }

    private void filterChatters() { // TODO: __INJECT_METHOD
        if (orgChatters == null) {
            return;
        }

        Chatters chatters = orgChatters;
        if (!TextUtils.isEmpty(searchUserText)) {
            chatters = UserSearch.get().filterChatters(orgChatters, searchUserText);
        }

        clear();
        addMoreViewers(chatters);
    }

    public final void setSearchUserText(String text) { // TODO: __INJECT_METHOD
        searchUserText = text;
        filterChatters();
    }

    /* ... */

    private void addMoreViewers(Chatters chatters) {
        /* ... */

        throw new VirtualImpl();
    }

    private void addItems(String str, List<String> list) {
        /* ... */

        throw new VirtualImpl();
    }

    public final void clear() {
        /* ... */

        throw new VirtualImpl();
    }
}
