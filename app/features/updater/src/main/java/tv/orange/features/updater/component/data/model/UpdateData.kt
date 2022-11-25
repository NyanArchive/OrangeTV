package tv.orange.features.updater.component.data.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class UpdateData(
    val build: Int,
    val codename: String,
    val url: String,
    val logoUrl: String?,
    val changelog: String?,
    val size: Long = -1
) : Parcelable
