package tv.orange.features.blacklist.data.model

data class KeywordData(
    val word: String,
    val type: Type
) {
    enum class Type {
        CASESENSITIVE,
        INSENSITIVE,
        USERNAME
    }
}
