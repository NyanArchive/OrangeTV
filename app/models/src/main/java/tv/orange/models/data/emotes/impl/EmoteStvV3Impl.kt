package tv.orange.models.data.emotes.impl

import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote

data class EmoteStvV3Impl(
    private val emoteId: String,
    private val emoteCode: String,
    private val animated: Boolean,
    private val packageSet: EmotePackageSet,
    private val isZeroWidth: Boolean = false,
    private val baseUrl: String,
    private val sizes: Triple<String, String?, String?> // small, medium, large
) : Emote {
    override fun getCode() = emoteCode
    override fun isAnimated(): Boolean = animated
    override fun getPackageSet(): EmotePackageSet = packageSet
    override fun isZeroWidth(): Boolean = isZeroWidth

    override fun getUrl(size: Emote.Size): String = baseUrl + when (size) {
        Emote.Size.LARGE -> sizes.third ?: sizes.second ?: sizes.first
        Emote.Size.MEDIUM -> sizes.second ?: sizes.first
        Emote.Size.SMALL -> sizes.first
    }
}