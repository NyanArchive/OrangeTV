package tv.twitch.android.shared.chat.messagefactory;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tv.orange.features.badges.bridge.OrangeMessageBadge;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.chat.bridge.BackgroundUrlDrawable;
import tv.orange.features.chat.bridge.StackEmoteToken;
import tv.orange.features.emotes.bridge.EmoteToken;
import tv.orange.models.abc.EmoteCardModelWrapper;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.EventDispatcher;
import tv.twitch.android.models.chat.MessageBadge;
import tv.twitch.android.models.chat.MessageToken;
import tv.twitch.android.models.emotes.EmoteModel;
import tv.twitch.android.models.webview.WebViewSource;
import tv.twitch.android.provider.chat.ChatMessageInterface;
import tv.twitch.android.shared.bits.cheermote.CheermotesHelper;
import tv.twitch.android.shared.chat.UrlImageClickableProvider;
import tv.twitch.android.shared.chat.chatsource.IClickableUsernameSpanListener;
import tv.twitch.android.shared.chat.util.ChatItemClickEvent;
import tv.twitch.android.shared.chat.util.ClickableEmoteSpan;
import tv.twitch.android.shared.emotes.utils.AnimatedEmotesUrlUtil;
import tv.twitch.android.shared.preferences.chatfilters.ChatFiltersSettings;
import tv.twitch.android.shared.ui.elements.span.CenteredImageSpan;
import tv.twitch.android.shared.ui.elements.span.MediaSpan$Type;
import tv.twitch.android.shared.ui.elements.span.TwitchUrlSpanClickListener;
import tv.twitch.android.shared.ui.elements.span.UrlDrawable;

public class ChatMessageSpanFactory {
    private AnimatedEmotesUrlUtil animatedEmotesUrlUtil;

    /* ... */

    private final CharSequence imageSpannable(Drawable drawable, String str, String str2, UrlImageClickableProvider urlImageClickableProvider, boolean z) {
        throw new VirtualImpl();
    }

    private final CharSequence usernameSpannable(ChatMessageInterface chatMessageInterface, int color, IClickableUsernameSpanListener iClickableUsernameSpanListener, boolean z, String str, String str2) {
        color = ChatHookProvider.fixUsernameSpanColor(color); // TODO: __HOOK_PARAM

        /* ... */

        throw new VirtualImpl();
    }

    public final ChatMessageSpanGroup createChatMessageSpanGroup(ChatMessageInterface chatMessageInfo, boolean z, boolean z2, int i, int i2, IClickableUsernameSpanListener iClickableUsernameSpanListener, TwitchUrlSpanClickListener twitchUrlSpanClickListener, WebViewSource webViewSource, String str, boolean z3, ChatFiltersSettings chatFiltersSettings, Integer num, EventDispatcher<ChatItemClickEvent> eventDispatcher, List<String> list, Set<EmoteModel.WithOwner> followerEmotes, CheermotesHelper cheermotesHelper) {
        chatMessageInfo = ChatHookProvider.get().hookMessageInterface(chatMessageInfo, i2); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    private final CharSequence badgeSpannable(MessageBadge messageBadge, int i) {
        if (messageBadge instanceof OrangeMessageBadge) { // TODO: __INJECT_CODE
            return badgeSpannable((OrangeMessageBadge) messageBadge);
        }

        /* ... */

        throw new VirtualImpl();
    }

    public final SpannedString parseChatMessageTokens(ChatMessageInterface chatMessageInterface, boolean z, boolean z2, WebViewSource webViewSource, TwitchUrlSpanClickListener twitchUrlSpanClickListener, String str, ChatMessageFactory.TextStyle textStyle, int i, ChatFiltersSettings chatFiltersSettings, Integer num, EventDispatcher<ChatItemClickEvent> eventDispatcher, List<String> list, Set<EmoteModel.WithOwner> followerEmotes, String str2, CheermotesHelper cheermotesHelper) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        MessageToken token = null;
        if (token instanceof EmoteToken) { // TODO: __INJECT_CODE
            spannableStringBuilder.append(emoteSpannable((EmoteToken) token, chatMessageInterface, eventDispatcher));
        } else if (token instanceof StackEmoteToken) {
            spannableStringBuilder.append(stackEmoteSpannable((StackEmoteToken) token, chatMessageInterface, num, eventDispatcher));
        }

        /* ... */

        SpannedString ret = new SpannedString(spannableStringBuilder);
        return ChatHookProvider.fixDeletedMessage(ret, chatMessageInterface); // TODO: __INJECT_CODE
    }

