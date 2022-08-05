package tv.orange.features.chathistory.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.models.gql.twitch.MessageBufferChatHistoryQuery
import tv.orange.features.chathistory.data.mapper.ChatHistoryMapper
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.chat.ChatMessageInfo
import javax.inject.Inject

class TwitchApolloSource @Inject constructor(
    val apolloClient: GraphQlService,
    val mapper: ChatHistoryMapper
) {
    fun getMessages(channelName: String): Single<List<ChatMessageInfo>> {
        return apolloClient.singleForQuery(
            MessageBufferChatHistoryQuery(channelLogin = channelName),
            { data: MessageBufferChatHistoryQuery.Data ->
                return@singleForQuery data.channel?.recentChatMessages?.mapNotNull {
                    mapper.map(it)
                } ?: emptyList()
            }, true, true, true, false
        ).subscribeOn(Schedulers.io())
    }
}