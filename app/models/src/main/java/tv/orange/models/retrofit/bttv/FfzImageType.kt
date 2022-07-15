package tv.orange.models.retrofit.bttv

import com.google.gson.annotations.SerializedName

enum class FfzImageType(val value: String) {
    @SerializedName("png")
    PNG("png"),

    @SerializedName("gif")
    GIF("gif");
}