    private final CharSequence emoteSpannable(MessageToken.EmoticonToken emoticonToken, ChatMessageInterface chatMessageInterface, Integer num, EventDispatcher<ChatItemClickEvent> eventDispatcher) {
        throw new VirtualImpl();
    }

    private CharSequence stackEmoteSpannable(StackEmoteToken stackEmoteToken, ChatMessageInterface chatMessageInterface, Integer num, EventDispatcher<ChatItemClickEvent> eventDispatcher) {
        List<UrlDrawable> stack = new ArrayList<>();
        for (EmoteToken t : stackEmoteToken.getStack()) {
            stack.add(new UrlDrawable(t.getEmoteUrl(), MediaSpan$Type.Emote, true));
        }

        if (stackEmoteToken.getCore().isTwitchToken()) {
            SpannableString spannable = (SpannableString) emoteSpannable(stackEmoteToken.getCore().getTwitchToken(), chatMessageInterface, num, eventDispatcher);
            CenteredImageSpan[] spans = spannable.getSpans(0, spannable.length(), CenteredImageSpan.class);
            CenteredImageSpan span = spans[0];
            UrlDrawable drawable = (UrlDrawable) span.getImageDrawable();
            drawable.addToStack(stack);

            return spannable;
        } else {
            EmoteToken token = stackEmoteToken.getCore().getOrangeToken();
            UrlDrawable drawable = new UrlDrawable(token.getEmoteUrl(), MediaSpan$Type.Emote, true);
            drawable.addToStack(stack);

            SpannableString spannable = new SpannableString(imageSpannable(drawable, token.getEmoteCode(), "", null, true));
            if (eventDispatcher != null) {
                spannable.setSpan(new ClickableEmoteSpan(new EmoteCardModelWrapper(token.getEmoteCode(), token.getEmoteCardUrl(), token.getPackageSet()).toJsonString(), chatMessageInterface, eventDispatcher), 0, spannable.length() - 1, 33);
            }

            return spannable;
        }
    }

    private CharSequence badgeSpannable(OrangeMessageBadge messageBadge) { // TODO: __INJECT_METHOD
        Drawable drawable = new BackgroundUrlDrawable(messageBadge.getBadgeUrl(), MediaSpan$Type.Badge, messageBadge.getBadgeBackgroundColor());
        return imageSpannable(drawable, messageBadge.getBadgeName(), "", null, true);
    }

    private CharSequence emoteSpannable(EmoteToken token, ChatMessageInterface cmi, EventDispatcher<ChatItemClickEvent> eventDispatcher) { // TODO: __INJECT_METHOD
        Drawable drawable = new UrlDrawable(token.getEmoteUrl(), MediaSpan$Type.Emote, true);
        SpannableString spannable = new SpannableString(imageSpannable(drawable, token.getEmoteCode(), "", null, true));
        if (eventDispatcher != null) {
            spannable.setSpan(new ClickableEmoteSpan(new EmoteCardModelWrapper(token.getEmoteCode(), token.getEmoteCardUrl(), token.getPackageSet()).toJsonString(), cmi, eventDispatcher), 0, spannable.length() - 1, 33);
        }

        return spannable;
    }

    /* ... */
}
