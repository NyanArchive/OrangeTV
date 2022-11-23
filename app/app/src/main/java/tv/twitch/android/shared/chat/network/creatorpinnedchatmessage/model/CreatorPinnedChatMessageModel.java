package tv.twitch.android.shared.chat.network.creatorpinnedchatmessage.model;

import tv.orange.features.chat.ChatHookProvider;

public class CreatorPinnedChatMessageModel {
    private CreatorPinnedChatMessageMessageModel pinnedMessage;
    private CreatorPinnedChatMessageChannelModel channel;
    private CreatorPinnedChatMessagePinnedByUserModel pinnedByUser;

    /* ... */

    public final CreatorPinnedChatMessageMessageModel getPinnedMessage() { // TODO: __REPLACE_METHOD
        return ChatHookProvider.get().hookPinnedMessage(this.pinnedMessage, this.channel, this.pinnedByUser);
    }

    /* ... */
}
