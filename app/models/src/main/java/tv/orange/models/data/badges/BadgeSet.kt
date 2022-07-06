package tv.orange.models.data.badges

import android.util.ArrayMap
import android.util.SparseArray

data class BadgeSet(val badges: SparseArray<HashSet<Badge>>) {
    constructor() : this(SparseArray(0))

    fun getBadges(userId: Int): List<Badge> {
        return badges[userId]?.toList() ?: emptyList()
    }

    fun isEmpty(): Boolean {
        return badges.size() == 0
    }

    class Builder {
        private val badges = SparseArray<HashSet<Badge>>()
        private val cache: MutableMap<Badge, HashSet<Badge>> = ArrayMap()

        fun build(): BadgeSet {
            return BadgeSet(badges)
        }

        fun addBadge(badge: Badge, userId: Int) {
            val set = badges.get(userId) ?: run {
                synchronized(this) {
                    badges.get(userId) ?: run {
                        badges.put(userId, getOrCreateNewSet(badge))
                        return
                    }
                }
            }

            if (set.size == 1) {
                synchronized(this) {
                    if (set.size == 1) {
                        badges.put(userId, HashSet(set).apply { add(badge) })
                        return
                    }
                }
            }

            set.add(badge)
        }

        private fun getOrCreateNewSet(badge: Badge): HashSet<Badge> {
            return cache[badge] ?: run {
                synchronized(this) {
                    cache[badge] ?: run {
                        return@synchronized HashSet<Badge>().apply {
                            add(badge)
                            cache[badge] = this
                        }
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "BadgeSet(${badges.size()})"
    }

    fun hasBadges(userId: Int): Boolean {
        return badges.get(userId, null)?.isNotEmpty() ?: false
    }
}