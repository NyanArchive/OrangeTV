package tv.orange.features.logs.component.data.source

import com.apollographql.apollo3.api.Optional
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.logs.component.data.mapper.LogsMapper
import tv.orange.features.logs.component.data.model.Message
import tv.orange.features.logs.component.data.model.UserInfo
import tv.orange.features.logs.di.scope.LogsScope
import tv.orange.models.gql.twitch.UserInfoQuery
import tv.orange.models.gql.twitch.ViewerCardModLogsMessagesBySenderQuery
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Inject

@LogsScope
class LogsDataSource @Inject constructor(
    val apolloClient: GraphQlService,
    val mapper: LogsMapper
) {
    fun getMessages(userLogin: String, channelId: String): Single<List<Message>> {
        return getUserInfo(userLogin).flatMap { userInfo ->
            apolloClient.singleForQuery(
                ViewerCardModLogsMessagesBySenderQuery(
                    senderID = userInfo.userId,
                    channelID = channelId,
                    first = Optional.presentIfNotNull(200)
                ), { data: ViewerCardModLogsMessagesBySenderQuery.Data ->
                    return@singleForQuery mapper.map(data)
                }, true, true, true, false
            ).subscribeOn(Schedulers.io())
        }.subscribeOn(Schedulers.io())
    }

    private fun getUserInfo(login: String): Single<UserInfo> {
        return apolloClient.singleForQuery(
            UserInfoQuery(
                userLogin = Optional.presentIfNotNull(login),
                userId = Optional.presentIfNotNull(null)
            ), { data: UserInfoQuery.Data ->
                return@singleForQuery mapper.map(data)
            }, true, true, true, false
        ).subscribeOn(Schedulers.io())
    }
}