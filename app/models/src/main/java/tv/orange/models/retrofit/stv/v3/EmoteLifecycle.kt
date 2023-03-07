package tv.orange.models.retrofit.stv.v3

import com.google.gson.annotations.SerializedName

enum class EmoteLifecycle{
    @SerializedName("-1")
    Deleted,
    @SerializedName("0")
    Pending,
    @SerializedName("1")
    Processing,
    @SerializedName("2")
    Disabled,
    @SerializedName("3")
    Live,
    @SerializedName("-2")
    Failed
}
