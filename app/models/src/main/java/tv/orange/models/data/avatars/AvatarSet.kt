package tv.orange.models.data.avatars

import android.util.ArrayMap

class AvatarSet {
    private val map = ArrayMap<String, String>()

    fun add(channelName: String, url: String) {
        map[channelName.lowercase()] = url
    }

    fun get(channelName: String): String? {
        return map[channelName]
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }
}