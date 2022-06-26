package tv.orange.features.badges.models

data class BadgeImpl(
    val badgeCode: String,
    val badgeUrl: String,
    val badgeBackgroundColor: Int,
    val badgeReplaces: String?
) : Badge {
    override fun getCode(): String {
        return badgeCode
    }

    override fun getUrl(): String {
        return badgeUrl
    }

    override fun getBackgroundColor(): Int {
        return badgeBackgroundColor
    }

    override fun getReplaces(): String? {
        return badgeReplaces
    }
}