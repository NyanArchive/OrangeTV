package tv.orange.models.data.emotes

import tv.orange.models.abs.EmotePackageSet

interface Emote {
    fun getCode(): String

    fun getUrl(size: Size): String

    fun isAnimated(): Boolean

    fun getPackageSet(): EmotePackageSet

    enum class Size {
        LARGE,
        MEDIUM,
        SMALL
    }
}