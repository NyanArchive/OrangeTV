package tv.orange.features.chat.data.model

import tv.orange.models.abc.EmotePackageSet

sealed interface FavEmote {
    fun getCode(): String
    fun isAnimated(): Boolean
    fun getChannelId(): Int
    fun getPackageSet(): EmotePackageSet
}

data class OrangeFavEmote(
    private val code: String,
    private val isAnimated: Boolean,
    private val channelId: Int,
    private val packageSet: EmotePackageSet
) : FavEmote {
    override fun getCode(): String {
        return code
    }

    override fun isAnimated(): Boolean {
        return isAnimated
    }

    override fun getChannelId(): Int {
        return channelId
    }

    override fun getPackageSet(): EmotePackageSet {
        return packageSet
    }
}

data class TwitchFavEmote(
    private val code: String,
    private val isAnimated: Boolean,
    private val channelId: Int,
    private val packageSet: EmotePackageSet,
    val emoteId: String
) : FavEmote {
    override fun getCode(): String {
        return code
    }

    override fun isAnimated(): Boolean {
        return isAnimated
    }

    override fun getChannelId(): Int {
        return channelId
    }

    override fun getPackageSet(): EmotePackageSet {
        return packageSet
    }
}