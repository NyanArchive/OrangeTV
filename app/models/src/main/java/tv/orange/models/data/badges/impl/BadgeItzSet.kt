package tv.orange.models.data.badges.impl

import android.util.SparseArray
import tv.orange.models.data.badges.Badge

class BadgeItzSet(
    private val badges: SparseArray<Badge>
) {
    constructor() : this(SparseArray(0))

    fun getBadges(userId: Int): List<Badge> {
        return badges[userId]?.let { listOf(it) } ?: emptyList()
    }

    fun addBadge(badge: Badge, userId: Int) {
        badges[userId] = badge
    }

    fun isEmpty(): Boolean {
        return badges.size() == 0
    }

    override fun toString(): String {
        return "BadgeSet(${badges.size()})"
    }

    fun hasBadges(userId: Int): Boolean {
        return badges[userId] != null
    }
}