package tv.orange.features.emotes.component

import tv.orange.features.emotes.component.model.factory.RoomFactory
import tv.orange.features.emotes.component.model.room.RoomCache
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

class EmoteProvider @Inject constructor(val roomFactory: RoomFactory) {
    private val global = roomFactory.createGlobal()
    private val channel = RoomCache(3)

    private fun getGlobalEmote(code: String): Emote? {
        return global.getEmote(code)
    }

    fun getEmote(code: String, channelId: Int): Emote? {
        return channel[channelId]?.getEmote(code) ?: getGlobalEmote(code)
    }

    fun requestChannelEmotes(channelId: Int) {
        channel[channelId]?.refresh() ?: fetchChannelEmotes(channelId)
    }

    private fun fetchChannelEmotes(channelId: Int) {
        channel.put(channelId, roomFactory.create(channelId).apply { fetch() })
    }

    fun fetchGlobalEmotes() {
        global.fetch()
    }

    fun refreshGlobalEmotes() {
        global.refresh()
    }

    fun clear() {
        channel.evictAll()
    }
}