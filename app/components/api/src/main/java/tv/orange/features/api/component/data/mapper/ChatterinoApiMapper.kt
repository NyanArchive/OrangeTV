package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.badges.BadgeImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.retrofit.chatterino.BadgesData
import javax.inject.Inject

class ChatterinoApiMapper @Inject constructor() {
    fun map(response: BadgesData): BadgeSet {
        return BadgeSet.Builder().apply {
            response.badges.forEach { badge ->
                badge.users.forEach { userIdString ->
                    userIdString.toIntOrNull()?.let { userId ->
                        addBadge(BadgeImpl(code = "chatterino", url = badge.image2), userId)
                    }
                }
            }
        }.build()
    }
}