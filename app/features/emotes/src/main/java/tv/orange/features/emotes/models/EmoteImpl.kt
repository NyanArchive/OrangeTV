package tv.orange.features.emotes.models

data class EmoteImpl(
    val emoteCode: String,
    val smallUrl: String,
    val mediumUrl: String?,
    val largeUrl: String?,
    val animated: Boolean
) : Emote {
    override fun getCode() = emoteCode

    override fun isAnimated(): Boolean = animated

    override fun getUrl(size: Emote.Size): String? {
        return when (size) {
            Emote.Size.LARGE -> largeUrl
            Emote.Size.MEDIUM -> mediumUrl
            Emote.Size.SMALL -> smallUrl
        }
    }
}