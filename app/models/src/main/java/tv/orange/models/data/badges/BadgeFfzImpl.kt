package tv.orange.models.data.badges

data class BadgeFfzImpl(
    private val type: Type,
    private val url: String,
    private val backgroundColor: Int,
    private val replaces: String?
) : Badge {
    override fun getCode(): String {
        return type.value ?: "unknown"
    }

    override fun getUrl(): String {
        return url
    }

    override fun getBackgroundColor(): Int {
        return backgroundColor
    }

    override fun getReplaces(): String? {
        return replaces
    }

    enum class Type(val value: String?) {
        SUPPORTER("supporter"),
        UNKNOWN(null)
    }
}