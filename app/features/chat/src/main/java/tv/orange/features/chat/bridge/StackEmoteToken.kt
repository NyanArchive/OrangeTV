package tv.orange.features.chat.bridge

import tv.orange.features.emotes.bridge.EmoteToken
import tv.twitch.android.models.chat.MessageToken

data class StackEmoteToken(
    val core: EmoteToken,
    val stack: MutableList<EmoteToken> = mutableListOf()
) : MessageToken(null)