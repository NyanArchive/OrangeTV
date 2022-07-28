package tv.twitch.android.shared.chat.adapter.item;

import android.content.Context;
import android.text.Spanned;

import java.util.List;

import io.reactivex.subjects.PublishSubject;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.EventDispatcher;
import tv.twitch.android.shared.chat.adapter.SystemMessageType;
import tv.twitch.android.shared.chat.util.ChatItemClickEvent;

public class MessageRecyclerItem {
    private int authorUserId;
    private EventDispatcher<ChatItemClickEvent> clickEventDispatcher;
    private Context context;
    private String displayName;
    private boolean hasBeenDeleted;
    private boolean hasModAccess;
    private boolean isChatClearGlideResourcesExperimentEnabled;
    private float lineSpacingMultiplier;
    private List<String> mentionedUsers;
    private Spanned message;
    private PublishSubject<ChatMessageClickedEvents> messageClickEventDispatcher;
    private String messageId;
    private int messageTimestamp;
    private SystemMessageType msgType;
    private int paddingPx;
    private String rawMessage;
    private float textSizePx;
    private String username;

    /* ... */

    public void markAsDeleted() {
        Spanned createDeletedSpanFromChatMessageSpan = ChatHookProvider.hook(messageId,
                message,
                context,
                messageClickEventDispatcher,
                hasModAccess
        ); // TODO: __INJECT_CODE
        if (createDeletedSpanFromChatMessageSpan != null) {
            this.message = createDeletedSpanFromChatMessageSpan;
        }
        this.hasBeenDeleted = true;
        
        throw new VirtualImpl();
    }

    /* ... */
}
