package tv.orange.features.emotes.component.model.room

import android.util.LruCache
import tv.orange.core.Logger

class RoomCache(size: Int) : LruCache<Int, Room>(size) {
    override fun entryRemoved(
        evicted: Boolean,
        key: Int?,
        oldValue: Room?,
        newValue: Room?
    ) {
        super.entryRemoved(evicted, key, oldValue, newValue)
        oldValue?.let { room ->
            room.clear()
            Logger.debug("entryRemoved: $room")
        }
    }
}