package tv.orange.models.data.emotes

data class EmoteImpl(
    private val emoteCode: String,
    private val smallUrl: String,
    private val mediumUrl: String?,
    private val largeUrl: String?,
    private val animated: Boolean
) : Emote {
    override fun getCode() = emoteCode

    override fun isAnimated(): Boolean = animated

    override fun getUrl(size: Emote.Size): String {
        return when (size) {
            Emote.Size.LARGE -> largeUrl ?: mediumUrl ?: smallUrl
            Emote.Size.MEDIUM -> mediumUrl ?: smallUrl
            Emote.Size.SMALL -> smallUrl
        }
    }
}