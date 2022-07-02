package tv.orange.features.emotes.component.model.room

import tv.orange.features.emotes.component.model.EmotePackage
import tv.orange.models.data.emotes.Emote

class RoomImpl : Room {
    private val packages: MutableList<EmotePackage> = mutableListOf()

    fun isEmpty(): Boolean {
        return packages.all { pack -> pack.isEmpty() }
    }

    override fun add(pack: EmotePackage) {
        packages.add(pack)
    }

    override fun getEmotes(): List<Emote> {
        return packages.flatMap { it.getEmotes() }
    }

    override fun getEmote(code: String): Emote? {
        packages.forEach { pack ->
            pack.getEmote(code)?.let { emote: Emote ->
                return emote
            }
        }

        return null
    }

    override fun fetch() {
        packages.forEach { pack ->
            pack.refresh(force = true)
        }
    }

    override fun refresh() {
        packages.forEach { pack ->
            pack.refresh(force = false)
        }
    }

    override fun clear() {
        packages.forEach { pack ->
            packages.remove(pack)
            pack.clear()
        }
    }
}