package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteImpl
import tv.orange.models.retrofit.bttv.BttvChannel
import tv.orange.models.retrofit.bttv.BttvEmote
import tv.orange.models.retrofit.bttv.FfzEmote
import tv.orange.models.retrofit.bttv.ImageType
import javax.inject.Inject

class BttvApiMapper @Inject constructor() {
    fun mapBttvEmotes(emotes: List<BttvEmote>): List<Emote> {
        return emotes.map { emote ->
            EmoteImpl(
                emoteCode = emote.code,
                animated = emote.imageType == ImageType.GIF,
                smallUrl = getEmoteUrl("1x", emote.id),
                mediumUrl = getEmoteUrl("2x", emote.id),
                largeUrl = getEmoteUrl("3x", emote.id)
            )
        }
    }

    fun mapFfzEmotes(emotes: List<FfzEmote>): List<Emote> {
        return emotes.mapNotNull { emote ->
            val small = emote.images["1x"] ?: return@mapNotNull null

            EmoteImpl(
                emoteCode = emote.code,
                animated = false,
                smallUrl = small,
                mediumUrl = emote.images["2x"],
                largeUrl = emote.images["4x"]
            )
        }
    }

    fun mapChannelEmotes(emotes: BttvChannel): List<Emote> {
        return mapBttvEmotes(emotes.sharedEmotes) + mapBttvEmotes(emotes.channelEmotes)
    }

    companion object {
        private const val BTTV_EMOTE_CDN = "https://cdn.betterttv.net/emote/"

        private fun getEmoteUrl(size: String, emoteId: String): String {
            return "$BTTV_EMOTE_CDN$emoteId/$size"
        }
    }
}