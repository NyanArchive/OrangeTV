package tv.orange.models.retrofit.itz

data class EmotesData(
    val global_emotes: List<ItzEmote>,
    val channel_emotes: HashMap<String, ItzChannel>
)