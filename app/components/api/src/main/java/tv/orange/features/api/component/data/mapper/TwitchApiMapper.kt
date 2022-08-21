package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.UserInfo
import tv.orange.models.gql.twitch.UserInfoQuery
import javax.inject.Inject

class TwitchApiMapper @Inject constructor() {
    fun map(response: UserInfoQuery.Data): UserInfo? {
        return response.user?.let { user ->
            UserInfo(userId = user.id, userName = user.login)
        }
    }
}