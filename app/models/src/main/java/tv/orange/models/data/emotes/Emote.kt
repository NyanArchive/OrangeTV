package tv.orange.models.data.emotes

import tv.orange.models.abc.EmotePackageSet

interface Emote {
    fun getCode(): String

    fun getUrl(size: Size): String

    fun isAnimated(): Boolean

    fun getPackageSet(): EmotePackageSet

    fun isZeroWidth(): Boolean

    enum class Size {
        LARGE,
        MEDIUM,
        SMALL
    }
}