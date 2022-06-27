package tv.oranges.features.chathistory

import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.oranges.features.chathistory.bridge.ILiveChatSource
import tv.oranges.features.chathistory.data.repository.ChatHistoryRepository
import tv.oranges.features.chathistory.di.component.DaggerChatHistoryComponent
import tv.oranges.features.chathistory.di.scope.ChatHistoryScope
import tv.twitch.android.models.channel.ChannelInfo
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.chat.events.ChatConnectionEvents
import javax.inject.Inject

@ChatHistoryScope
class ChatHistory @Inject constructor(val repository: ChatHistoryRepository) {
    fun requestChatHistory(
        event: ChatConnectionEvents,
        source: ILiveChatSource,
        channel: ChannelInfo?
    ) {
        channel?.let { info ->
            if (event is ChatConnectionEvents.ChatConnectingEvent) {
                if (info.id == event.getChannelId()) {
                    injectChatHistory(source = source, channelName = info.name)
                }
            }
        }
    }

    private fun injectChatHistory(source: ILiveChatSource, channelName: String?) {
        if (channelName.isNullOrBlank()) {
            return
        }

        source.addDisposable(
            repository.getMessages(channelName = channelName).subscribe({
                if (it.isNullOrEmpty()) {
                    source.addChatHistoryMessage("mod_chat_history_no_messages")
                    return@subscribe
                }
                source.addChatHistoryMessages(it.map { message ->
                    "${message.displayName}: ${message.text}"
                })
            }) {
                it.printStackTrace()
                source.addChatHistoryMessage("mod_chat_history_no_messages")
            })
    }

    companion object {
        private val INSTANCE by lazy {
            val hook = DaggerChatHistoryComponent.factory()
                .create(
                    Core.getInjector().provideComponent(CoreComponent::class),
                    Core.getInjector().provideComponent(GraphQlService::class)
                ).chatHistory

            Logger.debug("created: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): ChatHistory {
            return INSTANCE
        }
    }
}