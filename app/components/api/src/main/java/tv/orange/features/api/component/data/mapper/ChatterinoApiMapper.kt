package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.badges.BadgeBaseImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.retrofit.chatterino.BadgesData
import javax.inject.Inject

class ChatterinoApiMapper @Inject constructor() {
    fun map(response: BadgesData): BadgeSet {
        return BadgeSet.Builder().apply {
            response.badges.forEach { badge ->
                val badgeImpl = BadgeBaseImpl(
                    code = "chatterino",
                    url = badge.image2
                )
                badge.users.forEach { userIdString ->
                    userIdString.toIntOrNull()?.let { userId ->
                        addBadge(
                            badge = badgeImpl,
                            userId = userId
                        )
                    }
                }
            }
        }.build()
    }
}