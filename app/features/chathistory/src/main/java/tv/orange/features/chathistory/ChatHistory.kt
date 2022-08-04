package tv.orange.features.chathistory

import io.reactivex.android.schedulers.AndroidSchedulers
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.models.Feature
import tv.orange.features.chathistory.bridge.ILiveChatSource
import tv.orange.features.chathistory.data.repository.ChatHistoryRepository
import tv.orange.features.chathistory.di.scope.ChatHistoryScope
import tv.twitch.android.models.channel.ChannelInfo
import tv.twitch.android.shared.chat.events.ChatConnectionEvents
import javax.inject.Inject

@ChatHistoryScope
class ChatHistory @Inject constructor(
    val repository: ChatHistoryRepository
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(ChatHistory::class.java)
    }

    fun requestChatHistory(
        event: ChatConnectionEvents,
        source: ILiveChatSource,
        channel: ChannelInfo?
    ) {
        Logger.debug("event: $event, source: $source, channel: $channel")
        if (!Flag.CHAT_HISTORY.valueBoolean()) {
            return
        }

        channel?.let { info ->
            if (event is ChatConnectionEvents.ChatConnectingEvent && info.id == event.getChannelId()) {
                injectTwitchChatHistory(
                    source = source,
                    channelName = info.name,
                    channelId = event.channelId
                )
            }
        }
    }

    private fun injectTwitchChatHistory(
        source: ILiveChatSource,
        channelName: String?,
        channelId: Int
    ) {
        Logger.debug("source: $source, channelName: $channelName, channelId: $channelId")
        if (channelName.isNullOrBlank()) {
            return
        }

        source.addChatHistoryMessage(
            repository.getSystemMessage("[Twitch] Fetching messages..."),
            channelId
        )

        source.addDisposable(
            repository.getMessages(channelName = channelName)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ messages ->
                    if (messages.isNullOrEmpty()) {
                        return@subscribe
                    }

                    messages.forEach { message ->
                        source.addChatHistoryMessage(message, channelId)
                    }
                }) {
                    it.printStackTrace()
                    source.addChatHistoryMessage(
                        repository.getSystemMessage("[Twitch] Error: ${it.localizedMessage}"),
                        channelId
                    )
                }
        )
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}