package tv.oranges.features.chathistory.data.repository

import android.graphics.Color
import io.reactivex.Single
import tv.oranges.features.chathistory.data.model.ChatHistoryMessage
import tv.oranges.features.chathistory.data.model.ChatMessageInterfaceSystemMessage
import tv.oranges.features.chathistory.data.source.TwitchApolloSource
import tv.twitch.android.models.extensions.ExtensionModel
import tv.twitch.android.shared.chat.parser.ExtensionMessageKt
import javax.inject.Inject

class ChatHistoryRepository @Inject constructor(val twitchApolloSource: TwitchApolloSource) {
    fun getMessages(channelName: String): Single<List<ChatHistoryMessage>> {
        return twitchApolloSource.getMessages(channelName)
    }

    fun getSystemMessage(text: String): ExtensionMessageKt {
        return ExtensionMessageKt(
            ChatMessageInterfaceSystemMessage(text),
            ExtensionModel("", "", "", null),
            Color.GRAY
        )
    }
}