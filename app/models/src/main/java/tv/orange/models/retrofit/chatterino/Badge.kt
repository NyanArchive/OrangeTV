package tv.orange.models.retrofit.chatterino

data class Badge(
    val tooltip: String,
    val image1: String,
    val image2: String,
    val image3: String,
    val users: List<String>
)
