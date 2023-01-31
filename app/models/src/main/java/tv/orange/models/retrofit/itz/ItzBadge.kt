package tv.orange.models.retrofit.itz

data class ItzBadge(
    val image1: String,
    val image2: String,
    val image3: String,
    val users: Set<String>,
    val tooltip: String
)