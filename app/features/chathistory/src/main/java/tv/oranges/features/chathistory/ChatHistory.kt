package tv.oranges.features.chathistory

import io.reactivex.android.schedulers.AndroidSchedulers
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.oranges.features.chathistory.bridge.ILiveChatSource
import tv.oranges.features.chathistory.data.mapper.ChatHistoryMapper
import tv.oranges.features.chathistory.data.repository.ChatHistoryRepository
import tv.oranges.features.chathistory.di.component.DaggerChatHistoryComponent
import tv.oranges.features.chathistory.di.scope.ChatHistoryScope
import tv.twitch.android.models.channel.ChannelInfo
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.chat.events.ChatConnectionEvents
import javax.inject.Inject

@ChatHistoryScope
class ChatHistory @Inject constructor(
    val repository: ChatHistoryRepository,
    val mapper: ChatHistoryMapper
) {
    fun requestChatHistory(
        event: ChatConnectionEvents,
        source: ILiveChatSource,
        channel: ChannelInfo?
    ) {
        channel?.let { info ->
            if (event is ChatConnectionEvents.ChatConnectingEvent) {
                if (info.id == event.getChannelId()) {
                    injectChatHistory(
                        source = source,
                        channelName = info.name,
                        channelId = event.channelId
                    )
                }
            }
        }
    }

    private fun injectChatHistory(source: ILiveChatSource, channelName: String?, channelId: Int) {
        if (channelName.isNullOrBlank()) {
            return
        }

        source.addDisposable(
            repository.getMessages(channelName = channelName)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ messages ->
                    if (messages.isNullOrEmpty()) {
                        return@subscribe
                    }

                    source.addChatHistoryMessages(channelId, messages.map { message ->
                        mapper.map(message)
                    })
                }) {
                    it.printStackTrace()
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