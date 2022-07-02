package tv.orange.features.badges.component.model

import tv.orange.models.data.badges.Badge
import tv.orange.models.data.badges.BadgeSet

data class BadgeContainer(
    val ffzBadges: BadgeSet,
) {
    constructor() : this(BadgeSet())

    fun getBadges(userId: Int): Collection<Badge> {
        return ffzBadges.getBadges(userId)
    }

    fun isEmpty(): Boolean {
        return ffzBadges.isEmpty()
    }
}