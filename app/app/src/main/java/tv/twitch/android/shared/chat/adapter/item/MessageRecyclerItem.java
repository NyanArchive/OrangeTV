package tv.twitch.android.shared.chat.adapter.item;

import android.content.Context;
import android.text.Spanned;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.subjects.PublishSubject;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.chat.bridge.IMessageRecyclerItem;
import tv.orange.features.chat.bridge.VHBinder;
import tv.orange.features.pronouns.PronounSetter;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.adapters.AbstractTwitchRecyclerViewHolder;
import tv.twitch.android.core.adapters.RecyclerAdapterItem;
import tv.twitch.android.core.mvp.viewdelegate.EventDispatcher;
import tv.twitch.android.shared.chat.adapter.SystemMessageType;
import tv.twitch.android.shared.chat.util.ChatItemClickEvent;

public class MessageRecyclerItem implements IMessageRecyclerItem { // TODO: __IMPLEMENT
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
    private Integer highlightColor; // TODO: __INJECT_FIELD

    /* ... */

    public MessageRecyclerItem(Context context, String str, int i, String str2, String str3, int i2,
                               Spanned message, SystemMessageType systemMessageType, float f, int i3,
                               float f2, boolean z, boolean z2, List<String> mentionedUsers,
                               String str4, EventDispatcher<ChatItemClickEvent> eventDispatcher,
                               PublishSubject<ChatMessageClickedEvents> publishSubject, boolean z3) {
        message = ChatHookProvider.maybeAddTimestamp(message, i, i2); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public MessageRecyclerItem() {
        throw new VirtualImpl();
    }

    public void markAsDeleted() {
        Spanned createDeletedSpanFromChatMessageSpan = ChatHookProvider.hook(
                messageId,
                message,
                context,
                messageClickEventDispatcher,
                hasModAccess
        ); // TODO: __INJECT_CODE
        if (createDeletedSpanFromChatMessageSpan != null) {
            this.message = createDeletedSpanFromChatMessageSpan;
        }

        /* ... */

        throw new VirtualImpl();
    }

    public final boolean getHasBeenDeleted() {
        return this.hasBeenDeleted;
    }

    @Override
    public void setHighlightColor(@Nullable Integer highlightColor) { // TODO: __INJECT_METHOD
        this.highlightColor = highlightColor;
    }

    public void bindToViewHolder(RecyclerView.ViewHolder viewHolder) {
        /* ... */

        throw new VirtualImpl();
    }

    @Nullable
    @Override
    public Integer getHighlightColor() { // TODO: __INJECT_METHOD
        return highlightColor;
    }

    public static final class ChatMessageViewHolder extends AbstractTwitchRecyclerViewHolder {
        private PronounSetter pronounSetter = null;

        /* ... */

        public ChatMessageViewHolder(View view) {
            super(view);

            throw new VirtualImpl();
        }

        @Override
        public void onBindDataItem(RecyclerAdapterItem item) { // TODO: __INJECT_METHOD
            super.onBindDataItem(item);

            if (pronounSetter != null) {
                pronounSetter.destroy();
            }
            pronounSetter = VHBinder.INSTANCE.bindPronoun(this, item);
        }

        @Override
        public void onRecycled() {
            super.onRecycled();

            /* ... */

            if (pronounSetter != null) { // TODO: __INJECT_CODE
                pronounSetter.destroy();
            }
            pronounSetter = null;

            throw new VirtualImpl();
        }

        /* ... */
    }

    /* ... */
}
