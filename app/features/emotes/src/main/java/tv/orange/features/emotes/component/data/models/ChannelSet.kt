package tv.orange.features.emotes.component.data.models

import tv.orange.features.emotes.models.Emote

data class ChannelSet(val bttvEmotes: EmoteSet, val ffzEmotes: EmoteSet) {
    val allEmotes: List<Emote>
        get() = bttvEmotes.getEmotes() + ffzEmotes.getEmotes()

    fun getEmote(code: String): Emote? {
        return bttvEmotes.getEmote(code) ?: ffzEmotes.getEmote(code)
    }
}