package tv.oranges.features.chathistory.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.models.gql.twitch.MessageBufferChatHistoryQuery
import tv.oranges.features.chathistory.data.mapper.ChatHistoryMapper
import tv.oranges.features.chathistory.data.model.ChatHistoryMessage
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Inject

class TwitchApolloSource @Inject constructor(val apolloClient: GraphQlService, val mapper: ChatHistoryMapper) {
    fun getMessages(channelName: String): Single<List<ChatHistoryMessage>> {
        val builder = MessageBufferChatHistoryQuery(channelLogin = channelName)
        return apolloClient.singleForQuery(
            builder,
            { data: MessageBufferChatHistoryQuery.Data ->
                return@singleForQuery data.channel?.recentChatMessages?.map {
                    mapper.map(it)
                } ?: emptyList()
            }, true, true, true, false
        ).subscribeOn(Schedulers.io())
    }
}