package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.badges.BadgeImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.retrofit.nop.DonationsData
import javax.inject.Inject

class NopApiMapper @Inject constructor() {
    fun mapBadges(donations: DonationsData): BadgeSet {
        val builder = BadgeSet.Builder()

        val defaultBadgeUrl = donations.defaultBadgeUrl

        donations.users?.let { users ->
            users.forEach { user ->
                val badgeUrl = user.badgeUrl?.ifBlank {
                    defaultBadgeUrl
                } ?: defaultBadgeUrl
                user.userId.toIntOrNull()?.let { userId ->
                    builder.addBadge(BadgeImpl("nop", badgeUrl), userId)
                }
            }
        }

        return builder.build()
    }

    fun mapBadges(homies: HashMap<String, String>): BadgeSet {
        val builder = BadgeSet.Builder()

        homies.forEach { entry ->
            entry.key.toIntOrNull()?.let { userId ->
                builder.addBadge(BadgeImpl("homies", getHomiesUrl(entry.value)), userId)
            }
        }

        return builder.build()
    }

    companion object {
        private fun getHomiesUrl(badgeId: String): String {
            return "https://nopbreak.ru/shared/homies/badges/" + badgeId
        }
    }
}