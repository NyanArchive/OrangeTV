package tv.twitch.android.shared.chat.chatheader;

import tv.orange.features.chat.ChatHookProvider;

public class ChatHeaderVisibilityCalculator {
    /* ... */

    private final boolean shouldHideChatHeaderBasedOnDefaultRules(boolean p0) { // TODO: __REPLACE_METHOD
        if (ChatHookProvider.hideChatHeader()) {
            return true;
        }

        return p0;
    }

    /* ... */
}
