package tv.orange.features.badges.component.model.room

import tv.orange.features.badges.component.model.BadgePackage
import tv.orange.models.data.badges.Badge

interface Room {
    fun add(pack: BadgePackage)

    fun getBadges(userId: Int): List<Badge>

    fun fetch()

    fun refresh()

    fun clear()

    fun hasBadges(userId: Int): Boolean
}