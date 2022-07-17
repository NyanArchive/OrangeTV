package tv.twitch.android.shared.chat.messageinput;

import tv.orange.features.chat.ChatHookProvider;

public class MessageInputViewDelegate {
    public final void render() {
        boolean z2 = false;
        z2 = ChatHookProvider.changeBitsButtonVisibility(z2);  // TODO: __HOOK_PARAM
        /* ... */
    }
}
