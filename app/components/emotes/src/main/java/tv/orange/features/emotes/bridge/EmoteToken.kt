package tv.orange.features.emotes.bridge

import tv.orange.models.abs.EmotePackageSet
import tv.twitch.android.models.chat.MessageToken

data class EmoteToken(val emoteCode: String, val emoteUrl: String, val emoteCardUrl: String, val packageSet: EmotePackageSet) : MessageToken(null)