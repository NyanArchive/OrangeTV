package tv.orange.models.retrofit.ffz

data class FfzBadges(
    val badges: List<FfzBadge>,
    val users: HashMap<String, HashSet<Int>>
)
