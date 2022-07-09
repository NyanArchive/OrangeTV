package tv.orange.models.retrofit.nop

data class DonationUser(
    val userId: String,
    val userName: String,
    val displayName: String,
    val date: String,
    val type: String?,
    val badgeUrl: String?
)
