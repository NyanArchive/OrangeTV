package tv.twitch.android.shared.chat.viewerlist;

import android.text.TextUtils;

import java.util.List;

import tv.orange.features.usersearch.Hook;
import tv.twitch.android.models.chat.Chatters;

public final class ViewerListAdapterBinder { // TODO: @features:usersearch
    private Chatters orgChatters; // TODO: __INJECT_FIELD
    private String searchUserText; // TODO: __INJECT_FIELD

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
            chatters = Hook.Companion.getInstance().filterChatters(orgChatters, searchUserText);
        }

        clear();
        addMoreViewers(chatters);
    }

    public final void setSearchUserText(String text) { // TODO: __INJECT_METHOD
        searchUserText = text;
        filterChatters();
    }

    private final void addMoreViewers(Chatters chatters) {/* ... */}

    private final void addItems(String str, List<String> list) {/* ... */}

    public final void clear() {/* ... */}
}
