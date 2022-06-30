package tv.orange.features.emotes.component.data.models

import tv.orange.features.emotes.models.Emote

data class EmoteContainer(
    val bttvEmotes: EmoteSet,
    val ffzEmotes: EmoteSet,
    val stvEmotes: EmoteSet
) {
    constructor() : this(EmoteSet(emptyList()), EmoteSet(emptyList()), EmoteSet(emptyList()))

    val allEmotes: List<Emote>
        get() = bttvEmotes.getEmotes() + ffzEmotes.getEmotes() + stvEmotes.getEmotes()

    val isEmpty: Boolean
        get() = bttvEmotes.isEmpty() && ffzEmotes.isEmpty() && stvEmotes.isEmpty()

    fun getEmote(code: String): Emote? {
        return bttvEmotes.getEmote(code) ?: ffzEmotes.getEmote(code) ?: stvEmotes.getEmote(code)
    }
}