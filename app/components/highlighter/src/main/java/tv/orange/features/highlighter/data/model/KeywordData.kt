package tv.orange.features.highlighter.data.model

data class KeywordData(
    val word: String,
    val type: Type,
    val color: Int,
    val vibration: Boolean = false
) {
    enum class Color(color: String) {
        DEFAULT("#ff7961"),
        PINK500L("#ff6090"),
        PURPLE500L("#d05ce3"),
        DEEP_PURPLE500L("#9a67ea"),
        INDIGO500L("#757de8"),
        BLUE500L("#6ec6ff"),
        LIGHT_BLUE500L("#67daff"),
        CYAN500L("#62efff"),
        TEAL500L("#52c7b8"),
        GREEN500L("#80e27e"),
        LIGHT_GREEN500L("#bef67a"),
        LIME500L("#ffff6e"),
        YELLOW500L("#ffff72"),
        AMBER500L("#fff350"),
        ORANGE500L("#ffc947"),
        DEEP_ORANGE500L("#ff8a50"),
        BROWN500L("#a98274"),
        GREY500L("#cfcfcf"),
        BLUE_GREY_500L("#8eacbb");

        val value = android.graphics.Color.parseColor(color)

        companion object {
            fun resolve(newColor: Int): Color {
                return values().firstOrNull { it.value == newColor } ?: DEFAULT
            }

            val colors: IntArray = values().map { it.value }.toIntArray()
        }
    }

    enum class Type {
        CASESENSITIVE,
        INSENSITIVE,
        USERNAME
    }
}
