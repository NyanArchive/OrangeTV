package tv.orange.models.retrofit.ffzap

import android.graphics.Color

data class FfzAPBadge(
    val id: String?,
    val tier: Int?,
    val badge_color: String?,
    val badge_is_colored: Int?,
    val admin: Int?
) {
    fun parseColor(): Int {
        return badge_color?.let { color ->
            Color.parseColor(color)
        } ?: Color.TRANSPARENT
    }
}