package tv.orange.models.retrofit.bttv

import android.util.ArrayMap

data class FfzEmote(
    val id: Int,
    val code: String,
    val images: ArrayMap<String, String?>,
    val imageType: FfzImageType
)
