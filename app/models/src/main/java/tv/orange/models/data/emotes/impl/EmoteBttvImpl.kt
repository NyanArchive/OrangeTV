package tv.orange.models.data.emotes.impl

import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote

data class EmoteBttvImpl(
    private val emoteId: String,
    private val emoteCode: String,
    private val animated: Boolean,
    private val packageSet: EmotePackageSet,
    private val isZeroWidthEmote: Boolean = false,
    private val useWebp: Boolean = false
) : Emote {
    override fun getCode() = emoteCode
    override fun isAnimated(): Boolean = animated
    override fun isZeroWidth(): Boolean = isZeroWidthEmote
    override fun getPackageSet(): EmotePackageSet = packageSet

    override fun getUrl(size: Emote.Size): String = when (size) {
        Emote.Size.LARGE -> getEmoteUrl(emoteId, "3x", useWebp)
        Emote.Size.MEDIUM -> getEmoteUrl(emoteId, "2x", useWebp)
        Emote.Size.SMALL -> getEmoteUrl(emoteId, "1x", useWebp)
    }

    companion object {
        private const val CDN_URL = "https://cdn.betterttv.net/emote/"

        private fun getEmoteUrl(
            emoteId: String,
            size: String,
            useWebpSource: Boolean = false
        ): String {
            return if (useWebpSource) {
                "$CDN_URL$emoteId/$size.webp"
            } else {
                "$CDN_URL$emoteId/$size"
            }
        }
    }
}