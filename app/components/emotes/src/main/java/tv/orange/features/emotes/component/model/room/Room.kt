package tv.orange.features.emotes.component.model.room

import tv.orange.features.emotes.component.model.EmotePackage
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote

interface Room {
    fun add(pack: EmotePackage)

    fun getEmotes(): List<Emote>

    fun getEmotesMap(): List<Pair<EmotePackageSet, List<Emote>>>

    fun getEmote(code: String): Emote?

    fun getEmote(code: String, emotePackageSet: EmotePackageSet): Emote?

    fun fetch()

    fun refresh()

    fun clear()
}