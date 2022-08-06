package tv.orange.features.logs.component.data.source

import com.apollographql.apollo3.api.Optional
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.repository.TwitchRepository
import tv.orange.features.logs.component.data.mapper.LogsMapper
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.features.logs.di.scope.LogsScope
import tv.orange.models.gql.twitch.ModLogsMessagesBySenderQuery
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.network.graphql.TwitchApolloClient
import javax.inject.Inject

@LogsScope
class LogsDataSource @Inject constructor(
    val apolloClient: GraphQlService,
    val twitchRepository: TwitchRepository,
    val mapper: LogsMapper
) {
    fun getMessages(userLogin: String, channelId: String): Single<List<MessageItem>> {
        return twitchRepository.getUserInfo(userLogin).flatMap { userInfo ->
            apolloClient.singleForQuery(
                ModLogsMessagesBySenderQuery(
                    senderID = userInfo.userId,
                    channelID = channelId,
                    first = Optional.presentIfNotNull(200)
                ), { data: ModLogsMessagesBySenderQuery.Data ->
                    return@singleForQuery mapper.map(data)
                }, true, true, true, false
            ).subscribeOn(Schedulers.io())
        }.subscribeOn(Schedulers.io())
    }
}