package tv.orange.features.emotes.component.data.mapper

import tv.orange.features.emotes.models.Emote
import tv.orange.features.emotes.models.EmoteImpl
import tv.orange.models.retrofit.stv.StvEmote
import javax.inject.Inject

class StvEmotesApiMapper @Inject constructor() {
    fun map(emotes: List<StvEmote>): List<Emote> {
        return emotes.map { emote ->
            EmoteImpl(
                emoteCode = emote.name,
                animated = emote.mime == "image/gif" || emote.mime == "image/webp",
                smallUrl = getUrl("1x", emote.id),
                mediumUrl = getUrl("2x", emote.id),
                largeUrl = getUrl("4x", emote.id)
            )
        }
    }

    companion object {
        private const val STV_CDN = "https://cdn.7tv.app/emote/"

        private fun getUrl(size: String, emoteId: String): String {
            return "$STV_CDN$emoteId/$size"
        }
    }
}