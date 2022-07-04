package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.badges.BadgeImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.retrofit.chatterino.Badges
import javax.inject.Inject

class ChatterinoApiMapper @Inject constructor() {
    fun mapBadges(badges: Badges): BadgeSet {
        val builder = BadgeSet.Builder()

        badges.badges.forEach { badge ->
            badge.users.forEach { userIdString ->
                userIdString.toIntOrNull()?.let { userId ->
                    builder.addBadge(BadgeImpl("chatterino", badge.image2), userId)
                }
            }
        }

        return builder.build()
    }
}