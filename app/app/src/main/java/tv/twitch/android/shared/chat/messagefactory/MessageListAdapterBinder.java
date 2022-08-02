package tv.twitch.android.shared.chat.messagefactory;

import tv.twitch.android.shared.chat.adapter.ChatAdapter;
import tv.twitch.chat.ChatMessageInfo;

public class MessageListAdapterBinder {
    private ChatAdapter adapter;
    private ChatMessageFactory messageFactory;

    /* ... */

    public void addChatHistoryMessage(ChatMessageInfo messageInfo, int channelId) { // TODO: __INJECT_METHOD
        this.adapter.addMessage(this.messageFactory.createChatHistoryMessage(messageInfo, channelId));
    }
}
