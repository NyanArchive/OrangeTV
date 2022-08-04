package tv.orange.features.chathistory.data.repository

import io.reactivex.Single
import tv.orange.features.chathistory.data.source.TwitchApolloSource
import tv.twitch.chat.ChatMessageInfo
import tv.twitch.chat.ChatTextToken
import tv.twitch.chat.ChatUserMode
import javax.inject.Inject

class ChatHistoryRepository @Inject constructor(val twitchApolloSource: TwitchApolloSource) {
    fun getMessages(channelName: String): Single<List<ChatMessageInfo>> {
        return twitchApolloSource.getMessages(channelName)
    }

    fun getSystemMessage(text: String): ChatMessageInfo {
        return ChatMessageInfo().apply {
            this.userMode = ChatUserMode().apply {
                this.system = true
            }
            this.tokens = listOf(ChatTextToken().apply {
                this.text = text
            }).toTypedArray()
        }
    }
}