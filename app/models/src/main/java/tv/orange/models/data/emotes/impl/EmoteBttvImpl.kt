package tv.orange.models.data.emotes.impl

import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote

data class EmoteBttvImpl(
    private val emoteId: String,
    private val emoteCode: String,
    private val animated: Boolean,
    private val packageSet: EmotePackageSet,
    private val isZeroWidthEmote: Boolean = false
) : Emote {
    override fun getCode() = emoteCode
    override fun isAnimated(): Boolean = animated
    override fun isZeroWidth(): Boolean = isZeroWidthEmote
    override fun getPackageSet(): EmotePackageSet = packageSet

    override fun getUrl(size: Emote.Size): String = when (size) {
        Emote.Size.LARGE -> getEmoteUrl(emoteId, "3x")
        Emote.Size.MEDIUM -> getEmoteUrl(emoteId, "2x")
        Emote.Size.SMALL -> getEmoteUrl(emoteId, "1x")
    }

    companion object {
        private const val CDN_URL = "https://cdn.betterttv.net/emote/"

        private fun getEmoteUrl(emoteId: String, size: String): String {
            return "$CDN_URL$emoteId/$size"
        }
    }
}