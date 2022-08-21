package tv.orange.features.api.component.data.mapper

import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteImpl
import tv.orange.models.retrofit.bttv.BttvChannelData
import tv.orange.models.retrofit.bttv.BttvEmote
import tv.orange.models.retrofit.bttv.FfzEmote
import tv.orange.models.retrofit.bttv.FfzImageType
import javax.inject.Inject

class BttvApiMapper @Inject constructor() {
    fun mapEmotes(response: BttvChannelData): List<Emote> {
        return mapEmotes(
            emotes = response.sharedEmotes,
            isChannelEmotes = true
        ) + mapEmotes(
            emotes = response.channelEmotes,
            isChannelEmotes = true
        )
    }

    fun mapEmotes(emotes: List<BttvEmote>, isChannelEmotes: Boolean): List<Emote> {
        return emotes.map { emote ->
            EmoteImpl(
                emoteCode = emote.code,
                animated = emote.imageType == FfzImageType.GIF,
                smallUrl = getBttvEmoteUrl("1x", emote.id),
                mediumUrl = getBttvEmoteUrl("2x", emote.id),
                largeUrl = getBttvEmoteUrl("3x", emote.id),
                packageSet = if (isChannelEmotes) EmotePackageSet.BttvChannel else EmotePackageSet.BttvGlobal,
                isZeroWidth = if (!isChannelEmotes) BTTV_GLOBAL_ZW_CODES.contains(emote.code) else false
            )
        }
    }

    fun mapFfzEmotes(emotes: List<FfzEmote>, isChannelEmotes: Boolean): List<Emote> {
        return emotes.mapNotNull { emote ->
            val small = emote.images["1x"] ?: return@mapNotNull null

            EmoteImpl(
                emoteCode = emote.code,
                animated = false,
                smallUrl = small,
                mediumUrl = emote.images["2x"],
                largeUrl = emote.images["4x"],
                packageSet = if (isChannelEmotes) EmotePackageSet.FfzChannel else EmotePackageSet.FfzGlobal
            )
        }
    }


    companion object {
        private const val BTTV_EMOTE_CDN = "https://cdn.betterttv.net/emote/"

        private val BTTV_GLOBAL_ZW_CODES = hashSetOf(
            "SoSnowy", "IceCold", "SantaHat", "TopHat",
            "ReinDeer", "CandyCane", "cvMask", "cvHazmat"
        )

        private fun getBttvEmoteUrl(size: String, emoteId: String): String {
            return "$BTTV_EMOTE_CDN$emoteId/$size"
        }
    }
}