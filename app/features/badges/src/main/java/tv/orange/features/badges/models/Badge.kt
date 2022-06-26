package tv.orange.features.badges.models

interface Badge {
    fun getCode(): String

    fun getUrl(): String

    fun getBackgroundColor(): Int

    fun getReplaces(): String?
}