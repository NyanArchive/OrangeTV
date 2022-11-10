package tv.orange.features.updater.component.data.model

data class UpdateData(
    val build: Int,
    val codename: String,
    val url: String,
    val logoUrl: String?,
    val changelog: String?,
    val size: Long = -1
)
