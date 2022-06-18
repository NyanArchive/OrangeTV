package tv.twitch.android.shared.chat.messagefactory;

import java.util.List;
import java.util.Set;

import tv.orange.features.emotes.Hook;
import tv.twitch.android.core.mvp.viewdelegate.EventDispatcher;
import tv.twitch.android.models.emotes.EmoteModel;
import tv.twitch.android.models.webview.WebViewSource;
import tv.twitch.android.provider.chat.ChatMessageInterface;
import tv.twitch.android.shared.chat.chatsource.IClickableUsernameSpanListener;
import tv.twitch.android.shared.preferences.chatfilters.ChatFiltersSettings;
import tv.twitch.android.shared.ui.elements.span.TwitchUrlSpanClickListener;

public class ChatMessageFactory {
    private final ChatMessageSpanGroup createChatMessageSpanGroup(ChatMessageInterface chatMessageInterface, boolean z, boolean z2, int userId, int channelId, IClickableUsernameSpanListener iClickableUsernameSpanListener, TwitchUrlSpanClickListener twitchUrlSpanClickListener, WebViewSource webViewSource, String str, boolean z3, ChatFiltersSettings chatFiltersSettings, Integer num, EventDispatcher eventDispatcher, List<String> list, Set<EmoteModel.WithOwner> set) {
        chatMessageInterface = Hook.Companion.getInstance().injectEmotes(chatMessageInterface, channelId);

        return null;
    }
}
