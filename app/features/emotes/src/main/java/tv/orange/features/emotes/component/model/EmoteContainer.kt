package tv.orange.features.emotes.component.model

import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteSet

data class EmoteContainer(
    val bttvEmotes: EmoteSet,
    val ffzEmotes: EmoteSet,
    val stvEmotes: EmoteSet
) {
    constructor() : this(EmoteSet(), EmoteSet(), EmoteSet())

    val allEmotes: List<Emote>
        get() = bttvEmotes.getEmotes() + ffzEmotes.getEmotes() + stvEmotes.getEmotes()

    fun isEmpty(): Boolean {
        return bttvEmotes.isEmpty() && ffzEmotes.isEmpty() && stvEmotes.isEmpty()
    }

    fun getEmote(code: String): Emote? {
        return bttvEmotes.getEmote(code) ?: ffzEmotes.getEmote(code) ?: stvEmotes.getEmote(code)
    }
}