package tv.oranges.features.chathistory.data.repository

import io.reactivex.Single
import tv.oranges.features.chathistory.data.model.ChatHistoryMessage
import tv.oranges.features.chathistory.data.source.TwitchApolloSource
import javax.inject.Inject

class ChatHistoryRepository @Inject constructor(val twitchApolloSource: TwitchApolloSource) {
    fun getMessages(channelName: String): Single<List<ChatHistoryMessage>> {
        return twitchApolloSource.getMessages(channelName)
    }
}