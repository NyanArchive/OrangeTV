package tv.orange.features.api.component.data.source

import com.apollographql.apollo3.api.Optional
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.data.mapper.TwitchApiMapper
import tv.orange.models.data.UserInfo
import tv.orange.models.gql.twitch.UserInfoQuery
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Inject

class TwitchApiSource @Inject constructor(
    val apolloClient: GraphQlService,
    val twitchApiMapper: TwitchApiMapper
) {
    fun getUserInfo(login: String): Single<UserInfo> {
        return apolloClient.singleForQuery(
            UserInfoQuery(
                userLogin = Optional.presentIfNotNull(login),
                userId = Optional.presentIfNotNull(null)
            ), { data: UserInfoQuery.Data ->
                return@singleForQuery twitchApiMapper.map(data) ?: throw Exception("data is null")
            }, true, true, true, false
        ).subscribeOn(Schedulers.io())
    }
}