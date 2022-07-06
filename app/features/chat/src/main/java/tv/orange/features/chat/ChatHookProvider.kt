package tv.orange.features.chat

import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.models.LifecycleAware
import tv.orange.core.models.LifecycleController
import tv.orange.features.badges.bridge.OrangeMessageBadge
import tv.orange.features.badges.component.BadgeProvider
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.chat.bridge.ChatMessageInterfaceWrapper
import tv.orange.features.chat.di.component.DaggerChatComponent
import tv.orange.features.emotes.bridge.EmoteToken
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.orange.models.data.emotes.Emote
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.ChatMessageInterface
import javax.inject.Inject

class ChatHookProvider @Inject constructor(
    val emoteProvider: EmoteProvider,
    val badgeProvider: BadgeProvider
) : LifecycleAware {
    companion object {
        private val INSTANCE: ChatHookProvider by lazy {
            val hook = DaggerChatComponent.builder()
                .badgesComponent(Core.getInjector().provideComponent(BadgesComponent::class))
                .emotesComponent(Core.getInjector().provideComponent(EmotesComponent::class))
                .build().hook

            Logger.debug("Provide new instance: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): ChatHookProvider {
            return INSTANCE
        }
    }

    fun registerLifecycle(controller: LifecycleController) {
        controller.registerLifecycleListener(this)
    }

    fun hookMessageInterface(
        cmi: ChatMessageInterface,
        channelId: Int
    ): ChatMessageInterface {
        val badges = injectBadges(cmi.badges, cmi.userId, channelId)
        val tokens = injectEmotes(cmi.tokens, cmi.userId, channelId)

        return ChatMessageInterfaceWrapper(cmi = cmi, badges = badges, tokens = tokens)
    }

    private fun injectBadges(
        badges: List<MessageBadge>,
        userId: Int,
        channelId: Int
    ): List<MessageBadge> {
        if (!badgeProvider.hasBadges(userId)) {
            return badges
        }

        val newBadges = badgeProvider.getBadges(userId).toMutableList()
        if (newBadges.isEmpty()) {
            return badges
        }

        val stack = mutableListOf<MessageBadge>()
        badges.forEach { badge ->
            val replaces = newBadges.firstOrNull { it.getReplaces() == badge.name }

            if (replaces != null) {
                stack.add(
                    OrangeMessageBadge(
                        replaces.getCode(),
                        replaces.getUrl(),
                        replaces.getBackgroundColor()
                    )
                )
                newBadges.remove(replaces)
            } else {
                stack.add(badge)
            }
        }
        stack.addAll(newBadges.map {
            OrangeMessageBadge(it.getCode(), it.getUrl(), it.getBackgroundColor())
        })

        return stack
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
                    val emote = emoteProvider.getEmote(word, channelId)
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

    override fun onAllComponentDestroyed() {
        emoteProvider.clear()
        badgeProvider.clear()
    }

    override fun onSdkResume() {
        badgeProvider.refreshBadges()
        emoteProvider.refreshGlobalEmotes()
    }

    override fun onFirstActivityCreated() {
        emoteProvider.fetchGlobalEmotes()
        badgeProvider.fetchBadges()
    }

    override fun onConnectingToChannel(channelId: Int) {
        emoteProvider.requestChannelEmotes(channelId)
    }

    override fun onAllComponentStopped() {}
    override fun onAccountLogout() {}
    override fun onFirstActivityStarted() {}
    override fun onConnectedToChannel(channelId: Int) {}
}