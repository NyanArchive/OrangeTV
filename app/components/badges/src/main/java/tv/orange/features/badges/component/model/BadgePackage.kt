package tv.orange.features.badges.component.model

import tv.orange.models.data.badges.Badge

interface BadgePackage {
    fun getBadges(userId: Int): List<Badge>

    fun refresh(force: Boolean)

    fun isEmpty(): Boolean

    fun clear()

    fun hasBadges(userId: Int): Boolean
}