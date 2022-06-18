package tv.orange.features.emotes.models

interface EmoteProvider {
    fun getEmote(code: String, channelId: Int, userId: Int): Emote

    fun getChannelEmotes(channelId: Int): List<Emote>

    fun getUserEmotes(userId: Int): List<Emote>

    fun requestEmotes(channelId: Int)

    fun requestUserEmotes(userId: Int)
}