package tv.orange.models.data

data class UserInfo(
    val userId: String,
    val userName: String
) {
    fun idToInt(): Int = userId.toIntOrNull() ?: 0
}