package tv.orange.models.data.badges

import android.graphics.Color

data class BadgeBaseImpl(
    private val code: String,
    private val url: String,
    private val backgroundColor: Int = Color.TRANSPARENT,
    private val replaces: String? = null
) : Badge {
    override fun getCode(): String = code
    override fun getUrl(): String = url
    override fun getBackgroundColor(): Int = backgroundColor
    override fun getReplaces(): String? = replaces
}