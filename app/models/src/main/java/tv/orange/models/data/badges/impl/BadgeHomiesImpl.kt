package tv.orange.models.data.badges.impl

import android.graphics.Color
import tv.orange.models.data.badges.Badge

data class BadgeHomiesImpl(
    private val badgeId: String,
) : Badge {
    override fun getCode(): String = "homies"
    override fun getUrl(): String = "$CDN_URL$badgeId"
    override fun getBackgroundColor(): Int = Color.TRANSPARENT
    override fun getReplaces(): String? = null

    companion object {
        private const val CDN_URL = "https://nopbreak.ru/shared/homies/badges/"
    }
}