package tv.orange.models.retrofit.ffz

data class FfzBadgesData(
    val badges: List<FfzBadge>,
    val users: HashMap<String, HashSet<Int>>
)
