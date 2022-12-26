package tv.orange.models.data.badges.impl

import tv.orange.models.data.badges.Badge

data class BadgeFfzAPImpl(
    private val badgeId: Int,
    private val backgroundColor: Int,
) : Badge {
    override fun getCode(): String = "ffzap"
    override fun getUrl(): String = "$CDN_URL$badgeId/3"
    override fun getBackgroundColor(): Int = backgroundColor
    override fun getReplaces(): String? = null

    companion object {
        private const val CDN_URL = "https://api.ffzap.com/v1/user/badge/"
    }
}