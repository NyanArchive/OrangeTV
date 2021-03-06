package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.badges.BadgeFfzImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.retrofit.ffz.FfzBadgesData
import javax.inject.Inject

class FfzApiMapper @Inject constructor() {
    fun mapBadges(response: FfzBadgesData): BadgeSet {
        val builder = BadgeSet.Builder()
        val badges = response.badges.associateBy { it.id }
        val supporter = BadgeFfzImpl.Type.SUPPORTER
        val unknown = BadgeFfzImpl.Type.UNKNOWN

        response.users.forEach { entry ->
            val emoteId = entry.key
            val userIds = entry.value

            emoteId.toIntOrNull()?.let { id ->
                userIds.forEach { userId ->
                    badges[id]?.let { ffzBadge ->
                        builder.addBadge(
                            BadgeFfzImpl(
                                type = if (ffzBadge.name == supporter.value) supporter else unknown,
                                url = getUrl(ffzBadge.id),
                                backgroundColor = ffzBadge.parseColor(),
                                replaces = ffzBadge.replaces
                            ), userId
                        )
                    }
                }
            }
        }

        return builder.build()
    }

    companion object {
        private const val FFZ_CDN = "https://cdn.frankerfacez.com/badge/"

        private fun getUrl(badgeId: Int): String {
            return "$FFZ_CDN$badgeId/2"
        }
    }
}