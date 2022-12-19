package tv.orange.models.data.badges.impl

import android.graphics.Color
import tv.orange.models.data.badges.Badge
import java.util.*

data class BadgeHomiesNewImpl(
    private val uuid: UUID,
) : Badge {
    override fun getCode(): String = "homies"
    override fun getUrl(): String = "$CDN_URL${uuid.toBadgeId()}"
    override fun getBackgroundColor(): Int = Color.TRANSPARENT
    override fun getReplaces(): String? = null

    companion object {
        private const val CDN_URL = "https://nopbreak.ru/shared/homies/badges/"

        private fun UUID.toBadgeId(): String {
            return this.toString().replace("-", "")
        }
    }
}