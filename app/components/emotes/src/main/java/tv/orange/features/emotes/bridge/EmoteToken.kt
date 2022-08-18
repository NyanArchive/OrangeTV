package tv.orange.features.emotes.bridge

import tv.orange.models.abc.EmotePackageSet
import tv.twitch.android.models.chat.MessageToken

data class EmoteToken(
    val emoteCode: String,
    val emoteUrl: String,
    val emoteCardUrl: String,
    val packageSet: EmotePackageSet,
    val isZeroWidth: Boolean = false
) : MessageToken(null)