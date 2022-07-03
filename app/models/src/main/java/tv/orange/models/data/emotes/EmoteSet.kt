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

    fun getEmotes(): List<Emote> {
        return map.values.toList()
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun toString(): String {
        return "EmoteSet(${getEmotes().size})"
    }
}