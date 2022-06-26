package tv.orange.features.badges.component.data.mapper

import tv.orange.features.badges.component.data.models.BadgeSet
import tv.orange.features.badges.models.BadgeImpl
import tv.orange.models.retrofit.ffz.FfzBadges
import javax.inject.Inject

class FfzBadgesApiMapper @Inject constructor() {
    fun map(response: FfzBadges): BadgeSet {
        val set = BadgeSet()
        val badges = response.badges.associateBy { it.id }

        response.users.forEach { entry ->
            val emoteId = entry.key
            val userIds = entry.value

            emoteId.toIntOrNull()?.let { id ->
                userIds.forEach { userId ->
                    badges[id]?.let { ffzBadge ->
                        set.addBadge(
                            BadgeImpl(
                                badgeCode = ffzBadge.name,
                                badgeUrl = getUrl(ffzBadge.id),
                                badgeBackgroundColor = ffzBadge.parseColor(),
                                badgeReplaces = ffzBadge.replaces
                            ), userId
                        )
                    }
                }
            }
        }

        return set
    }

    companion object {
        private const val FFZ_CDN = "https://cdn.frankerfacez.com/badge/"

        private fun getUrl(badgeId: Int): String {
            return "$FFZ_CDN$badgeId/2"
        }
    }
}