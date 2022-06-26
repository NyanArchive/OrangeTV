package tv.orange.features.badges.models

import tv.orange.features.badges.component.data.models.BadgeSet

class BadgeContainer(
    val ffzBadges: BadgeSet,
) {
    constructor() : this(BadgeSet())

    fun getBadges(userId: Int): Collection<Badge> {
        return ffzBadges.getBadges(userId)
    }

    val size: Int
            get() = ffzBadges.size

}