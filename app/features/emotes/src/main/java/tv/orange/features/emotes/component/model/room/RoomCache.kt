package tv.orange.features.emotes.component.model.room

import android.util.LruCache

class RoomCache(size: Int) : LruCache<Int, Room>(size) {
    override fun entryRemoved(
        evicted: Boolean,
        key: Int?,
        oldValue: Room?,
        newValue: Room?
    ) {
        super.entryRemoved(evicted, key, oldValue, newValue)
        oldValue?.clear()
    }

    fun clear() {
        evictAll()
    }

    fun refresh() {
        snapshot().values.forEach { it.fetch() }
    }
}