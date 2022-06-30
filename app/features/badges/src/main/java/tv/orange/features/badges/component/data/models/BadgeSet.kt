package tv.orange.features.badges.component.data.models

import android.util.SparseArray
import tv.orange.features.badges.models.Badge

data class BadgeSet(val badges: SparseArray<HashSet<Badge>>) {
    constructor() : this(SparseArray(0))

    fun getBadges(userId: Int): Collection<Badge> {
        return badges[userId] ?: return emptyList()
    }

    fun isEmpty(): Boolean {
        return badges.size() != 0
    }
}