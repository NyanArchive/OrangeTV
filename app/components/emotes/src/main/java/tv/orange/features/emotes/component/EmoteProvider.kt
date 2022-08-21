package tv.orange.features.emotes.component

import tv.orange.features.emotes.component.model.factory.RoomFactory
import tv.orange.features.emotes.component.model.room.RoomCache
import tv.orange.features.emotes.di.scope.EmotesScope
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

@EmotesScope
class EmoteProvider @Inject constructor(
    val roomFactory: RoomFactory
) {
    private var global = roomFactory.createGlobal()
    private var channel = RoomCache(roomFactory, 3)

    fun getEmotesMap(channelId: Int): List<Pair<EmotePackageSet, List<Emote>>> {
        val channel = channel.get(channelId)?.getEmotesMap() ?: emptyList()
        return global.getEmotesMap() + channel
    }

    fun getEmote(code: String, channelId: Int): Emote? {
        return channel[channelId]?.getEmote(code = code) ?: global.getEmote(code = code)
    }

    fun requestChannelEmotes(channelId: Int) {
        channel[channelId]?.refresh() ?: fetchChannelEmotes(channelId = channelId)
    }

    fun clear() {
        global = roomFactory.createGlobal()
    }

    fun fetch() {
        global.fetch()
        channel.fetch()
    }

    fun rebuild() {
        global = roomFactory.createGlobal()
        channel.rebuild()
        fetch()
    }

    private fun fetchChannelEmotes(channelId: Int) {
        channel.put(channelId, roomFactory.create(channelId = channelId).apply { fetch() })
    }
}