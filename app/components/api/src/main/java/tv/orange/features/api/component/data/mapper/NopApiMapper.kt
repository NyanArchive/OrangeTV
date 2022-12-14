package tv.orange.features.api.component.data.mapper

import tv.orange.core.LoggerImpl
import tv.orange.models.data.badges.BadgeBaseImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.badges.impl.BadgeHomiesImpl
import tv.orange.models.data.badges.impl.BadgeHomiesNewImpl
import tv.orange.models.data.badges.impl.BadgeItzSet
import tv.orange.models.retrofit.nop.DonationsData
import java.math.BigInteger
import java.util.*
import javax.inject.Inject

class NopApiMapper @Inject constructor() {
    fun map(response: DonationsData): BadgeSet {
        return BadgeSet.Builder().apply {
            val defaultBadgeUrl = response.defaultBadgeUrl
            val defaultBadge = BadgeBaseImpl(code = "nopbreak", url = defaultBadgeUrl)

            response.users?.forEach { user ->
                user.userId.toIntOrNull()?.let { userId ->
                    user.badgeUrl?.ifBlank { null }?.let { badgeUrl ->
                        addBadge(
                            badge = BadgeBaseImpl(
                                code = "nopbreak",
                                url = badgeUrl
                            ),
                            userId = userId
                        )
                    } ?: run {
                        addBadge(
                            badge = defaultBadge,
                            userId = userId
                        )
                    }
                }
            }
        }.build()
    }

    fun map(response: HashMap<String, String>): BadgeItzSet {
        val set = BadgeItzSet()

        var old = 0
        var new = 0
        response.forEach { entry ->
            entry.key.toIntOrNull()?.let { userId ->
                entry.value.toUuid()?.let { uuid ->
                    set.addBadge(
                        badge = BadgeHomiesNewImpl(uuid),
                        userId = userId
                    )
                    new++
                } ?: run {
                    set.addBadge(
                        badge = BadgeHomiesImpl(entry.value),
                        userId = userId
                    )
                    old++
                }
            }
        }

        LoggerImpl.devDebug("new: $new, old: $old")

        return set
    }

    companion object {
        private fun String.toUuid(): UUID? {
            if (length != 32) {
                return null
            }

            return try {
                UUID(
                    BigInteger(substring(0, 16), 16).toLong(),
                    BigInteger(substring(16, 32), 16).toLong()
                )
            } catch (th: Throwable) {
                th.printStackTrace()

                return null
            }
        }
    }
}