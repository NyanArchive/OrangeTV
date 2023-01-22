package tv.orange.features.api.component.data.mapper

import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteBaseImpl
import tv.orange.models.data.emotes.impl.EmoteBttvImpl
import tv.orange.models.retrofit.bttv.BttvChannelData
import tv.orange.models.retrofit.bttv.BttvEmote
import tv.orange.models.retrofit.bttv.FfzEmote
import tv.orange.models.retrofit.bttv.FfzImageType
import javax.inject.Inject

class BttvApiMapper @Inject constructor() {
    fun mapEmotes(response: BttvChannelData): List<Emote> {
        return mapEmotes(
            emotes = response.sharedEmotes,
            isGlobalEmotes = false
        ) + mapEmotes(
            emotes = response.channelEmotes,
            isGlobalEmotes = false
        )
    }

    fun mapEmotes(emotes: List<BttvEmote>, isGlobalEmotes: Boolean): List<Emote> {
        return emotes.map { emote ->
            EmoteBttvImpl(
                emoteId = emote.id,
                emoteCode = emote.code,
                animated = emote.imageType == FfzImageType.GIF,
                packageSet = if (!isGlobalEmotes) {
                    EmotePackageSet.BttvChannel
                } else {
                    EmotePackageSet.BttvGlobal
                },
                isZeroWidthEmote = if (isGlobalEmotes) {
                    BTTV_GLOBAL_ZW_CODES.contains(emote.code)
                } else {
                    false
                },
                useWebp = Flag.BTTV_WEBP.asBoolean()
            )
        }
    }

    fun mapFfzEmotes(emotes: List<FfzEmote>, isGlobalEmotes: Boolean): List<Emote> {
        return emotes.mapNotNull { emote ->
            val small = emote.images["1x"] ?: return@mapNotNull null

            EmoteBaseImpl(
                emoteCode = emote.code,
                animated = false,
                smallUrl = small,
                mediumUrl = emote.images["2x"],
                largeUrl = emote.images["4x"],
                packageSet = if (!isGlobalEmotes) {
                    EmotePackageSet.FfzChannel
                } else {
                    EmotePackageSet.FfzGlobal
                }
            )
        }
    }


    companion object {
        private val BTTV_GLOBAL_ZW_CODES = hashSetOf(
            "SoSnowy", "IceCold", "SantaHat", "TopHat",
            "ReinDeer", "CandyCane", "cvMask", "cvHazmat"
        )
    }
}