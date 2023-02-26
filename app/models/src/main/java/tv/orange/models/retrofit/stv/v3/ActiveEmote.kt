package tv.orange.models.retrofit.stv.v3

data class ActiveEmote(
    val id: String,
    val name: String,
    val flags: Int,
    val data: EmotePartial?
)
