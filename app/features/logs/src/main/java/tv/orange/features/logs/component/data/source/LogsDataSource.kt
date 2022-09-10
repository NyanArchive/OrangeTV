package tv.orange.features.logs.component.data.source

import com.apollographql.apollo3.api.Optional
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.repository.TwitchRepository
import tv.orange.features.logs.component.data.mapper.LogsMapper
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.models.abc.TwitchComponentProvider
import tv.orange.models.gql.twitch.ModLogsMessagesBySenderQuery
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Inject

class LogsDataSource @Inject constructor(
    val tcp: TwitchComponentProvider,
    val twitchRepository: TwitchRepository,
    val mapper: LogsMapper
) {
    fun getMessages(userLogin: String, channelId: String): Single<List<MessageItem>> {
        return twitchRepository.getUserInfo(login = userLogin).flatMap { userInfo ->
            tcp.getProvider(GraphQlService::class).get().singleForQuery(
                ModLogsMessagesBySenderQuery(
                    senderID = userInfo.userId,
                    channelID = channelId,
                    first = Optional.presentIfNotNull(200)
                ), mapper::map, true, true, true, false
            ).subscribeOn(Schedulers.io())
        }.subscribeOn(Schedulers.io())
    }
}