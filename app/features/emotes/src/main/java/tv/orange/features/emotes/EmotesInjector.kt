package tv.orange.features.emotes

import tv.orange.core.models.LifecycleAware
import tv.orange.features.emotes.bridge.EmoteToken
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.features.emotes.di.scope.EmotesScope
import tv.orange.models.data.emotes.Emote
import tv.twitch.android.models.chat.MessageToken
import javax.inject.Inject

@EmotesScope
class EmotesInjector @Inject constructor(val emoteProvider: EmoteProvider) : LifecycleAware {
    fun injectEmotes(
        tokens: List<MessageToken>,
        userId: Int,
        channelId: Int
    ): List<MessageToken> {
        val stack = mutableListOf<MessageToken>()

        var injected = false
        tokens.forEach { token ->
            if (token is MessageToken.TextToken) {
                val words = token.text.split(" ")
                for (word in words) {
                    val emote = emoteProvider.getEmote(word, channelId, userId)
                    if (emote != null) {
                        if (!injected) {
                            injected = true
                        }
                        stack.add(EmoteToken(emote.getCode(), emote.getUrl(Emote.Size.MEDIUM)))
                    } else {
                        stack.add(MessageToken.TextToken("$word ", token.flags))
                    }
                }
            } else {
                stack.add(token)
            }
        }

        if (injected) {
            return stack
        }

        return tokens
    }

    private fun requestChannelEmotes(channelId: Int?) {
        channelId ?: return

        if (channelId <= 0) {
            return
        }

        emoteProvider.requestChannelEmotes(channelId)
    }

    override fun onAllComponentDestroyed() {
        emoteProvider.clear()
    }

    override fun onAllComponentStopped() {}

    override fun onSdkResume() {}

    override fun onAccountLogout() {}

    override fun onFirstActivityCreated() {
        emoteProvider.fetchEmotes()
    }

    override fun onFirstActivityStarted() {}

    override fun onConnectedToChannel(channelId: Int) {}

    override fun onConnectingToChannel(channelId: Int) {
        requestChannelEmotes(channelId)
    }
}