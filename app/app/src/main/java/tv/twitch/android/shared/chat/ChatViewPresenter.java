package tv.twitch.android.shared.chat;

import tv.orange.features.chathistory.ChatHistory;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.channel.ChannelInfo;
import tv.twitch.android.shared.chat.events.ChatConnectionEvents;

public class ChatViewPresenter {
    public LiveChatSource liveChatSource;
    private ChannelInfo channel;

    /* ... */

    public final void onChannelStateChanged(ChatConnectionEvents chatConnectionEvents) {
        ChatHistory.get().requestChatHistory(chatConnectionEvents, liveChatSource, channel); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}