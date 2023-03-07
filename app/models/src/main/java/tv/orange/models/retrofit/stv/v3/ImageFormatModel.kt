package tv.orange.models.retrofit.stv.v3

import com.google.gson.annotations.SerializedName


data class ImageFormatModel(
    val name: String,
    val static_name: String,
    val format: ImageFormat,
    val width: Int,
    val height: Int
) {
    enum class ImageFormat{
        @SerializedName("AVIF")
        AVIF,
        @SerializedName("WEBP")
        WEBP,
        @SerializedName("GIF")
        GIF,
        @SerializedName("PNG")
        PNG
    }
}
