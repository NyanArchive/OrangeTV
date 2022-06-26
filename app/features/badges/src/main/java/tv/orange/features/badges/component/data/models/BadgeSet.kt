package tv.orange.features.badges.component.data.models

import android.util.ArrayMap
import android.util.SparseArray
import tv.orange.features.badges.models.Badge

class BadgeSet {
    private val badges = SparseArray<HashSet<Badge>>()
    private val cache: MutableMap<Badge, HashSet<Badge>> = ArrayMap()

    fun getBadges(userId: Int): Collection<Badge> {
        return badges[userId] ?: return emptyList()
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

    fun clearCache() {
        cache.clear()
    }

    fun clear() {
        badges.clear()
    }

    val size: Int = badges.size()
}