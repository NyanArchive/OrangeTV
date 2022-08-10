package tv.twitch.android.shared.chat.messagefactory;

import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import tv.orange.features.badges.bridge.OrangeMessageBadge;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.chat.bridge.BackgroundUrlDrawable;
import tv.orange.features.emotes.bridge.EmoteToken;
import tv.orange.models.exception.VirtualImpl;
import tv.orange.models.abc.EmoteCardModelWrapper;
import tv.twitch.android.core.mvp.viewdelegate.EventDispatcher;
import tv.twitch.android.models.chat.MessageBadge;
import tv.twitch.android.models.chat.MessageToken;
import tv.twitch.android.models.emotes.EmoteModel;
import tv.twitch.android.models.webview.WebViewSource;
import tv.twitch.android.provider.chat.ChatAdapterItem;
import tv.twitch.android.provider.chat.ChatMessageInterface;
import tv.twitch.android.shared.chat.UrlImageClickableProvider;
import tv.twitch.android.shared.chat.adapter.item.MessageRecyclerItem;
import tv.twitch.android.shared.chat.chatsource.IClickableUsernameSpanListener;
import tv.twitch.android.shared.chat.util.ChatItemClickEvent;
import tv.twitch.android.shared.chat.util.ClickableEmoteSpan;
import tv.twitch.android.shared.preferences.chatfilters.ChatFiltersSettings;
import tv.twitch.android.shared.ui.elements.span.MediaSpan$Type;
import tv.twitch.android.shared.ui.elements.span.TwitchUrlSpanClickListener;
import tv.twitch.android.shared.ui.elements.span.UrlDrawable;
import tv.twitch.chat.ChatMessageInfo;

public class ChatMessageFactory {
    private ContextWrapper context;

    /* ... */

    public enum TextStyle {
        CHAT_MESSAGE,
        ACTION,
        SYSTEM_MESSAGE,
        HIGHLIGHTED_MESSAGE
    }

    /* ... */

    private final ChatAdapterItem createUserNoticeRecyclerItem(ChatMessageInfo chatMessageInfo, int userId, int channelId, IClickableUsernameSpanListener iClickableUsernameSpanListener, String dismissUrl, Integer num, Spanned spanned, EventDispatcher<ChatItemClickEvent> eventDispatcher, Set<EmoteModel.WithOwner> followerEmotes) {
        throw new VirtualImpl();
    }

    private final ChatMessageSpanGroup createChatMessageSpanGroup(ChatMessageInterface chatMessageInterface, boolean z, boolean z2, int userId, int channelId, IClickableUsernameSpanListener iClickableUsernameSpanListener, TwitchUrlSpanClickListener twitchUrlSpanClickListener, WebViewSource webViewSource, String str, boolean z3, ChatFiltersSettings chatFiltersSettings, Integer num, EventDispatcher eventDispatcher, List<String> list, Set<EmoteModel.WithOwner> set) {
        chatMessageInterface = ChatHookProvider.get().hookMessageInterface(chatMessageInterface, channelId); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    private final CharSequence imageSpannable(Drawable drawable, String str, String str2, UrlImageClickableProvider urlImageClickableProvider, boolean z) {
        throw new VirtualImpl();
    }

    private final CharSequence badgeSpannable(MessageBadge messageBadge, int i) {
        if (messageBadge instanceof OrangeMessageBadge) { // TODO: __INJECT_CODE
            return badgeSpannable((OrangeMessageBadge) messageBadge);
        }

        /* ... */

        throw new VirtualImpl();
    }

    public final SpannedString parseChatMessageTokens(ChatMessageInterface chatMessageInterface, boolean z, boolean z2, WebViewSource webViewSource, TwitchUrlSpanClickListener twitchUrlSpanClickListener, String str, TextStyle textStyle, int i, ChatFiltersSettings chatFiltersSettings, Integer num, EventDispatcher<ChatItemClickEvent> eventDispatcher, List<String> list, Set<EmoteModel.WithOwner> followerEmotes, String str2) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        MessageToken token = null;
        if (token instanceof EmoteToken) { // TODO: __INJECT_CODE
            spannableStringBuilder.append(emoteSpannable((EmoteToken) token, chatMessageInterface, eventDispatcher));
        }

        throw new VirtualImpl();
    }

    /* ... */

    private CharSequence emoteSpannable(EmoteToken token, ChatMessageInterface cmi, EventDispatcher<ChatItemClickEvent> eventDispatcher) { // TODO: __INJECT_METHOD
        Drawable drawable = new UrlDrawable(token.getEmoteUrl(), MediaSpan$Type.Emote, true);
        SpannableString spannable = new SpannableString(imageSpannable(drawable, token.getEmoteCode(), "", null, true));
        if (eventDispatcher != null) {
            spannable.setSpan(new ClickableEmoteSpan(new EmoteCardModelWrapper(token.getEmoteCode(), token.getEmoteCardUrl(), token.getPackageSet()).toJsonString(), cmi, eventDispatcher), 0, spannable.length() - 1, 33);
        }

        return spannable;
    }

    private CharSequence badgeSpannable(OrangeMessageBadge messageBadge) { // TODO: __INJECT_METHOD
        Drawable drawable = new BackgroundUrlDrawable(messageBadge.getBadgeUrl(), MediaSpan$Type.Badge, messageBadge.getBadgeColor());
        return imageSpannable(drawable, messageBadge.getBadgeName(), "", null, true);
    }

    public ChatAdapterItem createChatHistoryMessage(ChatMessageInfo message, int channelId) { // TODO: __INJECT_METHOD
        Spanned header = null;
        if (!message.userMode.system) {
            header = new SpannedString(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date((long) message.timestamp * 1000)));
        }
        return createUserNoticeRecyclerItem(message, message.nameColorARGB, channelId, null, null, null, header, null, Collections.emptySet());
    }


    public final ChatAdapterItem createChatMessageItem(ChatMessageInterface chatMessageInterface, boolean z, boolean z2, int i, int i2, IClickableUsernameSpanListener iClickableUsernameSpanListener, TwitchUrlSpanClickListener twitchUrlSpanClickListener, WebViewSource webViewSource, String str, boolean z3, ChatFiltersSettings chatFiltersSettings, EventDispatcher<ChatItemClickEvent> eventDispatcher, Set<EmoteModel.WithOwner> set) {
        /* ... */

        MessageRecyclerItem ret = new MessageRecyclerItem();

        /* ... */

        ChatHookProvider.get().setShouldHighlightBackground(this, ret, chatMessageInterface); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }
}