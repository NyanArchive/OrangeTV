package tv.orange.features.emotes.component.model

import tv.orange.models.data.emotes.Emote

interface EmotePackage {
    fun getEmote(code: String): Emote?

    fun getEmotes(): List<Emote>

    fun refresh(force: Boolean)

    fun isEmpty(): Boolean

    fun getToken(): String

    fun clear()
}