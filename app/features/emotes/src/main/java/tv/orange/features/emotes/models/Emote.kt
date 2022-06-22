package tv.orange.features.emotes.models

interface Emote {
    fun getCode(): String

    fun getUrl(size: Size): String?

    fun isAnimated(): Boolean

    enum class Size {
        LARGE,
        MEDIUM,
        SMALL
    }
}