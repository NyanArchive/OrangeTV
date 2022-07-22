package tv.orange.models.data.badges

import android.graphics.Color

data class BadgeImpl(
    private val code: String,
    private val url: String,
    private val backgroundColor: Int = Color.TRANSPARENT,
    private val replaces: String? = null
) : Badge {
    override fun getCode(): String {
        return code
    }

    override fun getUrl(): String {
        return url
    }

    override fun getBackgroundColor(): Int {
        return backgroundColor
    }

    override fun getReplaces(): String? {
        return replaces
    }
}