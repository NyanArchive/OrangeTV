package tv.orange.features.api.component.data.mapper

import tv.orange.models.data.avatars.AvatarSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteImpl
import tv.orange.models.retrofit.stv.StvEmote
import javax.inject.Inject

class StvApiMapper @Inject constructor() {
    fun mapEmotes(emotes: List<StvEmote>): List<Emote> {
        return emotes.map { emote ->
            EmoteImpl(
                emoteCode = emote.name,
                animated = emote.mime == "image/gif" || emote.mime == "image/webp",
                smallUrl = getEmoteUrl("1x", emote.id),
                mediumUrl = getEmoteUrl("2x", emote.id),
                largeUrl = getEmoteUrl("4x", emote.id)
            )
        }
    }

    fun mapAvatars(map: HashMap<String, String>): AvatarSet {
        val avatarSet = AvatarSet()
        map.forEach {
            avatarSet.add(channelName = it.key.lowercase(), url = it.value)
        }

        return avatarSet
    }

    companion object {
        private const val STV_EMOTE_CDN = "https://cdn.7tv.app/emote/"

        private fun getEmoteUrl(size: String, emoteId: String): String {
            return "$STV_EMOTE_CDN$emoteId/$size"
        }
    }
}