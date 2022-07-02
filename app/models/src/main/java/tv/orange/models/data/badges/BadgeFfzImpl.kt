package tv.orange.models.data.badges

data class BadgeFfzImpl(
    val badgeType: Type,
    val badgeUrl: String,
    val badgeBackgroundColor: Int,
    val badgeReplaces: String?
) : Badge {
    override fun getCode(): String {
        return badgeType.value ?: "unknown"
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

    enum class Type(val value: String?) {
        SUPPORTER("supporter"),
        UNKNOWN(null)
    }
}