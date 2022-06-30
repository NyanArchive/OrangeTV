package tv.orange.features.emotes.bridge

import tv.twitch.android.models.chat.MessageToken

data class EmoteToken(val emoteCode: String, val emoteUrl: String): MessageToken(null) {}