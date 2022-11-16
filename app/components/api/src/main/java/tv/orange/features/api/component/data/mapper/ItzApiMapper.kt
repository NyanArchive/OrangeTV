package tv.orange.features.api.component.data.mapper

import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteImpl
import tv.orange.models.retrofit.itz.ItzEmote
import tv.orange.models.retrofit.itz.ItzResponseData
import javax.inject.Inject

class ItzApiMapper @Inject constructor() {
    fun mapEmotes(response: ItzResponseData): Pair<List<Emote>, HashMap<Int, MutableList<Emote>>> {
        val channelEmotes = hashMapOf<Int, MutableList<Emote>>()

        response.data.channel_emotes.forEach { entry ->
            entry.key.toIntOrNull()?.let { channelId ->
                if (!channelEmotes.containsKey(channelId)) {
                    channelEmotes[channelId] = mutableListOf()
                }
                channelEmotes[channelId]?.addAll(entry.value.emotes.map(Companion::map))
            }
        }

        val globalEmotes = response.data.global_emotes.map(Companion::map)

        return globalEmotes to channelEmotes
    }

    companion object {
        private fun map(emote: ItzEmote): Emote {
            return EmoteImpl(
                emoteCode = emote.name,
                animated = false,
                smallUrl = getEmoteUrl(emote.id, "1x"),
                mediumUrl = getEmoteUrl(emote.id, "2x"),
                largeUrl = getEmoteUrl(emote.id, "3x"),
                packageSet = EmotePackageSet.ItzChannel,
                isZeroWidth = false
            )
        }

        private fun getEmoteUrl(id: String, scale: String): String {
            return "https://itzalex.github.io/emote/{id}/{scale}".replace("{id}", id)
                .replace("{scale}", scale)
        }
    }
}