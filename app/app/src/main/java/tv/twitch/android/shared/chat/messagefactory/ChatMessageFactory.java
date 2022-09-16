package tv.twitch.android.shared.chat.messagefactory;

import android.text.Spanned;
import android.text.SpannedString;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import io.reactivex.subjects.PublishSubject;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.EventDispatcher;
import tv.twitch.android.models.emotes.EmoteModel;
import tv.twitch.android.models.webview.WebViewSource;
import tv.twitch.android.provider.chat.ChatAdapterItem;
import tv.twitch.android.provider.chat.ChatMessageInterface;
import tv.twitch.android.shared.chat.adapter.item.ChatMessageClickedEvents;
import tv.twitch.android.shared.chat.adapter.item.MessageRecyclerItem;
import tv.twitch.android.shared.chat.chatsource.IClickableUsernameSpanListener;
import tv.twitch.android.shared.chat.util.ChatItemClickEvent;
import tv.twitch.android.shared.preferences.chatfilters.ChatFiltersSettings;
import tv.twitch.android.shared.ui.elements.span.TwitchUrlSpanClickListener;
import tv.twitch.chat.ChatMessageInfo;

public class ChatMessageFactory {
    private final ChatHookProvider chatHookProvider = ChatHookProvider.get(); // TODO: __INJECT_FIELD

    /* ... */

    public enum TextStyle {
        CHAT_MESSAGE,
        ACTION,
        SYSTEM_MESSAGE,
        HIGHLIGHTED_MESSAGE
    }

    /* ... */

    private final ChatAdapterItem createUserNoticeRecyclerItem(ChatMessageInfo chatMessageInfo, int i, int i2, IClickableUsernameSpanListener iClickableUsernameSpanListener, String str, Integer num, Spanned spanned, EventDispatcher<ChatItemClickEvent> eventDispatcher, Set<EmoteModel.WithOwner> set) {
        throw new VirtualImpl();
    }

    public ChatAdapterItem createChatHistoryMessage(ChatMessageInfo message, int channelId) { // TODO: __INJECT_METHOD
        Spanned header = null;
        if (!message.userMode.system) {
            header = new SpannedString(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date((long) message.timestamp * 1000)));
        }
        return createUserNoticeRecyclerItem(message, message.nameColorARGB, channelId, null, null, null, header, null, Collections.emptySet());
    }

    public final ChatAdapterItem createChatMessageItem(ChatMessageInterface chatMessageInfo, boolean z, boolean z2, int i, int i2, IClickableUsernameSpanListener iClickableUsernameSpanListener, TwitchUrlSpanClickListener twitchUrlSpanClickListener, WebViewSource webViewSource, String str, boolean z3, ChatFiltersSettings chatFiltersSettings, EventDispatcher<ChatItemClickEvent> chatItemClickEventDispatcher, Set<EmoteModel.WithOwner> followerEmotes, PublishSubject<ChatMessageClickedEvents> publishSubject) {
        /* ... */

        MessageRecyclerItem ret = new MessageRecyclerItem();

        /* ... */

        chatHookProvider.setShouldHighlightBackground(ret, chatMessageInfo); // TODO: __INJECT_CODE
        ChatHookProvider.fixDeletedMessage(ret, chatMessageInfo);

        throw new VirtualImpl();
    }
}