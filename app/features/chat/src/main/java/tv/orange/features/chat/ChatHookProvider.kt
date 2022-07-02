package tv.orange.features.chat

import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.models.LifecycleController
import tv.orange.features.badges.BadgesInjector
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.chat.di.component.DaggerChatComponent
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.ChatMessageInterface
import javax.inject.Inject

class ChatHookProvider @Inject constructor(
    val emotes: tv.orange.features.emotes.EmotesInjector,
    val badges: BadgesInjector
) {
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
        controller.registerLifecycleListener(emotes)
        controller.registerLifecycleListener(badges)
    }

    fun hookMessageInterface(
        cmi: ChatMessageInterface,
        channelId: Int
    ): ChatMessageInterface {
        val badges = badges.injectBadges(cmi.badges, cmi.userId, channelId)
        val tokens = emotes.injectEmotes(cmi.tokens, cmi.userId, channelId)

        return object : ChatMessageInterface {
            override fun getBadges(): MutableList<MessageBadge> {
                return badges.toMutableList()
            }

            override fun getDisplayName(): String {
                return cmi.displayName
            }

            override fun getTimestampSeconds(): Int {
                return cmi.timestampSeconds
            }

            override fun getTokens(): MutableList<MessageToken> {
                return tokens.toMutableList()
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
}