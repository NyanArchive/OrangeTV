package tv.orange.models.data.emotes

interface Emote {
    fun getCode(): String

    fun getUrl(size: Size): String

    fun isAnimated(): Boolean

    enum class Size {
        LARGE,
        MEDIUM,
        SMALL
    }
}