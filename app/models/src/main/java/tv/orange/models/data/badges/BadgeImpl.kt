package tv.orange.models.data.badges

import android.graphics.Color

data class BadgeImpl(
    val badgeCode: String,
    val badgeUrl: String,
    val badgeBackgroundColor: Int = Color.TRANSPARENT,
    val badgeReplaces: String? = null
) : Badge {
    override fun getCode(): String {
        return badgeCode
    }

    override fun getUrl(): String {
        return badgeUrl
    }

    override fun getBackgroundColor(): Int {
        return badgeBackgroundColor
    }

    override fun getReplaces(): String? {
        return badgeReplaces
    }
}