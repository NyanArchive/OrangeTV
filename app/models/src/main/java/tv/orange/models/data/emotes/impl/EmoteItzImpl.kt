package tv.orange.models.data.emotes.impl

import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote

data class EmoteItzImpl(
    private val emoteId: String,
    private val emoteCode: String,
    private val packageSet: EmotePackageSet
) : Emote {
    override fun getCode() = emoteCode
    override fun getPackageSet(): EmotePackageSet = packageSet
    override fun isAnimated(): Boolean = false
    override fun isZeroWidth(): Boolean = false

    override fun getUrl(size: Emote.Size): String = when (size) {
        Emote.Size.LARGE -> getEmoteUrl(emoteId, "3x")
        Emote.Size.MEDIUM -> getEmoteUrl(emoteId, "2x")
        Emote.Size.SMALL -> getEmoteUrl(emoteId, "1x")
    }

    companion object {
        private const val CDN_URL = "https://cdn.7tv.app/emote/"

        private fun getEmoteUrl(emoteId: String, size: String): String {
            return "$CDN_URL$emoteId/$size"
        }
    }
}