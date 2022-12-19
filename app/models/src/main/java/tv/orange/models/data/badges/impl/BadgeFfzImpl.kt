package tv.orange.models.data.badges.impl

import tv.orange.models.data.badges.Badge

data class BadgeFfzImpl(
    private val badgeId: Int,
    private val type: Type,
    private val backgroundColor: Int,
    private val replaces: String?
) : Badge {
    override fun getCode(): String = type.value ?: "ffz"
    override fun getUrl(): String = "$CDN_URL$badgeId/2"
    override fun getBackgroundColor(): Int = backgroundColor
    override fun getReplaces(): String? = replaces

    enum class Type(val value: String?) {
        SUPPORTER("supporter"),
        UNKNOWN(null)
    }

    companion object {
        private const val CDN_URL = "https://cdn.frankerfacez.com/badge/"
    }
}