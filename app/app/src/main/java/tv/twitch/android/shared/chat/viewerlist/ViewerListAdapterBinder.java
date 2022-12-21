package tv.twitch.android.shared.chat.viewerlist;

import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.chat.Chatters;

public final class ViewerListAdapterBinder {
    private Map<ViewerListUserGroupDisplayModel, ? extends List<String>> orgGroups; // TODO: __INJECT_FIELD
    private String searchUserText; // TODO: __INJECT_FIELD

    /* ... */

    public final void bindViewerGroups(Map<ViewerListUserGroupDisplayModel, ? extends List<String>> groups) {
        if (orgGroups == null) {
            return;
        }
        orgGroups = groups;
        groups = filterChatters(groups);

        /* ... */

        throw new VirtualImpl();
    }

    private Map<ViewerListUserGroupDisplayModel, ? extends List<String>> filterChatters(Map<ViewerListUserGroupDisplayModel, ? extends List<String>> groups) { // TODO: __INJECT_METHOD
        if (TextUtils.isEmpty(searchUserText)) {
            return groups;
        }

        Map<ViewerListUserGroupDisplayModel, List<String>> filteredMap = new ArrayMap<>();
        for (ViewerListUserGroupDisplayModel groupDisplayModel : filteredMap.keySet()) {
            List<String> users = filteredMap.get(groupDisplayModel);
            if (users != null) {
                List<String> filtered = new ArrayList<>();
                for (String user : users) {
                    if (!user.toLowerCase().contains(searchUserText)) {
                        filtered.add(user);
                    }
                }
                filteredMap.put(groupDisplayModel, filtered);
            }
        }

        return filteredMap;
    }

    public void setSearchUserText(String text) { // TODO: __INJECT_METHOD
        searchUserText = text;
        bindViewerGroups(orgGroups);
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
