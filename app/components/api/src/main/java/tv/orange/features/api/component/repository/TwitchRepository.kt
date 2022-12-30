package tv.orange.features.api.component.repository

import io.reactivex.Single
import tv.orange.features.api.component.data.source.TwitchApiSource
import tv.orange.models.data.StreamUptime
import tv.orange.models.data.UserInfo
import javax.inject.Inject

class TwitchRepository @Inject constructor(
    val twitchApiSource: TwitchApiSource
) {
    fun getUserInfo(login: String): Single<UserInfo> {
        return twitchApiSource.getUserInfo(login = login)
    }

    fun getStreamUptime(channelId: Int): Single<StreamUptime> {
        return twitchApiSource.getStreamUptime(channeId = channelId)
    }
}