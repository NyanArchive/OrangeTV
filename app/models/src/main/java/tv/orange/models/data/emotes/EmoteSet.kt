package tv.orange.models.data.emotes


class EmoteSet(emotes: Collection<Emote>) {
    constructor(): this(emptyList())

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