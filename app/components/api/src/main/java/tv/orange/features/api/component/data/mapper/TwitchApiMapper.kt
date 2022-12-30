package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.StreamUptime
import tv.orange.models.data.UserInfo
import tv.orange.models.gql.twitch.StreamUptimeQuery
import tv.orange.models.gql.twitch.UserInfoQuery
import tv.orange.models.util.DateUtil
import javax.inject.Inject

class TwitchApiMapper @Inject constructor() {
    fun map(response: UserInfoQuery.Data): UserInfo? {
        return response.user?.let { user ->
            UserInfo(userId = user.id, userName = user.login)
        }
    }

    fun map(response: StreamUptimeQuery.Data): StreamUptime? {
        return response.user?.stream?.createdAt?.let { created ->
            StreamUptime(DateUtil.getStandardizeDateString(created.toString()))
        }
    }
}