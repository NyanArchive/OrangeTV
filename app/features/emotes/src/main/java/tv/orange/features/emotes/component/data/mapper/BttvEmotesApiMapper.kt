package tv.orange.features.emotes.component.data.mapper

import tv.orange.features.emotes.models.Emote
import tv.orange.features.emotes.models.EmoteImpl
import tv.orange.models.retrofit.bttv.BttvEmote
import tv.orange.models.retrofit.bttv.ImageType
import javax.inject.Inject

class BttvEmotesApiMapper @Inject constructor() {
    fun map(emotes: List<BttvEmote>): List<Emote> {
        return emotes.map { emote ->
            EmoteImpl(
                emoteCode = emote.code,
                animated = emote.imageType == ImageType.GIF,
                smallUrl = getUrl("1x", emote.id),
                mediumUrl = getUrl("2x", emote.id),
                largeUrl = getUrl("3x", emote.id)
            )
        }
    }

    companion object {
        private const val BTTV_CDN = "https://cdn.betterttv.net/emote/"

        private fun getUrl(size: String, emoteId: String): String {
            return "$BTTV_CDN$emoteId/$size"
        }
    }
}