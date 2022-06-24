package tv.orange.features.emotes

import tv.orange.core.Logger
import tv.orange.core.models.LifecycleAware
import tv.orange.features.core.CoreFeature
import tv.orange.features.emotes.bridge.EmoteToken
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.features.emotes.di.ApiModule
import tv.orange.features.emotes.di.DaggerEmotesComponent
import tv.orange.features.emotes.di.EmotesScope
import tv.orange.features.emotes.models.Emote
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.ChatMessageInterface
import javax.inject.Inject

@EmotesScope
class Hook @Inject constructor(val emoteProvider: EmoteProvider) : LifecycleAware {
    fun injectEmotes(
        cmi: ChatMessageInterface,
        channelId: Int
    ): ChatMessageInterface {
        val injected = injectEmotes(cmi.tokens, cmi.userId, channelId)

        return object : ChatMessageInterface {
            override fun getBadges(): MutableList<MessageBadge> {
                return cmi.badges
            }

            override fun getDisplayName(): String {
                return cmi.displayName
            }

            override fun getTimestampSeconds(): Int {
                return cmi.timestampSeconds
            }

            override fun getTokens(): MutableList<MessageToken> {
                return injected.toMutableList()
            }

            override fun getUserId(): Int {
                return cmi.userId
            }

            override fun getUserName(): String {
                return cmi.userName
            }

            override fun isAction(): Boolean {
                return cmi.isAction
            }

            override fun isDeleted(): Boolean {
                return cmi.isDeleted
            }

            override fun isSystemMessage(): Boolean {
                return cmi.isSystemMessage
            }
        }
    }

    private fun injectEmotes(
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

    companion object {
        private val INSTANCE by lazy {
            val hook = DaggerEmotesComponent.builder()
                .apiModule(ApiModule())
                .coreFeatureComponent(CoreFeature.get().component)
                .build().hook
            Logger.debug("created: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): Hook {
            return INSTANCE
        }
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

    override fun onSdkResume() {
        emoteProvider.updateEmotes()
    }

    override fun onAccountLogout() {}

    override fun onFirstActivityCreated() {
        emoteProvider.fetchEmotes()
    }

    override fun onFirstActivityStarted() {}

    override fun onConnectedToChannel(channelId: Int) {
        requestChannelEmotes(channelId)
    }
}