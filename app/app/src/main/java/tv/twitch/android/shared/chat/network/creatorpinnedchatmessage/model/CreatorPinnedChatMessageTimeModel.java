package tv.twitch.android.shared.chat.network.creatorpinnedchatmessage.model;

import tv.orange.core.CoreHook;

public class CreatorPinnedChatMessageTimeModel {
    private Long timeMessagePinnedMS;
    private Long timeMessageSentMS;
    private Long timeMessageUnpinnedMS;

    /* ... */


    public final Long getTimeMessageUnpinnedMS() {
        return this.timeMessageUnpinnedMS;
    }

    public CreatorPinnedChatMessageTimeModel(Long l, long j, Long l2) {
        this.timeMessageSentMS = l;
        this.timeMessagePinnedMS = j;
        this.timeMessageUnpinnedMS = CoreHook.hookUnpinnedMS(l2); // TODO: __REPLACE_CODE
    }

    public final Long getTimeMessageSentMS() {
        return this.timeMessageSentMS;
    }

    public final long getTimeMessagePinnedMS() {
        return this.timeMessagePinnedMS;
    }

    /* ... */
}
