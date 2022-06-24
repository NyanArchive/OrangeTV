package tv.orange.models.retrofit.stv

data class StvEmote(
    val id: String,
    val name: String,
    val visibility: Int,
    val mime: String,
    val urls: List<List<String>>,
    val visibility_simple: Set<String>
)
