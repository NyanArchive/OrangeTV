package tv.orange.models.data.badges

interface Badge {
    fun getCode(): String

    fun getUrl(): String

    fun getBackgroundColor(): Int

    fun getReplaces(): String?
}