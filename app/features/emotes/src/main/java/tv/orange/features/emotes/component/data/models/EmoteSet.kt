package tv.orange.features.emotes.component.data.models

import tv.orange.features.emotes.models.Emote

class EmoteSet(emotes: Collection<Emote>) {
    private val map = LinkedHashMap<String, Emote>(emotes.size).apply {
        emotes.forEach { emote ->
            this[emote.getCode()] = emote
        }
    }

    fun addEmote(emote: Emote) {
        map[emote.getCode()] = emote
    }

    fun getEmote(code: String): Emote? {
        return map[code]
    }

    fun getEmotes(): Collection<Emote> {
        return map.values
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }
}