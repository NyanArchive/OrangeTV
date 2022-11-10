package tv.orange.models.retrofit.nop

data class UpdateChannelData(
    val codename: String?,
    val active: Boolean,
    val build: Int?,
    val apkUrl: List<String>?,
    val changelog: String?,
    val logoUrl: String?
)
