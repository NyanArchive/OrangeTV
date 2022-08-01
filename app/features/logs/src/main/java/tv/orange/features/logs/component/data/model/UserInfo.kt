package tv.orange.features.logs.component.data.model

data class UserInfo(
    val userId: String,
    val userName: String
) {
    fun idToInt(): Int = userId.toIntOrNull() ?: 0
}