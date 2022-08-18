package tv.orange.features.chat.bridge

import tv.orange.features.emotes.bridge.EmoteToken
import tv.twitch.android.models.chat.MessageToken

data class StackEmoteToken(
    val core: TokenHolder,
    val stack: MutableList<EmoteToken> = mutableListOf()
) : MessageToken(null) {
    class TokenHolder(private val token: MessageToken) {
        fun isTwitchToken(): Boolean = token is EmoticonToken

        fun getTwitchToken(): EmoticonToken {
            if (token is EmoticonToken) {
                return token
            } else {
                throw IllegalStateException("debug")
            }
        }

        fun getOrangeToken(): EmoteToken {
            if (token is EmoteToken) {
                return token
            } else {
                throw IllegalStateException("debug")
            }
        }
    }
}