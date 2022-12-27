package tv.orange.features.api.component.data.mapper

import android.graphics.Color
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.badges.impl.BadgeFfzAPImpl
import tv.orange.models.data.badges.impl.BadgeFfzImpl
import tv.orange.models.retrofit.ffz.FfzBadgesData
import tv.orange.models.retrofit.ffzap.FfzAPBadge
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

    fun mapBadges(response: List<FfzAPBadge>): BadgeSet {
        val builder = BadgeSet.Builder()
        response.filter { !FFZAP_SKIP_BADGE_IDS.contains(it.id) }.forEach { badge ->
            badge.id?.toIntOrNull()?.let { badgeId ->
                badge.tier?.let { tier ->
                    builder.addBadge(
                        BadgeFfzAPImpl(
                            badgeId = badgeId,
                            backgroundColor = if (tier >= 3 && badge.badge_is_colored != 0) {
                                badge.parseColor()
                            } else {
                                Color.TRANSPARENT
                            }
                        ), badgeId
                    )
                }
            }
        }

        return builder.build()
    }

    companion object {
        private val FFZAP_SKIP_BADGE_IDS = listOf("26964566")
    }
}