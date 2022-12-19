package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.badges.impl.BadgeFfzImpl
import tv.orange.models.retrofit.ffz.FfzBadgesData
import javax.inject.Inject

class FfzApiMapper @Inject constructor() {
    fun mapBadges(response: FfzBadgesData): BadgeSet {
        val builder = BadgeSet.Builder()
        val badges = response.badges.associateBy({ it.id }, { badge ->
            BadgeFfzImpl(
                badgeId = badge.id,
                type = if (badge.name == BadgeFfzImpl.Type.SUPPORTER.value) {
                    BadgeFfzImpl.Type.SUPPORTER
                } else {
                    BadgeFfzImpl.Type.UNKNOWN
                },
                backgroundColor = badge.parseColor(),
                replaces = badge.replaces
            )
        })

        response.users.forEach { entry ->
            val emoteId = entry.key
            val userIds = entry.value

            emoteId.toIntOrNull()?.let { id ->
                userIds.forEach { userId ->
                    badges[id]?.let { badge ->
                        builder.addBadge(
                            badge = badge,
                            userId = userId
                        )
                    }
                }
            }
        }

        return builder.build()
    }
}