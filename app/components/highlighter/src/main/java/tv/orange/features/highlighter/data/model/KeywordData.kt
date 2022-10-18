package tv.orange.features.highlighter.data.model

import android.graphics.Color
import androidx.annotation.ColorInt

data class KeywordData(
    val word: String,
    val type: Type,
    val color: Int,
    val vibration: Boolean = false
) {
    enum class Type {
        CASESENSITIVE,
        INSENSITIVE,
        USERNAME
    }

    companion object {
        @ColorInt
        val DEFAULT_COLOR = Color.parseColor("#ff7961")
    }
}
