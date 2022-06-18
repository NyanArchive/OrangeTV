package tv.orange.features.emotes.component

import tv.orange.features.emotes.models.Emote
import tv.orange.features.emotes.models.EmoteProvider

class EmoteProvider() : EmoteProvider {
    override fun getEmote(code: String, channelId: Int, userId: Int): Emote {
        TODO("Not yet implemented")
    }

    override fun getChannelEmotes(channelId: Int): List<Emote> {
        TODO("Not yet implemented")
    }

    override fun getUserEmotes(userId: Int): List<Emote> {
        TODO("Not yet implemented")
    }

    override fun requestEmotes(channelId: Int) {
        TODO("Not yet implemented")
    }

    override fun requestUserEmotes(userId: Int) {
        TODO("Not yet implemented")
    }
}