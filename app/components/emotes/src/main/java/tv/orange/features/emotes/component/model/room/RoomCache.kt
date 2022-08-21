package tv.orange.features.emotes.component.model.room

import android.util.LruCache
import tv.orange.features.emotes.component.model.factory.RoomFactory

class RoomCache(private val roomFactory: RoomFactory, size: Int) : LruCache<Int, Room>(size) {
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

    fun fetch() {
        snapshot().values.forEach { it.fetch() }
    }

    fun rebuild() {
        snapshot()?.let { snapshot ->
            clear()
            snapshot.keys.forEach { channelId ->
                put(channelId, roomFactory.create(channelId = channelId))
            }
        }
    }
}