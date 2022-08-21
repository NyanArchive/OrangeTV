package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.badges.BadgeImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.retrofit.nop.DonationsData
import javax.inject.Inject

class NopApiMapper @Inject constructor() {
    fun map(response: DonationsData): BadgeSet {
        return BadgeSet.Builder().apply {
            val defaultBadgeUrl = response.defaultBadgeUrl

            response.users?.let { users ->
                users.forEach { user ->
                    val badgeUrl = user.badgeUrl?.ifBlank { defaultBadgeUrl } ?: defaultBadgeUrl
                    user.userId.toIntOrNull()?.let { userId ->
                        addBadge(BadgeImpl(code = "nopbreak", url = badgeUrl), userId)
                    }
                }
            }
        }.build()
    }

    fun map(response: HashMap<String, String>): BadgeSet {
        val builder = BadgeSet.Builder()

        response.forEach { entry ->
            entry.key.toIntOrNull()?.let { userId ->
                builder.addBadge(
                    BadgeImpl(code = "homies", url = getHomiesUrl(entry.value)),
                    userId
                )
            }
        }

        return builder.build()
    }

    companion object {
        private fun getHomiesUrl(badgeId: String): String {
            return "https://nopbreak.ru/shared/homies/badges/$badgeId"
        }
    }
}