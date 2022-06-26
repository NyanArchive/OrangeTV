package tv.twitch.android.shared.chat.messagefactory;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;

import java.util.List;
import java.util.Set;

import tv.orange.features.badges.bridge.OrangeMessageBadge;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.chat.bridge.BackgroundUrlDrawable;
import tv.orange.features.emotes.bridge.EmoteToken;
import tv.twitch.android.core.mvp.viewdelegate.EventDispatcher;
import tv.twitch.android.models.chat.MessageBadge;
import tv.twitch.android.models.chat.MessageToken;
import tv.twitch.android.models.emotes.EmoteModel;
import tv.twitch.android.models.webview.WebViewSource;
import tv.twitch.android.provider.chat.ChatMessageInterface;
import tv.twitch.android.shared.chat.UrlImageClickableProvider;
import tv.twitch.android.shared.chat.chatsource.IClickableUsernameSpanListener;
import tv.twitch.android.shared.chat.util.ChatItemClickEvent;
import tv.twitch.android.shared.preferences.chatfilters.ChatFiltersSettings;
import tv.twitch.android.shared.ui.elements.span.MediaSpan$Type;
import tv.twitch.android.shared.ui.elements.span.TwitchUrlSpanClickListener;
import tv.twitch.android.shared.ui.elements.span.UrlDrawable;

public class ChatMessageFactory {
    public enum TextStyle {
        CHAT_MESSAGE,
        ACTION,
        SYSTEM_MESSAGE,
        HIGHLIGHTED_MESSAGE
    }

    private final ChatMessageSpanGroup createChatMessageSpanGroup(ChatMessageInterface chatMessageInterface, boolean z, boolean z2, int userId, int channelId, IClickableUsernameSpanListener iClickableUsernameSpanListener, TwitchUrlSpanClickListener twitchUrlSpanClickListener, WebViewSource webViewSource, String str, boolean z3, ChatFiltersSettings chatFiltersSettings, Integer num, EventDispatcher eventDispatcher, List<String> list, Set<EmoteModel.WithOwner> set) {
        chatMessageInterface = ChatHookProvider.get().hookMessageInterface(chatMessageInterface, channelId); // TODO: __INJECT_CODE

        return null;
    }

    private final CharSequence imageSpannable(Drawable drawable, String str, String str2, UrlImageClickableProvider urlImageClickableProvider, boolean z) {
        return null;
    }

    private final CharSequence badgeSpannable(MessageBadge messageBadge, int i) {
        if (messageBadge instanceof OrangeMessageBadge) { // TODO: __INJECT_CODE
            return badgeSpannable((OrangeMessageBadge) messageBadge);
        }


        /* ... */

        return null;
    }

    public final SpannedString parseChatMessageTokens(ChatMessageInterface chatMessageInterface, boolean z, boolean z2, WebViewSource webViewSource, TwitchUrlSpanClickListener twitchUrlSpanClickListener, String str, TextStyle textStyle, int i, ChatFiltersSettings chatFiltersSettings, Integer num, EventDispatcher<ChatItemClickEvent> eventDispatcher, List<String> list, Set<EmoteModel.WithOwner> followerEmotes, String str2) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        MessageToken token = null;
        if (token instanceof EmoteToken) { // TODO: __INJECT_CODE
            spannableStringBuilder.append(emoteSpannable((EmoteToken) token, eventDispatcher));
        }

        return null;
    }

    private CharSequence emoteSpannable(EmoteToken token, EventDispatcher<ChatItemClickEvent> eventDispatcher) { // TODO: __INJECT_METHOD
        Drawable drawable = new UrlDrawable(token.getEmoteUrl(), MediaSpan$Type.Emote);
        return imageSpannable(drawable, token.getEmoteCode(), "", null, true);
    }

    private CharSequence badgeSpannable(OrangeMessageBadge messageBadge) { // TODO: __INJECT_METHOD
        Drawable drawable = new BackgroundUrlDrawable(messageBadge.getBadgeUrl(), MediaSpan$Type.Badge, messageBadge.getBadgeColor());
        return imageSpannable(drawable, messageBadge.getBadgeName(), "", null, true);
    }
}