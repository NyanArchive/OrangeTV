package tv.twitch.android.shared.chat;

import tv.orange.features.chathistory.ChatHistory;
import tv.orange.features.logs.bridge.ChatLocalLogsCommand;
import tv.orange.features.logs.bridge.ChatTwitchLogsCommand;
import tv.orange.features.spam.bridge.ChatSpamCommand;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.channel.ChannelInfo;
import tv.twitch.android.shared.chat.command.ChatCommandAction;
import tv.twitch.android.shared.chat.events.ChatConnectionEvents;
import tv.twitch.android.shared.chat.model.ChatSendAction;

public class ChatViewPresenter {
    public LiveChatSource liveChatSource;
    private ChannelInfo channel;
    private ChatViewDelegate chatViewDelegate;

    /* ... */

    public final void onChannelStateChanged(ChatConnectionEvents chatConnectionEvents) {
        ChatHistory.get().requestChatHistory(chatConnectionEvents, liveChatSource, channel); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public final boolean maybeSubmitMessage(String str, ChatSendAction chatSendAction) {
        ChatCommandAction processChatCommand = null;

        /* ... */

        if (processChatCommand instanceof ChatSpamCommand) { // TODO: __INJECT_CODE
            return true;
        }

        if (processChatCommand instanceof ChatLocalLogsCommand) { // TODO: __INJECT_CODE
            return true;
        }

        if (processChatCommand instanceof ChatTwitchLogsCommand) { // TODO: __INJECT_CODE
            return true;
        }

        /* ... */

        throw new VirtualImpl();
    }

    public void onConfigurationChanged() {
        /* ... */

        UI.get().onChatViewPresenterConfigurationChanged(chatViewDelegate); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }


    /* ... */
}