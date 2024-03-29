package tv.orange.features.api.component.data.source

import com.apollographql.apollo3.api.Optional
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.data.mapper.TwitchApiMapper
import tv.orange.models.abc.TwitchComponentProvider
import tv.orange.models.data.StreamUptime
import tv.orange.models.data.UserInfo
import tv.orange.models.gql.twitch.StreamUptimeQuery
import tv.orange.models.gql.twitch.UserInfoQuery
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Inject

class TwitchApiSource @Inject constructor(
    val tcp: TwitchComponentProvider,
    val twitchApiMapper: TwitchApiMapper
) {
    fun getUserInfo(login: String): Single<UserInfo> {
        return tcp.getProvider(GraphQlService::class).get().singleForQuery(
            UserInfoQuery(
                userLogin = Optional.presentIfNotNull(login),
                userId = Optional.presentIfNotNull(null)
            ), { data: UserInfoQuery.Data ->
                return@singleForQuery twitchApiMapper.map(data) ?: throw Exception("data is null")
            }, true, true, true, false
        ).subscribeOn(Schedulers.io())
    }

    fun getStreamUptime(channeId: Int): Single<StreamUptime> {
        return tcp.getProvider(GraphQlService::class).get().singleForQuery(
            StreamUptimeQuery(
                userLogin = Optional.presentIfNotNull(null),
                userId = Optional.presentIfNotNull(channeId.toString())
            ), { data: StreamUptimeQuery.Data ->
                return@singleForQuery twitchApiMapper.map(data) ?: throw Exception("data is null")
            }, true, true, true, false
        ).subscribeOn(Schedulers.io())
    }
}