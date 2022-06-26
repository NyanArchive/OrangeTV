package tv.orange.models.retrofit.ffz

import android.graphics.Color

data class FfzBadge(
    val id: Int,
    val name: String,
    val replaces: String?,
    val color: String?
) {
    fun parseColor(): Int {
        return color?.let {
            Color.parseColor(color)
        } ?: Color.TRANSPARENT
    }
}
