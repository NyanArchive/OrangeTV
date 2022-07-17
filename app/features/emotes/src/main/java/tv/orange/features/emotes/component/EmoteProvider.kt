package tv.orange.features.emotes.component

import tv.orange.core.Logger
import tv.orange.features.emotes.component.model.factory.RoomFactory
import tv.orange.features.emotes.component.model.room.RoomCache
import tv.orange.features.emotes.di.scope.EmotesScope
import tv.orange.models.abs.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

@EmotesScope
class EmoteProvider @Inject constructor(val roomFactory: RoomFactory) {
    private var global = roomFactory.createGlobal()
    private val channel = RoomCache(3)

    private fun getGlobalEmote(code: String): Emote? {
        return global.getEmote(code)
    }

    fun getEmotesMap(channelId: Int): List<Pair<EmotePackageSet, List<Emote>>> {
        val channel = channel.get(channelId)?.getEmotesMap() ?: emptyList()
        return global.getEmotesMap() + channel
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
        Logger.debug("called")
        channel.clear()
        global = roomFactory.createGlobal()
    }
}